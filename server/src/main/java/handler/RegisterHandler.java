package handler;

import Model.ErrorResponse;
import Model.RegisterRequest;
import Model.RegisterResult;
import service.BadRequestException;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;
import dataaccess.DataAccessException;

public class RegisterHandler implements Route {
    private final UserService userService;
    private final Gson gson = new Gson();

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object handle(Request req, Response res) {
        try {
            // Convert JSON body into a RegisterRequest object
            RegisterRequest registerRequest = gson.fromJson(req.body(), RegisterRequest.class);

            // Call register function in UserService
            RegisterResult result = userService.register(registerRequest);

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

