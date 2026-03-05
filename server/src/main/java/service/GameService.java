package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.GameData;

public class GameService {
    private final DataAccess dao;
    private int idNumber = 1;

    public GameService(DataAccess dao) {
        this.dao = dao;
    }

    public RR.CreateGameResult createGame(RR.CreateGameRequest request) throws DataAccessException, BadRequestException, UnauthorizedException {
        //verify token
        if(request.authToken() == null || dao.getAuth(request.authToken()) == null) throw new UnauthorizedException("Error: unauthorized");

        //success
        int gameId = idNumber++;
        RR.CreateGameResult result = new RR.CreateGameResult(gameId);

        //createGame
        dao.createGame(new GameData(gameId, null, null, request.gameName(), new ChessGame()));

        return result;
    }
}
