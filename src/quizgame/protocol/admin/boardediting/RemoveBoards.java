/*
 * BoardsRemovedByName.java
 *
 * Created on den 29 november 2006
 */

package quizgame.protocol.admin.boardediting;

import quizgame.protocol.admin.*;

/**
 *
 * @author rheo
 */
public class RemoveBoards implements AdminPacket {
    public String[] boardNames;
}
