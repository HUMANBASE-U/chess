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

    public RR.EmptyResult joinGame(RR.JoinGameRequest request) throws DataAccessException, AlreadyTakenException, BadRequestException, UnauthorizedException {
        String color = request.color();
        int gameId = request.gameId();

        //verify if wrong input
        if(gameId <= 0 || color==null || dao.getGame(gameId) == null) throw new BadRequestException("Error: bad request");
        //verify color
        if(!color.equals("WHITE") && !color.equals("BLACK")) throw new BadRequestException("Error: bad request");
        //verify token
        if(request.authToken() == null || dao.getAuth(request.authToken()) == null) throw new UnauthorizedException("Error: unauthorized");

        //success
        AuthData auth = dao.getAuth(request.authToken());
        String username = auth.username();
        GameData oldGame = dao.getGame(gameId);
        GameData newGame;

        //如果是白的来了
        if(Objects.equals(color, "WHITE")) {
            if(oldGame.whiteUsername() == null) {
            newGame = new GameData(request.gameId(), username, oldGame.blackUsername(), oldGame.gameName(), oldGame.game());
            }else throw new AlreadyTakenException("Error: already taken");

        //如果是黑的来了
        }else              if(oldGame.blackUsername() == null) {
            newGame = new GameData(request.gameId(), oldGame.whiteUsername(), username, oldGame.gameName(), oldGame.game());
        }else throw new AlreadyTakenException("Error: already taken");
        //FINAL
        dao.updateGame(newGame);
        return new RR.EmptyResult();
    }

    public RR.ListGameResult listGame(RR.ListGameRequest request) throws DataAccessException, UnauthorizedException {
        //verify token
        if(request.authToken() == null || dao.getAuth(request.authToken()) == null) throw new UnauthorizedException("Error: unauthorized");
        return new RR.ListGameResult(dao.listGames());
    }
}
