/*
 * Server.java
 *
 * Created on den 6 mars 2007, 18:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package quizgame.server;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import quizgame.protocol.Authenticate;
import quizgame.protocol.Logout;
import quizgame.protocol.Packet;
import quizgame.protocol.admin.AdminPacket;
import quizgame.protocol.admin.QuestionTimedOut;
import quizgame.protocol.mainscreen.MainScreenPacket;
import quizgame.protocol.pulpit.PulpitPacket;
import quizgame.server.admin.AdminModel;
import quizgame.server.game.DefaultGameModel;
import quizgame.server.game.GameModel;
import quizgame.server.typer.TyperModel;

/**
 *
 * @author Dukken
 */
public class Server implements Thread.UncaughtExceptionHandler {
    
    private ConcurrentLinkedQueue<Job> jobQueue = new ConcurrentLinkedQueue<Job>();
    private Semaphore jobSemaphore = new Semaphore(0);
    
    private ConnectionListener connectionListener = new ConnectionListener(this);
    private AccountManager accountManager = new AccountManager(this);
    private GameBoardManager boardManager = new GameBoardManager();
    private TyperModel typerModel = new TyperModel(this);
    private AdminModel adminModel = new AdminModel(this);
    private PulpitModel pulpitModel = new PulpitModel(this);
    private MainscreenModel mainscreenModel = new MainscreenModel(this);
    private GameModel gameModel = new DefaultGameModel(this);
    private ActiveBoard activeBoard = null;
    
    
    /** Creates a new instance of Server */
    public Server() {
        try {
            connectionListener.createActiveServerSocket(8888);
            new Console(this);
            createWorker();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        Logger.getInstance().setUseConsole(true);
        new Server();
    }

    public void addJob(ClientHandler clientHandler, Packet packet) {
        jobQueue.offer(new Job(clientHandler, packet));
        jobSemaphore.release();
    }

    public PulpitModel getPulpitModel() {
        return pulpitModel;
    }

    public AdminModel getAdminModel() {
        return adminModel;
    }

    public MainscreenModel getMainscreenModel() {
        return mainscreenModel;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public TyperModel getTyperModel() {
        return typerModel;
    }


    private void createWorker() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setUncaughtExceptionHandler(Server.this);
                for(;;) {
                    jobSemaphore.acquireUninterruptibly();
                    Job job = jobQueue.poll();
                    
                    if(job.packet instanceof Logout) {
                        accountManager.handleLogoutJob(job.clientHandler, (Logout) job.packet);
                    } else if(job.packet instanceof Authenticate) {
                        accountManager.handleAuthenticateJob(job.clientHandler, (Authenticate) job.packet);
                    } else if(job.packet instanceof QuestionTimedOut) {
                        gameModel.timedOutListener((QuestionTimedOut) job.packet);
                    } else if(job.packet instanceof AdminPacket) {
                        adminModel.handleAdminJob(job.clientHandler, (AdminPacket) job.packet);
                    } else if(job.packet instanceof PulpitPacket) {
                        pulpitModel.handlePulpetJob(job.clientHandler, (PulpitPacket) job.packet);
                    } else if(job.packet instanceof MainScreenPacket) {
                        mainscreenModel.handleMainscreenJob(job.clientHandler, (MainScreenPacket) job.packet);
                    }
                }
            }
        }).start();
    }

    public AccountManager getUserManager() {
        return accountManager;
    }

    public GameBoardManager getBoardManager() {
        return boardManager;
    }
    
    public ActiveBoard getActiveBoard() {
        return activeBoard;
    }

    public void setActiveBoard(ActiveBoard activeBoard) {
        this.activeBoard = activeBoard;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Logger.getInstance().println("Server: Worker-thread caused an uncaught exception. " + e);
        e.printStackTrace();
        createWorker();
        Logger.getInstance().println("Server: A new worker-thread was created.");
    }
    
    private class Job {
        public final ClientHandler clientHandler;
        public final Packet packet;
        
        public Job(ClientHandler clientHandler, Packet packet) {
            this.clientHandler = clientHandler;
            this.packet = packet;
        }
    }
}
