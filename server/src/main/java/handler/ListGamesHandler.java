package handler;

import Model.ErrorResponse;
import Model.ListRequest;
import Model.ListResult;
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
            // Convert JSON body into a ListRequest object
            ListRequest listRequest = gson.fromJson(req.body(), ListRequest.class);

            // Call listGames function in GameService
            ListResult result = gameService.listGames(listRequest);

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
