/*
 * Board.java
 *
 * Created on den 4 december 2006
 */

package quizgame.protocol.admin.boardediting;

import quizgame.common.Category;
import quizgame.protocol.admin.*;

/**
 * @author rheo
 */
public class Board implements AdminPacket {
    public Category[] categories;
    public String name;
}
