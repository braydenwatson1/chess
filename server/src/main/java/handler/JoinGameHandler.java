package handler;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import handler.HandlerErrorException;
import TempModel.JoinRequest;
import service.BadRequestException;
import service.GameService;
import spark.Request;
import com.google.gson.Gson;

public class JoinGameHandler extends BaseHandler {

    public JoinGameHandler(DataAccess dbAccess) {
        super(dbAccess);
    }

    @Override
    protected Object processRequest(Request request, String authToken) throws HandlerErrorException, BadRequestException, DataAccessException {
        Gson gson = new Gson();

        // Deserialize request body
        JoinRequest joinRequest = gson.fromJson(request.body(), JoinRequest.class);

        // Ensure authToken is provided
        if (authToken == null || authToken.isEmpty()) {
            throw new HandlerErrorException("Auth token required.");
        }

        // Ensure joinRequest is valid
        if (joinRequest.GameID() == 0) {
            throw new HandlerErrorException("Game ID not valid.");
        }

        // Process the join request
        GameService gameService = new GameService(getDataAccess());
        JoinRequest newReq = new JoinRequest(joinRequest.GameID(), joinRequest.playColor(), authToken);

        try {
            gameService.joinGame(newReq);  // It returns null, so no need to return anything
            return null;
        } catch (BadRequestException | DataAccessException e) {
            throw new HandlerErrorException("Fail to join: " + e.getMessage());
        }
    }
}
