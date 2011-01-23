/*
 * AuthenticationFailed.java
 *
 * Created on den 24 november 2006, 19:15
 */

package quizgame.protocol;

public class AuthenticationFailed implements Packet {
    
    private String message;
    
    /**
     * Create authentication failed message
     * @param message the error message
     */
    public AuthenticationFailed(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
