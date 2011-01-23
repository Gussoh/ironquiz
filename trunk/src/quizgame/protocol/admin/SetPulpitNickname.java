/*
 * SetPulpitNickname.java
 *
 * Created on May 29, 2007, 2:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.protocol.admin;

/**
 *
 * @author gsohtell
 */
public class SetPulpitNickname implements AdminPacket {
    
    private String name, nickname;

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }
    
    /** Creates a new instance of SetPulpitNickname */
    public SetPulpitNickname(String name, String nickname) {
        this.name = name;
        this.nickname = nickname;
    }
}
