package client;

import chess.ChessMove;
import com.google.gson.Gson;

import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    private static final Gson GSON = new Gson();
    Session session;
    private final NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws client.ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String json) {
                    ServerMessage serverMessage = GSON.fromJson(json, ServerMessage.class);
                    notificationHandler.onMessage(serverMessage);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void makeMove(UserGameCommand.CommandType type, String authToken, Integer gameId, ChessMove move) throws IOException {
        UserGameCommand cmd = new UserGameCommand(
                type,
                authToken,
                gameId,
                move
        );
        String json = GSON.toJson(cmd);
        session.getBasicRemote().sendText(json);

    }


    public void connect(UserGameCommand.CommandType commandType, String auth, int gameId) throws IOException {
        UserGameCommand cmd = new UserGameCommand(
                commandType,
                auth,
                gameId);
        String json = GSON.toJson(cmd);
        session.getBasicRemote().sendText(json);
    }
}