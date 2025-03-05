package dataAccess;

import model.AuthData;

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
    public AuthData getAuth(String myAuthToken) throws DataAccessException {
        for (AuthData A : db) {
            if (A.authToken().equals(myAuthToken)) {
                return A;
            }
        }
        throw new DataAccessException("Auth Token does not exist: " + myAuthToken);
    }

    @Override
    public void deleteAuth(String myAuthToken) throws DataAccessException {
        for (AuthData A : db) {
            if (A.authToken().equals(myAuthToken)) {
                db.remove(A);
                return;
            }
        }
        throw new DataAccessException("Auth Token does not exist, unable to delete: " + myAuthToken);
    }
}