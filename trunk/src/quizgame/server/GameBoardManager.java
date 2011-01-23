/*
 * GameBoardManager.java
 *
 * Created on den 10 mars 2007, 00:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeSet;
import quizgame.admin.SortedArrays;
import quizgame.common.Category;
import quizgame.common.InGameCategory;
import quizgame.common.InGameQuestion;
import quizgame.common.Question;
import quizgame.protocol.admin.boardediting.BoardAdded;
import quizgame.protocol.admin.boardediting.BoardsRemoved;
import quizgame.protocol.admin.boardediting.SetBoardName;
import quizgame.protocol.mainscreen.SetQuestionUsedUp;

/**
 *  A new approach to GameBoard managing.
 */
public class GameBoardManager {
    // TODO : Make me!
    
    private Category[] currentCategories;
    private File boardDirectory = new File("boards"); // TODO read this from config.ini
 //   private InGameCategory[] inGameCategories;
    private String[] boardNames;
    
    // File reading/writing
    private String questionDelimiter = ServerConfig.getInstance().getString("Gameboard", "questionDelimiter");
    private String categoryMark = ServerConfig.getInstance().getString("Gameboard", "categoryMark");
    private String charset = ServerConfig.getInstance().getString("Gameboard", "charset");
    
    public GameBoardManager() {
        File[] boardFiles = boardDirectory.listFiles();
        
        if(boardFiles == null) {
            boardFiles = new File[0];
        }
        
        boardNames = new String[boardFiles.length];
        for(int i = 0; i < boardFiles.length; i++) {
            boardNames[i] = boardFiles[i].getName();
        }
        Arrays.sort(boardNames);
    }
    
    /**
     *  Removes boards. that is physically delete them from the harddrive.
     *  @return a BoardsRemoved message that should be sent to all admins.
     */
    public BoardsRemoved removeBoards(String[] names) {
        int affectedCount = 0;
        int boardNamesIndex = 0;
        int indicesIndex = 0;
        int[] indices = new int[names.length];
        BoardsRemoved message;
        String[] oldBoardNames = boardNames;
        
        for(String name : names) {
            if(name != null) {
                int index = Arrays.binarySearch(boardNames, name);
                if(index >= 0) {
                    indices[indicesIndex] = index;
                    indicesIndex++;
                }
            }
        }
        
        for(int i = 0; i < indicesIndex; i++) {
            int index = indices[i];
            if(boardNames[index] != null) {
                new File(boardDirectory, boardNames[index]).delete();
                boardNames[index] = null;
                affectedCount++;
            }
        }
        
        boardNames = new String[boardNames.length - affectedCount];
        indices = new int[affectedCount];
        indicesIndex = 0;
        
        for(String boardName : oldBoardNames) {
            if(boardName == null) {
                indices[indicesIndex] = boardNamesIndex + indicesIndex;
                indicesIndex++;
            } else {
                boardNames[boardNamesIndex] = boardName;
                boardNamesIndex++;
            }
        }
        
        message = new BoardsRemoved();
        message.boardIndices = indices;
        return message;
    }
    
    /**
     *  Changes a boardname.
     *  @return a SetBoardName packet that should be sent to all clients.
     */
    public SetBoardName setBoardName(String oldName, String newName) {
        int newIndex;
        int oldIndex;
        File targetFile;
        SetBoardName message;
        
        if(newName == null) {
            return null;
        }
        
        oldIndex = Arrays.binarySearch(boardNames, oldName);
        
        if(oldIndex < 0) {
            return null;
        }
        
        newIndex = Arrays.binarySearch(boardNames, newName);
        
        if(newIndex >= 0) {
            return null;
        }
        
        newIndex = -newIndex - 1;
        targetFile = new File(boardDirectory, newName);
        
        if(!targetFile.getParentFile().equals(boardDirectory)) {
            // TODO Alert the client about the problem
            return null;
        }
        
        if(!new File(boardDirectory, boardNames[oldIndex]).renameTo(targetFile)) {
            // TODO Alert the client about the problem
            return null;
        }
        
        if(newIndex > oldIndex) {
            newIndex--;
            System.arraycopy(boardNames, oldIndex + 1, boardNames, oldIndex, newIndex - oldIndex);
        } else if(newIndex < oldIndex) {
            System.arraycopy(boardNames, newIndex, boardNames, newIndex + 1, oldIndex - newIndex);
        }
        
        boardNames[newIndex] = newName;
        message = new SetBoardName();
        message.boardIndex = oldIndex;
        message.newName = newName;
        // sendToAllAdmins(message);
        return message;
    }
    
    public BoardAdded saveBoard(String name, Category[] categories) {
        int index;
        File targetFile = new File(boardDirectory, name);
        PrintWriter output;
        
        if(!targetFile.getParentFile().equals(boardDirectory)) {
            Logger.getInstance().println("GameBoardManager.saveBoard(): Targetfile could not be created.");
            return null;
        }
        
        try {
            targetFile.delete();
            /*output = new ObjectOutputStream(new FileOutputStream(targetFile));
            output.writeObject(categories);
             */
            output = new PrintWriter(targetFile, charset);
            for (Category category : categories) {
                output.println(categoryMark + category.name.trim());
                for (Question question : category.questions) {
                    output.print(question.score + questionDelimiter + " " + question.question.trim() + "   ");
                    for (String answer : question.answers) {
                        output.print(" " + questionDelimiter + " " + answer.trim());
                    }
                    output.println();
                }
                output.println();
            }
            output.close();
            
        } catch (IOException ex) {
            Logger.getInstance().println("GameBoardManager.saveBoard(): " + ex);
            return null;
        }
        
        index = Arrays.binarySearch(boardNames, name);
        
        if(index < 0) {
            Logger.getInstance().println("GameBoardManager.saveBoard(): An admin saved a new board called \"" + name + "\"");
            BoardAdded message = new BoardAdded();
            boardNames = SortedArrays.add(boardNames, new String[boardNames.length + 1], -index - 1, name);
            message.boardName = name;
            return message;
        }
        return null;
    }
    
    public Category[] getBoard(String name) {
        
        File boardFile = new File(boardDirectory, name);
        
        if(!boardFile.getParentFile().equals(boardDirectory)) {
            // TODO Alert the client about the problem
            return null;
        }
        
        try {
            //   input = new ObjectInputStream(new FileInputStream(boardFile));
            //   input = new BufferedReader(new InputStreamReader(new FileInputStream(name), charset));
            Scanner sc = new Scanner(boardFile, charset);
            TreeSet<Category> categories = new TreeSet<Category>();
            TreeSet<Question> questions = new TreeSet<Question>();
            Category currCategory = null;
            while(sc.hasNextLine()) {
                
                String currentLine = sc.nextLine().trim();
                Logger.getInstance().println("Parsing line (size: " + currentLine.length() + "): " + currentLine);
                if(currentLine.startsWith(categoryMark)) {
                    if(currCategory != null) {
                        currCategory.questions = questions.toArray(new Question[0]);
                        questions.clear();
                    }
                    currCategory = new Category();
                    currCategory.name = currentLine.substring(1).trim();
                    categories.add(currCategory);
                } else if(currCategory != null) {
                    String[] splittedQuestion = currentLine.split(";");
                    if(currentLine.length() == 0) {
                      // Just an empty line, do nothing  
                    } else if(splittedQuestion.length < 2) {
                        Logger.getInstance().println("GameBoardManager.getBoard(): WARNING! Following line in category " + currCategory.name + " is not recognized: " + currentLine);
                    } else {
                        try {
                            Question question = new Question();
                            question.score = Integer.parseInt(splittedQuestion[0].trim());
                            question.question = splittedQuestion[1].trim();
                            
                            String[] answers = new String[splittedQuestion.length - 2];
                            
                            for (int i = 0; i < answers.length; i++) {
                                answers[i] = splittedQuestion[i + 2];
                            }
                            
                            question.answers = answers;
                            questions.add(question);
                            
                        } catch(NumberFormatException ex) {
                            Logger.getInstance().println("GameBoardManager.getBoard(): WARNING! Following line in category " + currCategory.name + " is not recognized: " + currentLine);
                        }
                    }
                } else {
                    Logger.getInstance().println("GameBoardManager.getBoard(): ERROR when reading file '" + name + "'. Expected file to begin with category name. Found: " + currentLine);
                    return null;
                }
                
            }
            
            if(currCategory != null) {
                currCategory.questions = questions.toArray(new Question[0]);
            }
            sc.close();
            return categories.toArray(new Category[0]);
        } catch (IOException ex) {
            // TODO Alert the client about the problem
            return null;
        }
        
    }
     
    public String[] getBoardNames() {
        return boardNames;
    }
    
}
