/*
 * MainScreenConnection.java
 *
 * Created on den 25 november 2006
 */

package quizgame.mainscreen;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import javax.swing.SwingUtilities;
import quizgame.protocol.mainscreen.AuthenticateMainScreen;
import quizgame.common.ClientConnection;
import quizgame.common.SoundPlayer;
import quizgame.protocol.Packet;
import quizgame.protocol.mainscreen.PlaySound;
import quizgame.protocol.mainscreen.SetCategories;
import quizgame.protocol.mainscreen.SetCurrentQuestion;
import quizgame.protocol.mainscreen.SetNoCurrentQuestion;
import quizgame.protocol.mainscreen.SetQuestionUsedUp;

/**
 *
 * @author rheo
 */
public class MainScreenConnection extends ClientConnection {
    private MainScreenPanel mainScreenPanel;
    
    /**
     * Creates a new instance of MainScreenConnection
     * @param serverHost address to the server to fetch remote objects from
     * @param username username to use when authenticating to the server
     * @param password password to use when authenticating to the server
     */
    public MainScreenConnection(String serverHost, String username, String password, MainScreenPanel mainScreenPanel) throws IOException {
        super(serverHost, new AuthenticateMainScreen(username, password));
        this.mainScreenPanel = mainScreenPanel;
    }
    
    
    protected void handleObject(final Packet object) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
                if(object instanceof SetCategories) {
                    mainScreenPanel.setCategories(((SetCategories) object).categories);
                } else if(object instanceof SetQuestionUsedUp) {
                    SetQuestionUsedUp data = (SetQuestionUsedUp) object;
                    mainScreenPanel.setQuestionUsedUp(data.categoryIndex, data.questionIndex, data.usedUp);
                } else if(object instanceof SetCurrentQuestion) {
                    SetCurrentQuestion data = (SetCurrentQuestion) object;
                    mainScreenPanel.setCurrentQuestion(data.categoryIndex, data.questionIndex, data.question);
                } else if(object instanceof SetNoCurrentQuestion) {
                    mainScreenPanel.setNoCurrentQuestion();
                } else if(object instanceof PlaySound) {

                    System.out.println("KUKHWLVETE");
                    
                    PlaySound playSound = (PlaySound)object;
                    
                    if (!SoundPlayer.getInstance().isLoaded(playSound.getSound())) {
                        String thisDirectory = new File(".").getAbsolutePath();
                        SoundPlayer.getInstance().loadSound(thisDirectory + playSound.getSound(), playSound.getSound());
                    }
                    
                    SoundPlayer.getInstance().play(playSound.getSound());

                } else {
                    // TODO Unrecognized object recieved. This is a problem. Do something.
                }
            }
        });
    }
}
