/*
 * TyperModeInactiveQuestion.java
 *
 * Created on den 9 mars 2007, 23:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.protocol.typer;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import quizgame.typer.Typer;

/**
 *
 * @author Gussoh
 */
public class TyperModeInactiveQuestion extends TyperMode {
    private String lastAnswer;
    
    /**
     * @param lastQuestionAnswer The answer(s) to the last question. Set to null for no last answer.
     */
    public TyperModeInactiveQuestion(String lastQuestionAnswer) {
        lastAnswer = lastQuestionAnswer;
    }
    
    public void run(Typer t) {        
        JLabel text;
        if(lastAnswer != null) {
            text = new JLabel("<html>The answer to the last question was: <br><br><i>" + lastAnswer + "</i><br><br>Waiting for next question...</html>");
        } else {
            text = new JLabel("<html>Waiting for question..</html>");
        }
        
        text.setVerticalAlignment(JLabel.CENTER);
        text.setHorizontalAlignment(JLabel.CENTER);
        
        t.getMainPanel().add(text, BorderLayout.CENTER);
        
        t.getMainPanel().validate();
    }
}
