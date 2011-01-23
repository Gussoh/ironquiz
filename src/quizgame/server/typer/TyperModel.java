/*
 * TyperModel.java
 *
 * Created on den 9 mars 2007, 17:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.server.typer;

import quizgame.protocol.typer.TyperAnswer;
import quizgame.protocol.typer.TyperMode;
import quizgame.protocol.typer.TyperSelectionAnswer;
import quizgame.server.ClientHandler;
import quizgame.server.Server;

/**
 *
 * @author Dukken
 */
public class TyperModel {
    
    private TyperMode currentMode;
    private boolean allowedToAnswer;
    private TyperScoringAlgorithm scoreAlgorithm;

    private Server server;
    
    public TyperModel(Server server) {
        this.server = server;
    }
    
    /**
     *  Sets the current question and sends it out to the typers.
     *  allowedToAnswer is also set to true.
     *  Need to be in NORMAL mode.
     */
    public void setCurrentMode(TyperMode newMode) {
        this.currentMode = newMode;
       
    }

    public TyperScoringAlgorithm getScoreAlgorithm() {
        return scoreAlgorithm;
    }

    public void setScoreAlgorithm(TyperScoringAlgorithm scoreAlgorithm) {
        this.scoreAlgorithm = scoreAlgorithm;
    }

    /**
     *  Sets whether answers are allowed or not from the typers. 
     *  This will be used to decide how to handle future calls to handleTyperAnser().
     */
    public void setAllowedToAnswer(boolean allowedToAnswer) {
        this.allowedToAnswer = allowedToAnswer;
        
    }
    
  
    /**
     *  Sends the current scoretable to all typers.
     */
    public void sendScoreTable() {
        // FIX ME
    }
      
    /**
     *  Evaluates the answer and sets the points according to the TyperAnswer packet.
     *  The function only has effect if allowToAnswer is true.
     */
    public void handleTyperAnswer(TyperAnswer answer) {
        
    }
    
    /**
     *  
     */
    public void handleCompetitorSelectionAnswer(TyperSelectionAnswer answer) {
        
    }
    
    /**
     *  Sends current hi-score and mode to the clientHandler.
     */
    public void updateTyper(ClientHandler clientHandler) {
        
    }

    public void initTyper(ClientHandler clientHandler) {
        
    }
        
}
