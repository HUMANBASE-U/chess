package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;

import java.util.UUID;

public class UserService {

    private DataAccess dao;

    public UserService(DataAccess dao) {
        this.dao = dao;
    }

    public RR.RegisterResult register(RR.RegisterRequest registerRequest) throws DataAccessException, AlreadyTakenException {
          if(dao.getUser(registerRequest.username())!=null) throw new AlreadyTakenException("Error: already taken");




          String authToken = newAuth();
          dao.createAuth(new AuthData(authToken, registerRequest.username()));//服务器存入authdata
        return new RR.RegisterResult(registerRequest.username(), authToken);
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public RR.LoginResult login(RR.LoginRequest loginRequest) throws DataAccessException{
        return
    }
    public void logout(RR.LogoutRequest logoutRequest) throws DataAccessException{}

    private static String newAuth() {
        return UUID.randomUUID().toString();
    }
}
