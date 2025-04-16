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

    public ListResult listGames(ListRequest req) throws BadRequestException, DataAccessException, UnauthorizedException {
        //if request is bad, error
        if (req == null || req.authToken() == null) {
            if (req == null) {throw new BadRequestException("listGame request is null");}
            throw new BadRequestException("listGame request cannot be null: " + req.toString());
        }

        String myAuthToken = req.authToken();

        //does authToken exist?
        try {
            AuthData auth = dbAccess.getAuthDAO().getAuth(myAuthToken);
        } catch (DataAccessException e) {
            throw new UnauthorizedException(e.getMessage());
        }
        //get hashset of games from the db
        HashSet<GameData> myHashSet = dbAccess.getGameDAO().listGames();

        if (myHashSet.isEmpty()) {
            return new ListResult(new HashSet<>());
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

    public CreateGameResult createGame(CreateGameRequest createGameReq) throws BadRequestException, DataAccessException, UnauthorizedException {
        //if request is bad, error
        if (createGameReq.gameName() == null || createGameReq.authToken() == null) {
            throw new BadRequestException("Error: create game request, game name, and authtoken cannot be null: " + createGameReq.toString());
        }

        String myGameName = createGameReq.gameName();
        String myAuthToken = createGameReq.authToken();

        //does authToken exist?
        try {
            AuthData auth = dbAccess.getAuthDAO().getAuth(myAuthToken);
        } catch (DataAccessException e) {
            throw new UnauthorizedException(e.getMessage());
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
                throw new BadRequestException("Error: new game did not get added to the db");
            }


        //return the new game name
        return new CreateGameResult(gameID);
    }

    public void joinGame(JoinRequest joinReq) throws BadRequestException, DataAccessException, ForbiddenException, UnauthorizedException {
        // If request is bad, error
        if (joinReq.playerColor() == null || joinReq.authToken() == null || joinReq.gameID() == null) {
            throw new BadRequestException("Error: create game request, game name, and authToken cannot be null" + joinReq.toString());
        }

        // Parse the gameID from string to int manually, with exception handling
        int myGameID;
        try {
            myGameID = Integer.parseInt(joinReq.gameID());
        } catch (NumberFormatException e) {
            throw new BadRequestException("Error: Invalid Game ID format in the joinGame function in service " + joinReq.gameID() + " or tostringed: " + joinReq.gameID().toString());
        }

        ChessGame.TeamColor myPlayColor = joinReq.playerColor();
        String myAuthToken = joinReq.authToken();
        AuthData myAuthData;
        try {
            myAuthData = dbAccess.getAuthDAO().getAuth(myAuthToken);
        } catch (DataAccessException e) {
            throw new UnauthorizedException(e.getMessage());
        }
        String myUsername = myAuthData.username();

        if (!Objects.equals(myAuthData.authToken(), myAuthToken)) {
            throw new BadRequestException("Error in the GameService::joinGame function. AuthTokens are supposed to match");
        }

        // Does authToken exist?
        AuthData auth = dbAccess.getAuthDAO().getAuth(myAuthToken);

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
}
