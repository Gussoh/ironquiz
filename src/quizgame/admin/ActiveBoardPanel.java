/*
 * ActiveBoardPanel.java
 *
 * Created on den 16 maj 2007, 16:01
 */

package quizgame.admin;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import quizgame.common.Category;
import quizgame.common.Entry;
import quizgame.common.InGameQuestion;
import quizgame.common.Question;
import quizgame.protocol.admin.PulpitAnswer;
import quizgame.protocol.pulpit.PulpitStatus;
import quizgame.server.ActiveBoard;

/**
 *
 * @author  Dukken
 */
public class ActiveBoardPanel extends javax.swing.JPanel implements ActionListener, KeyListener {
    
    private AdminConnection adminConnection;
    private ActiveBoard activeBoard;
    private Question selectedQuestion;
    private int selectedCategoryIndex, selectedQuestionIndex;
    private JFrame frame;
    private int pulpitAnswerQuestionId = 0;
    
    private HashMap<JButton, Entry<Integer, Integer>> buttonToCategoryAndQuestionMap;
    
    /**
     * Used by activeBoardTableRenderer
     */
    public ActiveBoard getActiveBoard() {
        return activeBoard;
    }
    
    /** Creates new form ActiveBoardPanel */
    public ActiveBoardPanel(AdminConnection adminConnection, JFrame frame) {
        this.frame = frame;
        addKeyListener(this);
        this.adminConnection = adminConnection;
        initComponents();
        deactivateButton.setEnabled(false);
        
        TimerTask t = new TimerTask() {
            public void run() {
                updateTimer();
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(t, 50, 50);
    }
    
    /**
     * This method is run at an fixed interval.
     */
    private void updateTimer() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if(!timerSlider.isEnabled()) {
                    timerSlider.setValue(timerSlider.getValue() + 50);
                }
            }
        });
    }
    
    /**
     * Server sends an active board.
     */
    public void setActiveBoard(ActiveBoard newActiveBoard) {
        if(newActiveBoard == null) {
            return;
        }
        
        this.activeBoard = newActiveBoard;
        
        if(activeBoard.isQuestionActive()) {
            activateQuestion(activeBoard.getActiveCategoryIndex(), activeBoard.getActiveQuestionIndex());
        } else {
            deactivateQuestion();
        }
        
        int maxNQuestions = 0;
        for (Category c : activeBoard.getCategories()) {
            if(maxNQuestions < c.questions.length)
                maxNQuestions = c.questions.length;
        }
        
        questionsPanel.removeAll();
        
        questionsPanel.setLayout(new GridLayout(maxNQuestions, activeBoard.getCategories().length));
        
        buttonToCategoryAndQuestionMap = new HashMap<JButton, Entry<Integer, Integer>>();
        for(int i = 0; i < maxNQuestions; i++) {
            for(int j = 0; j < activeBoard.getCategories().length; j++) {
                if(i < activeBoard.getCategories()[j].questions.length) {

                    JButton b = new JButton(activeBoard.getCategories()[j].questions[i].score + ": " + activeBoard.getCategories()[j].questions[i].question);
                    buttonToCategoryAndQuestionMap.put(b, new Entry(j, i));
                    b.addActionListener(this);
                    
                    if(j == activeBoard.getActiveCategoryIndex() && i == activeBoard.getActiveQuestionIndex()) {
                        b.setBackground(Color.GREEN);
                    } else {
                        if(activeBoard.getLastCategoryIndex() == j && activeBoard.getLastQuestionIndex() == i) {
                            b.setBackground(Color.YELLOW);
                        } else {
                            if(activeBoard.getInGameCategories()[j].questions[i].usedUp) {
                                b.setForeground(Color.GRAY);
                            } else {
                                b.setBackground(Color.WHITE);
                            }
                        }
                    }
                    
                    questionsPanel.add(b);
                } else {
                    questionsPanel.add(new JLabel(""));
                }
            }
        }
        
        validate();
    }
    
    public void actionPerformed(ActionEvent e) {
        if(buttonToCategoryAndQuestionMap.containsKey(e.getSource())) {
            selectedCategoryIndex = buttonToCategoryAndQuestionMap.get(e.getSource()).getKey();
            selectedQuestionIndex = buttonToCategoryAndQuestionMap.get(e.getSource()).getValue();
            
            Question selectedQuestion = activeBoard.getCategories()[selectedCategoryIndex].questions[selectedQuestionIndex];
            InGameQuestion igq = activeBoard.getInGameCategories()[selectedCategoryIndex].questions[selectedQuestionIndex];
            
            selectedAnswer.setText(Arrays.toString(selectedQuestion.answers));
            selectedQuestionLabel.setText(selectedQuestion.question);
            selectedPoints.setText("" + selectedQuestion.score);
            selectedUsed.setEnabled(true);
            activateButton.setEnabled(true);
            selectedUsed.setSelected(igq.usedUp);
        }
    }
    
    /**
     * server says a new game has begun!
     */
    public void startGameNotification() {
        showMessage("Game Started!");
    }
    
    /**
     *Server reports new status of game timer
     */
    public void gameTimerStatusChanged(boolean timerActive, int totalTime, int elapsedTime, boolean buttonsEnabled) {
        timerSlider.setEnabled(!timerActive);
        timerSlider.setMinimum(0);
        timerSlider.setMaximum(totalTime);
        timerSlider.setValue(elapsedTime);
        timerLabel.setText(elapsedTime + " / " + totalTime + " ms");
        
        playButton.setEnabled(buttonsEnabled);
        pauseButton.setEnabled(buttonsEnabled);
        endButton.setEnabled(buttonsEnabled);
    }
    
    private void activateQuestion(int categoryIndex, int questionIndex) {
        deactivateButton.setEnabled(true);
        activeQuestionQuestionLabel.setEnabled(true);
        activeQuestionAnswerLabel.setEnabled(true);
        activeQuestionPointsLabel.setEnabled(true);
        
        activeQuestionQuestionLabel.setText("Question: " + activeBoard.getActiveQuestion().question);
        activeQuestionAnswerLabel.setText("Answers: " + Arrays.toString(activeBoard.getActiveQuestion().answers));
        activeQuestionPointsLabel.setText("Points: " + activeBoard.getActiveQuestion().score);
    }
    
    
    private void deactivateQuestion() {
        deactivateButton.setEnabled(false);
        activeQuestionQuestionLabel.setEnabled(false);
        activeQuestionAnswerLabel.setEnabled(false);
        activeQuestionPointsLabel.setEnabled(false);
        
        activeQuestionQuestionLabel.setText("Question: none");
        activeQuestionAnswerLabel.setText("Answers: none");
        activeQuestionPointsLabel.setText("Points: none");
    }
    
    /**
     * New info from server regarding pulpits
     */
    public void setPulpitInfo(Map<String, PulpitStatus> info) {
        pulpitsPanel.removeAll();
        pulpitsPanel.setLayout(new GridLayout(info.values().size(), 1));
        
        boolean answering = false;
        
        for (Map.Entry<String, PulpitStatus> s : info.entrySet()) {
            System.out.println("Name: " + s.getKey() + " PulpitStatus: Nick: " + s.getValue().getNickname());
            pulpitsPanel.add(new PulpitInfoPanel(this, s.getKey(), s.getValue()));
            
            if(s.getValue().getState() == PulpitStatus.State.ANSWERING) {
                answering = true;
            }
        }
        
        if(answering) {
            pulpitAnswerCorrectButton.setEnabled(true);
            pulpitAnswerWrongButton.setEnabled(true);
        } else {
            pulpitAnswerCorrectButton.setEnabled(false);
            pulpitAnswerWrongButton.setEnabled(false);
        }
        
        validate();
    }
    
    /**
     * A puplet is answering, is it correct?
     */
    public void pulpetAnswer(PulpitAnswer answer) {
        pulpetAnswerInfoLabel.setText(answer.getName());
        pulpitAnswerCorrectButton.setEnabled(true);
        pulpitAnswerWrongButton.setEnabled(true);
        pulpitAnswerQuestionId = answer.getAnswerID();
    }
    
    public void showMessage(String text) {
        messageArea.setText(messageArea.getText() + new SimpleDateFormat("HH:mm:ss").format(new Date()) + " " + text + "\n");
        messageScroll.getVerticalScrollBar().setValue(messageScroll.getVerticalScrollBar().getMaximum());
    }
    
    public void timerChangedFromChangeTimerDialog(int newTimer) {
        adminConnection.modifyTimer(0, newTimer);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        activeBoardMenu = new javax.swing.JPopupMenu();
        jToggleButton1 = new javax.swing.JToggleButton();
        markedQuestionPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        selectedQuestionLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        selectedAnswer = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        selectedPoints = new javax.swing.JLabel();
        activateButton = new javax.swing.JButton();
        selectedUsed = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        activeQuestionPanel = new javax.swing.JPanel();
        activeQuestionQuestionLabel = new javax.swing.JLabel();
        activeQuestionAnswerLabel = new javax.swing.JLabel();
        activeQuestionPointsLabel = new javax.swing.JLabel();
        labelForTimer = new javax.swing.JLabel();
        timerLabel = new javax.swing.JLabel();
        endButton = new javax.swing.JButton();
        playButton = new javax.swing.JButton();
        timerSlider = new javax.swing.JSlider();
        deactivateButton = new javax.swing.JButton();
        pauseButton = new javax.swing.JButton();
        changeTimerButton = new javax.swing.JButton();
        pulpitsPanel = new javax.swing.JPanel();
        messagePanel = new javax.swing.JPanel();
        messageScroll = new javax.swing.JScrollPane();
        messageArea = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        pulpitAnswerCorrectButton = new javax.swing.JButton();
        pulpitAnswerWrongButton = new javax.swing.JButton();
        pulpetAnswerInfoLabel = new javax.swing.JLabel();
        questionsPanel = new javax.swing.JPanel();

        jToggleButton1.setText("jToggleButton1");

        markedQuestionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Selected Question"));

        jLabel1.setText("Question: ");

        selectedQuestionLabel.setText("(none selected)");

        jLabel2.setText("Answer:");

        selectedAnswer.setText("(none selected)");

        jLabel3.setText("Points:");

        selectedPoints.setText("(none selected)");

        activateButton.setText("Activate");
        activateButton.setEnabled(false);
        activateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activateButtonActionPerformed(evt);
            }
        });

        selectedUsed.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        selectedUsed.setEnabled(false);
        selectedUsed.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        selectedUsed.setMargin(new java.awt.Insets(0, 0, 0, 0));
        selectedUsed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectedUsedActionPerformed(evt);
            }
        });

        jLabel8.setText("Used:");

        org.jdesktop.layout.GroupLayout markedQuestionPanelLayout = new org.jdesktop.layout.GroupLayout(markedQuestionPanel);
        markedQuestionPanel.setLayout(markedQuestionPanelLayout);
        markedQuestionPanelLayout.setHorizontalGroup(
            markedQuestionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(markedQuestionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(markedQuestionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel3)
                    .add(jLabel2)
                    .add(jLabel1)
                    .add(jLabel8))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(markedQuestionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(selectedUsed)
                    .add(markedQuestionPanelLayout.createSequentialGroup()
                        .add(markedQuestionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(selectedPoints, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                            .add(selectedAnswer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, selectedQuestionLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(activateButton)))
                .addContainerGap())
        );
        markedQuestionPanelLayout.setVerticalGroup(
            markedQuestionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(markedQuestionPanelLayout.createSequentialGroup()
                .add(markedQuestionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(activateButton)
                    .add(selectedQuestionLabel)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(markedQuestionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(selectedAnswer)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(markedQuestionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(selectedPoints))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(markedQuestionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(selectedUsed)
                    .add(jLabel8))
                .addContainerGap(62, Short.MAX_VALUE))
        );

        activeQuestionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Active Question"));

        activeQuestionQuestionLabel.setText("Question:");

        activeQuestionAnswerLabel.setText("Answer:");

        activeQuestionPointsLabel.setText("Points:");

        labelForTimer.setText("Time:");

        timerLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        timerLabel.setText("0 / 0 ms");

        endButton.setText("End");
        endButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endButtonActionPerformed(evt);
            }
        });

        playButton.setText("Play");
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        timerSlider.setEnabled(false);

        deactivateButton.setText("De-activate");
        deactivateButton.setEnabled(false);
        deactivateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deactivateButtonActionPerformed(evt);
            }
        });

        pauseButton.setText("Pause");
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });

        changeTimerButton.setText("Change timer...");
        changeTimerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeTimerButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout activeQuestionPanelLayout = new org.jdesktop.layout.GroupLayout(activeQuestionPanel);
        activeQuestionPanel.setLayout(activeQuestionPanelLayout);
        activeQuestionPanelLayout.setHorizontalGroup(
            activeQuestionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(activeQuestionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(activeQuestionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(activeQuestionPanelLayout.createSequentialGroup()
                        .add(activeQuestionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(activeQuestionPanelLayout.createSequentialGroup()
                                .add(labelForTimer)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(timerLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(changeTimerButton))
                        .add(0, 0, 0)
                        .add(activeQuestionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(activeQuestionPanelLayout.createSequentialGroup()
                                .add(6, 6, 6)
                                .add(playButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 78, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(pauseButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 86, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(endButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE))
                            .add(activeQuestionPanelLayout.createSequentialGroup()
                                .add(5, 5, 5)
                                .add(timerSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)))
                        .add(132, 132, 132)
                        .add(deactivateButton))
                    .add(activeQuestionPointsLabel)
                    .add(activeQuestionQuestionLabel)
                    .add(activeQuestionAnswerLabel))
                .addContainerGap())
        );
        activeQuestionPanelLayout.setVerticalGroup(
            activeQuestionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(activeQuestionPanelLayout.createSequentialGroup()
                .add(activeQuestionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(activeQuestionPanelLayout.createSequentialGroup()
                        .add(activeQuestionQuestionLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(activeQuestionAnswerLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(activeQuestionPointsLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(activeQuestionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(labelForTimer)
                            .add(timerLabel)))
                    .add(activeQuestionPanelLayout.createSequentialGroup()
                        .add(62, 62, 62)
                        .add(timerSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 17, Short.MAX_VALUE)
                .add(activeQuestionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, deactivateButton)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, activeQuestionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(changeTimerButton)
                        .add(playButton)
                        .add(pauseButton)
                        .add(endButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pulpitsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pulpet info"));

        org.jdesktop.layout.GroupLayout pulpitsPanelLayout = new org.jdesktop.layout.GroupLayout(pulpitsPanel);
        pulpitsPanel.setLayout(pulpitsPanelLayout);
        pulpitsPanelLayout.setHorizontalGroup(
            pulpitsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 211, Short.MAX_VALUE)
        );
        pulpitsPanelLayout.setVerticalGroup(
            pulpitsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 335, Short.MAX_VALUE)
        );

        messagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Messages"));

        messageArea.setColumns(20);
        messageArea.setEditable(false);
        messageArea.setRows(5);
        messageScroll.setViewportView(messageArea);

        org.jdesktop.layout.GroupLayout messagePanelLayout = new org.jdesktop.layout.GroupLayout(messagePanel);
        messagePanel.setLayout(messagePanelLayout);
        messagePanelLayout.setHorizontalGroup(
            messagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(messagePanelLayout.createSequentialGroup()
                .add(messageScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)
                .addContainerGap())
        );
        messagePanelLayout.setVerticalGroup(
            messagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(messagePanelLayout.createSequentialGroup()
                .add(messageScroll)
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("Pulpet answer")));

        pulpitAnswerCorrectButton.setText("Correct");
        pulpitAnswerCorrectButton.setEnabled(false);
        pulpitAnswerCorrectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulpitAnswerCorrectButtonActionPerformed(evt);
            }
        });

        pulpitAnswerWrongButton.setText("Wrong");
        pulpitAnswerWrongButton.setEnabled(false);
        pulpitAnswerWrongButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulpitAnswerWrongButtonActionPerformed(evt);
            }
        });

        pulpetAnswerInfoLabel.setText("---");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(pulpitAnswerCorrectButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(pulpitAnswerWrongButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE))
                    .add(pulpetAnswerInfoLabel))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(pulpetAnswerInfoLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, pulpitAnswerWrongButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, pulpitAnswerCorrectButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        questionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Questions"));

        org.jdesktop.layout.GroupLayout questionsPanelLayout = new org.jdesktop.layout.GroupLayout(questionsPanel);
        questionsPanel.setLayout(questionsPanelLayout);
        questionsPanelLayout.setHorizontalGroup(
            questionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 822, Short.MAX_VALUE)
        );
        questionsPanelLayout.setVerticalGroup(
            questionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 183, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(questionsPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(markedQuestionPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(activeQuestionPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, messagePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(pulpitsPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(questionsPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(markedQuestionPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(activeQuestionPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(messagePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(pulpitsPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private void pulpitAnswerWrongButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pulpitAnswerWrongButtonActionPerformed
        adminConnection.judgePulpet(false, pulpitAnswerQuestionId);
    }//GEN-LAST:event_pulpitAnswerWrongButtonActionPerformed
    
    private void pulpitAnswerCorrectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pulpitAnswerCorrectButtonActionPerformed
        adminConnection.judgePulpet(true, pulpitAnswerQuestionId);
    }//GEN-LAST:event_pulpitAnswerCorrectButtonActionPerformed
    
    private void changeTimerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeTimerButtonActionPerformed
        new ChangeTimerDialog(this, timerSlider.getMaximum(), frame, true).setVisible(true);
    }//GEN-LAST:event_changeTimerButtonActionPerformed
    
    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        adminConnection.startTimer();
    }//GEN-LAST:event_playButtonActionPerformed
    
    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseButtonActionPerformed
        adminConnection.pauseTimer();
    }//GEN-LAST:event_pauseButtonActionPerformed
    
    private void endButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endButtonActionPerformed
        adminConnection.endTimer();
    }//GEN-LAST:event_endButtonActionPerformed
    
    private void deactivateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deactivateButtonActionPerformed
        adminConnection.deactivateQuestion();
    }//GEN-LAST:event_deactivateButtonActionPerformed
    
    private void activateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activateButtonActionPerformed
        System.out.println("activating: category: " + selectedCategoryIndex + " question: " + selectedQuestionIndex);
        adminConnection.setActiveQuestion(selectedCategoryIndex, selectedQuestionIndex);
        activateButton.setEnabled(false);
    }//GEN-LAST:event_activateButtonActionPerformed
    
    private void selectedUsedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectedUsedActionPerformed
        adminConnection.setQuestionUsedUp(selectedCategoryIndex, selectedQuestionIndex, selectedUsed.isSelected());
    }//GEN-LAST:event_selectedUsedActionPerformed
    
    /**
     * used by pulpetinfopanel to set new score
     */
    void changePulpetScore(String name, int newScore) {
        adminConnection.setPulpetScore(name, newScore);
    }
    
    /**
     * used by pulpetinfopanel to set new nickname
     */
    void changePulpetNickname(String name, String nickname) {
        adminConnection.setPulpetNickname(name, nickname);
    }

    public void keyTyped(KeyEvent e) {
        System.out.println(" Key typed: " + e.getKeyChar());
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton activateButton;
    private javax.swing.JPopupMenu activeBoardMenu;
    private javax.swing.JLabel activeQuestionAnswerLabel;
    private javax.swing.JPanel activeQuestionPanel;
    private javax.swing.JLabel activeQuestionPointsLabel;
    private javax.swing.JLabel activeQuestionQuestionLabel;
    private javax.swing.JButton changeTimerButton;
    private javax.swing.JButton deactivateButton;
    private javax.swing.JButton endButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel labelForTimer;
    private javax.swing.JPanel markedQuestionPanel;
    private javax.swing.JTextArea messageArea;
    private javax.swing.JPanel messagePanel;
    private javax.swing.JScrollPane messageScroll;
    private javax.swing.JButton pauseButton;
    private javax.swing.JButton playButton;
    private javax.swing.JLabel pulpetAnswerInfoLabel;
    private javax.swing.JButton pulpitAnswerCorrectButton;
    private javax.swing.JButton pulpitAnswerWrongButton;
    private javax.swing.JPanel pulpitsPanel;
    private javax.swing.JPanel questionsPanel;
    private javax.swing.JLabel selectedAnswer;
    private javax.swing.JLabel selectedPoints;
    private javax.swing.JLabel selectedQuestionLabel;
    private javax.swing.JCheckBox selectedUsed;
    private javax.swing.JLabel timerLabel;
    private javax.swing.JSlider timerSlider;
    // End of variables declaration//GEN-END:variables
    
}
