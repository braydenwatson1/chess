package service;

import TempModel.*;
import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;

import java.util.HashSet;


public class GameService {

    private DataAccess dbAccess;

    public ListResult listGames(AuthData authObj) throws BadRequestException, DataAccessException {
        //if request is bad, error
        if (authObj == null || authObj.authToken() == null || authObj.username() == null) {
            throw new BadRequestException("list games request cannot be null");
        }

        //does authToken exist?
        AuthData auth;
        try {
            auth = dbAccess.getAuthDAO().getAuth(authObj.authToken());
        } catch (DataAccessException e) {
            throw new DataAccessException("AuthToken not found in database.");
        }

        //does AuthToken match up with your username correctly?
        String matchingUsername = auth.username();
        if (!authObj.username().equals(matchingUsername)) {
            throw new BadRequestException("AuthToken does not match your username.");
        }

        //get a list of games and put it into a ListResult object

        ListResult finalResult = new ListResult(dbAccess.getGameDAO().listGames());
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

        //use update game funtion to add user into game as requested color
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
    
}
