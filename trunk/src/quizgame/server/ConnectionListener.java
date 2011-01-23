/*
 * ConnectionListener.java
 *
 * Created on den 6 mars 2007, 18:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.server;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 *  ConnectionListener class that can maintain multiple listening sockets.
 *  The sockets created by this class are of type SSLSocket. If no cipher is specified, Protocol TLS_DH_anon_WITH_AES_128_CBC_SHA will be used.
 *  With the default cipher suite, no certificates are used, instead the keys are shared by the Diffie-Hellman algorithm. 
 *  Were Diffie and Hellman nice dudes? Maybe. 
 *  @author Dukken
 */
public class ConnectionListener {
    private SSLServerSocketFactory serverFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
    private final Server server;
    private Map<Integer, ActiveServerSocket> activeSockets = Collections.synchronizedMap(new TreeMap<Integer, ActiveServerSocket>());
    
    private String[] enabledCiphers = {"TLS_DH_anon_WITH_AES_128_CBC_SHA"};
    private String[] enabledProtocols = {"TLSv1"};
    
    /** 
     *  Creates a new instance of ConnectionListener 
     *  with default values for ciphers and protocols.
     */
    public ConnectionListener(Server server) {
        this.server = server;       
    }
    
    /**
     *  Creates a new instance of ConnectionListener
     *  @param server Reference to the server.
     *  @param enabledCiphers An array of strings containing the ciphers to be enabled.
     *  @param enabledProtocols An array of strings containing the enabled protocols.
     */
    public ConnectionListener(Server server, String[] enabledCiphers, String[] enabledProtocols) {
        this.server = server;
        this.enabledCiphers = enabledCiphers;
        this.enabledProtocols = enabledProtocols;
        
    }
   
    /**
     *  Creates a new serversocket on the specified port. 
     *  Creates dedicated thread which will take care of incoming connections.
     *  @param port The port to use.
     *  @return true if the serversocket was created successfully.
     *  @throws IOException If a network error occurs when creating the socket.
     */
    public boolean createActiveServerSocket(int port) throws IOException {
        if(activeSockets.containsKey(port)) {
            return false;
        }
        ActiveServerSocket l = new ActiveServerSocket(port);
        activeSockets.put(port, l);
        new Thread(l).start();
        return true;
    }
    
    /**
     *  Returns a map with ports as key and ActiveServerSockets as values.
     *  The map returned will only contain entries which are still "online".
     */
    public Map<Integer, ActiveServerSocket> getActiveServerSockets() {
        return activeSockets;
    }
    
    /**
     *  Closes the ServerSocket on the specified port.
     *  @param port The Serversocket on this port will be closed.
     *  @return true if the serversocket was closed, false if there was no serversocket on that port.
     */
    public boolean closeActiveServerSocket(int port) {
        ActiveServerSocket l = activeSockets.remove(port); 
        if(l == null) {
            return false;
        }
        l.closeSocket();
        return true;
    }
    /**
     *  Class responsible for accepting incoming connection on a specific port.
     *  For each connection which successfully completes the handshake procedure,
     *  a ClientHandler will be created. 
     */
    public class ActiveServerSocket implements Runnable {
        private SSLServerSocket incomingSocket;
        private int port;
        private boolean closeOnAllErrors = false;
        private Semaphore closeSocket = new Semaphore(1);
        
        /**
         *  Returns the port the server socket uses.
         */
        public int getPort() {
            return port;
        }
        
        protected ActiveServerSocket(int port) throws IOException {
            this.port = port;
            this.closeOnAllErrors = closeOnAllErrors;
            
            // Casts IOException if unable to create socket on the specified port
            incomingSocket = (SSLServerSocket) serverFactory.createServerSocket(port);
            
            Logger.getInstance().println("ConnectionListener.listener(): * New connection listener created using port " + port);
            Logger.getInstance().println("ConnectionListener.listener(): * Enabled cipher suites:");
            
            incomingSocket.setEnabledCipherSuites(enabledCiphers);
            for(String name : incomingSocket.getEnabledCipherSuites()) {
                Logger.getInstance().println("ConnectionListener.listener(): * - " + name);
            }
            
            incomingSocket.setEnabledProtocols(enabledProtocols);
            Logger.getInstance().println("ConnectionListener.listener(): * Enabled protocols:");
            for(String name : incomingSocket.getEnabledProtocols()) {
                Logger.getInstance().println("ConnectionListener.listener(): * - " + name);
            }
        }
        
        /**
         *  Returns the closed state of the serversocket
         */
        protected boolean isClosed() {
            return incomingSocket.isClosed();
        }
        
        /**
         *  Closes the serversocket. Can be called internally if a network error occurs or from outside.
         *  A semaphore ensures that the closeSocket() code can only be called once.
         */
        protected void closeSocket() {
            if(closeSocket.tryAcquire()) {
                try {
                    incomingSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                activeSockets.remove(port);
                Logger.getInstance().println("ConnectionListener.listener.closeSocket(): Closed listening socket on port " + port);
            }
        }
        
        public void run() {
            
            while(!incomingSocket.isClosed()) {
                
                SSLSocket connection = null;
                try {
                    connection = (SSLSocket) incomingSocket.accept();
                    connection.startHandshake();
                    
                } catch (IOException ex) {
                    Logger.getInstance().println("ConnectionListener.Listener.run(): " + ex);
                    if(closeOnAllErrors) {
                        closeSocket();
                        break;
                    }
                }
                if(connection != null)
                    new ClientHandler(server, connection).start();
                    
            }
        }
    }
}