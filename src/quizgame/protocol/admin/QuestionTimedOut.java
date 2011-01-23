/*
 * QuestionTimedOut.java
 *
 * Created on den 8 maj 2007, 23:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.protocol.admin;

import quizgame.protocol.Packet;

/**
 *  Used internal by the server when the game timer has timed out.
 * @author Dukken
 */
public class QuestionTimedOut implements Packet {
    private int alarmID;
    /** Creates a new instance of QuestionTimedOut */
    public QuestionTimedOut(int alarmID) {
        this.alarmID = alarmID;
    }

    public int getAlarmID() {
        return alarmID;
    }

}
