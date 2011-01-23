/*
 * SetPulpitScore.java
 *
 * Created on May 29, 2007, 2:13 PM
 */

package quizgame.protocol.admin;

/**
 *
 * @author gsohtell
 */
public class SetPulpitScore implements AdminPacket {
    
    private String name;
    private int score;
    
    /** Creates a new instance of SetPulpitScore */
    public SetPulpitScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
