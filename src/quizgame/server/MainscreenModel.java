/*
 * MainscreenModel.java
 *
 * Created on den 26 maj 2007, 23:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.server;

import quizgame.common.InGameCategory;
import quizgame.protocol.mainscreen.MainScreenPacket;
import quizgame.protocol.mainscreen.PlaySound;
import quizgame.protocol.mainscreen.SetCategories;
import quizgame.protocol.mainscreen.SetCurrentQuestion;
import quizgame.protocol.mainscreen.SetGameState;
import quizgame.protocol.mainscreen.SetNoCurrentQuestion;
import quizgame.protocol.mainscreen.SetQuestionUsedUp;

/**
 *
 * @author Dukken
 */
public class MainscreenModel {
    
    private Server server;
    private boolean isStarted;
    private SetGameState.State state = SetGameState.State.INACTIVE;
    
    /** Creates a new instance of MainscreenModel */
    public MainscreenModel(Server server) {
        this.server = server;
    }
    
    public void handleMainscreenJob(ClientHandler clientHandler, MainScreenPacket mainScreenPacket) {
        
    }
    
    public void activateQuestion(int categoryIndex, int questionIndex, String question) {
        sendToAllMainscreens(new SetCurrentQuestion(categoryIndex, questionIndex, question));
    }
    
    public void startGame() {
        sendToAllMainscreens(new SetGameState(SetGameState.State.GAME_STARTED));
    }
    
    public void sendActiveBoard(InGameCategory[] categories) {
        sendToAllMainscreens(new SetCategories(categories));
    }
    
    public void deactivateQuestion() {
        sendToAllMainscreens(new SetNoCurrentQuestion());
    }
    
    public void setUsedUp(int categoryIndex, int questionIndex, boolean isUsedUp) {
        sendToAllMainscreens(new SetQuestionUsedUp(categoryIndex, questionIndex, isUsedUp));
    }
    
    public void initMainscreen(ClientHandler clientHandler) {
        if(server.getActiveBoard() == null) {
            clientHandler.send(new SetGameState(SetGameState.State.INACTIVE));
        } else {
            ActiveBoard board = server.getActiveBoard();
            clientHandler.send(new SetCategories(board.getInGameCategories()));
            if(board.isQuestionActive()) {
                clientHandler.send(new SetCurrentQuestion(board.getActiveCategoryIndex(), board.getActiveQuestionIndex(), board.getActiveQuestion().question));
            }
            clientHandler.send(new SetGameState(SetGameState.State.GAME_STARTED));
        }
        
    }
    
    private void sendToAllMainscreens(MainScreenPacket packet) {
        for (ClientHandler elem : server.getUserManager().getMainscreens().keySet()) {
            elem.send(packet);
        }
    }

    public void playSound(String sound) {
        sendToAllMainscreens(new PlaySound(sound));
    }
}
