/*
 * AuthenticatePulpit.java
 *
 * Created on den 25 november 2006, 15:03
 */

package quizgame.protocol.pulpit;

import java.io.IOException;
import quizgame.protocol.*;

/**
 *
 * @author rheo
 */
public class AuthenticatePulpit extends Authenticate implements PulpitPacket {
    
    public AuthenticatePulpit(String username, String password) throws IOException {
        super(username, password);
    }
}
