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
        if (req == null || req.authData().authToken() == null || req.authData().username() == null) {
            throw new BadRequestException("listGame request cannot be null");
        }

        AuthData myAuthData = req.authData();
        String myAuthToken = req.authData().authToken();
        String myUsername = req.authData().username();

        //does authToken exist?
         if (!authExist(myAuthToken)) {
             throw new BadRequestException("AuthToken does not exist.");
         }

        //does AuthToken match up with your username correctly?
        if (!authMatchUsername(myUsername, myAuthToken)) {
            throw new BadRequestException("AuthToken Does Not Match Username");
        }

        //get hashset of games from the db
        HashSet<GameData> myHashSet = dbAccess.getGameDAO().listGames();

        //put them into a List Result object
        ListResult finalResult = new ListResult(myHashSet);

        //return the list result
        return finalResult;
    }

    public CreateGameResult createGame(CreateGameRequest createGameReq) throws BadRequestException, DataAccessException {
        //if request is bad, error
        if (createGameReq == null || createGameReq.gameName() == null || createGameReq.authToken() == null) {
            throw new BadRequestException("Error: create game request, game name, and authtoken cannot be null");
        }

        //does authToken exist?
        AuthData auth;
        try {
            auth = dbAccess.getAuthDAO().getAuth(createGameReq.authToken());
        } catch (DataAccessException e) {
            throw new DataAccessException("AuthToken not found in database.");
        }


        //create a new game and add it to the db list
        HashSet<GameData> gamesList = dbAccess.getGameDAO().listGames();
        int oldLength = gamesList.size();
        int gameID = oldLength + 1;

        GameData newGame = new GameData(gameID, null, null, createGameReq.gameName(), new ChessGame());
        dbAccess.getGameDAO().addGame(newGame);

        HashSet<GameData> newGamesList = dbAccess.getGameDAO().listGames();
        int newLength = newGamesList.size();

        if (newLength != (oldLength + 1)) {
            throw new BadRequestException("new game did not get added to the db");
        }

        //return the new game name
        return new CreateGameResult(createGameReq.gameName());
    }

    public void joinGame(JoinRequest joinReq) throws BadRequestException, DataAccessException {
        //if request is bad, error
        if (joinReq == null || joinReq.playColor() == null || joinReq.authToken() == null) {
            throw new BadRequestException("Error: create game request, game name, and authtoken cannot be null");
        }

        //does authToken exist?
        AuthData auth;
        try {
            auth = dbAccess.getAuthDAO().getAuth(joinReq.authToken());
        } catch (DataAccessException e) {
            throw new DataAccessException("AuthToken not found in database.");
        }

        //does the game exist?
        GameData game;
        try {
            game = dbAccess.getGameDAO().getGame(joinReq.GameID());
        } catch (DataAccessException e) {
            throw new DataAccessException("Game not found in database.");
        }

        //this is who is already in the game:
        String whitePlayer = game.whiteUsername();
        String blackPlayer = game.blackUsername();

        //use update game function to add user into game as requested color
        if (joinReq.playColor() == ChessGame.TeamColor.WHITE) {
            //if the white player is not null, and is not your own username
            if (whitePlayer != null && !whitePlayer.equals(auth.username())) {
                throw new BadRequestException("White player already taken in this game");
            }
            else {
                dbAccess.getGameDAO().updateGame(new GameData(joinReq.GameID(), auth.username(), game.blackUsername(), game.gameName(), game.game()));
            }
        }
        if (joinReq.playColor() == ChessGame.TeamColor.BLACK) {
            //if the black player is not null, and is not your own username
            if (blackPlayer != null && !blackPlayer.equals(auth.username())) {
                throw new BadRequestException("Black player already taken in this game");
            }
            else {
                dbAccess.getGameDAO().updateGame(new GameData(joinReq.GameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game()));
            }
        }

    }

    public void cleardb() throws DataAccessException {
        dbAccess.getGameDAO().clear();
        dbAccess.getUserDAO().clear();
        dbAccess.getAuthDAO().clear();
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
