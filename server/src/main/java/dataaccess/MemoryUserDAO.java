package dataaccess;

import Model.UserData;

import javax.naming.AuthenticationException;
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
                throw new DataAccessException("Username already exists: " + newUser.username());
            }
        }
        db.add(newUser);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        for (UserData user : db) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        throw new DataAccessException("User not found: " + username);
    }

    @Override
    public void authenticateUser(String username, String password) throws DataAccessException {
        for (UserData user : db) {
            if (user.username().equals(username)) {
                if (!Objects.equals(user.password(), password)) {
                    throw new DataAccessException("Username and Password do not match");
                }
                else { return; }
            }
        }
        throw new DataAccessException("User not found: " + username);
    }

}