package client;

import chess.ChessGame;
import websocket.messages.ServerMessage;

public class NotificationHandler {
    private ServerMessage serverMessage;
    private ChessGame game;
    private String msg;
    private String errorMessage;
    public int gameVersion = 1;

    public void onMessage(ServerMessage message){
        this.serverMessage = message;

        switch (serverMessage.getServerMessageType()){
            case LOAD_GAME -> {
                this.game = serverMessage.game;
                gameVersion++;
            }

            case NOTIFICATION -> this.msg = serverMessage.message;
            case ERROR -> this.errorMessage = serverMessage.errorMessage;

            default -> {
                return;
            }
        }
    }

    public ServerMessage getServerMessage() {
        return serverMessage;
    }

    public void setServerMessage(ServerMessage serverMessage) {
        this.serverMessage = serverMessage;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
