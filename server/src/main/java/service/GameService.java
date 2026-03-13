package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;

import java.util.Objects;

public class GameService {
    private final DataAccess dao;

    public GameService(DataAccess dao) {
        this.dao = dao;
    }

    public RR.CreateGameResult createGame(RR.CreateGameRequest request) throws DataAccessException, BadRequestException, UnauthorizedException {
        //verify if wrong input
        if (request.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }
        //verify token
        if (request.authToken() == null || dao.getAuth(request.authToken()) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        int gameId = dao.createGame(new GameData(0, null, null, request.gameName(), new ChessGame()));
        return new RR.CreateGameResult(gameId);
    }

    public RR.EmptyResult joinGame(RR.JoinGameRequest request) 
    throws DataAccessException, AlreadyTakenException, BadRequestException, UnauthorizedException {
        String playerColor = request.playerColor();
        int gameId = request.gameID();

        //verify if wrong input
        if (gameId <= 0 || playerColor == null || dao.getGame(gameId) == null) {
            throw new BadRequestException("Error: bad request");
        }
        //verify playerColor
        if (!playerColor.equals("WHITE") && !playerColor.equals("BLACK")) {
            throw new BadRequestException("Error: bad request");
        }
        //verify token
        if (request.authToken() == null || dao.getAuth(request.authToken()) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        //success
        AuthData auth = dao.getAuth(request.authToken());
        String username = auth.username();
        GameData oldGame = dao.getGame(gameId);
        GameData newGame;

        //如果是白的来了
        if (Objects.equals(playerColor, "WHITE")) {
            if (oldGame.whiteUsername() == null) {
                newGame = new GameData(request.gameID(), username, oldGame.blackUsername(),
                        oldGame.gameName(), oldGame.game());
            } else {
                throw new AlreadyTakenException("Error: already taken");
            }

        //如果是黑的来了
        } else if (oldGame.blackUsername() == null) {
            newGame = new GameData(request.gameID(), oldGame.whiteUsername(), username,
                    oldGame.gameName(), oldGame.game());
        } else {
            throw new AlreadyTakenException("Error: already taken");
        }
        //FINAL
        dao.updateGame(newGame);
        return new RR.EmptyResult();
    }

    public RR.ListGameResult listGame(RR.ListGameRequest request) throws DataAccessException, UnauthorizedException {
        //verify token
        if (request.authToken() == null || dao.getAuth(request.authToken()) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return new RR.ListGameResult(dao.listGames());
    }
}
