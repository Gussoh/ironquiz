/*
 * ActiveBoard.java
 *
 * Created on den 3 maj 2007, 23:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.server;

import quizgame.common.Category;
import quizgame.common.InGameCategory;
import quizgame.common.InGameQuestion;
import quizgame.common.Question;
import quizgame.protocol.admin.AdminPacket;

/**
 *  This class is the model for the active game board. This class is not thread-safe.
 * @author Dukken
 */
public class ActiveBoard implements AdminPacket {
    private Category[] categories;
    private InGameCategory[] inGameCategories;
    private int activeCategoryIndex = -1, activeQuestionIndex = -1, lastCategoryIndex = -1, lastQuestionIndex = -1;
    private String name;
    private boolean isQuestionActive;
    transient private Server server;

    public Category[] getCategories() {
        return categories;
    }

    public InGameCategory[] getInGameCategories() {
        return inGameCategories;
    }
    
    
    
    /**
     *  Creates a new ActiveBoard. All questions will initially have the usedUp variable set to false.
     *  @param categories The categories the board should be made of.
     *  @param name the name of the board.
     */
    public ActiveBoard(Server server, String name, Category[] categories) {
        this.server = server;
        this.name = name;
        if(categories == null) {
            Logger.getInstance().println("ActiveBoard: An empty board has been set as active board.");
            server.getMainscreenModel().sendActiveBoard(inGameCategories);
            inGameCategories = null;
            return;
        }
        
        this.categories = categories;
        inGameCategories = new InGameCategory[categories.length];
        
        for(int i = 0; i < categories.length; i++) {
            inGameCategories[i] = new InGameCategory();
            inGameCategories[i].name = categories[i].name;
            inGameCategories[i].questions = new InGameQuestion[categories[i].questions.length];
            
            for(int j = 0; j < categories[i].questions.length; j++) {
                inGameCategories[i].questions[j] = new InGameQuestion();
                inGameCategories[i].questions[j].score = categories[i].questions[j].score;
                inGameCategories[i].questions[j].usedUp = false;
            }
        }
        Logger.getInstance().println("ActiveBoard: The board '" + name + "' has been chosen as the active board.");
        server.getMainscreenModel().sendActiveBoard(inGameCategories);
    }

    /**
     *  Returns the name of this board.
     */
    public String getName() {
        return name;
    }
    
    /**
     *  Returns the index of the active category. The caller should first check that isQuestionActive() returns true.
     */
    public int getActiveCategoryIndex() {
        return activeCategoryIndex;
    }
    
    /**
     *  Returns the index of the active question. The caller should first check that isQuestionActive() returns true.
     */
    public int getActiveQuestionIndex() {
        return activeQuestionIndex;
    }
    
    /**
     *  Sets the active question if there is no active question currenlty set.
     *  @param categoryIndex the index of the category.
     *  @param questionIndex the index of the question.
     *  @return true if question was set as active, otherwise false.
     */
    public boolean setActiveQuestion(int categoryIndex, int questionIndex) {
        if(!isQuestionActive) {
            activeCategoryIndex = categoryIndex;
            activeQuestionIndex = questionIndex;
            isQuestionActive = true;
            Logger.getInstance().println("ActiveBoard: Question[" + categoryIndex + ", " + questionIndex + "] has been activated.");
            server.getMainscreenModel().activateQuestion(categoryIndex, questionIndex, getActiveQuestion().question);
            return true;
        }
        return false;
    }
    
    /**
     *  Changes the used up status for any question.
     *  @param categoryIndex the index of the category.
     *  @param questionIndex the index of the question.
     *  @param isUsedUp true if usedUp, otherwise false.
     */
    public void setUsedUp(int categoryIndex, int questionIndex, boolean isUsedUp) {
        inGameCategories[categoryIndex].questions[questionIndex].usedUp = isUsedUp;
        Logger.getInstance().println("ActiveBoard: Question[" + categoryIndex + ", " + questionIndex + "] has been set as used up.");
        server.getMainscreenModel().setUsedUp(categoryIndex, questionIndex, isUsedUp);
    }
    
    /**
     *  Deactivates the active question if and only if a question is active and sets is as used up.
     *  @returns true if question was deactivated, otherwise false.
     */
    public boolean deactivateQuestion() {
        if(isQuestionActive) {
            lastQuestionIndex = activeQuestionIndex;
            lastCategoryIndex = activeCategoryIndex;
            inGameCategories[activeCategoryIndex].questions[activeQuestionIndex].usedUp = true;
            isQuestionActive = false;
            Logger.getInstance().println("ActiveBoard: Question[" + activeCategoryIndex + ", " + activeQuestionIndex + "] has been deactivated.");
            server.getMainscreenModel().deactivateQuestion();
            
            activeQuestionIndex = -1;
            activeCategoryIndex = -1;
            return true;
        }
        return false;
    }
    
    /**
     *  Returns true if a question is active, otherwise false.
     */
    public boolean isQuestionActive() {
        return isQuestionActive;
    }

    public int getLastCategoryIndex() {
        return lastCategoryIndex;
    }

    public int getLastQuestionIndex() {
        return lastQuestionIndex;
    }
    
    /**
     *  Returns the active question. If no question is active, this mehtod returns null.
     */
    public Question getActiveQuestion() {
        if(isQuestionActive) {
            return categories[activeCategoryIndex].questions[activeQuestionIndex];
        }
        return null;
    }

}
