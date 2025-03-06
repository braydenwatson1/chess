package service;

import TempModel.*;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;

import java.util.Objects;
import java.util.UUID;

public class GameService {

    private DataAccess dbAccess;

    public ListResult listGames(AuthData authObj) throws BadRequestException, DataAccessException {
        //if request is bad, error
        if (authObj == null || authObj.authToken() == null || authObj.username() == null) {
            throw new BadRequestException("Error: AuthToken cannot be null. See UserService class: logout() function");
        }

        //does authToken exist?
        AuthData auth;
        try {
            auth = dbAccess.getAuthDAO().getAuth(authObj.authToken());
        } catch (DataAccessException e) {
            throw new DataAccessException("AuthToken not found in database.");
        }

        //does AuthToken match up with your username correctly?
        String matchingUsername = auth.username();
        if (!authObj.username().equals(matchingUsername)) {
            throw new BadRequestException("AuthToken does not match your username.");
        }

        //get a list of games and put it into a ListResult object

        ListResult finalResult = new ListResult(dbAccess.getGameDAO().listGames());
        return finalResult;
    }
}
