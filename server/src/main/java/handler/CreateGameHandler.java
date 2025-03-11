package handler;

import Model.CreateGameRequest;
import Model.CreateGameResult;
import com.google.gson.Gson;
import service.GameService;
import service.BadRequestException;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import spark.Request;

public class CreateGameHandler extends BaseHandler {

    private final GameService gameService;

    // Constructor - initialize the GameService
    public CreateGameHandler(DataAccess dbAccess, GameService gameService) {
        super(dbAccess); // Call the BaseHandler constructor
        this.gameService = gameService;
    }

    @Override
    protected Object processRequest(Request request, String authToken) throws HandlerErrorException, BadRequestException, DataAccessException {
        try {
            // Parse the request body into a CreateGameRequest object
            CreateGameRequest createGameRequest = new Gson().fromJson(request.body(), CreateGameRequest.class);

            // Create the game via the GameService
            CreateGameResult result = gameService.createGame(createGameRequest);

            // Return the result
            return result;

        } catch (BadRequestException e) {
            // Handle bad request errors
            throw new HandlerErrorException("Bad Request: " + e.getMessage());
        } catch (DataAccessException e) {
            // Handle database errors
            throw new HandlerErrorException("Database error: " + e.getMessage());
        }
    }
}

