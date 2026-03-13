package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;
import java.util.UUID;

public class UserService {

    private final DataAccess dao;

    public UserService(DataAccess dao) {
        this.dao = dao;
    }

//    private void writeHashedPasswordToDatabase(String username, String hashedPassword) {
//    }
//
//    void storeUserPassword(String username, String clearTextPassword) {
//        String hashedPassword = BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
//
//        // write the hashed password in database along with the user's other information
//        writeHashedPasswordToDatabase(username, hashedPassword);
//    }
//
//    private Object readHashedPasswordFromDatabase(String username) {
//    }
//
//    boolean verifyUser(String username, String providedClearTextPassword) {
//        // read the previously hashed password from the database
//        var hashedPassword = readHashedPasswordFromDatabase(username);
//
//        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
//    }



    public RR.RegisterResult register(RR.RegisterRequest registerRequest) throws DataAccessException, AlreadyTakenException, BadRequestException {
        verifyInCaseBlank(registerRequest.username(), registerRequest.password(), registerRequest.email());  //Pre

        if (dao.getUser(registerRequest.username()) != null) {
            throw new AlreadyTakenException("Error: already taken");
        }


//if nothing wrong, do:
        String authToken = newAuth();
        dao.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email())); // 存入服务器userData
        dao.createAuth(new AuthData(authToken, registerRequest.username()));//服务器存入authdata
        return new RR.RegisterResult(registerRequest.username(), authToken);
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public RR.LoginResult login(RR.LoginRequest loginRequest) throws DataAccessException, BadRequestException, UnauthorizedException {
        //verify if wrong input
        String userName = loginRequest.username();
        String password = loginRequest.password();
        if (userName == null || password == null) {
            throw new BadRequestException("Error: bad request");
        }

        //verify your name and password
        if (dao.getUser(userName) == null
                || !Objects.equals(dao.getUser(userName).password(), password)) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        String authToken = newAuth();
        dao.createAuth(new AuthData(authToken, userName));
        return new RR.LoginResult(loginRequest.username(), authToken);
    }

    public void logout(RR.LogoutRequest logoutRequest) throws DataAccessException, UnauthorizedException {
        //verify token
        if (logoutRequest.authToken() == null
                || dao.getAuth(logoutRequest.authToken()) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        dao.deleteAuth(logoutRequest.authToken());
    }

    private static String newAuth() {
        return UUID.randomUUID().toString();
    }

    private static void verifyInCaseBlank(String... lines) throws BadRequestException{
        for (String l : lines) {
            if (l == null) {
                throw new BadRequestException("Error: bad request");
            }
        }
    }
}
