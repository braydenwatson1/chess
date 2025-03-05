package dataAccess;

import model.GameData;

public interface GameDAO {

    void clear() throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void deleteGame(int gameID) throws DataAccessException;
    void listGames() throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;

}