/*
 * Server.java
 *
 * Created on den 24 november 2006
 */

package quizgame.oldserver;

import java.util.HashSet;
import java.util.TreeMap;
import quizgame.common.Category;
import quizgame.common.InGameCategory;




/**
 *
 * @author rheo
 */
public class Server {
    private boolean boardVisible = true;
    private int currentCategoryIndex = -1;
    private int currentQuestionIndex;
    private int lastPulpetNumber;
    private Category[] currentCategories;
    private HashSet<ClientHandler> admins = new HashSet<ClientHandler>();
    private HashSet<ClientHandler> mainScreens = new HashSet<ClientHandler>();
    private HashSet<ClientHandler> typers = new HashSet<ClientHandler>(); // Added by Dukken 2007-01-22
    private InGameCategory[] inGameCategories;
    private ServerConfig config = new ServerConfig(); // TODO Read config from file
    private String[] boardNames;
    private TreeMap<Integer,Pulpit> pulpets = new TreeMap<Integer,Pulpit>();
    private TreeMap<String,Player> players = new TreeMap<String,Player>();
    private TreeMap<String,TyperAccount> typerAccounts = new TreeMap<String, TyperAccount>(); // Added by Dukken 2007-01-22
 /*
    private Server() {
        File[] boardFiles = config.boardDirectory.listFiles();
        ServerSocket listen;
  
        if(boardFiles == null) {
            boardFiles = new File[0];
        }
  
        boardNames = new String[boardFiles.length];
        for(int i = 0; i < boardFiles.length; i++) {
            boardNames[i] = boardFiles[i].getName();
        }
        Arrays.sort(boardNames);
  
        try {
            listen = new ServerSocket(config.listenPort);
        } catch (IOException ex) {
            logWrite("Failed to open listening socket: " + ex.getMessage());
            return;
        }
  
        new Console(this).start();
  
        for (;;) {
            Socket client;
  
            try {
                client = listen.accept();
            } catch (IOException ex) {
                logWrite("An exception occured while waiting for clients to connect: " + ex.getMessage());
                return;
            }
  
            new ClientHandler(this, client).start();
        }
    }
  
    public void logWrite(String message) {
        System.out.println(message);
    }
  
    public boolean authenticate(ClientHandler clientHandler, UserAccount.Role role, Authenticate authenticate, byte[] challange) {
        /*byte[] digest;
        MessageDigest md;
        UserAccount userAccount;
        String loginIncorrectMessage = "Username or password is incorrect.";
  
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            clientHandler.writeObject(new InternalServerError());
            return false;
        }
  
        userAccount = config.userAccounts.get(authenticate.username);
  
        if(userAccount == null) {
            clientHandler.writeObject(new AuthenticationFailed(loginIncorrectMessage));
            return false;
        }
  
        try {
            md.update(userAccount.password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            clientHandler.writeObject(new InternalServerError());
            return false;
        }
  
        digest = md.digest(challange);
  
        if(digest.length != authenticate.passwordHash.length) {
            clientHandler.writeObject(new AuthenticationFailed(loginIncorrectMessage));
            return false;
        }
  
        for(int i = 0; i < digest.length; i++) {
            if(digest[i] != authenticate.passwordHash[i]) {
                clientHandler.writeObject(new AuthenticationFailed(loginIncorrectMessage));
                return false;
            }
        }
  
        if(!userAccount.roles.contains(role)) {
            clientHandler.writeObject(new InsufficientPrivileges());
            return false;
        }
  
        clientHandler.writeObject(new AuthenticationSuccessful());
        return true;
    }
  
  
  
    public boolean addAdminConnection(ClientHandler clientHandler, Authenticate authenticate, byte[] challange) {
        if(!authenticate(clientHandler, UserAccount.Role.ADMIN, authenticate, challange)) {
            return false;
        }
  
        if(boardNames.length > 0) {
            BoardNames message = new BoardNames();
            message.boardNames = boardNames;
            clientHandler.writeObject(message);
        }
  
        admins.add(clientHandler);
        return true;
    }
  
    public boolean addMainScreenConnection(ClientHandler clientHandler, Authenticate authenticate, byte[] challange) {
        if(!authenticate(clientHandler, UserAccount.Role.MAIN_SCREEN, authenticate, challange)) {
            return false;
        }
  
        if(boardVisible) {
            SetCategories message = new SetCategories();
            message.categories = inGameCategories;
            clientHandler.writeObject(message);
        }
  
        mainScreens.add(clientHandler);
        return true;
    }
  
    public boolean addPulpetConnection(ClientHandler clientHandler, Authenticate authenticate, byte[] challange) {
        Pulpit pulpet;
        SetPulpetNumber message;
  
        if(!authenticate(clientHandler, UserAccount.Role.PULPET, authenticate, challange)) {
            return false;
        }
  
        lastPulpetNumber++;
        pulpet = new Pulpit();
        pulpet.clientHandler = clientHandler;
        pulpets.put(lastPulpetNumber, pulpet);
        message = new SetPulpetNumber();
        message.pulpetNumber = lastPulpetNumber;
        clientHandler.writeObject(message);
        return true;
    }
  
    // Added by Dukken 2007-01-22
    public boolean addTyperConnection(ClientHandler clientHandler, AuthenticateTyper authenticate, byte[] challange) {
        /*
        String loginIncorrect = "Login incorrect";
  
        if(authenticate.username == null) {
            return false;
        }
  
        TyperAccount account = typerAccounts.get(authenticate.username);
        if(authenticate.createAccount) {
            if(account != null) {
                // Account already exists
                clientHandler.writeObject(new AuthenticationFailed("Login name already exists"));
                return false;
            }
  
            account = new TyperAccount();
            account.name = authenticate.username;
            account.password = new String(challange);
            account.score = 0;
            typerAccounts.put(authenticate.username, account);
  
        } else {
            if(account == null) {
                clientHandler.writeObject(new AuthenticationFailed(loginIncorrect));
                return false;
            }
  
            if(!account.password.equals(new String(authenticate.passwordHash))) {
                clientHandler.writeObject(new AuthenticationFailed(loginIncorrect));
                return false;
            }
        }
  
        clientHandler.writeObject(new AuthenticationSuccessful());
  
        return true;
    }
  
    public void removeAdminConnection(ClientHandler clientHandler) {
        admins.remove(clientHandler);
    }
  
    public void removeMainScreenConnection(ClientHandler clientHandler) {
        mainScreens.remove(clientHandler);
    }
  
    public void removePulpetConnection(ClientHandler clientHandler) {
        for(Map.Entry<Integer,Pulpit> pulpet : pulpets.entrySet()) {
            if (pulpet.getValue().clientHandler == clientHandler) {
                if (pulpet.getValue().player != null) {
                    pulpet.getValue().player.pulpet = null;
                }
                pulpets.remove(pulpet.getKey());
            }
        }
    }
  
    public void sendToAllAdmins(Serializable object) {
        for(ClientHandler clientHandler : admins) {
            clientHandler.writeObject(object);
        }
    }
  
    private void sendToAllMainScreens(Serializable object) {
        for(ClientHandler clientHandler : mainScreens) {
            clientHandler.writeObject(object);
        }
    }
  
    public void removeBoards(String[] names) {
        int affectedCount = 0;
        int boardNamesIndex = 0;
        int indicesIndex = 0;
        int[] indices = new int[names.length];
        BoardsRemoved message;
        String[] oldBoardNames = boardNames;
  
        for(String name : names) {
            if(name != null) {
                int index = Arrays.binarySearch(boardNames, name);
                if(index >= 0) {
                    indices[indicesIndex] = index;
                    indicesIndex++;
                }
            }
        }
  
        for(int i = 0; i < indicesIndex; i++) {
            int index = indices[i];
            if(boardNames[index] != null) {
                new File(config.boardDirectory, boardNames[index]).delete();
                boardNames[index] = null;
                affectedCount++;
            }
        }
  
        boardNames = new String[boardNames.length - affectedCount];
        indices = new int[affectedCount];
        indicesIndex = 0;
  
        for(String boardName : oldBoardNames) {
            if(boardName == null) {
                indices[indicesIndex] = boardNamesIndex + indicesIndex;
                indicesIndex++;
            } else {
                boardNames[boardNamesIndex] = boardName;
                boardNamesIndex++;
            }
        }
  
        message = new BoardsRemoved();
        message.boardIndices = indices;
        sendToAllAdmins(message);
    }
  
    public void setBoardName(String oldName, String newName) {
        int newIndex;
        int oldIndex;
        File targetFile;
        SetBoardName message;
  
        if(newName == null) {
            return;
        }
  
        oldIndex = Arrays.binarySearch(boardNames, oldName);
  
        if(oldIndex < 0) {
            return;
        }
  
        newIndex = Arrays.binarySearch(boardNames, newName);
  
        if(newIndex >= 0) {
            return;
        }
  
        newIndex = -newIndex - 1;
        targetFile = new File(config.boardDirectory, newName);
  
        if(!targetFile.getParentFile().equals(config.boardDirectory)) {
            // TODO Alert the client about the problem
            return;
        }
  
        if(!new File(config.boardDirectory, boardNames[oldIndex]).renameTo(targetFile)) {
            // TODO Alert the client about the problem
            return;
        }
  
        if(newIndex > oldIndex) {
            newIndex--;
            System.arraycopy(boardNames, oldIndex + 1, boardNames, oldIndex, newIndex - oldIndex);
        } else if(newIndex < oldIndex) {
            System.arraycopy(boardNames, newIndex, boardNames, newIndex + 1, oldIndex - newIndex);
        }
  
        boardNames[newIndex] = newName;
        message = new SetBoardName();
        message.boardIndex = oldIndex;
        message.newName = newName;
        sendToAllAdmins(message);
    }
  
    public void saveBoard(String name, Category[] categories) {
        int index;
        File targetFile = new File(config.boardDirectory, name);
        ObjectOutputStream output;
  
        if(!targetFile.getParentFile().equals(config.boardDirectory)) {
            // TODO Alert the client about the problem
            return;
        }
  
        try {
            targetFile.delete();
            output = new ObjectOutputStream(new FileOutputStream(targetFile));
            output.writeObject(categories);
        } catch (IOException ex) {
            // TODO Alert the client about the problem
            return;
        }
  
        index = Arrays.binarySearch(boardNames, name);
  
        if(index < 0) {
            BoardAdded message = new BoardAdded();
            boardNames = SortedArrays.add(boardNames, new String[boardNames.length + 1], -index - 1, name);
            message.boardName = name;
            sendToAllAdmins(message);
        }
    }
  
    public Category[] getBoard(String name) {
        Category[] categories;
        File boardFile = new File(config.boardDirectory, name);
        ObjectInputStream input;
  
        if(!boardFile.getParentFile().equals(config.boardDirectory)) {
            // TODO Alert the client about the problem
            return null;
        }
  
        try {
            input = new ObjectInputStream(new FileInputStream(boardFile));
        } catch (IOException ex) {
            // TODO Alert the client about the problem
            return null;
        }
  
        try {
            categories = (Category[]) input.readObject();
        } catch (IOException ex) {
            // TODO Alert the client about the problem
            return null;
        } catch (ClassNotFoundException ex) {
            // TODO Alert the client about the problem
            return null;
        }
  
        return categories;
    }
  
    public void setBoard(String name) {
        Category[] categories = getBoard(name);
  
        if(categories == null) {
            return;
        }
  
        boardVisible = false;
        currentCategories = categories;
        inGameCategories = new InGameCategory[categories.length];
  
        for(int i = 0; i < categories.length; i++) {
            inGameCategories[i] = new InGameCategory();
            inGameCategories[i].name = categories[i].name;
            inGameCategories[i].questions = new InGameQuestion[categories[i].questions.length];
  
            for(int j = 0; j < categories[i].questions.length; j++) {
                inGameCategories[i].questions[j] = new InGameQuestion();
                inGameCategories[i].questions[j].score = categories[i].questions[j].score;
            }
        }
    }
  
    public void setBoardVisible(boolean visible) {
        if(currentCategories != null && visible != boardVisible) {
            SetBoardVisible setBoardVisible = new SetBoardVisible();
            SetCategories setCategories = new SetCategories();
  
            boardVisible = visible;
            currentCategoryIndex = -1;
            setBoardVisible.boardVisible = visible;
  
            if(visible) {
                setCategories.categories = inGameCategories;
            } else {
                setCategories.categories = null;
            }
  
            sendToAllAdmins(setBoardVisible);
            sendToAllMainScreens(setCategories);
        }
    }
  
    public void setQuestionUsedUp(int categoryIndex, int questionIndex, boolean usedUp) {
        SetQuestionUsedUp message;
  
        if(currentCategories == null) {
            return;
        }
  
        try {
            inGameCategories[categoryIndex].questions[questionIndex].usedUp = usedUp;
        } catch(ArrayIndexOutOfBoundsException ex) {
            return;
        }
  
        message = new SetQuestionUsedUp();
        message.categoryIndex = categoryIndex;
        message.questionIndex = questionIndex;
        message.usedUp = usedUp;
  
        sendToAllAdmins(message);
  
        if(boardVisible) {
            sendToAllMainScreens(message);
        }
    }
  
    public void setCurrentQuestion(int categoryIndex, int questionIndex) {
        if(boardVisible) {
            SetCurrentQuestion message;
  
            try {
                inGameCategories[categoryIndex].questions[questionIndex].usedUp = true;
            } catch(ArrayIndexOutOfBoundsException ex) {
                return;
            }
  
            currentCategoryIndex = categoryIndex;
            currentQuestionIndex = questionIndex;
  
            message = new SetCurrentQuestion();
            message.categoryIndex = categoryIndex;
            message.questionIndex = questionIndex;
            message.question = currentCategories[categoryIndex].questions[questionIndex].question;
            sendToAllMainScreens(message);
        }
    }
  
    public void setNoCurrentQuestion() {
        if(currentCategoryIndex >= 0) {
            currentCategoryIndex = -1;
            sendToAllAdmins(new SetNoCurrentQuestion());
            sendToAllMainScreens(new SetNoCurrentQuestion());
        }
    }
  
    public TreeMap<String,Integer> getPlayers() {
        TreeMap<String,Integer> treeMap = new TreeMap<String,Integer>();
  
        for (Map.Entry<String,Player> player : players.entrySet()) {
            treeMap.put(player.getKey(), player.getValue().score);
        }
  
        return treeMap;
    }
  
    public boolean addPlayer(String name) {
        if (players.containsKey(name)) {
            return false;
        }
  
        players.put(name, new Player());
  
        return true;
    }
  
    public boolean setPlayerScore(String name, int score) {
        Player player = players.get(name);
  
        if (player == null) {
            return false;
        }
  
        player.score = score;
  
        if (player.pulpet != null) {
          /*SetPulpetScore message = new SetPulpetScore();
            message.score = player.score;
            player.pulpet.clientHandler.writeObject(message);
  
        }
  
        return true;
    }
  
    public boolean removePlayer(String name) {
        Player player = players.remove(name);
  
        if (player != null && player.pulpet != null) {
            player.pulpet.clientHandler.writeObject(new SetPulpetNameAndScore());
            player.pulpet.player = null;
        }
  
        return player != null;
    }
  
    public boolean renamePlayer(String oldName, String newName) {
        Player player = players.remove(oldName);
  
        if (player == null || players.containsKey(newName)) {
            return false;
        }
  
        if (player.pulpet != null) {
            SetPulpetNameAndScore message = new SetPulpetNameAndScore();
            message.name = newName;
            message.score = player.score;
            player.pulpet.clientHandler.writeObject(message);
        }
  
        players.put(newName, player);
  
        return true;
    }
  
    public boolean bindPlayerPulpet(String name, int pulpetNumber) {
        Player player = players.get(name);
        Pulpit pulpet;
    //    SetPulpetNameAndScore message;
  
        if (player == null || player.pulpet != null) {
            return false;
        }
  
        pulpet = pulpets.get(pulpetNumber);
  
        if(pulpet == null || pulpet.player != null) {
            return false;
        }
  
        player.pulpet = pulpet;
        pulpet.player = player;
  
    //    message = new SetPulpetNameAndScore();
        message.name = name;
        message.score = player.score;
        pulpet.clientHandler.writeObject(message);
  
        return true;
    }
  
    public boolean unbindPlayerPulpet(String name) {
        Player player = players.get(name);
  
        if(player == null || player.pulpet == null) {
            return false;
        }
  
       // player.pulpet.clientHandler.writeObject(new SetPulpetNameAndScore());
        player.pulpet.player = null;
        player.pulpet = null;
  
        return true;
    }
  
    /**
  * @param args the command line arguments
  */
    public static void main(String[] args) {
        new Server();
    }
}

class Player {
    public int score;
    public Pulpit pulpet;
}

class Pulpit {
    public ClientHandler clientHandler;
    public Player player;
}
