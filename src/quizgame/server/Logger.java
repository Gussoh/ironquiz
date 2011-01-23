/*
 * Logger.java
 *
 * Created on den 7 mars 2007, 00:29
 *
 */

package quizgame.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import quizgame.protocol.LogMessage;
import quizgame.server.usertypes.Account;

/**
 *  A Logger class supporting logging to stdout, ClientHandler's and file.
 *  Singleton pattern used so it easily can be accessed from everywhere.
 * @author Dukken
 */
public class Logger {
    
    private static Logger instance = new Logger();
    private PrintStream fileStream;
    private boolean useConsole;
    private List<ClientHandler> logSubscribers = new LinkedList<ClientHandler>();
    

    private Logger() {
        
    }
    /**
     *  Returns the only instance of this class.
     */
    public static Logger getInstance() {
        return instance;
    }
    
    /**
     *  Set whether log messages should be written to console (stdout) or not.
     */
    public void setUseConsole(boolean useConsole) {
        this.useConsole = useConsole;
    }

    /**
     *  Returns true if log messages are written to console (stdout), otherwise false.
     */
    public boolean isUseConsole() {
        return useConsole;
    }
    
    /**
     *  Prints a new log message. 
     *  Will print to console, clientHandlers or/and to a logfile, depending on this object settings.
     *  @param message The message to log.
     */
    public void println(String message) {
        if(useConsole) {
            System.out.println(message);
        }
        for (ClientHandler c : logSubscribers) {
            c.send(new LogMessage(message));
        }
        if(fileStream != null) {
            fileStream.println(message);
        }
    }
    
    /**
     *  Set a file to be used for log messages.
     *  If another logfile is already in use, it will first be closed before the new file is used.
     *  @param file The file to be used for log messages. If file exist, it will be truncated to 0 size.
     *  @throws FileNotFoundException If some error occurs while opening/creating the file.
     */
    public void setLogfile(File file) throws FileNotFoundException {
        if(fileStream != null)
            closeLogfile();
        
        fileStream = new PrintStream(file);
        println("Logger.setLogfile(): File \"" + file + "\" has been set as new logfile");
    }
    /**
     *  Closes the log file.
     */
    public void closeLogfile() {
        if(fileStream != null) {
            println("Logger.closeLogfile(): Logfile closed.");
            fileStream.close();
        }
    }
    /**
     *  Makes a clientHandler recieve log messages. 
     *  This method does not care about security. 
     *  The caller should check if the clientHandler has the rights to receive log messages or not before calling this method.
     *  @param clientHandler The clientHandler to receive log messages.
     *  @return true if the clientHandler was added to the subscriber list. False if clientHandler already is in the subscriber list.
     */
    public boolean addClientSubscriber(ClientHandler clientHandler) {
        if(logSubscribers.contains(clientHandler)) 
            return false;
        
        logSubscribers.add(clientHandler);
        return true;
    }
    
    /**
     *  Removes a clientHandler from the log subscriber list. 
     *  When this function returns, the specified client will not receive any more LogMessages.
     *  @param clientHandler The clientHandler to remove.
     *  @return true if the clientHandler was removed, false if not existed in list.
     */
    public boolean removeClientSubscriber(ClientHandler clientHandler) {
        return logSubscribers.remove(clientHandler);
    }
}
