package ui;

import client.ChessClient;
import client.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {

    ServerFacade server;
    PostloginREPL postloginREPL;

    public Repl(ServerFacade server) {
        this.server = server;
        postloginREPL = new PostloginREPL(server);
    }

    public void run() {
        System.out.println(RESET_TEXT_COLOR + RESET_BG_COLOR + "Welcome to the Chess Client. Login or Register to start.");
        printHelp();
        String[] results;
        Scanner scanner = new Scanner(System.in);
        results = scanner.nextLine().split(" ");

        while (!results[0].equals("quit")) {
            if (results[0].equals("help")) {
                printHelp();
            }
            else if (results[0].equals("login")) {
                
            }
            else if (results[0].equals("register")) {

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