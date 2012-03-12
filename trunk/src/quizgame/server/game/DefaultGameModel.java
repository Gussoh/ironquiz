/*
 * DefaultGameModel.java
 *
 * Created on den 28 maj 2007, 09:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.server.game;

import quizgame.protocol.admin.AdminMessage;
import quizgame.protocol.admin.QuestionTimedOut;
import quizgame.protocol.typer.TyperMode;
import quizgame.server.ClientHandler;
import quizgame.server.Server;
import quizgame.server.usertypes.PulpitAccount;
import quizgame.typer.Typer;

/**
 *
 * @author Dukken
 */
public class DefaultGameModel implements GameModel {
    
    private Server server;
    private GameTimer gameTimer;
    private boolean pulpitAnswering = false;


    /** Creates a new instance of DefaultGameModel */
    public DefaultGameModel(Server server) {
        this.server = server;
        gameTimer = new GameTimer(server, 5000);
    }
    
    public void activateQuestion(int category, int question, ClientHandler clientHandler) {
        if(server.getActiveBoard().setActiveQuestion(category, question)) {
            gameTimer.stopAndResetTimer();
            server.getAdminModel().sendActiveBoard();
            server.getAdminModel().sendGameTimer(gameTimer, true);
        } else {
            clientHandler.send(new AdminMessage("There is already a question active"));
        }
        
    }
    
    public void deactivateQuestion(ClientHandler clientHandler) {
        if(gameTimer.isRunning()) {
            clientHandler.send(new AdminMessage("Cannot de-activate question while the game timer is running."));
            return;
        } else if(pulpitAnswering) {
            clientHandler.send(new AdminMessage("Cannot de-activate question while a pulpit is answering."));
            return;
        }
        
        if(server.getActiveBoard().deactivateQuestion()) {
            server.getAdminModel().sendActiveBoard();
            server.getMainscreenModel().deactivateQuestion();
            server.getPulpitModel().resetPersistentLocks();
            server.getPulpitModel().lockPulpits();
            server.getPulpitModel().updatePulpits();
            server.getAdminModel().sendPulpitInformation();
            server.getAdminModel().sendGameTimer(gameTimer, false);
        } else {
            clientHandler.send(new AdminMessage("There is no question active."));
        }
        
    }
    
    public boolean startGame(ClientHandler clientHandler) {
        server.getMainscreenModel().startGame();
        return true;
    }
    
    public void setQuestionUsedUp(int category, int question, boolean isUsedUp, ClientHandler clientHandler) {
        server.getActiveBoard().setUsedUp(category, question, isUsedUp);
        server.getAdminModel().sendActiveBoard();
    }
    
    public GameTimer getGameTimer() {
        return gameTimer;
    }
    
    public void timedOutListener(QuestionTimedOut questionTimedOut) {
        if(gameTimer.isAlarmIDValid(questionTimedOut.getAlarmID())) {
            System.out.println("Timer timed out");
            gameTimer.pauseTimer();
            gameTimer.setElapsed(gameTimer.getTotal());
            server.getPulpitModel().lockPulpits();
            server.getPulpitModel().updatePulpits();
            server.getAdminModel().sendPulpitInformation();
            server.getAdminModel().sendGameTimer(gameTimer, true);
            
            //Play sound on mainscreen
            server.getMainscreenModel().playSound("sound/endTime.wav");
            
        }
    }
    
    public void pulpetPressedButton(PulpitAccount pulpit) {
        gameTimer.pauseTimer();
        server.getPulpitModel().pulpitAnswer(pulpit);
        server.getPulpitModel().updatePulpits();
        server.getAdminModel().sendPulpitInformation();
        server.getAdminModel().sendGameTimer(gameTimer, false);
        server.getAdminModel().pulpitRequestToAnswer(pulpit);
        pulpitAnswering = true;

        //Play sound on mainscreen
        server.getMainscreenModel().playSound("sound/answer.wav");

    }
    
    public void judgePulpit(PulpitAccount pulpit, boolean wasRight) {
        server.getPulpitModel().judgePulpit(pulpit, wasRight);
        if(wasRight) {
            server.getPulpitModel().lockPulpits();
            server.getPulpitModel().updatePulpits();
            gameTimer.setElapsed(gameTimer.getTotal());
            server.getAdminModel().sendGameTimer(gameTimer, false);
        } else {
            server.getPulpitModel().addPersistentLock(pulpit);
            server.getPulpitModel().unlockPulpits();
            server.getPulpitModel().updatePulpits();
            gameTimer.setElapsed(0);
            gameTimer.startTimer();
            server.getAdminModel().sendGameTimer(gameTimer, true);
        }
        
        pulpitAnswering = false;
        server.getAdminModel().sendPulpitInformation();
    }
    
    public void startTimer(ClientHandler clientHandler) {
        if(pulpitAnswering || !server.getActiveBoard().isQuestionActive()) {
            clientHandler.send(new AdminMessage("Cannot start timer if a pulpit is answering or a question is not active."));
            return;
        }
        
        if(gameTimer.startTimer()) {
            System.out.println("Timer has been started");
            server.getAdminModel().sendGameTimer(gameTimer, true);
            server.getPulpitModel().unlockPulpits();
            server.getPulpitModel().updatePulpits();
            server.getAdminModel().sendPulpitInformation();
        }
    }
    
    public void pauseTimer(ClientHandler clientHandler) {
        if(pulpitAnswering || !server.getActiveBoard().isQuestionActive()) {
            clientHandler.send(new AdminMessage("Cannot pause timer if a pulpet is answering or a question is not active."));
            return;
        }
        
        if(gameTimer.pauseTimer()) {
            server.getAdminModel().sendGameTimer(gameTimer, true);
        }
    }
    
    public void endTimer(ClientHandler clientHandler) {
        if(pulpitAnswering || !server.getActiveBoard().isQuestionActive()) {
            clientHandler.send(new AdminMessage("Cannot pause timer if a pulpet is answering or a question is not active."));
            return;
        }
        
        gameTimer.pauseTimer();
        gameTimer.setElapsed(gameTimer.getTotal());
        server.getAdminModel().sendGameTimer(gameTimer, false);
        server.getPulpitModel().lockPulpits();
        server.getPulpitModel().updatePulpits();
        server.getAdminModel().sendPulpitInformation();
    }

    public boolean isTimerButtonsEnabled() {
        if(server.getActiveBoard() == null)
            return false; 
        
        return (server.getActiveBoard().isQuestionActive()) && (pulpitAnswering == false);
    }
}
