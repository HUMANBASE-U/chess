package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        String url = "http://localhost:8080";
        if (args.length >= 1) {
            url = args[0];
        }

        ServerFacade facade = new ServerFacade(url);
        NotificationHandler handler = new NotificationHandler();
        WebSocketFacade socket = null;
        try{
            socket = new WebSocketFacade(url, handler);
        } catch (ResponseException ex) {
            System.out.println("WebSocket initiation failed: " + ex.getMessage());
            return;
        }
        Client client = new Client(facade, socket, handler);
        client.run();
    }
}
