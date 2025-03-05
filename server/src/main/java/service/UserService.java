package service;

import TempModel.UserData;

public class UserService {
        public RegisterResult register(RegisterRequest registerRequest) {
            //if request is bad, error
            if (user == null || user.username() == null || user.password() == null || user.email() == null) {
                throw new BadRequestException("Error: Username, Password, and email must not be null");
            }

            //create UserData object to insert into db

            //call createUser function, which inherently checks if
            // user already exists, and adds it to the db
            createUser(userDataObject);

            //create authToken
            //add authToken to db

            //return the authToken & username

        }

        public LoginResult login(LoginRequest loginRequest) {}
        public void logout(LogoutRequest logoutRequest) {}
}

