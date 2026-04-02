package server;

import dataaccess.DataAccessException;
import dataaccess.SqlDao;
import handler.Handler;
import io.javalin.*;
import server.websocket.WebSocketHandler;
import service.ClearService;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        SqlDao dao;
        try {
            dao = new SqlDao();
        } catch (DataAccessException e) {
            throw new RuntimeException("failed to initialize database", e);
        }
        Handler handler = new Handler(new ClearService(dao), new UserService(dao), new GameService(dao));

        //handlers 双线

        javalin.delete("/db", handler::clearHandler);

        javalin.post("/user", handler::registerHandler);
        javalin.post("/session", handler::loginHandler);
        javalin.delete("/session", handler::logoutHandler);

        javalin.post("/game", handler::createGameHandler);
        javalin.put("/game", handler::joinGameHandler);
        javalin.get("/game", handler::listGameHandler);

        WebSocketHandler webSocketHandler = new WebSocketHandler(dao);
        javalin.ws("/ws", ws -> {
            ws.onConnect(webSocketHandler::handleConnect);
            ws.onMessage(webSocketHandler::handleMessage);
            ws.onClose(webSocketHandler::handleClose);
        });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }


}


