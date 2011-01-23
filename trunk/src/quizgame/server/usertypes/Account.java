/*
 * Account.java
 *
 * Created on den 6 mars 2007, 22:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.server.usertypes;

/**
 *  All quizgame accounts should extends this account
 *  @author Dukken
 */
public class Account {
    private String name;    
    private String password; 
    
    /**
     *  Creates a new Account.
     *  @param name The username.
     *  @param password The password.
     */
    public Account(String name, String password) {
        this.name = name;
        this.password = password;
    }
    
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String toString() {
        return "<" + name + ">";
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(Object obj) {
        if(obj instanceof Account) {
            return name.equalsIgnoreCase(((Account) obj).name);
        }
        return false;
    }
}
