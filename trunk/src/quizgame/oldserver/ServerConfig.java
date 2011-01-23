/*
 * ServerConfig.java
 *
 * Created on den 24 november 2006, 16:05
 */

package quizgame.oldserver;

import java.io.File;
import java.io.Serializable;
import java.util.EnumSet;
import java.util.TreeMap;

/**
 *
 * @author rheo
 */
public class ServerConfig implements Serializable {
    public int listenPort = 8888;
    public File boardDirectory = new File("boards");
    public TreeMap<String,UserAccount> userAccounts = new TreeMap<String,UserAccount>();
    
    public ServerConfig() {
        UserAccount userAccount = new UserAccount();
        userAccount.password = "";
        userAccount.roles = EnumSet.allOf(UserAccount.Role.class);
        userAccounts.put("", userAccount);
    }
}
