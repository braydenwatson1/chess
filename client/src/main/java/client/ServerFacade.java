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

    public void logout(LogoutRequest req) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, req, null);
    }

    public ListResult listGames(ListRequest req) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, req, ListResult.class);
    }

    public CreateGameResult createGame(CreateGameRequest req) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, req, CreateGameResult.class);
    }

    public String joinGame(JoinRequest req) throws ResponseException {
        var path = "/game";
        return this.makeRequest("PUT", path, req, null);
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

            // Automatically set Authorization header if request has an authToken
            String authToken = extractAuthToken(request);
            if (authToken != null) {
                http.setRequestProperty("Authorization", authToken);
            }

            // Set output only for POST/PUT methods
            if (method.equals("POST") || method.equals("PUT")) {
                http.setDoOutput(true);
                writeBody(request, http);
            }
            http.connect();
            throwIfNotSuccessful(http);
            if (responseClass == null) {
                return null;
            }

            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            System.out.println("DEBUG: ErROR caught 1");
            throw ex;
        } catch (Exception ex) {
            System.out.println("DEBUG: ErROR caught 2");
            System.out.println("Error is:" + ex.getMessage());
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


    private String extractAuthToken(Object request) {
        if (request == null) {
            return null;
        }
        try {
            // Check if request object has a method getAuthToken()
            var method = request.getClass().getMethod("authToken");
            return (String) method.invoke(request);
        } catch (Exception e) {;
            return null;  // If method doesn't exist, just return null
        }
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}