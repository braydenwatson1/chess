package dataaccess;

import Model.GameData;
import Model.UserData;
import chess.ChessGame;
import chess.InvalidMoveException;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.HashSet;

public class SQLUserDAOtest {

    private UserDAO userDAO;
    private UserData dummyUserData;


    @BeforeEach
    void before() throws SQLException, DataAccessException {
        DatabaseManager.createDatabase();
        DataAccess dbAccess = new SQLDataAccess();
        userDAO = dbAccess.getUserDAO();
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE user")) {
                statement.executeUpdate();
            }
        }
        dummyUserData = new UserData("username", "password", "email");
    }


    @AfterEach
    void after() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE user")) {
                statement.executeUpdate();
            }
        }
    }


    //tests:


    @Test
    void testClear() throws DataAccessException, SQLException {
        userDAO.createUser(dummyUserData);
        userDAO.clear();

        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.getUser(dummyUserData.username()); //should trigger exception
        });
    }


    @Test
    void testAddPositive() throws DataAccessException {
        userDAO.createUser(dummyUserData);

        // Retrieve user data from the database
        UserData inDB = userDAO.getUser(dummyUserData.username());

        // Use BCrypt to compare the raw password (dummyUserData.password()) with the stored hash in DB (inDB.password())
        boolean passwordMatches = BCrypt.checkpw(dummyUserData.password(), inDB.password());

        // Assert that the password matches
        Assertions.assertTrue(passwordMatches, "The password should match the stored hash");

        Assertions.assertEquals(dummyUserData.username(), inDB.username());
        Assertions.assertEquals(dummyUserData.email(), inDB.email());
    }


    @Test
    void testAddNegative() throws DataAccessException, SQLException {
        userDAO.createUser(dummyUserData);

        // Retrieve user data from DB after creating the user
        UserData inDB = userDAO.getUser(dummyUserData.username());

        // Assert that the user in the DB is equal to the dummy user data, but compare everything except the password
        Assertions.assertEquals(dummyUserData.username(), inDB.username());
        Assertions.assertEquals(dummyUserData.email(), inDB.email());

        // Try to create the same user again, this should throw a DataAccessException due to the unique constraint
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(dummyUserData); // Attempt to create the same user
        });
    }


    @Test
    void testGetPositive() throws DataAccessException, SQLException {
        userDAO.createUser(dummyUserData);

        // Retrieve the user from the database
        UserData gotten = userDAO.getUser(dummyUserData.username());

        // Use BCrypt to check if the raw password matches the stored hash
        boolean passwordMatches = BCrypt.checkpw(dummyUserData.password(), gotten.password());
        Assertions.assertTrue(passwordMatches, "The passwords should match.");

        // Assert that other fields are correctly retrieved
        Assertions.assertEquals(gotten.username(), dummyUserData.username());
        Assertions.assertEquals(gotten.email(), dummyUserData.email());
    }



    @Test
    void testGetNegative() throws DataAccessException, SQLException {
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.getUser("abc"); //should trigger exception
        });
    }


    @Test
    void UserExistsPositive() throws DataAccessException {
        userDAO.createUser(dummyUserData);
        Assertions.assertTrue(userDAO.userExists(dummyUserData.username()));
    }


    @Test
    void authenticatePositive() throws DataAccessException, InvalidMoveException {
        userDAO.createUser(dummyUserData);
        Assertions.assertDoesNotThrow(() -> {
            userDAO.authenticateUser(dummyUserData.username(), dummyUserData.password());
        });
    }
}



