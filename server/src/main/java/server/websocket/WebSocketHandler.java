package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
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
import java.util.Objects;

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
            notification.message = createNotification(auth.username(), game);
            json = gson.toJson(notification);
            connections.broadcastExcept(command.getGameID(), session, json);

        } catch (DataAccessException e) {
            sendError(session, "Error: Something went wrong");
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
        if (command.getAuthToken() == null || command.getGameID() == null) {
            sendError(session, "Error: expect a valid auth or game id");
            return;
        }

        try{
            //验证 auth 和 gameid
            AuthData auth = dao.getAuth(command.getAuthToken());
            GameData gameData = dao.getGame(command.getGameID());

            if(auth==null || gameData ==null){
                sendError(session, "Error: incorrect input");
                return;
            }

            //移出游戏
            String userName = auth.username();
            if(Objects.equals(userName, gameData.whiteUsername())){
                gameData = gameData.changeWhiteUsername(null);
            }
            if(Objects.equals(userName, gameData.blackUsername())) {
                gameData = gameData.changeBlackUsername(null);
            }
            dao.updateGame(gameData);

            //notify everyone
            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            notification.message = userName + " left!";
            String json = gson.toJson(notification);
            connections.broadcastExcept(command.getGameID(), session, json);

            //断开连接
            connections.remove(session);

        } catch (DataAccessException e) {
            sendError(session, "Error: Something went wrong");
        }
    }

    private void resign(UserGameCommand command, Session session) throws IOException {
        if (command.getAuthToken() == null || command.getGameID() == null) {
            sendError(session, "Error: expect a valid auth or game id");
            return;
        }

        try{
            //验证 auth 和 gameid
            AuthData auth = dao.getAuth(command.getAuthToken());
            GameData gameData = dao.getGame(command.getGameID());

            if(auth==null || gameData ==null){
                sendError(session, "Error: incorrect input");
                return;
            }
            ChessGame game = gameData.game();//防御性位置

            //检测
            String userName = auth.username();
            if(!Objects.equals(userName, gameData.whiteUsername())
                    && !Objects.equals(userName, gameData.blackUsername())){
                sendError(session, "Error: we notice that you are not a player!");
                return;
            }

            if(game.gameOverFlag){
                sendError(session, "Error: Can not resign more than once!");
                return;
            }

            //投降
            game.gameOverFlag = true;
            gameData = gameData.changeGame(game);
            dao.updateGame(gameData);

            //notify everyone
            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            notification.message = userName + " has already resigned!";
            String json = gson.toJson(notification);
            connections.superBroadcast(command.getGameID(), json);

        } catch (DataAccessException e) {
            sendError(session, "Error: Something went wrong");
        }
    }

    private void makeMove(UserGameCommand command, Session session) throws IOException {
        if (command.getAuthToken() == null || command.getGameID() == null) {
            sendError(session, "Error: expect a valid auth or game id");
            return;
        }
        try{
            AuthData auth = dao.getAuth(command.getAuthToken());
            GameData gameData = dao.getGame(command.getGameID());
            if(auth==null || gameData ==null){
                sendError(session, "Error: incorrect input");
                return;
            }
            ChessGame game = gameData.game();//防御性位置
            String userName = auth.username();
            if(!Objects.equals(auth.username(), gameData.whiteUsername())
                    && !Objects.equals(auth.username(), gameData.blackUsername())){
                sendError(session, "Error: we notice that you are not a player");
                return;
            }

            if(Objects.equals(userName, gameData.whiteUsername())){
                if (game.getTeamTurn() == ChessGame.TeamColor.BLACK){
                    sendError(session, "Error: we notice that you are pretending your opponent");
                    return;
                }
            }
            if(Objects.equals(userName, gameData.blackUsername())){
                if (game.getTeamTurn() == ChessGame.TeamColor.WHITE){
                    sendError(session, "Error: Hi, we notice that you are pretending your opponent");
                    return;
                }
            }
            if(game.gameOverFlag){
                sendError(session, "Error: game already over!");
                return;
            }
            ChessGame.TeamColor color = game.getTeamTurn();
            ChessGame.TeamColor reversedColor = color ==
                    ChessGame.TeamColor.WHITE? ChessGame.TeamColor.BLACK: ChessGame.TeamColor.WHITE;
            ChessMove move = command.getMove();
            game.makeMove(move);
            dao.updateGame(gameData.changeGame(game));
            ServerMessage loadGameMsg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            loadGameMsg.game = game;
            String json = gson.toJson(loadGameMsg);
            connections.superBroadcast(command.getGameID(), json);
            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            notification.message = "A move was made!";
            json = gson.toJson(notification);
            connections.broadcastExcept(command.getGameID(), session, json);

            //如果胜负已分，一定是你先手
            if(game.isInCheckmate(reversedColor)){
                //notify everyone
                notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                String msg = color == ChessGame.TeamColor.WHITE
                        ?gameData.blackUsername() + "  is in checkmate, game over!"
                        :gameData.whiteUsername() + "  is in checkmate, game over!!";
                notification.message = msg;
                json = gson.toJson(notification);
                connections.superBroadcast(command.getGameID(), json);
                game.gameOverFlag = true;
                dao.updateGame(gameData.changeGame(game));
                return;
            }
            if(game.isInStalemate(reversedColor)){
                //notify everyone
                notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                String msg = color == ChessGame.TeamColor.WHITE
                        ?gameData.blackUsername() + "  is in isInStalemate!"
                        :gameData.whiteUsername() + "  is in isInStalemate!";
                notification.message = msg;
                json = gson.toJson(notification);
                connections.superBroadcast(command.getGameID(), json);
                game.gameOverFlag = true;
                dao.updateGame(gameData.changeGame(game));
                return;
            }
            //如果check
            if(game.isInCheck(reversedColor)){
                //notify everyone
                notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                String msg = color == ChessGame.TeamColor.WHITE
                        ?gameData.blackUsername() + "  is in check!"
                        :gameData.whiteUsername() + "  is in check!";
                notification.message = msg;
                json = gson.toJson(notification);
                connections.superBroadcast(command.getGameID(), json);
            }
        } catch (DataAccessException e) {
            sendError(session, "Error: Something went wrong");
        } catch (InvalidMoveException e) {
            sendError(session, "Error: Invalid move");
        }
    }
}
