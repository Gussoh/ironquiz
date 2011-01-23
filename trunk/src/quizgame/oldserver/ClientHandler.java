/*
 * ClientHandler.java
 *
 * Created on den 24 november 2006
 */

package quizgame.oldserver;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



/**
 *
 * @author rheo
 */
public class ClientHandler extends Thread {
    private enum ClientRole {NOT_AUTHENTICATED, ADMIN, MAIN_SCREEN, PULPET, TYPER};
    /** The challange sent. The challange is appended to the user account password when hashing. */
    private byte[] challange = new byte[16];
    private ClientRole clientRole = ClientRole.NOT_AUTHENTICATED;
    private ObjectInputStream objectInput;
    private ObjectOutputStream objectOutput;
    /** The configuration for the server this client is associated with */
    private Server server;
    /** Socket for the connection to the client handled by this */
    private Socket socket;
    
    private String accountName = null; // Used when ClientRole is TYPER
    
    /** Creates a new instance of ClientHandler */
    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }
 /*   
    public void run() {
        try {
            objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectInput = new ObjectInputStream(socket.getInputStream());
            new Random().nextBytes(challange);
            writeObject(new Challange(challange));
            
            while(recievePacket()) {
            }
        } catch (IOException ex) {
        }
        
        try {
            socket.close();
        } catch (IOException e) {
        }
        
        if(clientRole == ClientRole.ADMIN) {
            server.removeAdminConnection(this);
        } else if (clientRole == ClientRole.MAIN_SCREEN) {
            server.removeMainScreenConnection(this);
        } else if (clientRole == ClientRole.PULPET) {
            server.removePulpetConnection(this);
        }
    }
    
    private boolean recievePacket() throws IOException {
        /*Object inputObject;
        
        try {
            inputObject = objectInput.readObject();
        } catch (ClassNotFoundException ex) {
            return false;
        }
        
        if(inputObject instanceof AuthenticateAdmin && clientRole == ClientRole.NOT_AUTHENTICATED) {
            if(!server.addAdminConnection(this, (Authenticate) inputObject, challange)) {
                return false;
            }
            
            challange = null;
            clientRole = clientRole.ADMIN;
        } else if(inputObject instanceof AuthenticateMainScreen && clientRole == ClientRole.NOT_AUTHENTICATED) {
            if(!server.addMainScreenConnection(this, (Authenticate) inputObject, challange)) {
                return false;
            }
            
            challange = null;
            clientRole = clientRole.MAIN_SCREEN;
        } else if(inputObject instanceof AuthenticatePulpet && clientRole == ClientRole.NOT_AUTHENTICATED) {
            if(!server.addPulpetConnection(this, (Authenticate) inputObject, challange)) {
                return false;
            }
            
            challange = null;
            clientRole = clientRole.PULPET;
            
            // Added by Dukken 2007-01-22
        } else if(inputObject instanceof AuthenticateTyper && clientRole == ClientRole.NOT_AUTHENTICATED) {
            if(!server.addTyperConnection(this, (AuthenticateTyper) inputObject, challange)) {
                return false;
            }
            
            challange = null;
            clientRole = clientRole.TYPER;
            accountName = ((Authenticate) inputObject).username;
        } else if(inputObject instanceof RemoveBoards && clientRole == ClientRole.ADMIN) {
            server.removeBoards(((RemoveBoards) inputObject).boardNames);
        } else if(inputObject instanceof SetBoardNameByName && clientRole == ClientRole.ADMIN) {
            SetBoardNameByName message = (SetBoardNameByName) inputObject;
            server.setBoardName(message.oldName, message.newName);
        } else if(inputObject instanceof GetBoard) {
            Board message = new Board();
            message.name = ((GetBoard) inputObject).boardName;
            message.categories = server.getBoard(message.name);
            if(message.categories != null) {
                writeObject(message);
            }
        } else if(inputObject instanceof SetBoard && clientRole == ClientRole.ADMIN) {
            server.setBoard(((SetBoard) inputObject).boardName);
        } else if(inputObject instanceof SaveBoard && clientRole == ClientRole.ADMIN) {
            SaveBoard message = (SaveBoard) inputObject;
            server.saveBoard(message.boardName, message.categories);
        } else if(inputObject instanceof PulpetAnswer && clientRole == ClientRole.PULPET) {
            // FIXME
        } else {
            // Unknown packet type
            return false;
        }
        
        return true;
    }
    
    public void writeObject(Serializable object) {
        synchronized (objectOutput) {
            try {
                objectOutput.writeObject(object);
            } catch(IOException e1) {
                try {
                    socket.close();
                } catch (IOException e2) {
                }
            }
        }
    }*/
}
