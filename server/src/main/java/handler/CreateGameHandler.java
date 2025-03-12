package handler;

import Model.CreateGameRequest;
import Model.CreateGameResult;
import Model.ErrorResponse;
import service.BadRequestException;
import service.GameService;
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
            // Convert JSON body into a CreateGameRequest object
            CreateGameRequest createGameRequest = gson.fromJson(req.body(), CreateGameRequest.class);

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
        }
    }
}

