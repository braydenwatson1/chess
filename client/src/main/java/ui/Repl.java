package ui;

import Model.LoginRequest;
import Model.LoginResult;
import Model.RegisterRequest;
import Model.RegisterResult;
import client.ServerFacade;
import com.google.gson.Gson;

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

                    if (res.authToken() != null) {  // Assuming LoginResult has getAuthToken()
                        System.out.println(SET_TEXT_COLOR_GREEN + "Login successful!" + RESET_TEXT_COLOR);
                        System.out.println("DEBUG: Server response -> " + new Gson().toJson(res));

                        // innerRepl.run(server); // Uncomment when InnerRepl is ready
                    } else {
                        System.out.println(SET_TEXT_COLOR_RED + "Login failed." + RESET_TEXT_COLOR);
                    }

                    System.out.println(SET_TEXT_COLOR_YELLOW + "You have been logged out." + RESET_TEXT_COLOR);
                } catch (Exception e) {  // Catch any exception
                    System.out.println(SET_TEXT_COLOR_RED);
                    System.out.println("Username and password invalid");  // Print the error message
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
                    RegisterResult res = server.register(new RegisterRequest(results[1], results[2], results[3]));
                    System.out.println("Logging you in...");
                    LoginResult Lres = server.login(new LoginRequest(results[1], results[2]));
                    //innerRepl.run(server);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "You have been logged out." + RESET_TEXT_COLOR);
                } catch (Exception e) {
                    System.out.println("Registration failed:");
                }
            }
            else if (results[0].equals("quit")) {
                quited = true;
                break;
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