package model;
import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public GameData changeWhiteUsername(String newName) {
        return new GameData(gameID, newName, blackUsername, gameName, game);
    }
    public GameData changeBlackUsername(String newName) {
        return new GameData(gameID, whiteUsername, newName, gameName, game);
    }
    public GameData changeGame(ChessGame newGame) {
        return new GameData(gameID, whiteUsername, blackUsername, gameName, newGame);
    }
}
