/*
 * Pulpet.java
 *
 * Created on den 25 november 2006, 14:58
 */

package quizgame.pulpit;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import quizgame.common.ConnectionDialog;
import quizgame.common.Constants;

/**
 *
 * @author rheo
 */
public class Pulpit extends JFrame {
    /** Creates a new instance of Pulpit */
    
    private static final int CLICK_TIME_OUT = 1000;
    private long timeStampLastClick = 0;
    
    public Pulpit(final PulpitConnection pulpitConnection, final PulpitPanel pulpitPanel) {
        
        add(pulpitPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(Constants.applicationName + " - Pulpit");
        setUndecorated(true);
        pack();
        
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                long now = System.currentTimeMillis();
                if(e.getKeyCode() == KeyEvent.VK_SPACE && !pulpitPanel.isLocked() 
                        && (now - timeStampLastClick) > CLICK_TIME_OUT) {
                    pulpitConnection.answer();
                    timeStampLastClick = now;
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                long now = System.currentTimeMillis();
                if(!pulpitPanel.isLocked() 
                        && (now - timeStampLastClick) > CLICK_TIME_OUT) {
                    pulpitConnection.answer();
                    timeStampLastClick = now;
                }
            }
        });
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new ConnectionDialog(null, false, "Pulpit") {
            
            protected void onCancel() {
                System.exit(0);
            }
            protected void onConnect(final String serverHost, final String username, final String password) throws IOException {
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                //Dimension dimension = new Dimension(400,300);
                final PulpitPanel pulpitPanel = new PulpitPanel(dimension);
                
                final ConnectionDialog dialog = this;
                Thread connectThread = new Thread() {
                    public void run() {
                        try {
                            final PulpitConnection pulpitConnection = new PulpitConnection(serverHost, username, password, pulpitPanel);
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    
                                    dispose();
                                    new Pulpit(pulpitConnection, pulpitPanel).setVisible(true);
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
