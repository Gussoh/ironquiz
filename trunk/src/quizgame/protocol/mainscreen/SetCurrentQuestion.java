/*
 * SetCurrentQuestion.java
 *
 * Created on den 25 november 2006, 13:40
 */

package quizgame.protocol.mainscreen;

/**
 *
 * @author rheo
 */
public class SetCurrentQuestion implements MainScreenPacket {
    public int categoryIndex;
    public int questionIndex;
    public String question;
    
    public SetCurrentQuestion(int categoryIndex, int questionIndex, String question) {
        this.categoryIndex = categoryIndex;
        this.questionIndex = questionIndex;
        this.question = question;
    }
    

}
