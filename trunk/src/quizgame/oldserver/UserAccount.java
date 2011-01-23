/*
 * UserAccount.java
 *
 * Created on den 24 november 2006, 18:50
 */

package quizgame.oldserver;

import java.util.EnumSet;

/**
 *
 * @author rheo
 */
public class UserAccount {
    public enum Role {ADMIN, MAIN_SCREEN, PULPIT};
    String password;
    EnumSet<Role> roles;
}
