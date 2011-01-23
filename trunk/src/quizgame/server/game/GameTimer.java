/*
 * GameTimer.java
 *
 * Created on den 8 maj 2007, 23:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.server.game;

import java.util.Timer;
import java.util.TimerTask;
import quizgame.protocol.admin.QuestionTimedOut;
import quizgame.server.Server;

/**
 *  This class is not thread-safe. Its important that there is only one worker thread active.
 * @author Dukken
 */
public class GameTimer {
    
    private int total;
    private Timer timer = new Timer();
    private long started;
    private int elapsed;
    private int alarmID = 0;
    private boolean running = false;
    
    private Server server;
    /** Creates a new instance of GameTimer.
     *  @param server Reference to the server, needed when timer timeouts.
     *  @param delay The time in milliseconds before the timer will timeout.
     */
    public GameTimer(Server server, int total) {
        this.server = server;
        this.total = total;
    }
    
    /**
     *  Starts the timer if it is not running.
     *  @returns true if the call to this method changed the timer, otherwise false.
     */
    public boolean startTimer() {
        if(!running) {
            started = System.currentTimeMillis();
            try {
                timer.schedule(new InternalTimerTask(alarmID), total - elapsed);
            } catch (IllegalArgumentException ex) {
                // If delay - elapsed is negative. Can happen if stopAndReset hasn't been called.
                return false;
            }
            running = true;
            return true;
        }
        return false;
    }

    public boolean isRunning() {
        return running;
    }


    /**
     *  Pauses the timer if it is running.
     *  @returns true if the call to this method changed the timer, otherwise false.
     */
    public boolean pauseTimer() {
        if(running) {
            elapsed += System.currentTimeMillis() - started;
            alarmID++; // Invalidate the internal timertask.
            running = false;
            return true;
        }
        return false;
    }
    
    
    /**
     *  Stops and resets the timer whether it is running or paused.
     */
    public void stopAndResetTimer() {
        if(running) {
            alarmID++; //Invalidate the internal timertask
            running = false;
        }
        elapsed = 0;
    }
    
    /**
     *  Sets new total time in milliseconds.
     *  @returns true if the call to this method changed the timer, otherwise false.
     */
    public boolean setTotal(int total) {
        if(!running){
            this.total = total;
            return true;
        }
        
        return false;
    }
    
    public int getElapsed() {
        return elapsed;
    }
    
    /**
     *  Sets elapsed time. Must be called when the timer is paused.
     *  @returns true if the call to this method changed the timer, otherwise false.
     */
    public boolean setElapsed(int elapsed) {
        if(!running) {
            this.elapsed = elapsed;
            return true;
        }
        return false;
    }
    
    public int getTotal() {
        return total;
    }
    
    /**
     *  Check if an alarm/timeoutID is valid or not.
     *  @returns true if the id is valid, otherwise false.
     */
    public boolean isAlarmIDValid(int alarmID) {
        return this.alarmID == alarmID;
    }
    
    
    private class InternalTimerTask extends TimerTask {
        
        private int alarmID;
        public InternalTimerTask(int alarmID) {
            this.alarmID = alarmID;
        }
        
        public void run() {
            server.addJob(null, new QuestionTimedOut(alarmID));
        }
    }
}
