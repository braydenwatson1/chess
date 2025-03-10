package handler;

import TempModel.AuthData;
import TempModel.ListResult;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import service.GameService;
import service.BadRequestException;
import spark.Request;

public class ListGamesHandler extends BaseHandler {

    public ListGamesHandler(DataAccess dbAccess) {
        super(dbAccess);
    }

    @Override
    protected ListResult processRequest(Request request, String authToken) throws HandlerErrorException, BadRequestException, DataAccessException {
        // Check if auth token exists before proceeding
        if (authToken.isEmpty() || authToken == null) {
            throw new HandlerErrorException("Authorization token required.");
        }

        // Create GameService and fetch the list of games
        GameService gameService = new GameService(getDataAccess());

        DataAccess db = getDataAccess();
        String matchingUsername = db.getAuthDAO().getAuth(authToken).username();
        AuthData myAuthObj = new AuthData(matchingUsername, authToken);

        ListResult games = gameService.listGames(myAuthObj);

        // Return the list of games
        return games;
    }
}
