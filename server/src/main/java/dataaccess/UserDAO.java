package dataaccess;

import Model.UserData;

public interface UserDAO {

    void clear() throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    void authenticateUser(String username, String password) throws DataAccessException;
}