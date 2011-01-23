/*
 * JudgePulpit.java
 *
 * Created on den 16 maj 2007, 15:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.protocol.admin;

/**
 * Admin-To-Server packet. Decides whether a pulpet answered right or wrong.
 * @author Dukken
 */
public class JudgePulpit implements AdminPacket {

    private boolean wasRight;

    private int judgeID;
    
    /** Creates a new instance of JudgePulpit */
    public JudgePulpit(boolean wasRight, int judgeID) {
        this.wasRight = wasRight;
        this.judgeID = judgeID;
    }

    public boolean wasRight() {
        return wasRight;
    }

    public int getJudgeID() {
        return judgeID;
    }


}
