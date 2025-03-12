package handler;

import Model.*;
import service.BadRequestException;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;
import dataaccess.DataAccessException;

public class ListGamesHandler implements Route {
    private final GameService gameService;
    private final Gson gson = new Gson();

    public ListGamesHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public Object handle(Request req, Response res) {
        try {
            // Extract authToken from the Authorization header
            String authToken = req.headers("Authorization");

            if (authToken == null || authToken.isEmpty()) {
                res.status(400); // Bad Request
                return gson.toJson(new ErrorResponse("Authorization token is missing"));
            }

            // Convert JSON body into a CreateGameRequest object (contains gameName)
            ListRequest ListRequest = gson.fromJson(authToken,  ListRequest.class);

            // Call createGame function in GameService
            ListResult result = gameService.listGames(ListRequest);


            // Set HTTP response code
            res.status(200);
            return gson.toJson(result);

        } catch (BadRequestException e) {
            res.status(400); // Bad Request
            return gson.toJson(new ErrorResponse(e.getMessage()));
        } catch (DataAccessException e) {
            res.status(500); // Internal Server Error
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }
}
