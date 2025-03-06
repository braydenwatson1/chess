package service;

import TempModel.*;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;

import java.util.UUID;

public class UserService {

        private DataAccess dbAccess;

        public RegisterResult register(RegisterRequest regReq) throws BadRequestException, DataAccessException {

            //if request is bad, error
            if (regReq == null || regReq.username() == null || regReq.password() == null || regReq.email() == null) {
                throw new BadRequestException("Error: Username, Password, and email cannot be null. See UserService class: register() function");
            }

            //create UserData object to insert into db
            UserData newUser = new UserData(regReq.username(),regReq.password(),regReq.email());

            //call createUser function, which inherently checks if
            // user already exists, and adds it to the db
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

        public LoginResult login(LoginRequest logReq) throws BadRequestException{
            //if request is bad, error
            if (logReq == null || logReq.username() == null || logReq.password() == null) {
                throw new BadRequestException("Error: Username, Password cannot be null. See UserService class: register() function");
            }

            //create UserData object to insert into db
            UserData newUser = new UserData(logReq.username(), logReq.password(), logReq.email());

            //call createUser function, which inherently checks if
            // user already exists, and adds it to the db
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
        public void logout(LogoutRequest logoutRequest) {}
}

