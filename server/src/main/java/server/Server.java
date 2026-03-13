package server;

import dataaccess.DataAccessException;
import dataaccess.SqlDao;
import handler.Handler;
import dataaccess.MemoryDao;
import io.javalin.*;
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

        // Register your endpoints and exception handlers here.

        javalin.delete("/db", handler::clearHandler);

        javalin.post("/user", handler::registerHandler);
        javalin.post("/session", handler::loginHandler);
        javalin.delete("/session", handler::logoutHandler);

        javalin.post("/game", handler::createGameHandler);
        javalin.put("/game", handler::joinGameHandler);
        javalin.get("/game", handler::listGameHandler);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }


}


