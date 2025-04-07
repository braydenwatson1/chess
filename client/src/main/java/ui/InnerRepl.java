package ui;

import Model.*;
import chess.ChessGame;
import client.ServerFacade;
import java.util.Collection;
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

                if (results.length != 3) {
                    System.out.println("Incorrect format. Unable to process your join game request.");
                    printHelp();
                } else {
                    String id = results[1];
                    String color = results[2];
                    if (!color.equalsIgnoreCase("white") && !color.equalsIgnoreCase("black")) {
                    System.out.println("Not a valid Color. Please try your join request again.");
                    }
                    else {
                        joinGame(id, color, authToken);
                        BoardPrint bp = new BoardPrint(new ChessGame());
                        bp.printBoard(color,null);
                    }
                }
            }
            else if (results[0].equals("create")) {
                if (results.length != 2) {
                    System.out.println("Incorrect format. Unable to process your create game request.");
                    printHelp();
                } else {
                    String name = results[1];
                    CreateGameResult result = createGame(name, authToken);
                    if (result == null) {
                        System.out.println("There was an error in your create game request. please try again.");
                    }
                }
            }
            else if (results[0].equals("observe")) {
                System.out.println("DEBUG: havent set this up yet");
            }
            else {
                System.out.println("Command '" + results[0] + "' is not recognized.");
                printHelp();
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
            HashSet<String> printList = new HashSet<>();
            for(GameListData G : gamesList) {
                if (G.blackUsername() == null && G.whiteUsername() == null) {
                    printList.add("Game ID Number: " + Integer.toString(G.gameID()) + " -<>- Game Name: " + G.gameName() + " -<>- White Player: <SLOT EMPTY> -<>- Black Player: <SLOT EMPTY>");
                }
                else if (G.blackUsername() != null && G.whiteUsername() != null){
                    printList.add("Game ID Number: " + Integer.toString(G.gameID()) + " -<>- Game Name: " + G.gameName() + " -<>- White Player: " + G.whiteUsername() + " -<>- Black Player: " + G.blackUsername());
                }
                else if (G.blackUsername() == null && G.whiteUsername() != null) {
                    printList.add("Game ID Number: " + Integer.toString(G.gameID()) + " -<>- Game Name: " + G.gameName() + " -<>- White Player: " + G.whiteUsername() + " -<>- Black Player: <SLOT EMPTY>");
                }
                else if (G.blackUsername() != null && G.whiteUsername() == null) {
                    printList.add("Game ID Number: " + Integer.toString(G.gameID()) + " -<>- Game Name: " + G.gameName() + " -<>- White Player: <SLOT EMPTY> -<>- Black Player: " + G.blackUsername());
                }
            }
            for (String line : printList) {
                System.out.println(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_WHITE + line + RESET_TEXT_COLOR + RESET_BG_COLOR);
            }
        } catch (Exception e) {
            System.out.println(SET_TEXT_COLOR_RED + "Unexpected Error: contact admin. Unable to list games:" + e.getMessage() + e.getLocalizedMessage() + RESET_TEXT_COLOR);
        }
    }

    private void joinGame(String ID, String color, String authToken) {
        JoinRequest req = null;
        if (color.toLowerCase().equals("white")) {
            req = new JoinRequest(ID, ChessGame.TeamColor.WHITE, authToken);
        } else if (color.toLowerCase().equals("black")) {
            req = new JoinRequest(ID, ChessGame.TeamColor.BLACK, authToken);
        } else {
            System.out.println(SET_TEXT_COLOR_RED + "join request failed. invalid color choice" + RESET_TEXT_COLOR);
            printHelp();
        }

        //
        try {
            server.joinGame(req);
            System.out.println("Joining game now...");
        } catch (Exception e) {
            System.out.println(SET_TEXT_COLOR_RED + "Failed to join. Please ensure the game ID was entered correctly, and that your requested color is empty" + RESET_TEXT_COLOR);
        }

    }

    private CreateGameResult createGame(String gameName, String authToken) {
        CreateGameRequest req = null;
        if (gameName != null && authToken != null) {
            req = new CreateGameRequest(gameName, authToken);
        } else {
            System.out.println(SET_TEXT_COLOR_RED + "create game request failed. make sure to follow the proper format" + RESET_TEXT_COLOR);
            printHelp();
            return null;
        }
        //
        try {
            CreateGameResult result = server.createGame(req);
            System.out.println(SET_TEXT_COLOR_GREEN + "Game created Successfully. Available to join now! Here are all the current games: " + RESET_TEXT_COLOR);
            listGames(authToken);
            printHelp();
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return  null;
        }
    }

}
