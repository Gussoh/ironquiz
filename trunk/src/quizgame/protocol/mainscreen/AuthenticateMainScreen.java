/*
 * AuthenticateMainScreen.java
 *
 * Created on den 24 november 2006, 18:25
 */

package quizgame.protocol.mainscreen;

import java.io.IOException;
import quizgame.protocol.*;

/**
 *
 * @author rheo
 */
public class AuthenticateMainScreen extends Authenticate implements MainScreenPacket {
    
    public AuthenticateMainScreen(String username, String password) throws IOException {
        super(username, password);
    }
}
