package dataaccess.memory;

import dataaccess.*;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLDataAccess implements DataAccess {

    //my priv variables:
    private final GameDAO gameDAO;
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    //initialize db essentially
    public SQLDataAccess() throws DataAccessException {

        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }

        try (var newConnection = DatabaseManager.getConnection()) {
            var maybeNewTable = """            
                    CREATE TABLE if NOT EXISTS auth (
                                    username VARCHAR(100) NOT NULL,
                                    authToken VARCHAR(100) NOT NULL,
                                    PRIMARY KEY (authToken)
                                    )""";
            try (var createTableStm = newConnection.prepareStatement(maybeNewTable)) {
                createTableStm.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(e.getMessage());
        }


        authDAO = new SQLAuthDAO(newConnection);
        userDAO = new SQLUserDAO();
        gameDAO = new SQLGameDAO();

    }

        //override methods:
    @Override
    public GameDAO getGameDAO() {

        return gameDAO;
    }

    @Override
    public AuthDAO getAuthDAO() {

        return authDAO;
    }

    @Override
    public UserDAO getUserDAO() {

        return userDAO;
    }

}
