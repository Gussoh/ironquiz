/*
 * BoardEditorPanel.java
 */

package quizgame.admin;

import javax.swing.JOptionPane;
import quizgame.common.Category;
import quizgame.common.Question;

/**
 *
 * @author rheo
 */
public class BoardEditorPanel extends javax.swing.JPanel {
    public BoardTreeModel boardTreeModel = new BoardTreeModel(this);
    public String boardName;
    private AdminConnection adminConnection;
    
    /** Creates new form BoardEditorPanel */
    public BoardEditorPanel(AdminConnection adminConnection) {
        this.adminConnection = adminConnection;
        initComponents();
        
        for(int i = 0; i < boardTree.getRowCount(); i++) {
            boardTree.expandRow(i);
        }
        
        nodePanel.add(new NothingSelectedPanel());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JPanel jPanel1;
        javax.swing.JScrollPane jScrollPane1;

        newButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        saveAsButton = new javax.swing.JButton();
        nodePanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        boardTree = new javax.swing.JTree();
        deleteButton = new javax.swing.JButton();
        addCategoryButton = new javax.swing.JButton();
        addQuestionButton = new javax.swing.JButton();

        newButton.setText("New");
        newButton.setEnabled(false);
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        saveAsButton.setText("Save as...");
        saveAsButton.setEnabled(false);
        saveAsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsButtonActionPerformed(evt);
            }
        });

        nodePanel.setLayout(new javax.swing.BoxLayout(nodePanel, javax.swing.BoxLayout.X_AXIS));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Board"));
        boardTree.setModel(boardTreeModel);
        boardTree.setPreferredSize(new java.awt.Dimension(80, 54));
        boardTree.setRootVisible(false);
        boardTree.setShowsRootHandles(true);
        boardTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                boardTreeValueChanged(evt);
            }
        });

        jScrollPane1.setViewportView(boardTree);

        deleteButton.setText("Delete selected...");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        addCategoryButton.setText("Add category...");
        addCategoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCategoryButtonActionPerformed(evt);
            }
        });

        addQuestionButton.setText("Add question");
        addQuestionButton.setEnabled(false);
        addQuestionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addQuestionButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .add(deleteButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .add(addQuestionButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .add(addCategoryButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(addCategoryButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(addQuestionButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(deleteButton)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(nodePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .add(newButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(saveButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(saveAsButton)
                .addContainerGap(220, Short.MAX_VALUE))
        );

        layout.linkSize(new java.awt.Component[] {newButton, saveAsButton, saveButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(newButton)
                    .add(saveButton)
                    .add(saveAsButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(nodePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        if(JOptionPane.showConfirmDialog(this, "Delete selected?", null, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            boardTreeModel.delete(boardTree.getSelectionPath());
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void addQuestionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addQuestionButtonActionPerformed
        boardTreeModel.addQuestion((Category) boardTree.getSelectionPath().getPathComponent(1));
    }//GEN-LAST:event_addQuestionButtonActionPerformed

    private void boardTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_boardTreeValueChanged
        Object selected = boardTree.getLastSelectedPathComponent();
        
        addQuestionButton.setEnabled(selected != null);
        deleteButton.setEnabled(selected != null);
        nodePanel.removeAll();
        
        if(selected instanceof Category) {
            nodePanel.add(new CategoryEditPanel(boardTreeModel, (Category) selected));
        } else if(selected instanceof Question) {
            nodePanel.add(new QuestionEditPanel(boardTreeModel, (Category) boardTree.getSelectionPath().getPathComponent(1), (Question) selected));
        } else {
            nodePanel.add(new NothingSelectedPanel());
        }
        
        nodePanel.validate();
    }//GEN-LAST:event_boardTreeValueChanged

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        if(JOptionPane.showConfirmDialog(this, "Discard the current board?", null, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            boardName = null;
            boardTreeModel = new BoardTreeModel(this);
            boardTree.setModel(boardTreeModel);
            setChanged(false);
        }
    }//GEN-LAST:event_newButtonActionPerformed

    private void addCategoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCategoryButtonActionPerformed
        String name = JOptionPane.showInputDialog(this, "Name of new category");
        
        if(name != null) {
            boardTreeModel.addCategory(name);
        }
    }//GEN-LAST:event_addCategoryButtonActionPerformed

    private void saveAsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsButtonActionPerformed
        String name = JOptionPane.showInputDialog(this, "Name of board to save");
        
        if(name != null) {
            boardName = name;
            adminConnection.saveBoard(name, boardTreeModel.categories);
            setChanged(false);
        }
    }//GEN-LAST:event_saveAsButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        adminConnection.saveBoard(boardName, boardTreeModel.categories);
    }//GEN-LAST:event_saveButtonActionPerformed

    public void setChanged(boolean changed) {
        newButton.setEnabled(boardTreeModel.categories.length > 0);
        saveButton.setEnabled(boardTreeModel.categories.length > 0 && changed && boardName != null);
        saveAsButton.setEnabled(boardTreeModel.categories.length > 0);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addCategoryButton;
    private javax.swing.JButton addQuestionButton;
    public javax.swing.JTree boardTree;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton newButton;
    private javax.swing.JPanel nodePanel;
    private javax.swing.JButton saveAsButton;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
}
