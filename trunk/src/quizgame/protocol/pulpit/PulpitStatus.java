/*
 * PulpitStatus.java
 *
 * Created on den 11 maj 2007, 00:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.protocol.pulpit;

/**
 *
 * @author Dukken
 */
public class PulpitStatus implements PulpitPacket {
    public static enum State {LOCKED, UNLOCKED, ANSWERING};
    
    private int score;
    private boolean alwaysLocked;
    private State state;
    private String nickname;

    public void setAlwaysLocked(boolean alwaysLocked) {
        this.alwaysLocked = alwaysLocked;
    }

    public boolean isAlwaysLocked() {
        return alwaysLocked;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    public void addScore(int score) {
        this.score += score;
    }
    
    public PulpitStatus.State getState() {
        return state;
    }

    public void setState(PulpitStatus.State state) {
        this.state = state;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String name) {
        this.nickname = name;
    }

}
