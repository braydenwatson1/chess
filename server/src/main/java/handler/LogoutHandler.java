package handler;

import Model.LogoutRequest;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import service.UserService;
import service.BadRequestException;
import spark.Request;

public class LogoutHandler extends BaseHandler {

    public LogoutHandler(DataAccess dbAccess) {
        super(dbAccess);
    }

    @Override
    protected Object processRequest(Request request, String authToken) throws HandlerErrorException, BadRequestException, DataAccessException {
        // If there is no auth token, throw an error
        if (authToken == null || authToken.isEmpty()) {
            throw new HandlerErrorException("Missing authorization token.");
        }

        // Create UserService and call logout
        UserService userService = new UserService(getDataAccess());
        LogoutRequest req = new LogoutRequest(authToken)
        userService.logout(req);

        //logout doesn't return data
        return null;
    }
}
