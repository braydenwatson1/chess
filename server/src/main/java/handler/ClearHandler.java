package handler;

import service.GameService;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import handler.HandlerErrorException;
import spark.Request;
import spark.Response;

public class ClearHandler extends BaseHandler {

    private final GameService gameService;

    // Constructor - initialize GameService
    public ClearHandler(DataAccess dbAccess, GameService gameService) {
        super(dbAccess); // Call the BaseHandler constructor
        this.gameService = gameService;
    }

    @Override
    protected Object processRequest(Request request, String authToken) throws HandlerErrorException, DataAccessException {
        try {
            // Call the clear database method from GameService
            gameService.cleardb();

            // Return a success message after clearing the DB
            return "Database cleared successfully!";

        } catch (DataAccessException e) {
            // Handle database errors
            throw new HandlerErrorException("Error clearing the database: " + e.getMessage());
        }
    }
}
