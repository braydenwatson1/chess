package dataaccess;

import Model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class SQLUserDAO implements UserDAO {


    public SQLUserDAO() throws DataAccessException {
        try { DatabaseManager.createDatabase(); } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
        try (var conn = DatabaseManager.getConnection()) {
            var maybeTable = """            
                    CREATE TABLE if NOT EXISTS user (
                                    username VARCHAR(100) NOT NULL PRIMARY KEY,
                                    password VARCHAR(100) NOT NULL,
                                    email VARCHAR(100)
                                    )""";
            try (var createTableStatement = conn.prepareStatement(maybeTable)) {
                createTableStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM users";
        try (var myCon = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = myCon.prepareStatement(sql)) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Error clearing the users table: " + e.getMessage());
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void createUser(UserData newUser) throws DataAccessException {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newUser.username());
            stmt.setString(2, hashPassword(newUser.password()));  // Hashing the password before storing
            stmt.setString(3, newUser.email());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error creating user: " + e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String sql = "SELECT username, password, email FROM users WHERE username = ?";

        try (var Myc = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = Myc.prepareStatement(sql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    return new UserData(username, password, email);
                } else {
                    throw new DataAccessException("User not found: " + username);
                }
            } catch (SQLException e) {
                throw new DataAccessException("Error retrieving user: " + e.getMessage());
            }
        }catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean userExists(String username) throws DataAccessException {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new DataAccessException("Error checking if user exists: " + e.getMessage());
        }
    }

    @Override
    public void authenticateUser(String username, String password) throws DataAccessException {
        UserData user = getUser(username);
        if (!passwordMatches(password, user.password())) {
            throw new DataAccessException("Username and Password do not match.");
        }
    }

    // Helper method to hash the password
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());  // Using BCrypt to hash password
    }

    // Helper method to check if the password matches the hashed password in the database
    private boolean passwordMatches(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);  // Using BCrypt to compare password hashes
    }
}
