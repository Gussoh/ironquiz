/*
 * PulpitPing.java
 *
 * Created on den 2 mars 2012, 10:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.protocol;

import quizgame.protocol.admin.AdminPacket;
import quizgame.protocol.pulpit.PulpitPacket;

/**
 * Represents a ping from the server to a pulpit and then back to all admins.
 * 
 * @author joale1
 */
public class PulpitPing implements AdminPacket, PulpitPacket {
    public static final int PING_INTERVAL = 3000;
    
    private final String pulpitAccountName;
    private final long timestamp;
    
    public PulpitPing(String pulpitAccountName) {
        this.pulpitAccountName = pulpitAccountName;
        timestamp = System.currentTimeMillis();
    }

    public String getPulpitAccountName() {
        return pulpitAccountName;
    }
    
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "PulpitPing{" + "pulpitAccountName=" + pulpitAccountName + ", timestamp=" + timestamp + '}';
    }
}
