/*
 * SetBoardNameByName.java
 *
 * Created on den 29 november 2006, 12:03
 */

package quizgame.protocol.admin.boardediting;

import quizgame.protocol.admin.*;

/**
 *
 * @author rheo
 */
public class SetBoardNameByName implements AdminPacket {
    public String newName;
    public String oldName;
}
