/*
 * QuestionEditPanel.java
 *
 * Created on November 26
 */

package quizgame.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import quizgame.common.Category;
import quizgame.common.Question;

/**
 *
 * @author rheo
 */
public class QuestionEditPanel extends javax.swing.JPanel {
    private AnswerListModel answerListModel = new AnswerListModel();
    private BoardTreeModel boardTreeModel;
    private Category category;
    private Question question;
    
    /** Creates new form QuestionEditPanel */
    public QuestionEditPanel(BoardTreeModel boardTreeModel, Category category, Question question) {
        this.boardTreeModel = boardTreeModel;
        this.category = category;
        this.question = question;
        
        initComponents();
        
        scoreField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                // Plain text components don't fire these events
            }
            public void insertUpdate(DocumentEvent e) {
                scoreChanged();
            }
            public void removeUpdate(DocumentEvent e) {
                scoreChanged();
            }
        });
        questionField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                // Plain text components don't fire these events
            }
            public void insertUpdate(DocumentEvent e) {
                questionChanged();
            }
            public void removeUpdate(DocumentEvent e) {
                questionChanged();
            }
        });
        answerField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                // Plain text components don't fire these events
            }
            public void insertUpdate(DocumentEvent e) {
                answerChanged();
            }
            public void removeUpdate(DocumentEvent e) {
                answerChanged();
            }
        });
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JLabel jLabel1;
        javax.swing.JLabel jLabel2;
        javax.swing.JLabel jLabel3;
        javax.swing.JLabel jLabel4;
        javax.swing.JLabel jLabel5;
        javax.swing.JPanel jPanel1;
        javax.swing.JPanel jPanel2;
        javax.swing.JScrollPane jScrollPane1;

        jPanel1 = new javax.swing.JPanel();
        categoryBox = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        questionField = new javax.swing.JTextField();
        scoreField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        answerField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        answerList = new javax.swing.JList();
        addButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Question"));
        categoryBox.setModel(new CategoryComboBoxModel());

        jLabel4.setText("Category:");

        jLabel2.setText("Question:");

        jLabel1.setText("Score:");

        questionField.setText(question.question);

        scoreField.setText("" + question.score);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel4)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel2)
                            .add(jLabel1))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, categoryBox, 0, 272, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, scoreField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                            .add(questionField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(categoryBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(scoreField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(questionField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Answers"));
        jLabel3.setText("Answer:");

        answerList.setModel(answerListModel);
        answerList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                answerListValueChanged(evt);
            }
        });

        jScrollPane1.setViewportView(answerList);

        addButton.setText("Add");
        addButton.setEnabled(false);
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        editButton.setText("Edit");
        editButton.setEnabled(false);
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        removeButton.setText("Remove");
        removeButton.setEnabled(false);
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        jLabel5.setText("Answer list:");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel3)
                    .add(jLabel5))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                    .add(answerField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(addButton)
                    .add(editButton)
                    .add(removeButton))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(new java.awt.Component[] {addButton, editButton, removeButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(answerField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(addButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(editButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(removeButton))
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .add(jLabel5))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        int count = 0;
        int[] indices = answerList.getSelectedIndices();
        String[] oldAnswers = question.answers;
        
        for(int index : indices) {
            oldAnswers[index] = null;
        }
        
        question.answers = new String[oldAnswers.length - 1];
        
        for(String answer : oldAnswers) {
            if(answer != null) {
                question.answers[count] = answer;
                count++;
            }
        }
        
        answerListModel.fireIntervalRemoved(0, oldAnswers.length);
        
        if(indices[0] >= question.answers.length) {
            indices[0]--;
        }
        
        boardTreeModel.changed();
    }//GEN-LAST:event_removeButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        int newIndex = Arrays.binarySearch(question.answers, answerField.getText());
        int oldIndex = answerList.getSelectedIndex();
        
        if(newIndex < 0) {
            newIndex = -newIndex - 1;
            
            if(newIndex > oldIndex) {
                newIndex--;
                System.arraycopy(question.answers, oldIndex + 1, question.answers, oldIndex, newIndex - oldIndex);
            } else if(newIndex < oldIndex) {
                System.arraycopy(question.answers, newIndex, question.answers, newIndex + 1, oldIndex - newIndex);
            }
            
            question.answers[newIndex] = answerField.getText();
            
            if(newIndex == oldIndex) {
                answerListModel.fireContentsChanged(newIndex, newIndex);
            } else {
                answerListModel.fireIntervalRemoved(oldIndex, oldIndex);
                answerListModel.fireIntervalAdded(newIndex, newIndex);
                answerList.setSelectedIndex(newIndex);
            }
        } else if(newIndex != oldIndex) {
            JOptionPane.showMessageDialog(this, "Duplicate entry");
        }
    }//GEN-LAST:event_editButtonActionPerformed

    private void answerListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_answerListValueChanged
        int[] indices = answerList.getSelectedIndices();
        editButton.setEnabled(indices.length == 1);
        removeButton.setEnabled(indices.length > 0);
        
        if(indices.length > 0) {
            answerField.setText(answerList.getSelectedValue().toString());
        }
    }//GEN-LAST:event_answerListValueChanged

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        int index = Arrays.binarySearch(question.answers, answerField.getText());
        
        if(index < 0) {
            String[] oldAnswers = question.answers;
            index = -index - 1;
            question.answers = new String[oldAnswers.length + 1];
            question.answers[index] = answerField.getText();
            System.arraycopy(oldAnswers, 0, question.answers, 0, index);
            System.arraycopy(oldAnswers, index, question.answers, index + 1, oldAnswers.length - index);
            answerListModel.fireIntervalAdded(index, index);
            answerList.setSelectedIndex(index);
            boardTreeModel.changed();
        } else {
            JOptionPane.showMessageDialog(this, "Duplicate entry");
        }
    }//GEN-LAST:event_addButtonActionPerformed
    
    private void scoreChanged() {
        int score;
        
        try {
            score = Integer.parseInt(scoreField.getText());
        } catch(NumberFormatException ex) {
            return;
        }
        
        boardTreeModel.setQuestionScore(category, question, score);
    }
    
    private void questionChanged() {
        question.question = questionField.getText();
        boardTreeModel.changed();
    }
    
    private void answerChanged() {
        addButton.setEnabled(answerField.getText().length() > 0);
        editButton.setEnabled(answerField.getText().length() > 0 && answerList.getSelectedIndices().length == 1);
    }
    
    private class CategoryComboBoxModel implements ComboBoxModel {
        public void addListDataListener(ListDataListener l) {
        }

        public Object getElementAt(int index) {
            return boardTreeModel.categories[index];
        }

        public int getSize() {
            return boardTreeModel.categories.length;
        }

        public void removeListDataListener(ListDataListener l) {
        }

        public void setSelectedItem(Object anItem) {
            boardTreeModel.setQuestionCategory(category, question, (Category) anItem);
            category = (Category) anItem;
        }

        public Object getSelectedItem() {
            return category;
        }
    }
    
    private class AnswerListModel extends AbstractListModel {
        public int getSize() {
            return question.answers.length;
        }
        
        public Object getElementAt(int index) {
            return question.answers[index];
        }

        public void fireContentsChanged(int index0, int index1) {
            fireContentsChanged(this, index0, index1);
        }

        public void fireIntervalAdded(int index0, int index1) {
            fireIntervalAdded(this, index0, index1);
        }

        public void fireIntervalRemoved(int index0, int index1) {
            fireIntervalRemoved(this, index0, index1);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JTextField answerField;
    private javax.swing.JList answerList;
    private javax.swing.JComboBox categoryBox;
    private javax.swing.JButton editButton;
    private javax.swing.JTextField questionField;
    private javax.swing.JButton removeButton;
    private javax.swing.JTextField scoreField;
    // End of variables declaration//GEN-END:variables
}
