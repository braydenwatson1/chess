package handler;

import Model.JoinRequest;
import service.BadRequestException;
import service.GameService;
import Model.ErrorResponse;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;
import dataaccess.DataAccessException;

public class JoinGameHandler implements Route {
    private final GameService gameService;
    private final Gson gson = new Gson();

    public JoinGameHandler(GameService gameService) {
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

            // Convert JSON body into a JoinRequest object
            JoinRequest joinRequest = gson.fromJson(req.body(), JoinRequest.class);

            // Now set the authToken to the CreateGameRequest object
            JoinRequest newReq = new JoinRequest(joinRequest.GameID(),joinRequest.playColor(), authToken);

            // Call joinGame function in GameService
            gameService.joinGame(newReq);

            // Set HTTP response code
            res.status(200); // Successful join
            return gson.toJson(new ErrorResponse("Joined game successfully"));

        } catch (BadRequestException e) {
            res.status(400); // Bad Request
            return gson.toJson(new ErrorResponse(e.getMessage()));
        } catch (DataAccessException e) {
            res.status(500); // Internal Server Error
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }
}

