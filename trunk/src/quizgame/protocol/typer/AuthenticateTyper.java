package quizgame.protocol.typer;

import java.io.IOException;
import quizgame.protocol.*;

public class AuthenticateTyper extends Authenticate implements TyperPacket {
    public boolean createAccount;
    
    public AuthenticateTyper(String username, String password, boolean createAccount) throws IOException {
        super(username, password);
        this.createAccount = createAccount;
    }
}
