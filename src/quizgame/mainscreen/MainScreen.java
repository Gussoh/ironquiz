/*
 * MainScreen.java
 *
 * Created on den 25 november 2006, 07:12
 */

package quizgame.mainscreen;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import quizgame.common.ConnectionDialog;
import quizgame.common.Constants;

/**
 *
 * @author rheo
 */
public class MainScreen extends JFrame {
    private static final boolean test = false;
    
    /** Creates a new instance of MainScreen */
    public MainScreen(MainScreenConnection mainScreenConnection, MainScreenPanel mainScreenPanel) {
        
        setUndecorated(true);
        add(mainScreenPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(Constants.applicationName + " - Main screen");
        
        pack();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new ConnectionDialog(null, false, "Main screen") {
            protected void onCancel() {
                System.exit(0);
            }
            protected void onConnect(final String serverHost, final String username, final String password) {
                
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                final MainScreenPanel mainScreenPanel = new MainScreenPanel(dimension);
                if(test) {
                    dimension.width /= 2;
                    dimension.height /= 2;
                } else {
                    mainScreenPanel.resized();
                }
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            final MainScreenConnection mainScreenConnection = new MainScreenConnection(serverHost, username, password,mainScreenPanel);
                            
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    dispose();
                                    new MainScreen(mainScreenConnection, mainScreenPanel).setVisible(true);
                                }
                            });
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
                
            }
        };
    }
}
