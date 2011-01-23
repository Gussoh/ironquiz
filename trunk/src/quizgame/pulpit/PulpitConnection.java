/*
 * PulpetConnection.java
 *
 * Created on den 25 november 2006
 */

package quizgame.pulpit;

import java.io.IOException;
import javax.swing.SwingUtilities;
import quizgame.common.ClientConnection;
import quizgame.protocol.Packet;
import quizgame.protocol.pulpit.AuthenticatePulpit;
import quizgame.protocol.pulpit.PulpitAnswer;
import quizgame.protocol.pulpit.PulpitStatus;

/**
 *
 * @author rheo
 */
public class PulpitConnection extends ClientConnection {
    private PulpitPanel pulpetPanel;
    
    /**
     * Creates a new instance of MainScreenConnection
     * @param serverHost address to the server to fetch remote objects from
     * @param username username to use when authenticating to the server
     * @param password password to use when authenticating to the server
     */
    public PulpitConnection(String serverHost, String username, String password, PulpitPanel pulpetPanel) throws IOException {
        super(serverHost, new AuthenticatePulpit(username, password));
        this.pulpetPanel = pulpetPanel;
    }
    
    public void answer() {
        try {
            writeObject(new PulpitAnswer());
        } catch(IOException ex) {
            // TODO This is a problem. Do something.
        }
    }
    
    protected void handleObject(final Packet object) {
        if(object instanceof PulpitStatus) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    pulpetPanel.setStatus((PulpitStatus) object);
                    pulpetPanel.repaint();
                }
            });
        }
    }
}
