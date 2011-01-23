/*
 * ModifyTimer.java
 *
 * Created on den 16 maj 2007, 21:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.protocol.admin;

/**
 *  Admin-to-Server packet. Change timer properties
 * @author Dukken
 */
public class ModifyTimer implements AdminPacket {

    private int elapsed;
    private int total;
    
    /** Creates a new instance of ModifyTimer */
    public ModifyTimer(int elapsed, int total) {
        this.elapsed = elapsed;
        this.total = total;
    }

    public int getElapsed() {
        return elapsed;
    }

    public int getTotal() {
        return total;
    }

}
