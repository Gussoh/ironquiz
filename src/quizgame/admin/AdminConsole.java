/*
 * AdminConsole.java
 *
 * Created on May 22, 2007, 8:08 PM
 */

package quizgame.admin;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author  gsohtell
 */
public class AdminConsole extends javax.swing.JPanel {
    
    private AdminConnection adminConnection;
    
    /** Creates new form AdminConsole */
    public AdminConsole(AdminConnection adminConnection) {
        this.adminConnection = adminConnection;
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        textAreaScrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();

        textArea.setColumns(20);
        textArea.setRows(5);
        textAreaScrollPane.setViewportView(textArea);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(textAreaScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(textAreaScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    public void writeMessage(String text) {
        textArea.setText(textArea.getText() + "\n" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + " : " + text);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea textArea;
    private javax.swing.JScrollPane textAreaScrollPane;
    // End of variables declaration//GEN-END:variables
    
}
