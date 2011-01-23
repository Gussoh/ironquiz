/*
 * ClientHandler.java
 *
 * Created on den 24 november 2006
 */

package quizgame.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import quizgame.protocol.Authenticate;
import quizgame.protocol.Logout;
import quizgame.protocol.Packet;
import quizgame.protocol.admin.AdminPacket;
import quizgame.protocol.pulpit.PulpitPacket;
import quizgame.protocol.typer.TyperPacket;

/**
 *
 * @author rheo
 */
public class ClientHandler extends Thread {
  
    private AccountManager.ClientRole clientRole = AccountManager.ClientRole.NOT_AUTHENTICATED;
    private ObjectInputStream objectInput;
    private ObjectOutputStream objectOutput;
    
    /** The configuration for the server this client is associated with */
    private Server server;
    
    /** Socket for the connection to the client handled by this */
    private Socket socket;
    private String ip;
    
    private String accountName = null; // Used when ClientRole is TYPER
    private Semaphore closeConnectionSemaphore = new Semaphore(1), queueSemaphore = new Semaphore(0);
    private ConcurrentLinkedQueue<Packet> sendQueue = new ConcurrentLinkedQueue<Packet>();
    
    /** Creates a new instance of ClientHandler */
    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        ip = socket.getInetAddress().getHostAddress();
    }
    
    public void run() {
        try {
            objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectInput = new ObjectInputStream(socket.getInputStream());
            Logger.getInstance().println("ClientHandler.run(): New connection with " + this);
            createSender();
                       
            receive();
        } catch (IOException ex) {
        }
        
        closeConnection();
        
    }
    
    /**
     *  This method creates the thread responsible for sending messages.
     */
    private void createSender() {
        new Thread(new Runnable() {
            public void run() {
                
                for(;;) {
                    queueSemaphore.acquireUninterruptibly();
                    Packet packet = sendQueue.poll();
                    if(packet instanceof Logout) {
                        closeConnection();
                        return;
                    }
                    try {
                                               
                        objectOutput.writeObject(packet);
                        objectOutput.flush();
                        objectOutput.reset();
                    } catch (IOException ex) {
                        Logger.getInstance().println("ClientHandler.sender(): " + this + " caused exception: " + ex);
                        Logger.getInstance().println("Packet that was beeing serialized was: " + packet.getClass().getName());
                        ex.printStackTrace();
                        return;
                    }
                }
            }
        }).start();
        
    }
    
    /**
     *  Places a packet on this clientHandler's sendQueue.
     *  @param packet the packet to send.
     */
    public void send(Packet packet) {
        sendQueue.offer(packet);
        queueSemaphore.release();
    }
    
    /**
     *  Reveives packet from the socket. All packets except Logout will be added to the server job queue.
     *  Once this method is called, it will never return until an error occurs or the user logs out.
     */
    private void receive() {
        for(;;) {
            Packet packet;
            try {
                packet = (Packet) objectInput.readObject();
            } catch (ClassNotFoundException ex) {
                return;
            } catch (ClassCastException ex) {
                return;
            } catch (IOException ex) {
                return;
            }
            
            
            if(packet instanceof Authenticate && clientRole == AccountManager.ClientRole.NOT_AUTHENTICATED) {
                server.addJob(this, packet);
            } else if(packet instanceof AdminPacket && clientRole == AccountManager.ClientRole.ADMIN) {
                server.addJob(this, packet);
            } else if(packet instanceof PulpitPacket && clientRole == AccountManager.ClientRole.PULPIT) {
                server.addJob(this, packet);
            } else if(packet instanceof TyperPacket && clientRole == AccountManager.ClientRole.TYPER) {
                server.addJob(this, packet);
            } else if(packet instanceof Logout) {
                send(packet); // Tell sender thread to die as well. Sender will call closeConnection().
                return;
            }
        }
    }
    
    /**
     *  Returns this clientHandlers clientRole. The clientRole affects what packets will be added to the job queue.
     */
    public AccountManager.ClientRole getClientRole() {
        return clientRole;
    }
    
    /**
     *  Sets this clientHandler clientRole. The clientRole affects what packets will be added to the job queue.
     *  For security reasons, this method should only be called from the AccountManager.
     */
    public void setClientRole(AccountManager.ClientRole clientRole) {
        this.clientRole = clientRole;
    }
    
    /**
     *  Returns the remote computer ip-address as a string.
     */
    public String getIp() {
        return ip;
    }
        /*
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
         
         */
    
    /**
     *  Puts a LogoutPacket on the jobqueue. Closes the input/ouput streams. Closes the socket.
     *  A semaphore will ensure this method can only run once.
     */
    public void closeConnection() {
        if(closeConnectionSemaphore.tryAcquire()) {
            
            server.addJob(this, new Logout());

            try {
                objectOutput.close();
            } catch (IOException ex) {
            }
            try {
                objectInput.close();
            } catch (IOException ex) {
            }
            try {
                socket.close();
            } catch (IOException ex) {
            }
            Logger.getInstance().println("ClientHandler.closeConnection(): Closed connection to " + this);
        }
    }
    
    public String toString() {
        switch(clientRole) {
            case ADMIN:
                return "[Admin @ " + ip + "]";
            case MAIN_SCREEN:
                return "[Mainscreen @ " + ip + "]";
            case PULPIT:
                return "[Pulpit @ " + ip + "]";
            case TYPER:
                return "[Typer @ " + ip + "]";
            case NOT_AUTHENTICATED:
                return "[NotAuthenticated @ " + ip + "]";
        }
        return "";
    }
    
}
