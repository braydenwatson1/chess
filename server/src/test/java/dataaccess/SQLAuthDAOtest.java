package dataaccess;

import Model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class SQLAuthDAOtest {

        private AuthDAO authDAO;
        private AuthData dummyAuthData;

        @BeforeEach
        void before() throws SQLException, DataAccessException {
            DatabaseManager.createDatabase();
            DataAccess dbAccess = new SQLDataAccess();
            authDAO = dbAccess.getAuthDAO();
            try (var conn = DatabaseManager.getConnection()) {
                try (var statement = conn.prepareStatement("TRUNCATE auth")) {
                    statement.executeUpdate();
                }
            }
            dummyAuthData = new AuthData("username", "authToken");
        }

        @AfterEach
        void after() throws DataAccessException, SQLException {
            try (var conn = DatabaseManager.getConnection()) {
                try (var statement = conn.prepareStatement("TRUNCATE auth")) {
                    statement.executeUpdate();
                }
            }
        }

        //tests:

        @Test
        void testClear() throws DataAccessException, SQLException {
            authDAO.addAuth(dummyAuthData);
            authDAO.clear();

            Assertions.assertThrows(DataAccessException.class, () -> {
                authDAO.getAuth("authToken"); //should trigger exception
            });
        }

        @Test
        void testAddPositive() throws DataAccessException, SQLException {
            authDAO.addAuth(dummyAuthData);

            AuthData inDB = authDAO.getAuth(dummyAuthData.authToken());
            Assertions.assertEquals(dummyAuthData, inDB);
        }

        @Test
        void testAddNegative() throws DataAccessException, SQLException {
            authDAO.addAuth(dummyAuthData);

            Assertions.assertThrows(DataAccessException.class, () -> {
                authDAO.addAuth(dummyAuthData); //should trigger exception if i do it twice in a row
            });
        }

        @Test
        void testGetPositive() throws DataAccessException, SQLException {
            authDAO.addAuth(dummyAuthData);
            AuthData gotten = authDAO.getAuth(dummyAuthData.authToken());

            Assertions.assertEquals(gotten, dummyAuthData);
        }

        @Test
        void testGetNegative() throws DataAccessException, SQLException {
            String wrongOne = "abc123";

            Assertions.assertThrows(DataAccessException.class, () -> {
                authDAO.getAuth(wrongOne); //should trigger exception
            });
        }

        @Test
        void deletePositive() throws DataAccessException, SQLException {
            authDAO.addAuth(dummyAuthData);
            authDAO.deleteAuth(dummyAuthData.authToken());

            Assertions.assertThrows(DataAccessException.class, () -> {
                authDAO.getAuth(dummyAuthData.authToken()); //should trigger exception
            });
        }

        @Test
        void deleteNegative() throws DataAccessException, SQLException {
            Assertions.assertThrows(DataAccessException.class, () -> {
                authDAO.deleteAuth("abc"); //should trigger exception
            });
        }


    }
