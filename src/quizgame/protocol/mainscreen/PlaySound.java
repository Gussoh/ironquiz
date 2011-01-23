/*
 * PlaySound.java
 *
 * Created on den 9 juni 2007, 17:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.protocol.mainscreen;

import quizgame.protocol.admin.*;

/**
 *
 * @author Gussoh
 */
public class PlaySound implements MainScreenPacket {
    
    private String sound;
    
    /** Creates a new instance of PlaySound */
    public PlaySound(String sound) {
        this.sound = sound;
    }

    public String getSound() {
        return sound;
    }
}
