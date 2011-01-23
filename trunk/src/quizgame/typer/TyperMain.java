/*
 * TyperMain.java
 *
 * Created on March 9, 2007, 4:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.typer;

import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author gsohtell
 */
public class TyperMain {
    
    // For the connecting dialog
    private static JDialog connecting;
    private static TyperConnection connectingTyperConnection;
    
    public static void main(String[] args) {
        try {
            for (LookAndFeelInfo elem : UIManager.getInstalledLookAndFeels()) {
                if(elem.getClassName().equals(UIManager.getSystemLookAndFeelClassName()))
                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
            }
        } catch (Exception e) {
            System.out.println("MainFrame.MainFrame: could not set lookAndFeel, message: " + e.getMessage());
            
        }
        
        new TyperConnectionDialog(null, true) {
            protected void onCancel() {
                System.exit(0);
            }
            protected void onConnect(final String serverHost, final String username, final String password, final boolean newAccount) {
                final TyperConnectionDialog tcd = this;
                
                new Thread(new Runnable() {
                    public void run() {
                        
                        Runnable run = new Runnable() {
                            public void run() {
                                connecting = new JDialog(tcd, "Connecting..", true);
                                connecting.setLocationRelativeTo(tcd);
                                connecting.add(new ConnectingPanel());
                                connecting.pack();
                                connecting.setVisible(true);
                            }
                        };
                        
                        SwingUtilities.invokeLater(run);
                        
                        try {
                            
                            connectingTyperConnection = new TyperConnection(tcd, serverHost, username, password, newAccount);
                            
                            run = new Runnable() {
                                public void run() {
                                    tcd.dispose();
                                }
                            };
                            
                            SwingUtilities.invokeLater(run);
                            
                        } catch (IOException ex) {
                            tcd.connectionFailed(ex.getMessage());
                        } finally {
                            run = new Runnable() {
                                public void run() {
                                    connecting.dispose();
                                }
                            };
                            
                            SwingUtilities.invokeLater(run);
                        }
                    }
                }).start();
            }
        };
    }
}
