package service;

import Model.*;
import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;

import java.util.HashSet;
import java.util.Objects;


public class GameService {

    private final DataAccess dbAccess;

    public GameService(DataAccess dbAccess) {
        this.dbAccess = dbAccess;
    }

    public ListResult listGames(ListRequest req) throws BadRequestException, DataAccessException {
        //if request is bad, error
        System.out.println("DEBUG: WE ARE IN SERVICE IN THE LIST GAMES FUNCTION");
        if (req == null || req.authToken() == null) {
            assert req != null;
            throw new BadRequestException("listGame request cannot be null: " + req.toString());
        }

        String myAuthToken = req.authToken();

        //does authToken exist?
         if (!authExist(myAuthToken)) {
             throw new DataAccessException("Error: AuthToken does not exist.");
         }


        //get hashset of games from the db
        System.out.println("DEBUG: WE ARE ABOUT TO GET GAME DAO AND LIST GAMES FROM DATA ACCESS");
        HashSet<GameData> myHashSet = dbAccess.getGameDAO().listGames();

        if (myHashSet.isEmpty()) {
            System.out.println("DEBUG IN SERVICE ABOUT TO RETURN A NEW LIST RESULT (EMPTY)");
            return new ListResult(null);
        }
        HashSet<GameListData> simplifiedGames = new HashSet<>();
        for (GameData game : myHashSet) {
            GameListData simplifiedGame = new GameListData(
                    game.gameID(),
                    game.whiteUsername(),
                    game.blackUsername(),
                    game.gameName()
            );
            simplifiedGames.add(simplifiedGame);
        }
            //put them into a List Result object
            ListResult finalResult = new ListResult(simplifiedGames);

            //return the list result
            return finalResult;
    }

    public CreateGameResult createGame(CreateGameRequest createGameReq) throws BadRequestException, DataAccessException {
        //if request is bad, error
        if (createGameReq.gameName() == null || createGameReq.authToken() == null) {
            throw new BadRequestException("Error: create game request, game name, and authtoken cannot be null: " + createGameReq.toString());
        }

        String myGameName = createGameReq.gameName();
        String myAuthToken = createGameReq.authToken();

        //does authToken exist?
        if (!authExist(myAuthToken)) {
            throw new DataAccessException("Error: AuthToken does not exist.");
        }

        //create a new game and add it to the db list
        HashSet<GameData> gamesList = dbAccess.getGameDAO().listGames();
        int oldLength = gamesList.size();
        int gameID = oldLength + 1;

        GameData newGame = new GameData(gameID, null, null, myGameName, new ChessGame());
        dbAccess.getGameDAO().addGame(newGame);

        //verify that the list size expanded by 1:
            HashSet<GameData> newGamesList = dbAccess.getGameDAO().listGames();
            int newLength = newGamesList.size();

            if (newLength != (oldLength + 1)) {
                throw new BadRequestException("new game did not get added to the db");
            }

        //return the new game name
        return new CreateGameResult(gameID);
    }

    public void joinGame(JoinRequest joinReq) throws BadRequestException, DataAccessException, ForbiddenException {
        // If request is bad, error
        if (joinReq.playerColor() == null || joinReq.authToken() == null || joinReq.gameID() == null) {
            throw new BadRequestException("Error: create game request, game name, and authToken cannot be null" + joinReq.toString());
        }

        // Parse the gameID from string to int manually, with exception handling
        int myGameID;
        try {
            myGameID = Integer.parseInt(joinReq.gameID());
        } catch (NumberFormatException e) {
            throw new BadRequestException("Error: Invalid Game ID format in the joinGame function in service " + joinReq.gameID() + " or tostringed: "+joinReq.gameID().toString());
        }

        ChessGame.TeamColor myPlayColor = joinReq.playerColor();
        String myAuthToken = joinReq.authToken();
        AuthData myAuthData = dbAccess.getAuthDAO().getAuth(myAuthToken);
        String myUsername = myAuthData.username();

        if (!Objects.equals(myAuthData.authToken(), myAuthToken)) {
            throw new BadRequestException("Error in the GameService::joinGame function. AuthTokens are supposed to match");
        }

        // Does authToken exist?
        if (!authExist(myAuthToken)) {
            throw new BadRequestException("AuthToken does not exist.");
        }

        GameData gameData = dbAccess.getGameDAO().getGame(myGameID);

        // This is who is already in the game:
        String whitePlayer = gameData.whiteUsername();
        String blackPlayer = gameData.blackUsername();

        // Use update game function to add user into the game as requested color
        if (myPlayColor == ChessGame.TeamColor.WHITE) {
            // If the white player is not null and is not your own username
            if (whitePlayer != null && !whitePlayer.equals(myUsername)) {
                throw new ForbiddenException("White player already taken in this game");
            } else {
                dbAccess.getGameDAO().updateGame(new GameData(myGameID, myUsername, gameData.blackUsername(), gameData.gameName(), gameData.game()));
            }
        }
        if (joinReq.playerColor() == ChessGame.TeamColor.BLACK) {
            // If the black player is not null, and is not your own username
            if (blackPlayer != null && !blackPlayer.equals(myUsername)) {
                throw new ForbiddenException("Black player already taken in this game");
            } else {
                dbAccess.getGameDAO().updateGame(new GameData(myGameID, gameData.whiteUsername(), myUsername, gameData.gameName(), gameData.game()));
            }
        }
    }


    public void clear() throws DataAccessException {
        dbAccess.getGameDAO().clear();
    }

    //helper functions
    private boolean authExist(String authToken){
        try {
            dbAccess.getAuthDAO().getAuth(authToken);
        } catch (DataAccessException e) {
            return false;
        }
        return true;
    }

    private boolean authMatchUsername(String authToken, String username) throws DataAccessException {
     AuthData authData = dbAccess.getAuthDAO().getAuth(authToken);
     String correctUsername = authData.username();
     String correctAuth = authData.authToken();
     if (Objects.equals(username, correctUsername) && Objects.equals(authToken, correctAuth)) {
         return true;
     }
     return false;
    }

}
