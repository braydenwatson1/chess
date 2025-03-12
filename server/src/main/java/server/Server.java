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


        Spark.exception(BadRequestException.class, this::badRequestExceptionHandler);
        Spark.exception(DataAccessException.class, this::DataAccessExceptionHandler);
        Spark.exception(ForbiddenException.class, this::ForbiddenExceptionHandler);
        Spark.exception(Exception.class, this::otherExceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


    //helper functions for error handling
    private void otherExceptionHandler(Exception e, Request req, Response res) {
        res.status(500);
        res.body("Error here: 500: " + e.getMessage());

    }

    private void badRequestExceptionHandler(BadRequestException e, Request req, Response res) {
        res.status(400);
        res.body("Error: 400 bad request");

    }

    private void DataAccessExceptionHandler(DataAccessException e, Request req, Response res) {
        res.status(401);
        res.body("Error: 401 unauthorized");

    }

    private void ForbiddenExceptionHandler(ForbiddenException e, Request req, Response res) {
        res.status(403);
        res.body("Error: 403 forbidden");

    }


}