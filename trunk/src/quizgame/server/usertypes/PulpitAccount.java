/*
 *
 * Created on den 6 mars 2007, 22:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.server.usertypes;

import quizgame.protocol.pulpit.PulpitStatus;


/**
 *
 * @author Dukken
 */
public class PulpitAccount extends Account {

    private PulpitStatus pulpitStatus;

    public void setPulpitStatus(PulpitStatus pulpitStatus) {
        this.pulpitStatus = pulpitStatus;
    }

    public PulpitStatus getPulpitStatus() {
        return pulpitStatus;
    }


    public PulpitAccount(String name, String password) {
        super(name, password);
        this.pulpitStatus = new PulpitStatus();
    }
    
    

    

}
