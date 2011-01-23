/*
 * BoardsRemoved.java
 *
 * Created on den 28 november 2006
 */

package quizgame.protocol.admin.boardediting;

import quizgame.protocol.admin.*;

/**
 *
 * @author rheo
 */
public class BoardsRemoved implements AdminPacket {
    public int[] boardIndices;
}
