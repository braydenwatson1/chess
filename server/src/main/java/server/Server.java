package server;

import dataaccess.*;
import service.*;
import handler.*;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        DataAccess dbAccess = new MemoryDataAccess();
        UserService userService = new UserService(dbAccess);
        GameService gameService = new GameService(dbAccess);
        ClearAllService clearService = new ClearAllService(dbAccess);

        Spark.post("/user", new RegisterHandler(userService));
        Spark.path("/session", () -> {
            Spark.post("", new LoginHandler(userService));
            Spark.delete("", new LogoutHandler(userService));
        });
        Spark.path("/game", () -> {
            Spark.get("", new ListGamesHandler(gameService));
            Spark.post("", new CreateGameHandler(gameService));
            Spark.put("", new JoinGameHandler(gameService));
        });
        Spark.delete("/db", new ClearHandler(clearService));


        Spark.exception(Exception.class, (exception, request, response) -> {
            response.status(500);
            response.body("An internal error occurred: " + exception.getMessage());
        });

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}