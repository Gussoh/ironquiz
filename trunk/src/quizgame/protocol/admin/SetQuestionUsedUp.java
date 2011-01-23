/*
 * SetQuestionUsedUp.java
 *
 * Created on den 4 maj 2007, 00:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.protocol.admin;

/**
 *  Admin-to-server packet. Changes the usedUp status for a question on the active board.
 * @author Dukken
 */
public class SetQuestionUsedUp implements AdminPacket {

    private int questionIndex;
    private int categoryIndex;

    private boolean usedUp;
    
    /** Creates a new instance of SetQuestionUsedUp */
    public SetQuestionUsedUp(int categoryIndex, int questionIndex, boolean usedUp) {
        this.categoryIndex = categoryIndex;
        this.questionIndex = questionIndex;
        this.usedUp = usedUp;
    }

    public int getCategoryIndex() {
        return categoryIndex;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public boolean isUsedUp() {
        return usedUp;
    }

}
