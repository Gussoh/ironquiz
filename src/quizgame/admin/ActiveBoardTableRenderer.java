/*
 * ActiveBoardTableRenderer.java
 *
 * Created on May 29, 2007, 3:30 PM
 */

package quizgame.admin;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import quizgame.server.ActiveBoard;

/**
 *
 * @author gsohtell
 */
public class ActiveBoardTableRenderer extends DefaultTableCellRenderer{
    
    ActiveBoardPanel abp;
    
    public ActiveBoardTableRenderer(ActiveBoardPanel abp) {
        this.abp = abp;
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        ActiveBoard ab = abp.getActiveBoard();
        
        if(ab.getCategories().length <= column || ab.getCategories()[column].questions.length <= row) {
            return cell;
        }
        
        if(column == ab.getActiveCategoryIndex() && row == abp.getActiveBoard().getActiveQuestionIndex()) {
            cell.setBackground(Color.GREEN);
        } else {
            if(ab.getLastCategoryIndex() == column && ab.getLastQuestionIndex() == row) {
                cell.setBackground(Color.YELLOW);
            } else {
                if(ab.getInGameCategories()[column].questions[row].usedUp) {
                    cell.setBackground(Color.GRAY);
                } else {
                    cell.setBackground(Color.WHITE);
                }
            }
        }
        
        return cell;
    }
    
}
