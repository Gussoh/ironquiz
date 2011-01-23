/*
 * ChangeGameTimer.java
 *
 * Created on den 10 maj 2007, 23:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.protocol.admin;

/**
 *
 * @author Dukken
 */
public class SetTimerState implements AdminPacket {
    public static enum State {START, PAUSE, END}
    private State state;
    /** Creates a new instance of ChangeGameTimer */
    public SetTimerState(State state) {
        this.state = state;
    }

    public SetTimerState.State getState() {
        return state;
    }

}
