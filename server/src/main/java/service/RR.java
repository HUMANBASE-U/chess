package service;

import model.GameData;

import java.util.List;

public class RR {
    public record EmptyResult() {};

    //REG
    public record RegisterRequest(String username, String password, String email) {}
    public record RegisterResult(String username, String authToken) {}

    //LOG
    public record LoginRequest(String username, String password) {}
    public record LoginResult(String username, String authToken) {}
    public record LogoutRequest(String authToken) {}

    //GAME
    public record CreateGameRequest(String gameName, String authToken) {}
    public record CreateGameResult(int gameId) {}


    public record JoinGameRequest(int gameId, String color, String authToken) {}


    public record ListGameRequest(String gameName, String authToken) {}
    public record ListGameResult(List<GameData> gameDataListList) {}


}
