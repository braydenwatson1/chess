package ui;

import Model.LoginRequest;
import Model.LoginResult;
import Model.RegisterRequest;
import Model.RegisterResult;
import client.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {

    ServerFacade server;
    InnerRepl innerRepl;

    public Repl(ServerFacade server) {
        this.server = server;
        innerRepl = new InnerRepl(server);
    }

    public void run() {
        System.out.println(RESET_TEXT_COLOR + RESET_BG_COLOR + "Welcome to the Chess Client. Login or Register to start.");
        printHelp();

        boolean quited = false;
        while (!quited) {
            Scanner scanner = new Scanner(System.in);
            String[] results = scanner.nextLine().split(" ");
            if (results[0].equals("help")) {
                printHelp();
            }
            else if (results[0].equals("login")) {
                if (results.length != 3) {
                    System.out.println(SET_TEXT_COLOR_RED);
                    System.out.println("Please follow this format: ");
                    System.out.println("to login, enter: 'login USERNAME PASSWORD'");
                    System.out.println(RESET_TEXT_COLOR);
                }
                try {
                    LoginResult res = server.login(new LoginRequest(results[1], results[2]));

                    if (res.authToken() != null) {
                        System.out.println(SET_TEXT_COLOR_GREEN + "Login successful!" + RESET_TEXT_COLOR);

                        innerRepl.run(res.authToken());
                        System.out.println(SET_TEXT_COLOR_YELLOW + "You have been logged out." + RESET_TEXT_COLOR);
                    } else {
                        System.out.println(SET_TEXT_COLOR_RED + "Login failed." + RESET_TEXT_COLOR);
                    }

                    printHelp();
                } catch (Exception e) {  // Catch any exception
                    System.out.println(SET_TEXT_COLOR_RED);
                    System.out.println("Login failed.");
                    System.out.println(RESET_TEXT_COLOR);
                }
            }
            else if (results[0].equals("register")) {
                if (results.length != 4) {
                    System.out.println(SET_TEXT_COLOR_RED);
                    System.out.println("Please follow this format: ");
                    System.out.println("to register, enter: 'register USERNAME PASSWORD EMAIL'");
                    System.out.println(RESET_TEXT_COLOR);
                }
                try {
                    System.out.println("Registering...");
                    RegisterRequest newReq = new RegisterRequest(results[1], results[2], results[3]);
                    RegisterResult res = server.register(newReq);
                    System.out.println("Logging you in...");
                    LoginRequest logReq = new LoginRequest(results[1], results[2]);

                    LoginResult Lres = server.login(logReq);

                    if (res.authToken() != null) {
                        System.out.println(SET_TEXT_COLOR_GREEN + "Login successful!" + RESET_TEXT_COLOR);
                        innerRepl.run(Lres.authToken());
                    } else {
                        System.out.println(SET_TEXT_COLOR_RED + "Login failed." + RESET_TEXT_COLOR);
                    }
                    System.out.println(SET_TEXT_COLOR_YELLOW + "You have been logged out." + RESET_TEXT_COLOR);
                    printHelp();
                } catch (Exception e) {
                    System.out.println("Registration failed: Username taken already");
                }
            }
            else if (results[0].equals("quit")) {
                quited = true;
                break;
            }
            else if (results[0].equals("admin") && results[1].equals("override") && results[2].equals("clear")) {
                try {
                    server.clear();
                } catch (Exception e) {
                    System.out.println("Admin override to clear database failed");
                }
            }
            else {
                System.out.println("Command '" + results[0] + "' is not recognized.");
                printHelp();
            }
        }
        System.out.println("Exiting chess client. Thanks for playing!");
        return;
    }

    private void printHelp() {
        System.out.println(SET_TEXT_COLOR_BLUE);
        System.out.println("to register, enter: 'register USERNAME PASSWORD EMAIL'");
        System.out.println("to login, enter: 'login USERNAME PASSWORD'");
        System.out.println("for help menu, enter: 'help'");
        System.out.println("to exit the chess client, enter: 'quit'");
        System.out.println(RESET_TEXT_COLOR);
    }

}