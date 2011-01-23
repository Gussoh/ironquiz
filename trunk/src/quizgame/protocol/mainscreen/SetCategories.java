/*
 * SetBoard.java
 *
 * Created on den 25 november 2006, 08:29
 */

package quizgame.protocol.mainscreen;

import quizgame.common.InGameCategory;

/**
 *
 * @author rheo
 */
public class SetCategories implements MainScreenPacket {
    public InGameCategory[] categories;
    
    public SetCategories(InGameCategory[] categories) {
        this.categories = categories;
    }
}
