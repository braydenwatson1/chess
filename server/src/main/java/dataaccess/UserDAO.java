package dataaccess;

import TempModel.UserData;

public interface UserDAO {

    void clear() throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;

}