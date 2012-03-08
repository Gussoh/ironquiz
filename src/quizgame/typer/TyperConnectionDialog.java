/*
 * ConnectionDialog.java
 *
 * Created on November 23, 2006, 7:03 PM
 */

package quizgame.typer;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author  gsohtell
 */
public abstract class TyperConnectionDialog extends JDialog {
    
    /**
     *  Create a connectiondialog with an application name.
     *  @param parent   The parent JComponent
     *  @param modal    Set to true if dialog should be made modal
     *  @param application  The name of the application. eg. "Big"
     */
    public TyperConnectionDialog(JFrame parent, boolean modal, String application) {
        super(parent, modal);
        init(application);
    }
        
    /**
     *  Create a connectiondialog.
     *  @param parent   The parent JComponent
     *  @param modal    Set to true if dialog should be made modal
     */
    public TyperConnectionDialog(JFrame parent, boolean modal) {
        super(parent, modal);
        init(null);
    }
    
    private void init(String application) {
        initComponents();
        
        if(application != null) {
            welcomeLabel.setText(welcomeLabel.getText() + " - " + application);
            setTitle(application + " - " + getTitle());
        }
        
        pack();
        setVisible(true);
    }
    
    protected void showWarning(String message) {
        messagePanel.removeAll();
        messagePanel.setLayout(new BorderLayout());
        messagePanel.add(new JOptionPane(message, JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[0]));
        pack();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        loginCreateUserButtonGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        serverAddressField = new javax.swing.JTextField();
        usernameField = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        connectButton = new javax.swing.JButton();
        messagePanel = new javax.swing.JPanel();
        welcomeLabel = new javax.swing.JLabel();
        passwordAgainField = new javax.swing.JPasswordField();
        passwordAgainLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        loginRadioButton = new javax.swing.JRadioButton();
        createNewUserRadioButton = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Connect to server");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                WindowClosePressed(evt);
            }
        });

        jLabel1.setText("Server host or address:");

        jLabel2.setText("Username:");

        jLabel3.setText("Password:");

        passwordField.setToolTipText("The password for this username");
        passwordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TyperConnectionDialog.this.keyReleased(evt);
            }
        });

        serverAddressField.setToolTipText("Enter hostname or IP address of server. Connects to localhost if left empty.");
        serverAddressField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TyperConnectionDialog.this.keyReleased(evt);
            }
        });

        usernameField.setToolTipText("The username you would like to log in as.");
        usernameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TyperConnectionDialog.this.keyReleased(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.setToolTipText("Abort");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelPressed(evt);
            }
        });

        connectButton.setText("Connect");
        connectButton.setToolTipText("Connect to specified server");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectPressed(evt);
            }
        });

        welcomeLabel.setText("Welcome to Iron Quiz Client!");

        org.jdesktop.layout.GroupLayout messagePanelLayout = new org.jdesktop.layout.GroupLayout(messagePanel);
        messagePanel.setLayout(messagePanelLayout);
        messagePanelLayout.setHorizontalGroup(
            messagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(welcomeLabel)
        );
        messagePanelLayout.setVerticalGroup(
            messagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(welcomeLabel)
        );

        passwordAgainField.setToolTipText("Enter the same password again");
        passwordAgainField.setEnabled(false);

        passwordAgainLabel.setText("Again:");
        passwordAgainLabel.setEnabled(false);

        loginCreateUserButtonGroup.add(loginRadioButton);
        loginRadioButton.setSelected(true);
        loginRadioButton.setText("Login");
        loginRadioButton.setToolTipText("Login to an axisting account");
        loginRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        loginRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        loginCreateUserButtonGroup.add(createNewUserRadioButton);
        createNewUserRadioButton.setText("Create New User");
        createNewUserRadioButton.setToolTipText("Create a new account");
        createNewUserRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        createNewUserRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        createNewUserRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                createNewUserRadioButtonStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                    .add(messagePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(serverAddressField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(connectButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelButton))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                .add(jLabel3)
                                .add(jLabel2)
                                .add(passwordAgainLabel))
                            .add(loginRadioButton))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(createNewUserRadioButton)
                            .add(usernameField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                            .add(passwordField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                            .add(passwordAgainField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(messagePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(serverAddressField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(loginRadioButton)
                    .add(createNewUserRadioButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 17, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(usernameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(passwordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(passwordAgainField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(passwordAgainLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelButton)
                    .add(connectButton))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void createNewUserRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_createNewUserRadioButtonStateChanged
        passwordAgainField.setEnabled(createNewUserRadioButton.isSelected());
        passwordAgainLabel.setEnabled(createNewUserRadioButton.isSelected());
    }//GEN-LAST:event_createNewUserRadioButtonStateChanged

    private void keyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keyReleased
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            connectButton.doClick();
        }
    }//GEN-LAST:event_keyReleased

    private void WindowClosePressed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_WindowClosePressed
        onCancel();
    }//GEN-LAST:event_WindowClosePressed
    
    private void cancelPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelPressed
        onCancel();
    }//GEN-LAST:event_cancelPressed

    private void connectPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectPressed
        if(createNewUserRadioButton.isSelected()) {
            if(usernameField.getText().length() < 1) {
                showWarning("You must enter a username!");
                return;
            }
            
            if(!(new String(passwordField.getPassword())).equals(new String(passwordAgainField.getPassword())) ) {
                showWarning("The second password field does not have the same password as the first.");
                return;
            }
        }
        
        connectButton.setEnabled(false);
        onConnect(serverAddressField.getText(), usernameField.getText(), new String(passwordField.getPassword()), createNewUserRadioButton.isSelected());
        /*try {
        } catch (MalformedURLException e) {
            showWarning("The hostname entered is invalid.");
        } catch (RemoteException e) {
            showWarning("Failed connecting to the server: " + e.getMessage());
        } catch (IOException e) {
            showWarning(e.getMessage());
        } catch (NotBoundException e) {
            showWarning("The server does not offer the requested object.");
        }*/
    }//GEN-LAST:event_connectPressed
    
    /**
     * Invoked when dialog is canceled.
     */
    protected abstract void onCancel();
    
    /**
     * Invoked when the Connect button is pressed.
     * @param serverHost the server host entered
     * @param username the username entered
     * @param password the pasword entered
     */
    protected abstract void onConnect(String serverHost, String username, String password, boolean newAccount);
    
    protected void connectionFailed(final String error) {
        Runnable code = new Runnable() {
            public void run() {
                showWarning(error);
                connectButton.setEnabled(true);
            }
        };
        
        SwingUtilities.invokeLater(code);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton connectButton;
    private javax.swing.JRadioButton createNewUserRadioButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.ButtonGroup loginCreateUserButtonGroup;
    private javax.swing.JRadioButton loginRadioButton;
    private javax.swing.JPanel messagePanel;
    private javax.swing.JPasswordField passwordAgainField;
    private javax.swing.JLabel passwordAgainLabel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField serverAddressField;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}