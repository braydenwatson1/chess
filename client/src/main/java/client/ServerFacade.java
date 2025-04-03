package client;

import Model.*;
import com.google.gson.Gson;
import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest req) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, req, RegisterResult.class);
    }

    public LoginResult login(LoginRequest req) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, req, LoginResult.class);
    }

    //my handler seems to return an empty string for logout, so that is what im trying here
    public String logout(LogoutRequest req) throws ResponseException {
        var path = "/session";
        return this.makeRequest("DELETE", path, req, String.class);
    }

    public ListResult listGames(ListRequest req) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, req, ListResult.class);
    }

    public CreateGameResult createGame(CreateGameRequest req) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, req, CreateGameResult.class);
    }

    //same as logout. empty string is returned
    public String joinGame(JoinRequest req) throws ResponseException {
        var path = "/game";
        return this.makeRequest("PUT", path, req, String.class);
    }

    //same as logout. empty string is returned
    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);

            // Only set output for methods that require it (POST, PUT)
            if (method.equals("POST") || method.equals("PUT")) {
                http.setDoOutput(true);
            }

            // Write the request body only if necessary
            writeBody(request, http);

            http.connect();
            throwIfNotSuccessful(http);

            // Don't attempt to read a response if responseClass is null
            if (responseClass == null) {
                return null;
            }

            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "Error : other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        if (responseClass == null) {
            return null; // No response expected
        }

        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                response = new Gson().fromJson(reader, responseClass);
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}