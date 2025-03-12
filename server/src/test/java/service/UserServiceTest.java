package service;

import dataaccess.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
                //System.out.println("PreReq2 completed");
            }

    //Testing

}


