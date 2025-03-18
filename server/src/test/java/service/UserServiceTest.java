package service;

import Model.AuthData;
import Model.UserData;
import dataaccess.*;
import org.junit.jupiter.api.*;
import service.ClearAllService;

public class UserServiceTest {

    static AuthDAO authDAO;
    static UserDAO userDAO;
    static DataAccess dbAccess;
    //Pre-Requisites
            @BeforeAll
            public static void PreReq1() {
                dbAccess = new MemoryDataAccess();
                userDAO = dbAccess.getUserDAO();
                authDAO = dbAccess.getAuthDAO();
                //System.out.println("PreReq1 completed");
            }

            @BeforeEach
            public void preReq2() throws DataAccessException {
                ClearAllService myService = new ClearAllService(dbAccess);
                myService.clearAll();

                UserData dummyUser = new UserData("dummyUsername", "dummyPassword", "dummyEmail");
                //System.out.println("PreReq2 completed");
            }

    //Testing
    @Test
    @DisplayName("Register User Successfully")
    void createUserSuccessTest() throws BadRequestException, DataAccessException {
        AuthData finalAuth = dbAccess.getUserDAO().createUser(dummyUser);
        Assertions.assertEquals(authDAO.getAuth(resultAuth.authToken()), resultAuth);
    }

    @Test
    @DisplayName("Register User Error Test")
    void createUserTestNegative() throws BadRequestException {
        userService.createUser(defaultUser);
        Assertions.assertThrows(BadRequestException.class, () -> userService.createUser(defaultUser));
    }

    @Test
    @DisplayName("Proper Login User")
    void loginUserTestPositive() throws BadRequestException, UnauthorizedException, DataAccessException {
        userService.createUser(defaultUser);
        AuthData authData = userService.loginUser(defaultUser);
        Assertions.assertEquals(authDAO.getAuth(authData.authToken()), authData);
    }

    @Test
    @DisplayName("Improper Login User")
    void loginUserTestNegative() throws BadRequestException {
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.loginUser(defaultUser));

        userService.createUser(defaultUser);
        UserData badPassUser = new UserData(defaultUser.username(), "wrongPass", defaultUser.email());
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.loginUser(badPassUser));
    }
}


