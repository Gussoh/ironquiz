/*
 * Challange.java
 *
 * Created on den 24 november 2006, 17:21
 */

package quizgame.protocol;

/**
 *
 * @author rheo
 */
public class Challange implements Packet {
    /** The challange data to append to the user account password when hashing. */
    public byte[] challange;
    
    /**
     * Creates a new instance of Challange
     * @param challange the challange data for this to hold
     */
    public Challange(byte[] challange) {
        this.challange = challange;
    }
}
