/*
 * AdminMessage.java
 *
 * Created on den 14 maj 2007, 11:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.protocol.admin;

/**
 *  Sent to an admin if an error occurs.
 * @author Dukken
 */
public class AdminMessage implements AdminPacket {
    String message;
    /** Creates a new instance of AdminMessage */
    public AdminMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


}
