package service;

import TempModel.*;
import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

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
}
