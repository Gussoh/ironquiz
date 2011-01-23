/*
 * MainScreenGL.java
 *
 * Created on den 9 juni 2007, 00:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.mainscreenGL;

import com.sun.opengl.util.FPSAnimator;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import quizgame.common.ConnectionDialog;
import quizgame.common.Constants;

/**
 *
 * @author eklann
 */
public class MainScreenGL extends JFrame {
    
    /** Creates a new instance of MainScreenGL */
    public MainScreenGL(MainScreenGLConnection mainScreenGLConnection, MainScreenGLPanel mainScreenGLPanel) {
        setUndecorated(true);
        add(mainScreenGLPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(Constants.applicationName + " - Main screen");
        
        pack();
        
        FPSAnimator fpsAnimator = new FPSAnimator(mainScreenGLPanel, 100);
        fpsAnimator.start();
        
}
    
   public static void main(String[] args) {
        new ConnectionDialog(null, false, "Main screen") {
            protected void onCancel() {
                System.exit(0);
            }
            protected void onConnect(final String serverHost, final String username, final String password) {
                
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                final MainScreenGLPanel mainScreenGLPanel = new MainScreenGLPanel(dimension);

                dimension.height /= 3;
                dimension.width /= 3;
                
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            final MainScreenGLConnection mainScreenGLConnection = new MainScreenGLConnection(serverHost, username, password, mainScreenGLPanel);
                            
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    dispose();
                                    new MainScreenGL(mainScreenGLConnection, mainScreenGLPanel).setVisible(true);
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
