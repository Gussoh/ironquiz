/*
 * AdminConnection.java
 *
 * Created on den 26 november 2006, 09:08
 */

package quizgame.admin;

import java.io.IOException;
import javax.swing.SwingUtilities;
import quizgame.common.Category;
import quizgame.common.ClientConnection;
import quizgame.protocol.LogMessage;
import quizgame.protocol.Packet;
import quizgame.protocol.PulpitPing;
import quizgame.protocol.admin.*;
import quizgame.protocol.admin.boardediting.*;
import quizgame.server.ActiveBoard;

/**
 *
 * @author rheo
 */
public class AdminConnection extends ClientConnection {
    private Admin admin;
    private String requestedBoardName;
    
    /**
     * Creates a new instance of AdminConnection
     * @param serverHost address to the server to fetch remote objects from
     * @param username username to use when authenticating to the server
     * @param password password to use when authenticating to the server
     */
    public AdminConnection(String serverHost, String username, String password) throws IOException {
        super(serverHost, new AuthenticateAdmin(username, password));
    }
    
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    
    public void removeBoards(String[] names) {
        RemoveBoards message = new RemoveBoards();
        message.boardNames = names;
        
        try {
            writeObject(message);
        } catch(IOException ex) {
            // TODO This is a problem. Do something.
        }
        
    }
    
    public void setBoardName(String oldName, String newName) {
        SetBoardNameByName message = new SetBoardNameByName();
        message.newName = newName;
        message.oldName = oldName;
        
        try {
            writeObject(message);
        } catch(IOException ex) {
            // TODO This is a problem. Do something.
        }
    }
    
    public void getBoard(String boardName) {
        GetBoard message = new GetBoard();
        message.boardName = boardName;
        requestedBoardName = boardName;
        
        try {
            writeObject(message);
        } catch(IOException ex) {
            // TODO This is a problem. Do something.
        }
    }
    
    public void setBoard(String boardName) {
        SetActiveBoard message = new SetActiveBoard(boardName);
        
        try {
            writeObject(message);
        } catch(IOException ex) {
            // TODO This is a problem. Do something.
        }
    }
    
    public void saveBoard(String boardName, Category[] categories) {
        SaveBoard message = new SaveBoard();
        message.categories = categories;
        message.boardName = boardName;
        
        try {
            writeObject(message);
        } catch(IOException ex) {
            // TODO This is a problem. Do something.
        }
    }
    
    protected void handleObject(final Packet object) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
                admin.getAdminConsole().writeMessage("---" + object.getClass().getName() + " :" + object.toString() + "---");
                
                if(object instanceof BoardNames) {
                    admin.boardManager.boardsListModel.setBoardList(((BoardNames) object).boardNames);
                } else if(object instanceof BoardsRemoved) {
                    admin.boardManager.boardsListModel.removeBoards(((BoardsRemoved) object).boardIndices);
                } else if(object instanceof SetBoardName) {
                    admin.boardManager.boardsListModel.setBoardName(((SetBoardName) object).boardIndex, ((SetBoardName) object).newName);
                } else if(object instanceof Board) {
                    if(requestedBoardName != null && ((Board) object).name.equals(requestedBoardName)) {
                        admin.boardManager.boardEditorPanel.boardName = ((Board) object).name;
                        admin.boardManager.boardEditorPanel.boardTreeModel.setCategories(((Board) object).categories);
                        requestedBoardName = null;
                    }
                } else if(object instanceof BoardAdded) {
                    admin.boardManager.boardsListModel.addBoard(((BoardAdded) object).boardName);
                    
                    
                    // Active board packets
               
                } else if(object instanceof ActiveBoard) {
                   // System.out.println("AdminConnection.handleObject: isQuestionActive: " +((AdminActiveBoard) object).getActiveBoard().isQuestionActive());
                    admin.getActiveBoardPanel().setActiveBoard((ActiveBoard) object);
                } else if(object instanceof StartGameNotify) {
                    admin.getActiveBoardPanel().startGameNotification();
               
                } else if(object instanceof GameTimerStatusChange) {
                    GameTimerStatusChange packet = (GameTimerStatusChange) object;
                    admin.getActiveBoardPanel().gameTimerStatusChanged(packet.isRunning(), packet.getTotal(), packet.getElapsed(), packet.isButtonsEnabled());
                
                } else if(object instanceof LogMessage) {
                    LogMessage message = (LogMessage) object;
                    admin.getAdminConsole().writeMessage(message.getMessage());
                } else if(object instanceof AdminMessage) {
                    admin.getActiveBoardPanel().showMessage(((AdminMessage) object).getMessage());
                } else if(object instanceof PulpitInformation) {
                    admin.getActiveBoardPanel().setPulpitInfo(((PulpitInformation) object).getPulpets());
                } else if(object instanceof PulpitAnswer) {
                    admin.getActiveBoardPanel().pulpetAnswer((PulpitAnswer) object);
                } else if(object instanceof PulpitPing) {
                    admin.getActiveBoardPanel().pulpitPing((PulpitPing)object);
                }
                
            }
        });
        
    }
    
    public void getBoardNames() {
        try {
            writeObject(new GetBoardNames());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    public void setActiveBoard(String boardName) {
        try {
            writeObject(new SetActiveBoard(boardName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
        
    public void startGame() {
        try {
            writeObject(new StartGame());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void setQuestionUsedUp(int categoryIndex, int questionIndex, boolean usedUp) {
        try {
            writeObject(new SetQuestionUsedUp(categoryIndex, questionIndex, usedUp));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    public void startTimer() {
        try {
            writeObject(new SetTimerState(SetTimerState.State.START));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void pauseTimer() {
        try {
            writeObject(new SetTimerState(SetTimerState.State.PAUSE));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void endTimer() {
        try {
            writeObject(new SetTimerState(SetTimerState.State.END));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void modifyTimer(int elapsed, int total) {
        try {
            writeObject(new ModifyTimer(elapsed, total));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void setActiveQuestion(int categoryIndex, int questionIndex) {
        try {
            writeObject(new SetActiveQuestion(categoryIndex, questionIndex));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void deactivateQuestion() {
        try {
            writeObject(new DeactivateActiveQuestion());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void judgePulpet(boolean wasRight, int answerID) {
        try {
            writeObject(new JudgePulpit(wasRight, answerID));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void setPulpetScore(String name, int score) {
        try {
            writeObject(new SetPulpitScore(name, score));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void setPulpetNickname(String name, String nickname) {
        try {
            writeObject(new SetPulpitNickname(name, nickname));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
