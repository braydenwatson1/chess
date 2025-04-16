package dataaccess;

import Model.GameData;
import chess.ChessGame;
import com.google.gson.Gson;

import java.sql.*;
import java.util.HashSet;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        try { DatabaseManager.createDatabase(); } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
        try (var c = DatabaseManager.getConnection()) {
            var maybeTable = """            
                    CREATE TABLE if NOT EXISTS game (
                                    gameID INT NOT NULL PRIMARY KEY,
                                    whiteUsername VARCHAR(100),
                                    blackUsername VARCHAR(100),
                                    gameName VARCHAR(100),
                                    chessGame TEXT
                                    )""";
            try (var ps = c.prepareStatement(maybeTable)) {
                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (var myConnection = DatabaseManager.getConnection()) {
            try (var statement = myConnection.prepareStatement("TRUNCATE TABLE game")) {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void addGame(GameData newGameData) throws DataAccessException {
        String sql = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?, ?, ?, ?)";
        try (var myConnection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = myConnection.prepareStatement(sql)) {
                stmt.setInt(1, newGameData.gameID());
                stmt.setString(2, newGameData.whiteUsername());
                stmt.setString(3, newGameData.blackUsername());
                stmt.setString(4, newGameData.gameName());
                stmt.setString(5, serializeGame(newGameData.game()));  // Serialize ChessGame object
                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted == 0) {
                    throw new DataAccessException("Error. GameID already exists: " + newGameData.toString());
                }
            } catch (SQLException e) {
                throw new DataAccessException("Error adding game: " + e.getMessage());
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData getGame(int myGameID) throws DataAccessException {
        String sql = "SELECT * FROM game WHERE gameID = ?";
        try (var myConnection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = myConnection.prepareStatement(sql)) {
            stmt.setInt(1, myGameID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToGameData(rs);  // Map the result set to GameData
            } else {
                throw new DataAccessException("ERROR: Game ID does not exist: " + myGameID);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving game: " + e.getMessage());
        }
        }
        catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public void deleteGame(int myGameID) throws DataAccessException {
        String sql = "DELETE FROM game WHERE gameID = ?";
        try (var myConnection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = myConnection.prepareStatement(sql)) {
                stmt.setInt(1, myGameID);
                int rowsDeleted = stmt.executeUpdate();
                if (rowsDeleted == 0) {
                    throw new DataAccessException("Game ID does not exist, unable to delete: " + myGameID);
                }
            } catch (SQLException e) {
                throw new DataAccessException("Error deleting game: " + e.getMessage());
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public HashSet<GameData> listGames() throws DataAccessException {
        String sql = "SELECT * FROM game";
        try (var myConnection = DatabaseManager.getConnection()) {
            try (Statement stmt = myConnection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                HashSet<GameData> games = new HashSet<>();
                while (rs.next()) {
                    games.add(mapResultSetToGameData(rs));  // Map each row to GameData
                }
                return games;
            } catch (SQLException e) {
                throw new DataAccessException("Error listing games: " + e.getMessage());
            }
        }
        catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void updateGame(GameData myGame) throws DataAccessException {
        String sql = "UPDATE game SET whiteUsername = ?, blackUsername = ?, gameName = ?, chessGame = ? WHERE gameID = ?";
        try (var myConnection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = myConnection.prepareStatement(sql)) {
                stmt.setString(1, myGame.whiteUsername());
                stmt.setString(2, myGame.blackUsername());
                stmt.setString(3, myGame.gameName());
                stmt.setString(4, serializeGame(myGame.game()));  // Serialize ChessGame object
                stmt.setInt(5, myGame.gameID());
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated == 0) {
                    throw new DataAccessException("Error updating game, no such game found: " + myGame.toString());
                }
            } catch (SQLException e) {
                throw new DataAccessException("Error updating game: " + e.getMessage());
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public boolean gameExists(int gameID) throws DataAccessException {
        String sql = "SELECT 1 FROM game WHERE gameID = ?";
        try (var myConnection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = myConnection.prepareStatement(sql)) {
                stmt.setInt(1, gameID);
                ResultSet rs = stmt.executeQuery();
                return rs.next();  // If there's a result, the game exists
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        } catch (SQLException | DataAccessException e) {
            return false;
        }
    }

    // Helper method to map ResultSet to GameData
    private GameData mapResultSetToGameData(ResultSet rs) throws SQLException {
        int gameID = rs.getInt("gameID");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        String gameDataJson = rs.getString("chessGame");  // Retrieve the serialized game data
        ChessGame chessGame = deserializeGame(gameDataJson);  // Deserialize the ChessGame object
        return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
    }

    // Method to serialize ChessGame to JSON
    private String serializeGame(ChessGame game) {
        return new Gson().toJson(game);  // Use Gson to serialize the ChessGame object to a JSON string
    }

    // Method to deserialize JSON to ChessGame
    private ChessGame deserializeGame(String gameDataJson) {
        return new Gson().fromJson(gameDataJson, ChessGame.class);  // Use Gson to deserialize the JSON string to a ChessGame object
    }
}



