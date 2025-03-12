package handler;

import Model.ErrorResponse;
import Model.LoginRequest;
import Model.LoginResult;
import service.BadRequestException;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;
import dataaccess.DataAccessException;

public class LoginHandler implements Route {
    private final UserService userService;
    private final Gson gson = new Gson();

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object handle(Request req, Response res) {
        try {
            // Convert JSON body into a LoginRequest object
            LoginRequest loginRequest = gson.fromJson(req.body(), LoginRequest.class);

            // Call the login function in UserService
            LoginResult result = userService.login(loginRequest);

            // Set HTTP response code
            res.status(200);
            return gson.toJson(result);

        } catch (BadRequestException e) {
            res.status(401); // Bad Request
            return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
        } catch (DataAccessException e) {
            res.status(500); // Internal Server Error
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }
}
