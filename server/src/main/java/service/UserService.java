package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {

    private final DataAccess dao;

    public UserService(DataAccess dao) {
        this.dao = dao;
    }

    public RR.RegisterResult register(RR.RegisterRequest registerRequest) throws DataAccessException, AlreadyTakenException, BadRequestException {
        verifyInCaseBlank(registerRequest.username(), registerRequest.password(), registerRequest.email());  //Pre

        if(dao.getUser(registerRequest.username())!=null) throw new AlreadyTakenException("Error: already taken");

//if nothing wrong, do:
        String authToken = newAuth();
        dao.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email())); // 存入服务器userData
        dao.createAuth(new AuthData(authToken, registerRequest.username()));//服务器存入authdata
        return new RR.RegisterResult(registerRequest.username(), authToken);
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public RR.LoginResult login(RR.LoginRequest loginRequest) throws DataAccessException{

        String authToken = newAuth();
        return new RR.LoginResult(loginRequest.username(), authToken);
    }


    public void logout(RR.LogoutRequest logoutRequest) throws DataAccessException{}

    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////



    private static String newAuth() {
        return UUID.randomUUID().toString();
    }

    private static void verifyInCaseBlank(String... lines) throws BadRequestException{
        int count = 0,num = 0;
        for(String l : lines){
            num++;
            if(l!=null) count++;
        }
        if(count != num) throw new BadRequestException("Error: bad request");
    }
}
