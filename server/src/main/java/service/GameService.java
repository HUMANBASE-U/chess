package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;

import java.util.Objects;

public class GameService {
    private final DataAccess dao;
    private int idNumber = 1;

    public GameService(DataAccess dao) {
        this.dao = dao;
    }

    public RR.CreateGameResult createGame(RR.CreateGameRequest request) throws DataAccessException, BadRequestException, UnauthorizedException {
        //verify if wrong input
        if(request.gameName()==null) throw new BadRequestException("Error: bad request");
        //verify token
        if(request.authToken() == null || dao.getAuth(request.authToken()) == null) throw new UnauthorizedException("Error: unauthorized");

        //success
        int gameId = idNumber++;
        RR.CreateGameResult result = new RR.CreateGameResult(gameId);

        //createGame in dao
        dao.createGame(new GameData(gameId, null, null, request.gameName(), new ChessGame()));
        return result;
    }

    public RR.EmptyResult joinFame(RR.JoinGameRequest request) throws DataAccessException, AlreadyTakenException {
        String color = request.color();
        int gameId = request.gameId();



        //success
        AuthData auth = dao.getAuth(request.authToken());
        GameData oldGame = dao.getGame(gameId);
        //(分黑白）
        String username = auth.username();
        if(Objects.equals(color, "WHITE")) {
            if(oldGame.whiteUsername() != null) {
            GameData newGame = new GameData(request.gameId(), username, oldGame.blackUsername(), oldGame.gameName(), new ChessGame());
            }else throw new AlreadyTakenException("Error: already taken");

        }else              if(oldGame.whiteUsername() != null) {
            GameData newGame = new GameData(request.gameId(), username, oldGame.blackUsername(), oldGame.gameName(), new ChessGame());
        }else throw new AlreadyTakenException("Error: already taken");


        GameData newGame = new GameData(request.gameId(), oldGame.whiteUsername(),username,  oldGame.gameName(), new ChessGame());

        dao.updateGame();


        return new RR.EmptyResult();
    }
}
