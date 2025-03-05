package dataAccess;

import model.UserData;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO {

    private HashSet<UserData> db = new HashSet<>();

    @Override
    public void clear() throws DataAccessException {
        db.clear();
    }

    @Override
    public void createUser(String username, String password) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

}