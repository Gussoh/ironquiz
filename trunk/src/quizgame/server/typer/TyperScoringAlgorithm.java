/*
 * TyperScoringAlgorithm.java
 *
 * Created on den 10 mars 2007, 00:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.server.typer;

import quizgame.server.usertypes.TyperAccount;

/**
 *
 * @author Dukken
 */
public interface TyperScoringAlgorithm {
    
    public void setBaseScore(int score);
    
    public void resetAlgorithm();
    
    public void updateScore(int position, TyperAccount account);
    
}
