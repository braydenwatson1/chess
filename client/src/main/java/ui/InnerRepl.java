package ui;

import client.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class InnerRepl {
    ServerFacade server;
    InnerRepl innerRepl;

    public InnerRepl(ServerFacade server) {
        this.server = server;
        innerRepl = new InnerRepl(server);
    }

    public void run() {




        boolean loggedout = false;
        while (!loggedout) {
            Scanner scanner = new Scanner(System.in);
            String[] results = scanner.nextLine().split(" ");
            if (results[0].equals("help")) {
                printHelp();
            }
            else if (results[0].equals("logout")) {
                loggedout=true;
                break;
            }
        }
        // if you reach this point, this logs you out. there is nothing to return
    }

    private void printHelp() {
        private void printHelp() {
            System.out.println(SET_TEXT_COLOR_BLUE);
            System.out.println("to list all games, enter 'list'");
            System.out.println("to enter a game as a player, enter: 'join [BLACK or WHITE] GAME-NAME");
            System.out.println("for help menu, enter: 'help'");
            System.out.println("to logout, enter 'logout'");
            System.out.println(RESET_TEXT_COLOR);
        }
    }
}
