/*
 * AdminModel.java
 *
 * Created on den 13 mars 2007, 20:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.server.admin;

import java.util.Map;
import java.util.TreeMap;
import quizgame.common.Category;
import quizgame.protocol.admin.AdminMessage;
import quizgame.protocol.admin.AdminPacket;
import quizgame.protocol.admin.SetPulpitScore;
import quizgame.protocol.admin.SetTimerState;
import quizgame.protocol.admin.DeactivateActiveQuestion;
import quizgame.protocol.admin.GameTimerStatusChange;
import quizgame.protocol.admin.PulpitAnswer;
import quizgame.protocol.admin.PulpitInformation;
import quizgame.protocol.admin.StartGameNotify;
import quizgame.protocol.admin.boardediting.Board;
import quizgame.protocol.admin.boardediting.BoardNames;
import quizgame.protocol.admin.JudgePulpit;
import quizgame.protocol.admin.ModifyTimer;
import quizgame.protocol.admin.boardediting.GetBoard;
import quizgame.protocol.admin.boardediting.GetBoardNames;
import quizgame.protocol.admin.boardediting.RemoveBoards;
import quizgame.protocol.admin.boardediting.SaveBoard;
import quizgame.protocol.admin.SetActiveBoard;
import quizgame.protocol.admin.SetActiveQuestion;
import quizgame.protocol.admin.SetPulpitNickname;
import quizgame.protocol.admin.boardediting.SetBoardNameByName;
import quizgame.protocol.admin.SetQuestionUsedUp;
import quizgame.protocol.admin.StartGame;
import quizgame.protocol.pulpit.PulpitStatus;
import quizgame.server.ActiveBoard;
import quizgame.server.ClientHandler;
import quizgame.server.Logger;
import quizgame.server.Server;
import quizgame.server.game.GameTimer;
import quizgame.server.usertypes.PulpitAccount;

/**
 *
 * @author Dukken
 */
public class AdminModel {
    
    private Server server;
    private PulpitAccount pulpetAnswering;
    private int judgeID;
    
    /** Creates a new instance of AdminModel */
    public AdminModel(Server server) {
        this.server = server;
    }
    
    
    
    public void handleAdminJob(ClientHandler clientHandler, AdminPacket packet) {
        /** Packets related to active gaming */
        
        // Sets new active board to be used.
        if(packet instanceof SetActiveBoard) {
            String boardName = ((SetActiveBoard)packet).getBoardName();
            Category[] categories = server.getBoardManager().getBoard(boardName);
            if(categories != null) {
                server.setActiveBoard(new ActiveBoard(server, boardName, categories));
                sendActiveBoard();
            }
            
            
            // Admin wants to start the game.
        } else if(packet instanceof StartGame) {
            if(server.getGameModel().startGame(clientHandler)) {
                sendToAllAdmins(new StartGameNotify());
            }
            // Admin sets a question as used up on the activeboard. Should not be used when deactivating a active question.
        } else if(packet instanceof SetQuestionUsedUp) {
            if(server.getActiveBoard() == null) {
                clientHandler.send(new AdminMessage("No active board has been set."));
            } else {
                SetQuestionUsedUp usedUp = (SetQuestionUsedUp) packet;
                server.getGameModel().setQuestionUsedUp(usedUp.getCategoryIndex(), usedUp.getQuestionIndex(), usedUp.isUsedUp(), clientHandler); 
            }
            
            
            // Sets new active question
        } else if(packet instanceof SetActiveQuestion) {
            SetActiveQuestion activeQuestion = (SetActiveQuestion) packet;
            if(server.getActiveBoard() == null) {
                clientHandler.send(new AdminMessage("No active board has been set."));
            } else {
                server.getGameModel().activateQuestion(activeQuestion.getCategoryIndex(), activeQuestion.getQuestionIndex(), clientHandler);
            }
            
            // Deactivates current active question.
        } else if(packet instanceof DeactivateActiveQuestion) {
            if(server.getActiveBoard() == null) {
                clientHandler.send(new AdminMessage("No active board has been set."));
            } else {
               server.getGameModel().deactivateQuestion(clientHandler);
            }
            
            // Changes the game timer status.
        } else if(packet instanceof SetTimerState) {
            if(server.getActiveBoard() == null) {
                clientHandler.send(new AdminMessage("No active board has been set."));
            } else {
                switch(((SetTimerState) packet).getState()) {
                    case START:
                        server.getGameModel().startTimer(clientHandler);
                        break;
                        
                    case PAUSE:
                        server.getGameModel().pauseTimer(clientHandler);
                        break;
                        
                    case END:
                        server.getGameModel().endTimer(clientHandler);
                        break;
                }
            }
        } else if(packet instanceof SetPulpitNickname) {
            SetPulpitNickname nickname = (SetPulpitNickname) packet;
            server.getPulpitModel().setNickname(nickname.getName(), nickname.getNickname());
            sendPulpitInformation();
        } else if(packet instanceof SetPulpitScore) {
            SetPulpitScore pulpetScore = (SetPulpitScore) packet;
            server.getPulpitModel().setScore(pulpetScore.getName(), pulpetScore.getScore());
            sendPulpitInformation();
        } else if(packet instanceof ModifyTimer) {
            
            ModifyTimer modPacket = (ModifyTimer) packet;
            if(!server.getGameModel().getGameTimer().setTotal(modPacket.getTotal())) {
                clientHandler.send(new AdminMessage("Cannot modify the timer while the timer is running."));
                return;
            }
            server.getGameModel().getGameTimer().setElapsed(modPacket.getElapsed());
            sendGameTimer(server.getGameModel().getGameTimer(), server.getGameModel().isTimerButtonsEnabled());
            
        } else if(packet instanceof JudgePulpit) {
            JudgePulpit decision = (JudgePulpit) packet;
            if(pulpetAnswering != null && judgeID == decision.getJudgeID()) {
                judgeID++;
                server.getGameModel().judgePulpit(pulpetAnswering, decision.wasRight());
            }
            
            // Following packets are related to board editing.
        } else if(packet instanceof SetBoardNameByName) {
            SetBoardNameByName boardNamePacket = (SetBoardNameByName) packet;
            AdminPacket replyPacket = server.getBoardManager().setBoardName(boardNamePacket.oldName, boardNamePacket.newName);
            if(replyPacket != null) {
                sendToAllAdmins(replyPacket);
            }
            // TODO: send error packet if null.
            
        } else if(packet instanceof RemoveBoards) {
            AdminPacket replyPacket = server.getBoardManager().removeBoards(((RemoveBoards) packet).boardNames);
            if(replyPacket != null) {
                sendToAllAdmins(replyPacket);
            }
            // Todo: send error packet if null.
            
        } else if(packet instanceof GetBoard) {
            Board message = new Board();
            message.name = ((GetBoard) packet).boardName;
            message.categories = server.getBoardManager().getBoard(message.name);
            if(message.categories != null) {
                clientHandler.send(message);
            }
            // Todo: send error packet if null.
            
        } else if(packet instanceof SaveBoard)  {
            SaveBoard message = (SaveBoard) packet;
            AdminPacket reply = server.getBoardManager().saveBoard(message.boardName, message.categories);
            if(reply != null) {
                sendToAllAdmins(reply);
            }
        } else if(packet instanceof GetBoardNames) {
            BoardNames names = new BoardNames();
            names.boardNames = server.getBoardManager().getBoardNames();
            clientHandler.send(names);
            
        }
        
    }
    
    private void sendToAllAdmins(AdminPacket replyPacket) {
        for(ClientHandler ch : server.getUserManager().getAdmins().keySet()) {
            ch.send(replyPacket);
            
        }
    }
    
    /**
     *  Called by PulpetModel when a pulpet wants to answer.
     */
    public void pulpitRequestToAnswer(PulpitAccount account) {
        pulpetAnswering = account;
        sendPulpitInformation();
        sendToAllAdmins(new PulpitAnswer(account.getName(), judgeID));
    }
    
    public void sendPulpitInformation() {
        TreeMap<String, PulpitStatus> pulpets = new TreeMap<String, PulpitStatus>();
        for (PulpitAccount pulpet : server.getUserManager().getPulpits().values()) {
            pulpets.put(pulpet.getName(), pulpet.getPulpitStatus());
        }
        sendToAllAdmins(new PulpitInformation(pulpets));
    }
    
    /**
     *  Called by usermanager when after an AuthorizationSuccessful packet has been sent.
     *  Makes sure that the admin has the right game state right away.
     */
    public void initAdmin(ClientHandler clientHandler) {
        if(server.getActiveBoard() != null)
            clientHandler.send(server.getActiveBoard());
        
        TreeMap<String, PulpitStatus> pulpets = new TreeMap<String, PulpitStatus>();
        for (PulpitAccount pulpet : server.getUserManager().getPulpits().values()) {
            pulpets.put(pulpet.getName(), pulpet.getPulpitStatus());
        }
        
        clientHandler.send(new PulpitInformation(pulpets));
        GameTimer gameTimer = server.getGameModel().getGameTimer();
        clientHandler.send(new GameTimerStatusChange(gameTimer.isRunning(), gameTimer.getTotal(), gameTimer.getElapsed(), server.getGameModel().isTimerButtonsEnabled()));
        
        Logger.getInstance().addClientSubscriber(clientHandler);
    }
    
    public void sendGameTimer(GameTimer gameTimer, boolean buttonsEnabled) {
        sendToAllAdmins(new GameTimerStatusChange(gameTimer.isRunning(), gameTimer.getTotal(), gameTimer.getElapsed(), buttonsEnabled));
    }
    
    public void sendActiveBoard() {
        sendToAllAdmins(server.getActiveBoard());
    }
}