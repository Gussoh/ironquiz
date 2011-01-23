/*
 * GameTimerStatusChange.java
 *
 * Created on den 9 maj 2007, 00:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.protocol.admin;

/**
 *  Send to all admins when the game timer has changed.
 * @author Dukken
 */
public class GameTimerStatusChange implements AdminPacket {
    
    private boolean running;
    private int total;
    private int elapsed;
    private boolean buttonsEnabled;
    
    
    /** Creates a new instance of GameTimerStatusChange */
    public GameTimerStatusChange(boolean running, int delay, int elapsed, boolean buttonsEnabled) {
        this.running = running;
        this.total = delay;
        this.elapsed = elapsed;
        this.buttonsEnabled = buttonsEnabled;
    }

    public int getTotal() {
        return total;
    }

    public int getElapsed() {
        return elapsed;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isButtonsEnabled() {
        return buttonsEnabled;
    }
}
