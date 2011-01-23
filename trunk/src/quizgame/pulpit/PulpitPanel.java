/*
 * PulpetPanel.java
 *
 * Created on den 25 november 2006, 15:03
 */

package quizgame.pulpit;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import quizgame.protocol.pulpit.PulpitStatus;

/**
 *
 * @author rheo
 */
public class PulpitPanel extends JPanel {
    private int namePosX;
    private int namePosY;
    private int scorePosY;
    private Color lockColor = new Color(0, 0, 0, 127);
    private Color answerColor = new Color(0, 255, 0, 127);
    private Font nameFont;
    private Font notEnabledFont;
    private Font scoreFont;
    private boolean nameChanged = false;
    
    private PulpitStatus status = new PulpitStatus();
    
    /** Creates a new instance of PulpetPanel */
    public PulpitPanel(Dimension dimension) {
        setPreferredSize(dimension);
        notEnabledFont = new Font("Sans-Serif", Font.BOLD, (int) (dimension.getHeight() / 50));
        scoreFont = new Font("Sans-Serif", Font.BOLD, (int) (dimension.getHeight() / 4));
        status.setNickname("Waiting for server...");
        status.setState(PulpitStatus.State.LOCKED);
    }
    
    public boolean isLocked() {
        return status.getState() == PulpitStatus.State.LOCKED;
    }
    
    public void setStatus(PulpitStatus status) {
        if (! this.status.getNickname().equals(status.getNickname())) {
            nameChanged = true;
        }
        
        this.status = status;
    }
    
    
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setPaint(new GradientPaint(0, 0, new Color(151, 212, 255), 0, getHeight(), Color.WHITE));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        switch(status.getState()) {
            case ANSWERING:
                g.setColor(answerColor);
                g.fillRect(0, 0, getWidth(), getHeight());
                break;
                
            case LOCKED:
                g.setColor(lockColor);
                g.fillRect(0, 0, getWidth(), getHeight());
                break;
        }
        
        g.setColor(Color.BLACK);
        
        if(status.getNickname() != null) {
            if(nameFont == null || nameChanged) {
                System.out.println("Changed font.");
                nameFont = new Font("Sans-Serif", Font.BOLD, (int) Math.min(getHeight() / 5, 800 * getWidth() / g.getFontMetrics(new Font("Sans-Serif", Font.BOLD, 1000)).stringWidth(status.getNickname())));
                g.setFont(nameFont);
                namePosX = (getWidth() - g.getFontMetrics().stringWidth(status.getNickname())) / 2;
                namePosY = 2 * getHeight() / 7 + g.getFontMetrics().getAscent() / 2;
                scorePosY = 9 * getHeight() / 14 + g.getFontMetrics().getAscent() / 2;
                nameChanged = false;
            }
            
            g.setFont(nameFont);
            g.drawString(status.getNickname(), namePosX, namePosY);
            g.setFont(scoreFont);
            g.drawString("" + status.getScore(), (getWidth() - g.getFontMetrics().stringWidth("" + status.getScore())) / 2, scorePosY);
        } else {
            String text = "Waiting for response from server...";

            g.setFont(notEnabledFont);
            g.drawString(text, (getWidth() - g.getFontMetrics().stringWidth(text)) / 2, (getHeight() + g.getFontMetrics().getAscent()) / 2);
        }
    }
}
