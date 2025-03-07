package handler;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import service.HandlerErrorException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.net.HttpURLConnection;

public class BaseHandler implements Route {

    private final DataAccess dataAccess;
    public BaseHandler(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override
    public Object handle(Request request, Response response) throws HandlerErrorException {
        Gson gson = new Gson();
        String authToken = request.headers("Authorization");

        Object result = processRequest(request, authToken);

        response.status(HttpURLConnection.HTTP_OK);
        return gson.toJson(result);
    }

    protected Object processRequest(Request request, String authToken) throws HandlerErrorException {
        throw new HandlerErrorException("Subclasses must implement this method.");
    }

    protected DataAccess getDataAccess() {
        return dataAccess;
    }
}

