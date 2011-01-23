/*
 * SetQuestionUsed.java
 *
 * Created on den 25 november 2006, 12:30
 */

package quizgame.protocol.mainscreen;

/**
 *
 * @author rheo
 */
public class SetQuestionUsedUp implements MainScreenPacket {
    public boolean usedUp;
    public int categoryIndex;
    public int questionIndex;
    
    public SetQuestionUsedUp(int categoryIndex, int questionIndex, boolean usedUp) {
        this.categoryIndex = categoryIndex;
        this.questionIndex = questionIndex;
        this.usedUp = usedUp;
    }
}
