package service;

import TempModel.RegisterRequest;
import TempModel.LoginRequest;
import TempModel.RegisterResult;
import TempModel.UserData;

public class UserService {
        public RegisterResult register(RegisterRequest regReq) throws BadRequestException{

            //if request is bad, error
            if (regReq == null || regReq.username() == null || regReq.password() == null || regReq.email() == null) {
                throw new BadRequestException("Error: Username, Password, and email cannot be null. See UserService class: register() function");
            }

            //create UserData object to insert into db
            UserData newUser = new UserData(regReq.username(),regReq.password(),regReq.email());

            //call createUser function, which inherently checks if
            // user already exists, and adds it to the db
            dataaccess.UserDAO.createUser(newUser);

            //create authToken
            //add authToken to db

            //return the authToken & username

        }

        public LoginResult login(LoginRequest loginRequest) {}
        public void logout(LogoutRequest logoutRequest) {}
}

