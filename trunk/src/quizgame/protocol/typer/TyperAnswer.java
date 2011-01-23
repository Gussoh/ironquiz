/*
 * TyperAnswer.java
 *
 * Created on den 9 mars 2007, 23:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.protocol.typer;

/**
 *  Typers uses this packet to answer on TyperQuestions.
 *  Typers can 
 * @author Dukken
 */
public class TyperAnswer implements TyperPacket {
    private String answer;
    /** Creates a new instance of TyperAnswer */
    public TyperAnswer(String answer) {
        this.answer = answer;
    }
    
}
