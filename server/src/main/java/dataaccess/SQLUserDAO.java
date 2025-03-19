package dataaccess;

import Model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class SQLUserDAO implements UserDAO {

    private Connection connection;

    public SQLUserDAO(Connection connection) throws DataAccessException {
        this.connection = connection;
        createUserTableIfNeeded();
    }

    private void createUserTableIfNeeded() throws DataAccessException {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS users (
                    username VARCHAR(255) NOT NULL PRIMARY KEY,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(255)
                )""";

        try (PreparedStatement stmt = connection.prepareStatement(createTableSQL)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error creating user table: " + e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing the users table: " + e.getMessage());
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
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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
