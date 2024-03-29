/*
 * ChangeTimerDialog.java
 *
 * Created on May 29, 2007, 11:54 AM
 */

package quizgame.admin;

import javax.swing.JOptionPane;

/**
 *
 * @author  gsohtell
 */
public class ChangeTimerDialog extends javax.swing.JDialog {
    
    ActiveBoardPanel abp;
    
    /** Creates new form ChangeTimerDialog */
    public ChangeTimerDialog(ActiveBoardPanel abp, int oldTimerValue, java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.abp = abp;
        oldValueLabel.setText(oldTimerValue + "");
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        newTimerTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        s1Button = new javax.swing.JButton();
        s10Button = new javax.swing.JButton();
        s20Button = new javax.swing.JButton();
        s30Button = new javax.swing.JButton();
        s15Button = new javax.swing.JButton();
        s5Button = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        oldValueLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("New total time for each question:");

        s1Button.setText("1s");
        s1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s1ButtonActionPerformed(evt);
            }
        });

        s10Button.setText("10s");
        s10Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s10ButtonActionPerformed(evt);
            }
        });

        s20Button.setText("20s");
        s20Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s20ButtonActionPerformed(evt);
            }
        });

        s30Button.setText("30s");
        s30Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s30ButtonActionPerformed(evt);
            }
        });

        s15Button.setText("15s");
        s15Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s15ButtonActionPerformed(evt);
            }
        });

        s5Button.setText("5s");
        s5Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s5ButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Old value:");

        jLabel3.setText("ms");

        jLabel4.setText("New value:");

        oldValueLabel.setText("-----");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(okButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelButton))
                    .add(layout.createSequentialGroup()
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(oldValueLabel)
                        .add(26, 26, 26)
                        .add(jLabel4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(newTimerTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel3))
                    .add(layout.createSequentialGroup()
                        .add(s1Button)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(s5Button)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(s10Button)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(s15Button)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(s20Button)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(s30Button)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(s1Button)
                    .add(s10Button)
                    .add(s5Button)
                    .add(s15Button)
                    .add(s20Button)
                    .add(s30Button))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jLabel4)
                    .add(newTimerTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3)
                    .add(oldValueLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelButton)
                    .add(okButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        try {    
            abp.timerChangedFromChangeTimerDialog(Integer.parseInt(newTimerTextField.getText()));
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erroneus value!");
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    private void s5ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_s5ButtonActionPerformed
        newTimerTextField.setText(5000 + "");
    }//GEN-LAST:event_s5ButtonActionPerformed

    private void s10ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_s10ButtonActionPerformed
        newTimerTextField.setText(10000 + "");
    }//GEN-LAST:event_s10ButtonActionPerformed

    private void s15ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_s15ButtonActionPerformed
        newTimerTextField.setText(15000 + "");
    }//GEN-LAST:event_s15ButtonActionPerformed

    private void s20ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_s20ButtonActionPerformed
        newTimerTextField.setText(20000 + "");
    }//GEN-LAST:event_s20ButtonActionPerformed

    private void s30ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_s30ButtonActionPerformed
        newTimerTextField.setText(30000 + "");
    }//GEN-LAST:event_s30ButtonActionPerformed

    private void s1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_s1ButtonActionPerformed
        newTimerTextField.setText(1000 + "");
    }//GEN-LAST:event_s1ButtonActionPerformed
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField newTimerTextField;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel oldValueLabel;
    private javax.swing.JButton s10Button;
    private javax.swing.JButton s15Button;
    private javax.swing.JButton s1Button;
    private javax.swing.JButton s20Button;
    private javax.swing.JButton s30Button;
    private javax.swing.JButton s5Button;
    // End of variables declaration//GEN-END:variables
    
}
