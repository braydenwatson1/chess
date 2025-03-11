package service;

import Model.*;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;

import java.util.Objects;
import java.util.UUID;

public class UserService {

    private final DataAccess dbAccess;

    // Constructor
    public UserService(DataAccess dbAccess) {
        this.dbAccess = dbAccess;
    }

        public RegisterResult register(RegisterRequest regReq) throws BadRequestException, DataAccessException {

            //if request is bad, error
            if (regReq == null || regReq.username() == null || regReq.password() == null || regReq.email() == null) {
                throw new BadRequestException("Error: Username, Password, and email cannot be null. See UserService class: register() function");
            }

            //create UserData object to insert into db
            UserData newUser = new UserData(regReq.username(),regReq.password(),regReq.email());

            //call createUser function, which inherently checks if
            // user already exists, and adds it to the db if it doesn't
            dbAccess.getUserDAO().createUser(newUser);

            //create authToken
            String authTokenNum = UUID.randomUUID().toString();
            String associatedUsername = regReq.username();
            AuthData newAuthToken = new AuthData(associatedUsername, authTokenNum);

            //add authToken to db
            dbAccess.getAuthDAO().addAuth(newAuthToken);

            RegisterResult FinalResult = new RegisterResult(associatedUsername, authTokenNum);

            //return the authToken & username
            return FinalResult;
        }

        public LoginResult login(LoginRequest logReq) throws BadRequestException, DataAccessException {
            //if request is bad, error
            if (logReq == null || logReq.username() == null || logReq.password() == null) {
                throw new BadRequestException("Error: Username, Password cannot be null. See UserService class: register() function");
            }

            //authorization
            String userUsername = logReq.username();
            UserData user;
            try {
                user = dbAccess.getUserDAO().getUser(userUsername);
            } catch (DataAccessException e) {
                throw new BadRequestException("User not found: " + userUsername);
            }

            // is password correct?
            if (!Objects.equals(user.password(), logReq.password())) {
                throw new BadRequestException("password is incorrect");
            }

            //create authToken
            String authTokenNum = UUID.randomUUID().toString();
            String associatedUsername = logReq.username();
            AuthData newAuthToken = new AuthData(associatedUsername, authTokenNum);

            //add authToken to db
            dbAccess.getAuthDAO().addAuth(newAuthToken);

            LoginResult FinalResult = new LoginResult(associatedUsername, authTokenNum);

            //return the authToken & username
            return FinalResult;
        }
        public void logout(LogoutRequest logReq) throws BadRequestException, DataAccessException {
            //if request is bad, error
            if (logReq == null || logReq.authToken() == null) {
                throw new BadRequestException("Error: AuthToken cannot be null. See UserService class: logout() function");
            }

            //does the authToken exist?
            AuthData auth;
            try {
                auth = dbAccess.getAuthDAO().getAuth(logReq.authToken());
            } catch (DataAccessException e) {
                throw new DataAccessException("AuthToken not found in database");
            }


            //delete the authToken
            //this function deletes the whole authData object, not just the
            // authtoken string from the object it finds it in
            dbAccess.getAuthDAO().deleteAuth(auth.authToken());

        }
}

