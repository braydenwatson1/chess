package dataaccess;

import java.sql.SQLException;

public class SQLDataAccess implements DataAccess {

    //my priv variables:
    private final GameDAO gameDAO;
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    //initialize db
    public SQLDataAccess() throws DataAccessException {
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
