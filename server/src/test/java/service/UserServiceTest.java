package service;


import Model.*;
import dataaccess.*;
import dataaccess.memory.MemoryDataAccess;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest {

    static UserService userService;
    static DataAccess dbAccess;

    @BeforeAll
    static void init() throws DataAccessException {
        dbAccess = new MemoryDataAccess();
        dbAccess.getUserDAO().clear();
        dbAccess.getGameDAO().clear();
        dbAccess.getAuthDAO().clear();

        userService = new UserService(dbAccess);
    }

    @BeforeEach
    void setup() throws DataAccessException {
        dbAccess.getUserDAO().clear();
        dbAccess.getGameDAO().clear();
        dbAccess.getAuthDAO().clear();
    }

    @Test
    void RegisterTestPositive() throws BadRequestException, DataAccessException, ForbiddenException {
        RegisterResult result = userService.register(new RegisterRequest("bob", "bob", "bob"));

        Assertions.assertEquals(dbAccess.getAuthDAO().getAuth(result.authToken()).authToken(), result.authToken());
    }

    @Test
    void RegisterTestNegative() throws BadRequestException, ForbiddenException, DataAccessException {
        RegisterResult result = userService.register(new RegisterRequest("bob", "bob", "bob"));
        Assertions.assertThrows(ForbiddenException.class, () ->  userService.register(new RegisterRequest("bob", "bob", "bob")));
    }

    @Test
    void loginTestPositive() throws BadRequestException, DataAccessException, ForbiddenException {
        RegisterResult result = userService.register(new RegisterRequest("bob", "bob", "bob"));
        assertDoesNotThrow(() -> {
            userService.login(new LoginRequest("bob", "bob"));
        });
    }

    @Test
    void loginTestNegative() throws BadRequestException, ForbiddenException, DataAccessException {
        RegisterResult result = userService.register(new RegisterRequest("bob", "bob", "bob"));
        assertThrows(Exception.class, () -> {
            userService.login(new LoginRequest("bob", "WRONGPASSWORD"));
        });
    }

    @Test
    void logoutTestPositive() throws BadRequestException, ForbiddenException, DataAccessException {
        RegisterResult result = userService.register(new RegisterRequest("bob", "bob", "bob"));
        LoginResult LogResult = userService.login(new LoginRequest("bob", "bob"));
        assertDoesNotThrow(() -> {
            userService.logout(new LogoutRequest(LogResult.authToken()));
        });
    }

    @Test
    void logoutTestNegative() throws BadRequestException, ForbiddenException, DataAccessException {
        RegisterResult result = userService.register(new RegisterRequest("bob", "bob", "bob"));
        LoginResult LogResult = userService.login(new LoginRequest("bob", "bob"));
        assertThrows(Exception.class, () -> {
            userService.logout(new LogoutRequest("wrong"));
        });
    }

    @Test
    void clearTestPositive() throws BadRequestException, ForbiddenException, DataAccessException {
        userService.register(new RegisterRequest("bob", "bob", "bob"));
        dbAccess.getUserDAO().clear();
        Assertions.assertThrows(DataAccessException.class, () -> dbAccess.getUserDAO().getUser("bob"));
    }

}