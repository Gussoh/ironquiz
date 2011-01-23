/*
 * BoardTreeModel.java
 *
 * Created on November 27, 2006
 */

package quizgame.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import quizgame.common.Category;
import quizgame.common.Question;

public class BoardTreeModel implements TreeModel {
    public Category[] categories = new Category[0];
    private BoardEditorPanel boardEditorPanel;
    private Object root = new Object();
    private ArrayList<TreeModelListener> treeModelListeners = new ArrayList<TreeModelListener>();
    
    public BoardTreeModel(BoardEditorPanel boardEditorPanel) {
        this.boardEditorPanel = boardEditorPanel;
    }
    
    public Object getRoot() {
        return root;
    }
    
    public Object getChild(Object parent, int index) {
        if (parent == root) {
            return categories[index];
        } else if (parent instanceof Category) {
            return ((Category) parent).questions[index];
        } else {
            return null;
        }
    }
    
    public int getChildCount(Object parent) {
        if (parent == root) {
            return categories.length;
        } else if (parent instanceof Category) {
            return ((Category) parent).questions.length;
        } else {
            return 0;
        }
    }
    
    public boolean isLeaf(Object node) {
        return node instanceof Question;
    }
    
    public void valueForPathChanged(TreePath path, Object newValue) {
        // Not used
    }
    
    public int getIndexOfChild(Object parent, Object child) {
        if (parent == root) {
            return SortedArrays.binaryIndexOf(categories, (Category) child);
        } else if (parent instanceof Category) {
            return SortedArrays.binaryIndexOf(((Category) parent).questions, (Question) child);
        } else {
            return -1;
        }
    }
    
    public void addTreeModelListener(TreeModelListener l) {
        treeModelListeners.add(l);
    }
    
    public void removeTreeModelListener(TreeModelListener l) {
        treeModelListeners.add(l);
    }
    
    public void setCategories(Category[] categories) {
        this.categories = categories;
        
        for(TreeModelListener listener : treeModelListeners) {
            listener.treeStructureChanged(new TreeModelEvent(this, new Object[] {root}, null, new Object[] {root}));
        }
        
        boardEditorPanel.setChanged(false);
    }
    
    public void addCategory(String name) {
        int index;
        Category category = new Category();
        
        category.name = name;
        category.questions = new Question[0];
        index = Arrays.binarySearch(categories, category);
        
        if(index < 0) {
            index = -index - 1;
        }
        
        categories = SortedArrays.add(categories, new Category[categories.length + 1], index, category);
        nodesInserted(new TreeModelEvent(this, new Object[] {root}, new int[] {index}, new Object[] {category}));
        boardEditorPanel.boardTree.expandPath(new TreePath(new Object[] {root, category}));
        boardEditorPanel.boardTree.setSelectionPath(new TreePath(new Object[] {root, category}));
    }
    
    public void addQuestion(Category category) {
        Question question = new Question();
        Question[] oldQuestions = category.questions;
        
        category.questions = new Question[oldQuestions.length + 1];
        category.questions[0] = question;
        question.answers = new String[0];
        System.arraycopy(oldQuestions, 0, category.questions, 1, oldQuestions.length);
        nodesInserted(new TreeModelEvent(this, new Object[] {root, category}, new int[] {0}, new Object[] {question}));
        boardEditorPanel.boardTree.setSelectionPath(new TreePath(new Object[] {root, category, question}));
    }
    
    public void delete(TreePath path) {
        Object selected = path.getLastPathComponent();
        
        if(selected instanceof Category) {
            int index = SortedArrays.binaryIndexOf(categories, (Category) selected);
            categories = SortedArrays.remove(categories, new Category[categories.length - 1], index);
            nodesRemoved(new TreeModelEvent(this, new Object[] {root}, new int[] {index}, new Object[] {selected}));
        } else if(selected instanceof Question) {
            Category category = (Category) path.getPathComponent(1);
            Question[] oldQuestions = category.questions;
            int index = Arrays.binarySearch(oldQuestions, selected);
            
            category.questions = new Question[oldQuestions.length - 1];
            System.arraycopy(oldQuestions, 0, category.questions, 0, index);
            System.arraycopy(oldQuestions, index + 1, category.questions, index, category.questions.length - index);
            nodesRemoved(new TreeModelEvent(this, new Object[] {root, category}, new int[] {index}, new Object[] {selected}));
        }
    }
    
    public void setCategoryName(Category category, String newName) {
        int newIndex;
        int oldIndex = SortedArrays.binaryIndexOf(categories, category);
        
        Category newCategory = new Category();
        newCategory.name = newName;
        newIndex = SortedArrays.move(categories, oldIndex, Arrays.binarySearch(categories, newCategory));
        category.name = newName;
        
        if(newIndex == oldIndex) {
            nodesChanged(new TreeModelEvent(this, new Object[] {root}, new int[] {oldIndex}, new Object[] {category}));
        } else {
            nodesRemoved(new TreeModelEvent(this, new Object[] {root}, new int[] {oldIndex}, new Object[] {category}));
            nodesInserted(new TreeModelEvent(this, new Object[] {root}, new int[] {newIndex}, new Object[] {category}));
            boardEditorPanel.boardTree.expandPath(new TreePath(new Object[] {root, category}));
            boardEditorPanel.boardTree.setSelectionPath(new TreePath(new Object[] {root, category}));
        }
    }
    
    public void setQuestionCategory(Category oldCategory, Question question, Category newCategory) {
        int index = SortedArrays.binaryIndexOf(oldCategory.questions, question);
        oldCategory.questions = SortedArrays.remove(oldCategory.questions, new Question[oldCategory.questions.length - 1], index);
        nodesRemoved(new TreeModelEvent(this, new Object[] {root, oldCategory}, new int[] {index}, new Object[] {question}));
        index = Arrays.binarySearch(newCategory.questions, question);
        
        if(index < 0) {
            index = -index - 1;
        }
        
        newCategory.questions = SortedArrays.add(newCategory.questions, new Question[newCategory.questions.length + 1], index, question);
        nodesInserted(new TreeModelEvent(this, new Object[] {root, newCategory}, new int[] {index}, new Object[] {question}));
        boardEditorPanel.boardTree.setSelectionPath(new TreePath(new Object[] {root, newCategory, question}));
    }
    
    public void setQuestionScore(Category category, Question question, int score) {
        int newIndex;
        int oldIndex = SortedArrays.binaryIndexOf(category.questions, question);
        Question newQuestion = new Question();
        
        newQuestion.score = score;
        newIndex = SortedArrays.move(category.questions, oldIndex, Arrays.binarySearch(category.questions, newQuestion));
        question.score = score;
        
        if(newIndex == oldIndex) {
            nodesChanged(new TreeModelEvent(this, new Object[] {root, category}, new int[] {oldIndex}, new Object[] {question}));
        } else {
            nodesRemoved(new TreeModelEvent(this, new Object[] {root, category}, new int[] {oldIndex}, new Object[] {question}));
            nodesInserted(new TreeModelEvent(this, new Object[] {root, category}, new int[] {newIndex}, new Object[] {question}));
            boardEditorPanel.boardTree.setSelectionPath(new TreePath(new Object[] {root, category, question}));
        }
    }
    
    private void nodesChanged(TreeModelEvent event) {
        for(TreeModelListener listener : treeModelListeners) {
            listener.treeNodesChanged(event);
        }
        changed();
    }
    
    private void nodesInserted(TreeModelEvent event) {
        for(TreeModelListener listener : treeModelListeners) {
            listener.treeNodesInserted(event);
        }
        changed();
    }
    
    private void nodesRemoved(TreeModelEvent event) {
        for(TreeModelListener listener : treeModelListeners) {
            listener.treeNodesRemoved(event);
        }
        changed();
    }
    
    public void changed() {
        boardEditorPanel.setChanged(true);
    }
}