package client;


import Model.LoginRequest;
import Model.LogoutRequest;
import Model.RegisterRequest;
import Model.RegisterResult;
import org.junit.jupiter.api.*;
import server.Server;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static int port;
    private ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void setup() throws Exception {
        serverFacade = new ServerFacade("http://localhost:" + port);
        serverFacade.clear();
    }

    @AfterEach
    void cleanup() throws ResponseException {
        serverFacade.clear();
    }


    @Test
    public void registerPositiveTest() {
        assertDoesNotThrow(() -> {
            serverFacade.register(new RegisterRequest("bob", "bobsPassword", "bob@mail.com"));
        });
    }

    @Test
    public void registerNegativeTest() {
        assertThrows(ResponseException.class, () -> {
            serverFacade.register(new RegisterRequest("bob", "bobsPassword", "bob@mail.com"));
            serverFacade.register(new RegisterRequest("bob", "bobsPassword", "bob@mail.com")); // Duplicate
        });
    }

    @Test
    public void loginPositiveTest() {
        assertDoesNotThrow(() -> {
            RegisterResult result = serverFacade.register(new RegisterRequest("bob", "bobsPassword", "bob@mail.com"));
            String authToken = result.authToken();
            serverFacade.logout(new LogoutRequest(authToken));
            serverFacade.login(new LoginRequest("bob", "bobsPassword"));
        });
    }

    @Test
    public void loginNegativeTest() {
        assertThrows(ResponseException.class, () -> {
            RegisterResult result = serverFacade.register(new RegisterRequest("bob", "bobsPassword", "bob@mail.com"));
            String authToken = result.authToken();
            serverFacade.logout(new LogoutRequest(authToken));
            serverFacade.login(new LoginRequest("bob", "NOTbobsPassword"));
        });
    }

    @Test
    public void logoutPositiveTest() {
        assertDoesNotThrow(() -> {
            RegisterResult result = serverFacade.register(new RegisterRequest("bob", "bobsPassword", "bob@mail.com"));
            String authToken = result.authToken();
            serverFacade.logout(new LogoutRequest(authToken));
        });
    }

    @Test
    public void logoutNegativeTest() {
        assertThrows(ResponseException.class, () -> {
            serverFacade.register(new RegisterRequest("bob", "bobsPassword", "bob@mail.com"));
            serverFacade.logout(new LogoutRequest("wrongAuthToken bleh bleh"));
        });
    }



//    @Test
//    public void createGamePositiveTest() throws ResponseException {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        PrintStream originalSystemOut = System.out;  // Save original System.out
//        System.setOut(new PrintStream(outputStream)); // Redirect System.out to the outputStream
//
//        RegisterResult result = ServerFacade.register(new RegisterRequest("bob", "bobsPassword", "bob@mail.com"));
//        ServerFacade.cre
//
//        String output = outputStream.toString().trim();  // Capture printed content
//        assertTrue(output.contains("You have been logged out"), "Output was: " + output);
//
//        System.setOut(originalSystemOut);
//    }
//
//    @Test
//    public void createGameNegativeTest() {
//        assertEquals(-1, facade.createGame("gameName"));
//    }
//
//    @Test
//    public void listGamesPositiveTest() {
//        facade.register("username", "password", "email");
//        facade.createGame("gameName");
//        assertEquals(1, facade.listGames().size());
//    }
//
//    @Test
//    public void listGamesNegativeTest() {
//        assertEquals(facade.listGames(), HashSet.newHashSet(8));
//    }
//
//    @Test
//    public void joinGamePositiveTest() {
//        facade.register("username", "password", "email");
//        int id = facade.createGame("gameName");
//        assertTrue(facade.joinGame(id, "WHITE"));
//    }
//
//    @Test
//    public void joinGameNegativeTest() {
//        facade.register("username", "password", "email");
//        int id = facade.createGame("gameName");
//        facade.joinGame(id, "WHITE");
//        assertFalse(facade.joinGame(id, "WHITE"));
//    }


}
