package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final DataAccess dao;
    private final Gson gson = new Gson();

    public WebSocketHandler(DataAccess dao) {
        this.dao = dao;
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand command = gson.fromJson(ctx.message(), UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> connect(command, ctx.session);
                case LEAVE -> leave(command, ctx.session);
                case RESIGN -> resign(command, ctx.session);
                case MAKE_MOVE -> makeMove(command, ctx.session);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                sendError(ctx.session, "Error: invalid command");
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        connections.remove(ctx.session);
        System.out.println("Websocket closed");
    }


    private void connect(UserGameCommand command, Session session) throws IOException {
        if (command.getAuthToken() == null || command.getGameID() == null) {
            sendError(session, "Error: expect a valid auth or game id");
            return;
        }

        try{
            //验证 auth 和 gameid
            AuthData auth = dao.getAuth(command.getAuthToken());
            GameData game = dao.getGame(command.getGameID());

            if(auth==null || game==null){
                sendError(session, "Error: incorrect input");
                return;
            }

            //add session
            connections.add(session, command.getGameID());

            //send the gameBoard
            ServerMessage loadGameMsg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            loadGameMsg.game = game.game();
            String json = gson.toJson(loadGameMsg);
            connections.send(session, json);

            //notify everyone
            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            notification.msg = createNotification(auth.username(), game);
            json = gson.toJson(notification);
            connections.broadcastExcept(command.getGameID(), session, json);

        } catch (DataAccessException e) {
            sendError(session, "Error: incorrect input");
        }
    }

    private static String createNotification(String username, GameData gameData) {
        if (username.equals(gameData.whiteUsername())) {
            return username + " joined the game as white";
        }
        if (username.equals(gameData.blackUsername())) {
            return username + " joined the game as black";
        }
        return username + " is observing the game";
    }

    private void sendError(Session session, String text) throws IOException {
        ServerMessage err = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        err.errorMessage = text;
        connections.send(session, gson.toJson(err));
    }

    private void leave(UserGameCommand command, Session session) throws IOException {
    }

    private void resign(UserGameCommand command, Session session) throws IOException {
    }

    private void makeMove(UserGameCommand command, Session session) throws IOException {
    }
}
