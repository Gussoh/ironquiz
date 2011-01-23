/*
 * LogMessage.java
 *
 * Created on den 7 mars 2007, 00:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.protocol;

/**
 *
 * @author Dukken
 */
public class LogMessage implements Packet {
    
    private String message;
    /** Creates a new instance of LogMessage */
    public LogMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
