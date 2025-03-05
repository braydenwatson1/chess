package dataAccess;

import model.UserData;

import java.util.HashSet;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {

    private HashSet<UserData> db = new HashSet<>();

    @Override
    public void clear() throws DataAccessException {
        db.clear();
    }

    @Override
    public void createUser(UserData newUser) throws DataAccessException {

        for (UserData U : db) {
            if (Objects.equals(U.username(), newUser.username())) {
                throw new DataAccessException("User already exists: " + newUser.username());
            }
        }

        db.add(newUser);

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

}