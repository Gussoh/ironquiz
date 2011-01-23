/*
 * SaveBoard.java
 *
 * Created on den 3 december 2006
 */

package quizgame.protocol.admin.boardediting;

import quizgame.common.Category;
import quizgame.protocol.admin.*;

/**
 * @author rheo
 */
public class SaveBoard implements AdminPacket {
    public Category[] categories;
    public String boardName;
}
