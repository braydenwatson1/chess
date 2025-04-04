package ui;

import Model.*;
import client.ResponseException;
import client.ServerFacade;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class InnerRepl {
    ServerFacade server;


    public InnerRepl(ServerFacade server) {
        this.server = server;
    }

    public void run(String authToken) {
        printHelp();



        boolean loggedout = false;
        while (!loggedout) {
            Scanner scanner = new Scanner(System.in);
            String[] results = scanner.nextLine().split(" ");
            if (results[0].equals("help")) {
                printHelp();
            }
            else if (results[0].equals("logout")) {
                logout(authToken);
                loggedout = true;
                break;
            }
            else if (results[0].equals("list")) {
                listGames(authToken);
            }
            else if (results[0].equals("join")) {
                loggedout=true;
                break;
            }
            else if (results[0].equals("create")) {
                loggedout=true;
                break;
            }
            else if (results[0].equals("observe")) {
                loggedout=true;
                break;
            }
            else {

            }
        }
        // if you reach this point, this logs you out. there is nothing to return
    }

    private void printHelp() {
            System.out.println(SET_TEXT_COLOR_BLUE);
            System.out.println("to list all games, enter 'list'");
            System.out.println("to enter a game as a player, enter: 'join [BLACK or WHITE] GAME-NAME");
            System.out.println("for help menu, enter: 'help'");
            System.out.println("to logout, enter 'logout'");
            System.out.println(RESET_TEXT_COLOR);
    }

    private void logout(String authToken) {
        try {
            server.logout(new LogoutRequest(authToken));
        } catch (Exception e) {
            System.out.println(SET_TEXT_COLOR_RED + "Unexpected Error: contact admin. Unable to logout: " + e.getMessage() + RESET_TEXT_COLOR);
        }
    }

    private void listGames(String authToken) {
        try {
            ListResult listResult = server.listGames(new ListRequest(authToken));
            if (listResult.games() == null) {
                System.out.println("There are currently no games. Try creating a game to get started!");
                return;
            }
            Collection<GameListData> dataList = listResult.games();
            HashSet<GameListData> gamesList = new HashSet<GameListData>();
            gamesList.addAll(dataList);
            System.out.println("gamesList is: " + gamesList.toString());
            HashSet<String> printList = new HashSet<>();
            for(GameListData G : gamesList) {
                if (G.blackUsername() == null && G.whiteUsername() == null) {
                    printList.add("Game ID: " + Integer.toString(G.gameID()) + " -<>- Game Name: " + G.gameName() + " -<>- White Player: <SLOT EMPTY> -<>- Black Player: <SLOT EMPTY>");
                }
                else if (G.blackUsername() != null && G.whiteUsername() != null){
                    printList.add("Game ID: " + Integer.toString(G.gameID()) + " -<>- Game Name: " + G.gameName() + " -<>- White Player: " + G.whiteUsername() + " -<>- Black Player: " + G.blackUsername());
                }
                else if (G.blackUsername() == null && G.whiteUsername() != null) {
                    printList.add("Game ID: " + Integer.toString(G.gameID()) + " -<>- Game Name: " + G.gameName() + " -<>- White Player: " + G.whiteUsername() + " -<>- Black Player: <SLOT EMPTY>");
                }
                else if (G.blackUsername() != null && G.whiteUsername() == null) {
                    printList.add("Game ID: " + Integer.toString(G.gameID()) + " -<>- Game Name: " + G.gameName() + " -<>- White Player: <SLOT EMPTY> -<>- Black Player: " + G.blackUsername());
                }
            }
            for (String line : printList) {
                System.out.println(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_WHITE + line + RESET_TEXT_COLOR + RESET_BG_COLOR);
            }
        } catch (Exception e) {
            System.out.println(SET_TEXT_COLOR_RED + "Unexpected Error: contact admin. Unable to list games:" + e.getMessage() + e.getLocalizedMessage() + RESET_TEXT_COLOR);
        }
    }

}
