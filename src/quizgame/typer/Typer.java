/*
 * Typer.java
 *
 * Created on January 22, 2007, 3:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.typer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import quizgame.protocol.typer.Message;
import quizgame.protocol.typer.TyperPacket;

/**
 *
 * @author gsohtell
 */
public class Typer {
    
    private JFrame jf;
    private JPanel mainPanel = new JPanel();
    private boolean hasAnswered = true;
    private TyperConnection tc;
    
    public Typer(TyperConnection typerConnection) {
        tc = typerConnection;
        
        jf = new JFrame("IronQuiz Client");
        jf.setLayout(new BorderLayout());
        jf.add(mainPanel);
        jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);
        jf.setPreferredSize(new Dimension(400, 300));
        
        jf.pack();
        jf.setVisible(true);
    }
    
    public void send(TyperPacket p) {
        tc.send(p);
    }
    
    public JFrame getJFrame() {
        return jf;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
    
    public void connectionLost() {
        new JOptionPane("Connection lost to server!");
        System.exit(1);
    }
    
    void showMessage(Message message) {
        message.showJOptionPane(jf);
    }
}
