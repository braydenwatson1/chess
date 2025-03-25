package dataaccess;

import java.sql.SQLException;

public class SQLDataAccess implements DataAccess {

    //my priv variables:
    private final GameDAO gameDAO;
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    //initialize db essentially
    public SQLDataAccess() throws DataAccessException {
//i dont know that this is necesary so its a comment for now
//        try {
//            DatabaseManager.createDatabase();
//        } catch (DataAccessException e) {
//            throw new DataAccessException(e.getMessage());
//        }
//
//        try (var newConnection = DatabaseManager.getConnection()) {
//        } catch (DataAccessException | SQLException e) {
//            throw new DataAccessException(e.getMessage());
//        }

        authDAO = new SQLAuthDAO();
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
