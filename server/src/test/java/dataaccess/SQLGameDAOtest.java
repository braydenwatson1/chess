package dataaccess;

import Model.AuthData;
import Model.GameData;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashSet;

public class SQLGameDAOtest {

    
    private GameDAO gameDAO;
    private GameData dummyGameData;


    @BeforeEach
    void before() throws SQLException, DataAccessException {
        DatabaseManager.createDatabase();
        DataAccess dbAccess = new SQLDataAccess();
        gameDAO = dbAccess.getGameDAO();
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE game")) {
                statement.executeUpdate();
            }
        }
        dummyGameData = new GameData(1,"whiteUsername","blackUsername","gameName",new ChessGame());
    }


    @AfterEach
    void after() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE game")) {
                statement.executeUpdate();
            }
        }
    }


    //tests:


    @Test
    void testClear() throws DataAccessException, SQLException {
        gameDAO.addGame(dummyGameData);
        gameDAO.clear();

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.getGame(1); //should trigger exception
        });
    }


    @Test
    void testAddPositive() throws DataAccessException, SQLException {
        gameDAO.addGame(dummyGameData);

        GameData inDB = gameDAO.getGame(dummyGameData.gameID());
        Assertions.assertEquals(dummyGameData, inDB);
    }


    @Test
    void testAddNegative() throws DataAccessException, SQLException {
        gameDAO.addGame(dummyGameData);
        GameData nullGame = new GameData(1,"w", "b","g",new ChessGame());

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.addGame(nullGame); //should trigger exception if i do it twice in a row
        });
    }


    @Test
    void testGetPositive() throws DataAccessException, SQLException {
        gameDAO.addGame(dummyGameData);
        GameData gotten = gameDAO.getGame(dummyGameData.gameID());

        Assertions.assertEquals(gotten, dummyGameData);
    }


    @Test
    void testGetNegative() throws DataAccessException, SQLException {
        GameData wrongGame = new GameData(4,"wh","black","gName",new ChessGame());

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.getGame(1); //should trigger exception
        });
    }


    @Test
    void deletePositive() throws DataAccessException {
        gameDAO.addGame(dummyGameData);
        gameDAO.deleteGame(dummyGameData.gameID());

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.getGame(dummyGameData.gameID()); //should trigger exception
        });
    }


    @Test
    void deleteNegative() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.deleteGame(dummyGameData.gameID()); //should trigger exception
        });
    }


    @Test
    void listGamesPositive() throws DataAccessException {
        HashSet<GameData> gamesList = new HashSet<>();
        gamesList.add(dummyGameData);

        gameDAO.addGame(dummyGameData);
        HashSet<GameData> result = gameDAO.listGames();

        Assertions.assertEquals(gamesList, result);
    }


    @Test
    void updatePositive() throws DataAccessException, InvalidMoveException {
        gameDAO.addGame(dummyGameData);
        GameData updatedGame = new GameData(dummyGameData.gameID(), dummyGameData.whiteUsername(), "blackguy", dummyGameData.gameName(), dummyGameData.game());
        gameDAO.updateGame(updatedGame);

        GameData gotten = gameDAO.getGame(updatedGame.gameID());

        Assertions.assertEquals(gotten, updatedGame);
    }


    @Test
    void updateNegative() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.updateGame(dummyGameData); //should trigger exception
        });
    }


    @Test
    void gameExistsPositive() throws DataAccessException {
        gameDAO.addGame(dummyGameData);

        Assertions.assertTrue(gameDAO.gameExists(dummyGameData.gameID()));
    }


}
