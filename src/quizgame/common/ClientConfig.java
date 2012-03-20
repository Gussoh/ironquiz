/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quizgame.common;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import quizgame.server.IniEditor;

/**
 *
 * @author joale1
 */
public class ClientConfig implements Serializable {
    private static final String FILENAME = "client.ini";
    
    private static final String MSG_NO_CLIENT_CONFIG_FILE = "No client config file";
    
    private static final String SECTION_CONNECTION = "connection";
    private static final String OPTION_HOSTNAME = "hostname";
    private static final String OPTION_DEFAULT_HOSTNAME = "";
    
    private static ClientConfig clientConfig;
    
    private IniEditor iniEditor;
    
    
    private ClientConfig() {
        iniEditor = new IniEditor();
        try {
            iniEditor.load(FILENAME);
        } catch (IOException ex) {
            iniEditor.addSection(SECTION_CONNECTION);
            iniEditor.set(SECTION_CONNECTION, OPTION_HOSTNAME, OPTION_DEFAULT_HOSTNAME);
            Logger.getLogger(ClientConfig.class.getName()).log(Level.INFO, MSG_NO_CLIENT_CONFIG_FILE, ex);
        }
    }
    
    public static synchronized ClientConfig getInstance() {
        if(clientConfig == null) {
            clientConfig = new ClientConfig();
        }
        return clientConfig;
    }
    
    public String getHostname() {
        return iniEditor.get(SECTION_CONNECTION, OPTION_HOSTNAME);
    }
    
    public void setHostname(String hostname) {
        iniEditor.set(SECTION_CONNECTION, OPTION_HOSTNAME, hostname);
        try {
            iniEditor.save(FILENAME);
        } catch (IOException ex) {
            Logger.getLogger(ClientConfig.class.getName()).log(Level.INFO, MSG_NO_CLIENT_CONFIG_FILE, ex);
        }
    }
}
