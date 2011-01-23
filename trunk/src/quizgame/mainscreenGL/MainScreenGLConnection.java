/*
 * MainScreenGLConnection.java
 *
 * Created on den 8 juni 2007, 23:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.mainscreenGL;

import java.io.IOException;
import javax.swing.SwingUtilities;
import quizgame.common.ClientConnection;
import quizgame.protocol.Packet;
import quizgame.protocol.admin.SetQuestionUsedUp;
import quizgame.protocol.mainscreen.AuthenticateMainScreen;
import quizgame.protocol.mainscreen.SetCategories;
import quizgame.protocol.mainscreen.SetCurrentQuestion;
import quizgame.protocol.mainscreen.SetNoCurrentQuestion;

/**
 *
 * @author eklann
 */
public class MainScreenGLConnection extends ClientConnection {
    private MainScreenGLPanel mainScreenGLPanel;
    
    /**
     * Creates a new instance of MainScreenConnection
     * @param serverHost address to the server to fetch remote objects from
     * @param username username to use when authenticating to the server
     * @param password password to use when authenticating to the server
     */
    public MainScreenGLConnection(String serverHost, String username, String password, MainScreenGLPanel mainScreenGLPanel) throws IOException {
        super(serverHost, new AuthenticateMainScreen(username, password));
        this.mainScreenGLPanel = mainScreenGLPanel;
    }
    
    
    protected void handleObject(final Packet object) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
                if(object instanceof SetCategories) {
                    mainScreenGLPanel.setCategories(((SetCategories) object).categories);
                    //mainScreenPanel.setCategories(((SetCategories) object).categories);
                } else if(object instanceof SetQuestionUsedUp) {
                    SetQuestionUsedUp data = (SetQuestionUsedUp) object;
                    //mainScreenPanel.setQuestionUsedUp(data.categoryIndex, data.questionIndex, data.usedUp);
                } else if(object instanceof SetCurrentQuestion) {
                    //SetCurrentQuestion data = (SetCurrentQuestion) object;
                    //mainScreenPanel.setCurrentQuestion(data.categoryIndex, data.questionIndex, data.question);
                } else if(object instanceof SetNoCurrentQuestion) {
                    //mainScreenPanel.setNoCurrentQuestion();
                } else {
                    // TODO Unrecognized object recieved. This is a problem. Do something.
                }
            }
        });
    }
}
