package server;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //init the db
        DataAccess dbAccess;
        dbAccess = new MemoryDataAccess();

        Spark.delete("/db", HANDLERHERE::clear);
        Spark.post("/user", HANDLERHERE::register);
        Spark.post("/session", HANDLERHERE::login);
        Spark.delete("/session", HANDLERHERE::logout);
        Spark.get("/game", HANDLERHERE::listGames);
        Spark.post("/game", HANDLERHERE::createGame);
        Spark.put("/game", HANDLERHERE::joinGame);

        Spark.exceptions...{FILL IN LATER}
        
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
