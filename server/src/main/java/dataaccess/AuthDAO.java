package dataaccess;

import Model.AuthData;

public interface AuthDAO {

    void clear() throws DataAccessException;
    void addAuth(AuthData authData) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;

}