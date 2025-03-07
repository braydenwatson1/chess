package handler;

import dataaccess.DataAccessException;
import service.BadRequestException;
import dataaccess.DataAccess;
import service.UserService;
import handler.HandlerErrorException;
import com.google.gson.Gson;
import spark.Request;
import TempModel.LoginRequest;
import TempModel.LoginResult;

public class LoginHandler extends BaseHandler {

    public LoginHandler(DataAccess dbAccess) {
        super(dbAccess);
    }

    @Override
    protected Object processRequest(Request request, String authToken) throws HandlerErrorException, BadRequestException, DataAccessException {
        Gson gson = new Gson();
        // Deserialize request body into LoginRequest object
        LoginRequest req = gson.fromJson(request.body(), LoginRequest.class);

        // Use the UserService to handle login logic
        UserService userService = new UserService(getDataAccess());

        // Perform login and return the result
        try {
            return userService.login(req);
        } catch (BadRequestException | DataAccessException e) {
            throw new HandlerErrorException(e.toString());
        }
    }
}
