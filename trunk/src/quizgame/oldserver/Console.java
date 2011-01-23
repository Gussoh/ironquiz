/*
 * Console.java
 *
 * Created on den 24 november 2006, 21:32
 */

package quizgame.oldserver;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author rheo
 */
public class Console extends Thread {
    Server server;
    
    public Console(Server server) {
        this.server = server;
    }
    /*
    public void run() {
        final Scanner scanner = new Scanner(System.in);
        final TreeMap<String,Command> commands = new TreeMap<String,Command>();
        
        commands.put("boardhide", new Command() {
            public void run() {
                server.setBoardVisible(false);
            }
        });
        commands.put("boardshow", new Command() {
            public void run() {
                server.setBoardVisible(true);
            }
        });
        commands.put("help", new Command() {
            public void run() {
                for(String command : commands.keySet()) {
                    System.out.println(command);
                }
            }
        });
        commands.put("padd", new Command() {
            public void run() {
                if(!server.addPlayer(scanner.next())) {
                    System.out.println("Duplicate name");
                }
            }
        });
        commands.put("pbind", new Command() {
            public void run() {
                if(!server.bindPlayerPulpet(scanner.next(), scanner.nextInt())) {
                    System.out.println("Failed");
                }
            }
        });
        commands.put("punbind", new Command() {
            public void run() {
                if(!server.unbindPlayerPulpet(scanner.next())) {
                    System.out.println("Failed");
                }
            }
        });
        commands.put("pdel", new Command() {
            public void run() {
                if(!server.removePlayer(scanner.next())) {
                    System.out.println("No such player");
                }
            }
        });
        commands.put("plist", new Command() {
            public void run() {
                for(Map.Entry<String,Integer> player: server.getPlayers().entrySet()) {
                    System.out.println(player.getKey() + " " + player.getValue());
                }
            }
        });
        commands.put("prename", new Command() {
            public void run() {
                if(!server.renamePlayer(scanner.next(), scanner.next())) {
                    System.out.println("No such player");
                }
            }
        });
        commands.put("pscore", new Command() {
            public void run() {
                if(!server.setPlayerScore(scanner.next(), scanner.nextInt())) {
                    System.out.println("No such player");
                }
            }
        });
        /*commands.put("useradd", new Command() {
            public void run() {
                UserAccount userAccount = new UserAccount();
                String username = scanner.next();
                userAccount.password = scanner.next();
                userAccount.roles = EnumSet.noneOf(UserAccount.Role.class);
                config.userAccounts.put(username, userAccount);
            }
        });
        commands.put("userdel", new Command() {
            public void run() {
                if(config.userAccounts.remove(scanner.next()) == null) {
                    System.out.println("No such user");
                }
            }
        });
        commands.put("userinfo", new Command() {
            public void run() {
                UserAccount use
            Command commanrAccount = config.userAccounts.get(scanner.next());
                
                if(userAccount != null) {
                    System.out.println("Password: " + userAccount.password);
                    System.out.println("Roles: " + userAccount.roles);
                } else {
                    System.out.println("No such user");
                }
            }
        });
        commands.put("userlist", new Command() {
            public void run() {
                for(String username : config.userAccounts.keySet()) {
                    System.out.println(username);
                }
            }
        });
        commands.put("qcur", new Command() {
            public void run() {
                server.setCurrentQuestion(scanner.nextInt(), scanner.nextInt());
            }
        });
        commands.put("qnocur", new Command() {
            public void run() {
                server.setNoCurrentQuestion();
            }
        });
        commands.put("qused", new Command() {
            public void run() {
                server.setQuestionUsedUp(scanner.nextInt(), scanner.nextInt(), true);
            }
        });
        commands.put("qnotused", new Command() {
            public void run() {
                server.setQuestionUsedUp(scanner.nextInt(), scanner.nextInt(), false);
            }
        });
        commands.put("quit", new Command() {
            public void run() {
                System.exit(0);
            }
        });
        
        System.out.println("Type 'help' for a list of commands.");
        
        for(;;) {
            Command commandListener;
            String command;
            
            System.out.println("==========");
            
            try {
                command = scanner.next();
                
                commandListener = commands.get(command);
                
                if(commandListener != null) {
                    commandListener.run();
                } else {
                    System.out.println("Unrecognized command");
                }
                
                scanner.nextLine();
            } catch(NoSuchElementException ex) {
                return;
            }
        }
    }*/
}

interface Command {
    public void run();
}
