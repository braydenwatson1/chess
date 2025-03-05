package dataAccess;

import model.GameData;

import java.util.HashSet;

public interface GameDAO {

    void clear() throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void deleteGame(int gameID) throws DataAccessException;
    HashSet<GameData> listGames() throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;
    void addGame(GameData game) throws DataAccessException;
}