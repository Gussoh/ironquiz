/*
 * PulpitInformation.java
 *
 * Created on den 16 maj 2007, 15:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.protocol.admin;

import java.util.TreeMap;
import quizgame.protocol.pulpit.PulpitStatus;

/**
 *  Server-to-admin packet. Sent to all admins when a pulpet has changed.
 * @author Dukken
 */
public class PulpitInformation implements AdminPacket {
    
    TreeMap<String, PulpitStatus> pulpets;
    /** Creates a new instance of PulpitInformation */
    public PulpitInformation(TreeMap<String, PulpitStatus> pulpets) {
        this.pulpets = pulpets;
    }

    public TreeMap<String, PulpitStatus> getPulpets() {
        return pulpets;
    }

}
