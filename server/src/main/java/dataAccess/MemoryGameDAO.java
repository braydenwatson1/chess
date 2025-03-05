package dataAccess;

import model.AuthData;
import model.GameData;

import java.util.HashSet;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO {

    private HashSet<GameData> db = new HashSet<>();

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
    public void deleteGame(int myGameID) throws DataAccessException {
        for (GameData G : db) {
            if (G.gameID().equals(myGameID)) {
                db.remove(G);
                return;
            }
        }
        throw new DataAccessException("Auth Token does not exist, unable to delete: " + myAuthToken);
    }
}