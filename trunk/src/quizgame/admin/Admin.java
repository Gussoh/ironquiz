/*
 * Admin.java
 *
 * Created on den 26 november 2006
 */

package quizgame.admin;

import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import quizgame.common.ConnectionDialog;
import quizgame.common.Constants;

/**
 *
 * @author rheo
 */
public class Admin extends JFrame {
    BoardManagerPanel boardManager;
    private static AdminConnection adminConnection;
    private ActiveBoardPanel activeBoardPanel;
    private AdminConsole adminConsole;
    
    /** Creates a new instance of Admin */
    public Admin(AdminConnection adminConnection) {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        boardManager = new BoardManagerPanel(adminConnection);
        tabbedPane.addTab("Current board", activeBoardPanel = new ActiveBoardPanel(adminConnection, this));
        tabbedPane.addTab("Boards", boardManager);
        tabbedPane.addTab("Console", adminConsole = new AdminConsole(adminConnection));
        
        addKeyListener(activeBoardPanel);
        
        add(tabbedPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(Constants.applicationName + " - Admin");
        pack();
        
        adminConnection.setAdmin(this);
        adminConnection.getBoardNames();
    }

    public AdminConsole getAdminConsole() {
        return adminConsole;
    }

    public ActiveBoardPanel getActiveBoardPanel() {
        return activeBoardPanel;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        
        new ConnectionDialog(null, false, "Admin") {
            protected void onCancel() {
                System.exit(0);
            }
            protected void onConnect(final String serverHost, final String username, final String password) throws IOException {
                
                final ConnectionDialog dialog = this;
                Thread connectThread = new Thread() {
                    public void run() {
                        try {
                            adminConnection = new AdminConnection(serverHost, username, password);
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    dispose();
                                    new Admin(adminConnection).setVisible(true);
                                }
                            });
                            
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        
                    }
                };
                connectThread.start();
                
            }
        };
    }
}
