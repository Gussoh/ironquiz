/*
 * SetGameState.java
 *
 * Created on den 29 maj 2007, 11:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.protocol.mainscreen;

/**
 *
 * @author Dukken
 */
public class SetGameState implements MainScreenPacket {
    public enum State {INACTIVE, GAME_STARTED, GAME_OVER}
    private State state;
    /** Creates a new instance of SetGameState */
    public SetGameState(State state) {
        this.state = state;
    }

    public SetGameState.State getState() {
        return state;
    }

}
