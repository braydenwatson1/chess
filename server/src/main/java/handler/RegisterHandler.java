package handler;

import Model.RegisterRequest;
import dataaccess.DataAccessException;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import service.BadRequestException;
import service.UserService;
import spark.Request;
import Model.UserData;

public class RegisterHandler extends BaseHandler {

    public RegisterHandler(DataAccess dbAccess) {

        super(dbAccess);
    }

    @Override
    protected Object processRequest(Request request, String authToken) throws HandlerErrorException, BadRequestException, DataAccessException {
        Gson gson = new Gson();
        UserData user = gson.fromJson(request.body(), UserData.class);
        UserService userService = new UserService(getDataAccess());
        RegisterRequest req = new RegisterRequest(user.username(), user.password(), user.email());
        return userService.register(req);
    }
}
