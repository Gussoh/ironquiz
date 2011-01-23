/*
 * Question.java
 *
 * Created on den 27 november 2006
 */

package quizgame.common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Note: this class has a natural ordering that is inconsistent with equals.
 * @author rheo
 */
public class Question implements Comparable<Question>, Serializable {
    public int score;
    public String question;
    public String[] answers;
    
    public String toString() {
        return "" + score;
    }
    
    public int compareTo(Question question) {
        return new Integer(score).compareTo(question.score);
    }

}
