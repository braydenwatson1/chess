package dataaccess;
import Model.AuthData;
import java.sql.*;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        try {
            DatabaseManager.createDatabase();
            try (Connection c = DatabaseManager.getConnection();) {

                String sql = """
                    CREATE TABLE IF NOT EXISTS auth (
                        username VARCHAR(100) NOT NULL,
                        authToken VARCHAR(100) NOT NULL PRIMARY KEY
                    )""";
                PreparedStatement stmt = c.prepareStatement(sql);
                stmt.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error init SQLAuthDAO: " + e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "TRUNCATE TABLE auth";
        try (Connection c = DatabaseManager.getConnection();
            PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing auth table: " + e.getMessage());
        }
    }

    @Override
    public void addAuth(AuthData newAuthData) throws DataAccessException {
        String sql = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
        try (Connection c = DatabaseManager.getConnection();
            PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, newAuthData.username());
            stmt.setString(2, newAuthData.authToken());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error adding auth: " + e.getMessage());
        }
    }

    @Override
    public AuthData getAuth(String myAuthToken) throws DataAccessException {
        String sql = "SELECT username, authToken FROM auth WHERE authToken = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, myAuthToken);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new AuthData(rs.getString("username"), rs.getString("authToken"));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving auth: " + e.getMessage());
        }
        throw new DataAccessException("Error: Auth Token not found: " + myAuthToken);
    }

    @Override
    public void deleteAuth(String myAuthToken) throws DataAccessException {
        String sql = "DELETE FROM auth WHERE authToken = ?";
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, myAuthToken);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DataAccessException("Error: Auth Token does not exist, unable to delete: " + myAuthToken);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting auth: " + e.getMessage());
        }
    }

}

