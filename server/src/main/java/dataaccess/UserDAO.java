package dataaccess;

import Model.UserData;

public interface UserDAO {

    void clear() throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    boolean userExists(String username);
    void createUser(UserData user) throws DataAccessException;
    void authenticateUser(String username, String password) throws DataAccessException;
}