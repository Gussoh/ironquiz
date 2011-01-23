/*
 * SetBoard.java
 *
 * Created on den 4 december 2006
 */

package quizgame.protocol.admin;

/**
 *  Admin-to-Server packet. Sets the active board.
 * @author rheo
 */
public class SetActiveBoard implements AdminPacket {
    private String boardName;
    
    public SetActiveBoard(String boardName) {
        this.boardName = boardName;
    }

    public String getBoardName() {
        return boardName;
    }
    

}
