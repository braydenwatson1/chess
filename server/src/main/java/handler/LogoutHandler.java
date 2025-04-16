package handler;

import Model.ErrorResponse;
import Model.LogoutRequest;
import service.BadRequestException;
import service.ForbiddenException;
import service.UnauthorizedException;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;
import dataaccess.DataAccessException;

public class LogoutHandler implements Route {
    private final UserService userService;
    private final Gson gson = new Gson();

    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object handle(Request req, Response res) {
        try {
            // Extract the authToken from the request header
            String authToken = req.headers("Authorization");

            // If there's no auth token, return a 400 error
            if (authToken == null) {
                res.status(400); // Bad Request
                return gson.toJson(new ErrorResponse("Error: Missing Authorization token"));
            }

            // Call logout service
            userService.logout(new LogoutRequest(authToken));

            // Set response status to 200 (success) with an empty body
            res.status(200);
            return "";

        } catch (BadRequestException e) {
            res.status(400); // Bad Request
            return gson.toJson(new ErrorResponse(e.getMessage()));
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        } catch (UnauthorizedException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }
}
