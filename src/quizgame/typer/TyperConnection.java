/*
 * TyperConnection.java
 *
 * Created on January 22, 2007, 4:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.typer;

import java.io.IOException;
import javax.swing.SwingUtilities;
import quizgame.common.ClientConnection;
import quizgame.protocol.typer.AuthenticateTyper;
import quizgame.protocol.Packet;
import quizgame.protocol.typer.Message;
import quizgame.protocol.typer.TyperMode;
import quizgame.protocol.typer.TyperModeActiveQuestion;
import quizgame.protocol.typer.TyperModeInactiveQuestion;
import quizgame.protocol.typer.TyperPacket;

/**
 *
 * @author gsohtell
 */
public class TyperConnection extends ClientConnection {
    private Typer typer;
    
    /**
     * Creates a new instance of TyperConnection
     * @param serverHost address to the server to fetch remote objects from
     * @param username username to use when authenticating to the server
     * @param password password to use when authenticating to the server
     */
    public TyperConnection(TyperConnectionDialog typerConnectionDialog, String serverHost, String username, String password, boolean createAccount) throws IOException {
        super(serverHost, new AuthenticateTyper(username, password, createAccount));
        this.typer = new Typer(this);
    }
    
    protected void handleObject(Packet packet) {
        if(packet instanceof TyperMode) {
            final TyperMode tm = (TyperMode) packet;
            Runnable sr = new Runnable() {
                public void run() {
                    tm.resetFrame(typer);
                    tm.run(typer);
                }
            };
            SwingUtilities.invokeLater(sr);
        } else if(packet instanceof Message) {
            typer.showMessage((Message) packet);
        } else {
            System.out.println("Received unimplemented packet: ");
        }
    }
    
    public void send(TyperPacket p) {
        try {
            super.writeObject(p);
        } catch (IOException ex) {
            typer.connectionLost();
        }
    }
}
