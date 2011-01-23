/*
 * PulpitAnswer.java
 *
 * Created on den 16 maj 2007, 15:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.protocol.admin;

import quizgame.server.usertypes.PulpitAccount;

/**
 *  Server-to-admin packet. When a pulpet wants to answer a question.
 * @author Dukken
 */
public class PulpitAnswer implements AdminPacket {
    private String name;
    private int answerID;
    
    /** Creates a new instance of PulpitAnswer */
    public PulpitAnswer(String name, int answerID) {
        this.name = name;
        this.answerID = answerID;
    }

    public String getName() {
        return name;
    }

    public int getAnswerID() {
        return answerID;
    }


}
