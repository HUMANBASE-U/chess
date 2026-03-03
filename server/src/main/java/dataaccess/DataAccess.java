package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.List;

public interface DataAccess {

    //clear
    void clear() throws DataAccessException;

    //C
    void createUser(UserData user) throws DataAccessException;
    void createAuth(AuthData auth) throws DataAccessException;
    int createGame(GameData game) throws DataAccessException;

    //R
    AuthData getAuth(String authToken) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    List<GameData> listGames() throws DataAccessException;

    //U
    void updateGame(GameData game) throws DataAccessException;

    //D
    void deleteAuth(String authToken) throws DataAccessException;

}
