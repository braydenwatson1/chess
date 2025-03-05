package dataAccess;

import model.AuthData;
import model.GameData;

import java.util.HashSet;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {

    private HashSet<GameData> db = new HashSet<>();

    @Override
    public void clear() throws DataAccessException {
        db.clear();
    }

    @Override
    public void addGame(GameData newGameData) throws DataAccessException {

        for (GameData G : db) {
            if (G.gameID() == newGameData.gameID()) {
                throw new DataAccessException("Error. GameID already exists: " + newGameData.toString());
            }
            if (Objects.equals(G.gameName(), newGameData.gameName())) {
                throw new DataAccessException("Error. GameName already exists: " + newGameData.toString());
            }
        }

        db.add(newGameData);

    }

    @Override
    public GameData getGame(int myGameID) throws DataAccessException {
        for (GameData G : db) {
            if (G.gameID()==myGameID) {
                return G;
            }
        }
        throw new DataAccessException("Game ID does not exist: " + myGameID);
    }

    @Override
    public void deleteGame(int myGameID) throws DataAccessException {
        for (GameData G : db) {
            if (G.gameID() == myGameID) {
                db.remove(G);
                return;
            }
        }
        throw new DataAccessException("Auth Token does not exist, unable to delete: " + myGameID);
    }

    @Override
    public HashSet<GameData> listGames() throws DataAccessException {
        return db;
    }

    @Override
    public void updateGame(GameData myGame) throws DataAccessException {
        for (GameData G : db) {
            if (G.gameID() == myGame.gameID()) {
                db.remove(G);
                db.add(myGame);
                return;
            }
        }
        throw new DataAccessException("Game does not exist, unable to update: " + myGame.toString());
    }
}