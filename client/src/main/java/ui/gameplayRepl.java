package ui;

import client.ServerFacade;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class gameplayRepl {
    ServerFacade server;

    public gameplayRepl(ServerFacade server) {
        this.server = server;
    }

    public void run(String authToken) {
        printHelp();


        boolean gameEnded = false;
        while (!gameEnded) {
            Scanner scanner = new Scanner(System.in);
            String[] results = scanner.nextLine().split(" ");
            if (results[0].equals("help")) {
                printHelp();
            } else if (results[0].equals("resign")) {
                System.out.println(SET_TEXT_BOLD + SET_BG_COLOR_BLACK + SET_BG_COLOR_YELLOW);
                System.out.println("GAME OVER: \nYou have resigned. Your opponent has won. \nThank you for playing.");
                System.out.println(RESET_TEXT_COLOR + RESET_BG_COLOR + RESET_TEXT_BOLD_FAINT);
                gameEnded = true;
            } else if (results[0].equals("move")) {

            } else {
                System.out.println("Command '" + results[0] + "' is not recognized.");
                printHelp();
            }
        }
        // if you reach this point, this ends the game and sends you out of the repl. there is nothing to return
    }

    private void printHelp() {
        System.out.println("PRINT HELP FUNCTION");
    }


}

