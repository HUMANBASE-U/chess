package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;

public class GameService {
    private final DataAccess dao;

    public GameService(DataAccess dao) {
        this.dao = dao;
    }

    public RR.CreateGameRequest createGame() throws DataAccessException{


        //success
        dao.createGame()
        int gameId =
        RR.CreateGameResult result = new RR.CreateGameResult()
        return
    }
}
