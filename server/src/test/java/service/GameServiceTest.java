package service;

import Model.*;
import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.memory.MemoryDataAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class GameServiceTest {

    static GameService gameService;
    static DataAccess dbAccess;

    @BeforeAll
    static void init() throws DataAccessException {
        dbAccess = new MemoryDataAccess();
        dbAccess.getUserDAO().clear();
        dbAccess.getGameDAO().clear();
        dbAccess.getAuthDAO().clear();

        gameService = new GameService(dbAccess);
    }

    @BeforeEach
    void setup() throws DataAccessException {
        dbAccess.getUserDAO().clear();
        dbAccess.getGameDAO().clear();
        dbAccess.getAuthDAO().clear();
    }


    @Test
    void ListTestPositive() throws BadRequestException, DataAccessException, ForbiddenException, UnauthorizedException {
        //add authdata and game to db
        dbAccess.getAuthDAO().addAuth(new AuthData("bob","auth"));
        dbAccess.getGameDAO().addGame(new GameData(1,"joe", "kid","gameName", new ChessGame()));

        ListResult result = gameService.listGames(new ListRequest("auth"));
        Assertions.assertNotNull(result);
    }

    @Test
    void ListTestNegative() throws BadRequestException, ForbiddenException, DataAccessException {
        //add authdata and game to db
        dbAccess.getAuthDAO().addAuth(new AuthData("bob","auth"));
        dbAccess.getGameDAO().addGame(new GameData(1,"joe", "kid","gameName", new ChessGame()));

        Assertions.assertThrows(Exception.class, () -> gameService.listGames(new ListRequest("WRONGAUTH")));
    }

    @Test
    void CreateTestPositive() throws BadRequestException, DataAccessException, ForbiddenException {
        //add authdata to db
        dbAccess.getAuthDAO().addAuth(new AuthData("bob","auth"));

        assertDoesNotThrow(() -> {
            gameService.createGame(new CreateGameRequest("gameName", "auth"));
        });
    }

    @Test
    void CreateTestNegative() throws BadRequestException, ForbiddenException, DataAccessException {
        //add authdata to db
        dbAccess.getAuthDAO().addAuth(new AuthData("bob","auth"));

        Assertions.assertThrows(Exception.class, () -> gameService.createGame(new CreateGameRequest("gameName", "WRONGAUTH")));
    }

    @Test
    void JoinTestPositive() throws BadRequestException, DataAccessException, ForbiddenException {
        GameData newGame = new GameData(1, "joe", null,"nameoftheGAME",new ChessGame());
        dbAccess.getGameDAO().addGame(newGame);
        dbAccess.getAuthDAO().addAuth(new AuthData("username", "auth"));

        Assertions.assertDoesNotThrow( () -> {
            gameService.joinGame(new JoinRequest("1", ChessGame.TeamColor.BLACK,"auth"));
        });
    }

    @Test
    void JoinTestNegative() throws BadRequestException, ForbiddenException, DataAccessException {
        GameData newGame = new GameData(1, "joe", null,"nameoftheGAME",new ChessGame());
        dbAccess.getGameDAO().addGame(newGame);
        dbAccess.getAuthDAO().addAuth(new AuthData("username", "auth"));

        Assertions.assertThrows( Exception.class, () -> {
            gameService.joinGame(new JoinRequest("1", ChessGame.TeamColor.BLACK,"WRONGauth"));
        });
    }

    @Test
    void clearTestPositive() throws BadRequestException, ForbiddenException, DataAccessException {
        dbAccess.getGameDAO().addGame(new GameData(1,"bob", "joe", "name",new ChessGame()));
        gameService.clear();
        Assertions.assertThrows(DataAccessException.class, () -> dbAccess.getUserDAO().getUser("bob"));
    }
}
