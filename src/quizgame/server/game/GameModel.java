/*
 * GameModel.java
 *
 * Created on den 28 maj 2007, 09:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.server.game;

import quizgame.protocol.admin.QuestionTimedOut;
import quizgame.server.ClientHandler;
import quizgame.server.usertypes.PulpitAccount;

/**
 *
 * @author Dukken
 */
public interface GameModel {
    /**
     *  Should activate a question if the admin is allowed todo so.
     */
    public void activateQuestion(int category, int question, ClientHandler clientHandler);
    
    /**
     *  Should de-activate a question if the admin is allowed todo so. 
     */
    public void deactivateQuestion(ClientHandler clientHandler);
    
    
    public void setQuestionUsedUp(int category, int question, boolean isUsedUp, ClientHandler clientHandler);
    
    public void startTimer(ClientHandler clientHandler);
    
    public void pauseTimer(ClientHandler clientHandler);
    
    public void endTimer(ClientHandler clientHandler);
    
    /**
     *  Called by AdminModel when the admin wants to starts the game.
     *  This should show the board for big screen for example.
     *  Admins will be notified if the game has been started or not depending on the return value.
     *  @param clientHandler The admin responsible for the method call.
     *  @return true if the game is started, otherwise false.
     */
    public boolean startGame(ClientHandler clientHandler);
    
    /**
     *  Called when a pulpet presses the space button and is unlocked.
     */
    public void pulpetPressedButton(PulpitAccount pulpet);
    
    /**
     *  Called by AdminModel when a admin has decided whether a pulpet answered right or wrong.
     */
    public void judgePulpit(PulpitAccount pulpet, boolean wasRight);
    
    public GameTimer getGameTimer();
    public void timedOutListener(QuestionTimedOut questionTimedOut);
    
    public boolean isTimerButtonsEnabled();
}
