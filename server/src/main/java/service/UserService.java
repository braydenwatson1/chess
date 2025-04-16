package service;

import Model.*;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import java.util.UUID;

public class UserService {

    private final DataAccess dbAccess;

    // Constructor
    public UserService(DataAccess dbAccess) {
        this.dbAccess = dbAccess;
    }

    public RegisterResult register(RegisterRequest regReq) throws BadRequestException, DataAccessException, ForbiddenException {
        if (regReq == null || regReq.username() == null || regReq.password() == null || regReq.email() == null) {
            throw new BadRequestException("Error. Bad Request: Missing required fields in RegisterRequest");
        }

        String username = regReq.username();
        String password = regReq.password();
        String email = regReq.email();

        if (dbAccess.getUserDAO().userExists(username)) {
            throw new ForbiddenException("Error: Username: \"" + username + "\" already exists");
        }

        // No hashing here — let DAO handle it
        UserData newUser = new UserData(username, password, email);
        dbAccess.getUserDAO().createUser(newUser);

        String authToken = UUID.randomUUID().toString();
        dbAccess.getAuthDAO().addAuth(new AuthData(username, authToken));

        return new RegisterResult(username, authToken);
    }

    public LoginResult login(LoginRequest logReq) throws BadRequestException, DataAccessException, UnauthorizedException {
        if (logReq == null || logReq.username() == null || logReq.password() == null) {
            throw new BadRequestException("Error: Missing username or password in LoginRequest");
        }

        String username = logReq.username();
        String password = logReq.password();

        try {
            dbAccess.getUserDAO().authenticateUser(username, password);
        } catch (DataAccessException e) {
            throw new UnauthorizedException(e.getMessage());
        }
        String authToken = UUID.randomUUID().toString();
        dbAccess.getAuthDAO().addAuth(new AuthData(username, authToken));

        return new LoginResult(username, authToken);
    }

    public void logout(LogoutRequest logReq) throws BadRequestException, DataAccessException, UnauthorizedException {
        //if request is bad, error
        if (logReq == null || logReq.authToken() == null) {
            throw new BadRequestException("Error: AuthToken cannot be null. See UserService class: logout() function");
        }

        //does the authToken exist? will throw error if not
        AuthData auth;
        try {
            auth = dbAccess.getAuthDAO().getAuth(logReq.authToken());
        } catch (DataAccessException e) {
            throw new UnauthorizedException(e.getMessage());
        }
        //delete the authToken
        //this function deletes the whole authData object, not just the
        // authtoken string from the object it finds it in
        dbAccess.getAuthDAO().deleteAuth(auth.authToken());
    }

}

