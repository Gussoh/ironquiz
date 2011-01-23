/*
 * AuthenticationManager.java
 *
 * Created on den 6 mars 2007, 22:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import quizgame.protocol.Authenticate;
import quizgame.protocol.AuthenticationFailed;
import quizgame.protocol.AuthenticationSuccessful;
import quizgame.protocol.Logout;
import quizgame.protocol.admin.AuthenticateAdmin;
import quizgame.protocol.mainscreen.AuthenticateMainScreen;
import quizgame.protocol.pulpit.AuthenticatePulpit;
import quizgame.protocol.typer.AuthenticateTyper;
import quizgame.server.usertypes.Account;
import quizgame.server.usertypes.AccountDatabase;
import quizgame.server.usertypes.AdminAccount;
import quizgame.server.usertypes.MainscreenAccount;
import quizgame.server.usertypes.PulpitAccount;
import quizgame.server.usertypes.TyperAccount;

/**
 *  The account manager keeps track of all connected users.
 *  It takes care of login and logouts.
 *  It is not recommended to put or remove from the account maps outside of this class.
 * @author Dukken
 */
public class AccountManager {
    public enum ClientRole {NOT_AUTHENTICATED, ADMIN, MAIN_SCREEN, PULPIT, TYPER}; // Added typer 2007-01-22
    
    private Server server;
    private Map<ClientHandler, PulpitAccount> pulpits = new HashMap<ClientHandler, PulpitAccount>();
    private Map<ClientHandler, TyperAccount> typers = new HashMap<ClientHandler, TyperAccount>();
    private Map<ClientHandler, AdminAccount> admins = new HashMap<ClientHandler, AdminAccount>();
    private Map<ClientHandler, MainscreenAccount> mainscreens = new HashMap<ClientHandler, MainscreenAccount>();
    
    private AccountDatabase database = new AccountDatabase();
    /**
     *  Creates a new instance of the AccountManager.
     */
    public AccountManager(Server server) {
        this.server = server;
        
        if(ServerConfig.getInstance().getBoolean("Use account-file", "loadAtStartup")) {
            try {
                
                InputStreamReader reader = new InputStreamReader(new FileInputStream(ServerConfig.getInstance().getString("Use account-file", "accountFile")),
                        ServerConfig.getInstance().getString("Use account-file", "charset"));
                
                database.readAccounts(reader, false);
            } catch(IOException ex) {
                Logger.getInstance().println("AccountManager: " + ex);
            }
        }
    }
    
    /** Handles all Authentication jobs (logins that is).
     *  @param clientHandler clientHandler that requested this job.
     *  @param packet The authentication packet.
     */
    public void handleAuthenticateJob(ClientHandler clientHandler, Authenticate packet) {
        // This could happen if a bad client requests two or more authentications, one after another.
        if(clientHandler.getClientRole() != ClientRole.NOT_AUTHENTICATED)
            return;
        
        if(packet instanceof AuthenticateAdmin) {
            if(authenticate(clientHandler, admins, ClientRole.ADMIN, packet)) {
                server.getAdminModel().initAdmin(clientHandler);
            }
        } else if(packet instanceof AuthenticateMainScreen) {
            if(authenticate(clientHandler, mainscreens, ClientRole.MAIN_SCREEN, packet)) {
                server.getMainscreenModel().initMainscreen(clientHandler);
            }
        } else if(packet instanceof AuthenticatePulpit) {
            if(authenticate(clientHandler, pulpits, ClientRole.PULPIT, packet)) {
                server.getPulpitModel().initPulpit(clientHandler);
            }
        } else if(packet instanceof AuthenticateTyper) {
            if(authenticate(clientHandler, typers, ClientRole.TYPER, packet)) {
                server.getTyperModel().initTyper(clientHandler);
            }
        }
    }
    
    /**
     *  Takes care of Logoutpackets. Will remove the user from the right accountMap.
     */
    public void handleLogoutJob(ClientHandler clientHandler, Logout packet) {
        removeUser(clientHandler);
    }
    
    /**
     *  Returns a map with all connected admins as <ClientHandler -> AdminAccount> entries.
     */
    public Map<ClientHandler, AdminAccount> getAdmins() {
        return admins;
    }
    
    /**
     *  Returns a map with all connected Mainscreens as <ClientHandler - MainScreen> entries.
     */
    public Map<ClientHandler, MainscreenAccount> getMainscreens() {
        return mainscreens;
    }
    
    public Map<ClientHandler, PulpitAccount> getPulpits() {
        return pulpits;
    }
    
    public Map<ClientHandler, TyperAccount> getTypers() {
        return typers;
    }
    
    private boolean authenticate(ClientHandler clientHandler, Map accountMap, ClientRole role, Authenticate authenticate) {
        // TODO: verybroken
        String loginIncorrectMessage = "Username or password is incorrect.";
        String alreadyLoggedInMessage = "That username is already logged in.";
        
        
        
        Account account = createAccount(role, authenticate.getUsername(), new String(authenticate.getPasswordHash()));
        if(account == null) {
            clientHandler.send(new AuthenticationFailed(loginIncorrectMessage));
            return false;
        }
        
        
        // Check if the user is already logged in.
        if(accountMap.containsValue(account)) {
            clientHandler.send(new AuthenticationFailed(alreadyLoggedInMessage));
            return false;
        }
        
        
        if(ServerConfig.getInstance().getBoolean("Use account-file", role.toString())) {
            if(database.checkPassword(account.getName(), account.getPassword(), role)) {
                accountMap.put(clientHandler, account);
                clientHandler.setClientRole(role);
                clientHandler.send(new AuthenticationSuccessful());
                return true;
            } else {
                clientHandler.send(new AuthenticationFailed(loginIncorrectMessage));
                return false;
            }
        } else {
            accountMap.put(clientHandler, account);
            clientHandler.setClientRole(role);
            clientHandler.send(new AuthenticationSuccessful());
            return true;
        }
        
    }
    
    private Account createAccount(AccountManager.ClientRole role, String username, String password) {
        
        switch(role) {
            case ADMIN:
                return new AdminAccount(username, password);
            case MAIN_SCREEN:
                return new MainscreenAccount(username, password);
            case PULPIT:
                return new PulpitAccount(username, password);
            case TYPER:
                return new TyperAccount(username, password);
        }
        return null;
    }
    
    private void removeUser(ClientHandler clientHandler) {
        Account account;
        
        switch(clientHandler.getClientRole()) {
            case ADMIN:
                account = admins.remove(clientHandler);
                Logger.getInstance().removeClientSubscriber(clientHandler);
                break;
            case MAIN_SCREEN:
                account = mainscreens.remove(clientHandler);
                break;
            case PULPIT:
                account = pulpits.remove(clientHandler);
                break;
            case TYPER:
                account = typers.remove(clientHandler);
                break;
            default:
                return;
        }
        if(account == null)
            Logger.getInstance().println("AccountManager.removeUser(): No entry found for connection [" + clientHandler.getIp() + "].");
        else
            Logger.getInstance().println("AccountManager.removeUser(): [" + clientHandler.getIp() + "] - <" + account.getName() + "> has been logged out.");
        
        
    }
    
    public boolean addLogSubscriber(ClientHandler clientHandler) {
        Account account = admins.get(clientHandler);
        if(account != null) {
            boolean wasAdded = Logger.getInstance().addClientSubscriber(clientHandler);
            if(wasAdded) {
                Logger.getInstance().println("AccountManager.addLogSubscriber(): <" + account + ">");
                return true;
            }
        }
        return false;
    }
    
    
    
    
}
