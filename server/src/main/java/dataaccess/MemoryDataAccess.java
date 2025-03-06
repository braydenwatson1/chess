package dataaccess;

public class MemoryDataAccess implements DataAccess {

    //my priv variables:
    private final GameDAO gameDAO;
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    //initialize db essentially
    public MemoryDataAccess() {

        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();

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