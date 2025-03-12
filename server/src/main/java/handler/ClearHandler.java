package handler;

import service.ClearAllService;
import dataaccess.DataAccessException;
import Model.ErrorResponse;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

public class ClearHandler implements Route {
    private final ClearAllService clearAllService;
    private final Gson gson = new Gson();

    public ClearHandler(ClearAllService clearAllService) {
        this.clearAllService = clearAllService;
    }

    @Override
    public Object handle(Request req, Response res) {
        try {
            // Call clearAll function in ClearAllService to clear all data
            clearAllService.clearAll();

            // Set HTTP response code
            res.status(200); // Successful clear
            return gson.toJson(new ErrorResponse("All data cleared successfully"));

        } catch (DataAccessException e) {
            res.status(500); // Internal Server Error
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }
}
