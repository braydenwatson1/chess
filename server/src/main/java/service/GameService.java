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
        if (req == null || req.authToken() == null) {
            assert req != null;
            throw new BadRequestException("listGame request cannot be null: " + req.toString());
        }

        String myAuthToken = req.authToken();

        //does authToken exist?
         if (!authExist(myAuthToken)) {
             throw new BadRequestException("AuthToken does not exist.");
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
        if (createGameReq.gameName() == null || createGameReq.authToken() == null) {
            throw new BadRequestException("Error: create game request, game name, and authtoken cannot be null: " + createGameReq.toString());
        }

        String myGameName = createGameReq.gameName();
        String myAuthToken = createGameReq.authToken();

        //does authToken exist?
        if (!authExist(myAuthToken)) {
            throw new BadRequestException("AuthToken does not exist.");
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

    public void joinGame(JoinRequest joinReq) throws BadRequestException, DataAccessException {
        //if request is bad, error
        if (joinReq == null || joinReq.playColor() == null || joinReq.authData().authToken() == null) {
            throw new BadRequestException("Error: create game request, game name, and authtoken cannot be null");
        }

        int myGameID = joinReq.GameID();
        ChessGame.TeamColor myPlayColor = joinReq.playColor();
        AuthData myAuthData = joinReq.authData();
        String myAuthToken = myAuthData.authToken();
        String myUsername = myAuthData.username();

        //does authToken exist?
        if (!authExist(myAuthToken)) {
            throw new BadRequestException("AuthToken does not exist.");
        }

        //does AuthToken match up with your username correctly?
        if (!authMatchUsername(myUsername, myAuthToken)) {
            throw new BadRequestException("AuthToken Does Not Match Username");
        }

        GameData gameData = dbAccess.getGameDAO().getGame(myGameID);
        //this is who is already in the game:
        String whitePlayer = gameData.whiteUsername();
        String blackPlayer = gameData.blackUsername();

        //use update game function to add user into game as requested color
        if (myPlayColor == ChessGame.TeamColor.WHITE) {
            //if the white player is not null, and is not your own username
            if (whitePlayer != null && !whitePlayer.equals(myUsername)) {
                throw new BadRequestException("White player already taken in this game");
            }
            else {
                dbAccess.getGameDAO().updateGame(new GameData(myGameID, myUsername, gameData.blackUsername(), gameData.gameName(), gameData.game()));
            }
        }
        if (joinReq.playColor() == ChessGame.TeamColor.BLACK) {
            //if the black player is not null, and is not your own username
            if (blackPlayer != null && !blackPlayer.equals(myUsername)) {
                throw new BadRequestException("Black player already taken in this game");
            }
            else {
                dbAccess.getGameDAO().updateGame(new GameData(myGameID, gameData.whiteUsername() , myUsername, gameData.gameName(), gameData.game()));
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
