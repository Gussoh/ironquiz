/*
 * AuthenticateAdmin.java
 *
 * Created on den 24 november 2006, 18:49
 */

package quizgame.protocol.admin;

import java.io.IOException;
import quizgame.protocol.*;

/**
 *
 * @author rheo
 */
public class AuthenticateAdmin extends Authenticate implements AdminPacket {
    
    public AuthenticateAdmin(String username, String password) throws IOException {
        super(username, password);
    }
}
