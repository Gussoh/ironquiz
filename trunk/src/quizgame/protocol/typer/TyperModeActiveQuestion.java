/*
 * TyperModeActiveQuestion.java
 *
 * Created on den 9 mars 2007, 23:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.protocol.typer;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import quizgame.typer.Typer;

/**
 *
 * @author Gussoh
 */
public class TyperModeActiveQuestion extends TyperMode {
    /* For server */
    private String question;
    
    public TyperModeActiveQuestion(String question) {
        this.question = question;
    }
    
    
    /* For Typer */
    transient private boolean submitted = false;
    
    void submitAnswer(final JTextField input, final Typer t) {
        if(submitted)
            return;
        
        submitted = true;
        
        input.setEnabled(false);
        
        new Thread(new Runnable() {
            public void run() {
                t.send(new TyperAnswer(input.getText()));
            }
        }).start();
    }
    
    public void run(final Typer t) {
        JPanel mainPanel = t.getMainPanel();
        
        mainPanel.add(new JLabel("<html><b>" + question + "</b></html>"), BorderLayout.NORTH);
        final JTextField input = new JTextField();
        input.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
            }
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == e.VK_ENTER) {
                    submitAnswer(input, t);
                }
            }
            public void keyTyped(KeyEvent e) {
            }
        });
        
        mainPanel.add(input, BorderLayout.CENTER);
        
        t.getJFrame().validate();
    }
}
