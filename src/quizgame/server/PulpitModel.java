/*
 * PulpetModel.java
 *
 * Created on den 4 maj 2007, 00:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.server;

import java.util.Map;
import quizgame.protocol.pulpit.PulpitAnswer;
import quizgame.protocol.pulpit.PulpitPacket;
import quizgame.protocol.pulpit.PulpitStatus;
import quizgame.server.usertypes.PulpitAccount;

/**
 *
 * @author Dukken
 */
public class PulpitModel {
    
    
    private Server server;
    /** Creates a new instance of PulpetModel */
    public PulpitModel(Server server) {
        this.server = server;
    }
    
    /**
     *  Adds a persistent lock to a pulpet. A pulpet with a persistent lock will always have its state set to LOCKED.
     */
    public void addPersistentLock(PulpitAccount pulpet) {
        pulpet.getPulpitStatus().setAlwaysLocked(true);
        pulpet.getPulpitStatus().setState(PulpitStatus.State.LOCKED);
    }
    
    /**
     *  Sets the alwaysLocked variable to false for all pulpets. The pulpets will be affected after next call to updatePulpits().
     */
    public void resetPersistentLocks() {
        for (PulpitAccount elem : server.getUserManager().getPulpits().values()) {
            elem.getPulpitStatus().setAlwaysLocked(false);
        }
    }
    
    /**
     *  This method sends current pulpet status to all pulpets. It is the only method that sends data to the pulpets.
     */
    public void updatePulpits() {
        for (Map.Entry<ClientHandler, PulpitAccount> elem : server.getUserManager().getPulpits().entrySet()) {
            elem.getKey().send(elem.getValue().getPulpitStatus());
        }
    }
    
    
    /**
     *  Handles incoming pulpet packets. If a pulpet wants to answer, AdminModel will be notified as well.
     */
    public void handlePulpetJob(ClientHandler clientHandler, PulpitPacket packet) {
        
        if(packet instanceof PulpitAnswer ) {
            PulpitAccount pulpet = server.getUserManager().getPulpits().get(clientHandler);
            if(pulpet.getPulpitStatus().getState() == PulpitStatus.State.UNLOCKED) {
                server.getGameModel().pulpetPressedButton(pulpet); // notify AdminModel
            }
        }
    }
    
    /**
     *  Should be called by the adminModel when an admin has decided whether a pulpet answered right or wrong.
     *  The function will only update the score based on the ActiveQuestion of the ActiveBoard,
     *  taking into account active bonuses/penalties.
     *  @param pulpet The pulpet to be judged.
     *  @param wasCorrect Set to true if pulpet was answered correct, otherwise false.
     */
    public void judgePulpit(PulpitAccount pulpet, boolean wasCorrect) {
        int score = server.getActiveBoard().getActiveQuestion().score;
        pulpet.getPulpitStatus().addScore(wasCorrect ? score : -score);
    }
    
    /**
     *  Sets the state for the given pulpet to ANSWERING. All other pulpets will be set to LOCKED.
     *  Pulpets will be updated after next call to updatePulpits().
     *  @param pulpet The pulpet that is allowed to answer.
     */
    public void pulpitAnswer(PulpitAccount pulpet) {
        for (PulpitAccount elem : server.getUserManager().getPulpits().values()) {
            if(elem != pulpet)
                elem.getPulpitStatus().setState(PulpitStatus.State.LOCKED);
        }
        pulpet.getPulpitStatus().setState(PulpitStatus.State.ANSWERING);
    }
    
    /**
     *  For pulpets with the variable alwaysLocked set to true, state will be set to LOCKED.
     *  For all other pulpets, state will be set to UNLOCKED.
     *  Pulpet monitors will be updated after next call to updatePulpits().
     *  Should for examped be called when the question has been read and the pulpets are allowed to answer.
     */
    public void unlockPulpits() {
        for (PulpitAccount pulpet : server.getUserManager().getPulpits().values()) {
            if(pulpet.getPulpitStatus().isAlwaysLocked()) {
                pulpet.getPulpitStatus().setState(PulpitStatus.State.LOCKED);
            } else {
                pulpet.getPulpitStatus().setState(PulpitStatus.State.UNLOCKED);
            }
        }
    }
    
    /**
     *  Lock all pulpets. Pulpet monitors will be updated after next call to updatePulpits().
     */
    public void lockPulpits() {
        for (PulpitAccount pulpet : server.getUserManager().getPulpits().values()) {
            pulpet.getPulpitStatus().setState(PulpitStatus.State.LOCKED);
        }
    }
    
    /**
     *  Initializes the pulpet. Creates a default PulpetStatus object, sets it up with the pulpet account and sends it to the pulpet.
     *  @param clientHandler the pulpet.
     */
    public void initPulpit(ClientHandler clientHandler) {
        PulpitAccount pulpet = server.getUserManager().getPulpits().get(clientHandler);
        PulpitStatus pulpitStatus = new PulpitStatus();
        pulpitStatus.setNickname(pulpet.getName());
        pulpitStatus.setState(PulpitStatus.State.LOCKED);
        pulpitStatus.setScore(0);
        pulpet.setPulpitStatus(pulpitStatus);
        clientHandler.send(pulpitStatus);
        server.getAdminModel().sendPulpitInformation();
    }
    
    /**
     *  Change the nickname of a pulpet.
     */
    public void setNickname(String accountName, String nickname) {
        for ( Map.Entry<ClientHandler, PulpitAccount> elem : server.getUserManager().getPulpits().entrySet()) {
            if(elem.getValue().getName().equals(accountName)) {
                elem.getValue().getPulpitStatus().setNickname(nickname);
                elem.getKey().send(elem.getValue().getPulpitStatus());
                break;
            }
        }
    }
    
    public void setScore(String accountName, int score) {
        for ( Map.Entry<ClientHandler, PulpitAccount> elem : server.getUserManager().getPulpits().entrySet()) {
            if(elem.getValue().getName().equals(accountName)) {
                elem.getValue().getPulpitStatus().setScore(score);
                elem.getKey().send(elem.getValue().getPulpitStatus());
                break;
            }
        }
    }
}
