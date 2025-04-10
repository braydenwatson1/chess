package client;


import Model.*;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static int port;
    private ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
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



    @Test
    public void createGamePositiveTest() throws ResponseException {
        RegisterResult result = serverFacade.register(new RegisterRequest("alice", "password", "alice@mail.com"));
        String authToken = result.authToken();
        assertDoesNotThrow(() -> {
            serverFacade.createGame(new CreateGameRequest("MyGameName",authToken));
        });
    }

    @Test
    public void createGameNegativeTest() {
        assertThrows(ResponseException.class, () -> {
            // Using an invalid token
            serverFacade.createGame(new CreateGameRequest("NewGAME!", "hippityHopityBADauthtoken"));
        });
    }

    @Test
    public void listGamesPositiveTest() {
        assertDoesNotThrow(() -> {
            RegisterResult result = serverFacade.register(new RegisterRequest("alice", "password", "alice@mail.com"));
            String authToken = result.authToken();
            serverFacade.createGame(new CreateGameRequest("CoolGame",authToken));
            serverFacade.createGame(new CreateGameRequest("CoolGameAGAIN",authToken));

            var resultList = serverFacade.listGames(new ListRequest(authToken));
            assertNotNull(resultList);
            assertTrue(resultList.games().size() >= 2);
        });
    }

    @Test
    public void listGamesNegativeTest() {
        assertThrows(ResponseException.class, () -> {
            // Invalid auth token
            serverFacade.listGames(new ListRequest("WRONGTOKEN!!"));
        });
    }

    @Test
    public void joinGamePositiveTest() {
        assertDoesNotThrow(() -> {
            RegisterResult result = serverFacade.register(new RegisterRequest("charlie", "password", "charlie@mail.com"));
            String authToken = result.authToken();
            var createResult = serverFacade.createGame(new CreateGameRequest("joinME!", authToken));

            int gameID = createResult.gameID();
            serverFacade.joinGame(new JoinRequest(String.valueOf(createResult.gameID()), ChessGame.TeamColor.WHITE,authToken));
        });
    }

    @Test
    public void joinGameNegativeTest() {
        assertThrows(ResponseException.class, () -> {
            RegisterResult result = serverFacade.register(new RegisterRequest("dave", "password", "dave@mail.com"));
            String authToken = result.authToken();
            var createResult = serverFacade.createGame(new CreateGameRequest("joinME!", authToken));

            // Attempt to join with a wrong game ID or invalid color
            serverFacade.joinGame(new JoinRequest(String.valueOf(createResult.gameID()), ChessGame.TeamColor.BLACK,"WRONG")); // Invalid game ID and color
        });
    }


}
