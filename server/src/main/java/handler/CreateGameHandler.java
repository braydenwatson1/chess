package handler;

import Model.CreateGameRequest;
import Model.CreateGameResult;
import Model.ErrorResponse;
import service.BadRequestException;
import service.GameService;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;
import dataaccess.DataAccessException;

public class CreateGameHandler implements Route {
    private final GameService gameService;
    private final Gson gson = new Gson();

    public CreateGameHandler(GameService gameService) {
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
            CreateGameRequest createGameRequest = gson.fromJson(req.body(), CreateGameRequest.class);

            // Now set the authToken to the CreateGameRequest object
            createGameRequest = new CreateGameRequest(createGameRequest.gameName(), authToken);

            // Call createGame function in GameService
            CreateGameResult result = gameService.createGame(createGameRequest);

            // Set HTTP response code
            res.status(200);
            return gson.toJson(result);

        } catch (BadRequestException e) {
            res.status(400); // Bad Request
            return gson.toJson(new ErrorResponse(e.getMessage()));
        } catch (DataAccessException e) {
            res.status(500); // Internal Server Error
            return gson.toJson(new ErrorResponse(e.getMessage()));
        } catch (UnauthorizedException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }
}
