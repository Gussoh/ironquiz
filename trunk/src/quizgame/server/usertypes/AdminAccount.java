/*
 * AdminUser.java
 *
 * Created on den 6 mars 2007, 22:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.server.usertypes;

/**
 *
 * @author Dukken
 */
public class AdminAccount extends Account {
    
    /** Creates a new instance of AdminUser */
    public AdminAccount(String name, String password) {
        super(name, password);
    }
    
}
