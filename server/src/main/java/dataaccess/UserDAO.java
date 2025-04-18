package dataaccess;

import Model.UserData;
import service.UnauthorizedException;

public interface UserDAO {

    void clear() throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    boolean userExists(String username) throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    void authenticateUser(String username, String password) throws DataAccessException, UnauthorizedException;
}