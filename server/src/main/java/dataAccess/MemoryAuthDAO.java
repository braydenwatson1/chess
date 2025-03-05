package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashSet;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO {

    private HashSet<AuthData> db = new HashSet<>();

    @Override
    public void clear() throws DataAccessException {
        db.clear();
    }

    @Override
    public void addAuth(AuthData newAuthData) throws DataAccessException {

        for (AuthData A : db) {
            if (Objects.equals(A.authToken(), newAuthData.authToken())) {
                throw new DataAccessException("Error. AuthToken already exists: " + "--> " + newAuthData.toString() + " <--");
            }
        }

        db.add(newAuthData);

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



