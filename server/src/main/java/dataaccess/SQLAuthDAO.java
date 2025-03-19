package dataaccess;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLAuthDAO implements AuthDAO {


    public SQLAuthDAO() throws DataAccessException {
        try {
            DatabaseManager.createDatabase();
            try (Connection conn = DatabaseManager.getConnection();
                 Statement stmt = conn.createStatement()) {

                String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS auth (
                        username VARCHAR(100) NOT NULL,
                        authToken VARCHAR(100) NOT NULL PRIMARY KEY
                    )""";

                stmt.executeUpdate(createTableSQL);
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error initializing SQLAuthDAO: " + e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM auth";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing auth table: " + e.getMessage());
        }
    }

}

