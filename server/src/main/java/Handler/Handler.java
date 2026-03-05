package Handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.*;

import java.lang.reflect.Type;
import java.util.Map;

public class Handler {
    private final Gson gson = new Gson();

    private final ClearService clearService;
    private final UserService userService;
    private final GameService gameService;

    public Handler(ClearService clearService, UserService userService, GameService gameService) {
        this.clearService = clearService;
        this.userService = userService;
        this.gameService = gameService;

    }

    public void clearHandler(Context ctx){
        try{
            clearService.clear();
            ctx.status(200);
        } catch (DataAccessException e) {
            ctx.status(500);
        }
    }

    public void createGameHandler(Context ctx){
        try{
            RR.CreateGameRequest body = gson.fromJson(ctx.body(), RR.CreateGameRequest.class);
            RR.CreateGameResult result = gameService.createGame(new RR.CreateGameRequest(body.gameName(), ctx.header("authorization")));

            ctx.status(200);
            ctx.json(result);

        } catch (BadRequestException e) {
            ctx.status(400);
            ctx.json(Map.of("message", e.getMessage()));
        }catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.json(Map.of("message", e.getMessage()));
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.json(Map.of("message", e.getMessage()));
        }
    }

    public void joinGameHandler(Context ctx){
        try{
            RR.JoinGameRequest body = gson.fromJson(ctx.body(), (Type) RR.JoinGameRequest.class);
            gameService.joinGame(new RR.JoinGameRequest(body.gameID(), body.color(), ctx.header("authorization")));

            ctx.status(200);
            ctx.result("{}");
        } catch (BadRequestException e){
            ctx.status(400);
            ctx.json(Map.of("message", e.getMessage()));
        }catch (UnauthorizedException e){
            ctx.status(401);
            ctx.json(Map.of("message", e.getMessage()));
        }catch (AlreadyTakenException e){
            ctx.status(403);
            ctx.json(Map.of("message", e.getMessage()));
        }catch (DataAccessException e){
            ctx.status(500);
            ctx.json(Map.of("message", e.getMessage()));
        }
    }

    public void listGameHandler(Context ctx){
        try{
            String authToken = ctx.header("authorization");
            RR.ListGameResult result = gameService.listGame(new RR.ListGameRequest(authToken));
            ctx.status(200);
            ctx.json(result);
        } catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.json(Map.of("games", e.getMessage()));
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.json(Map.of("message", e.getMessage()));
        }
    }

    public void registerHandler(Context ctx) throws BadRequestException, DataAccessException, AlreadyTakenException {
        try {
            RR.RegisterRequest request = gson.fromJson(ctx.body(), RR.RegisterRequest.class);
            RR.RegisterResult result = userService.register(request);
            ctx.status(200);
            ctx.json(result);
        } catch (BadRequestException e) {
            ctx.status(400);
            ctx.json(Map.of("message", e.getMessage()));
        } catch (AlreadyTakenException e) {
            ctx.status(403);
            ctx.json(Map.of("message", e.getMessage()));
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.json(Map.of("message", e.getMessage()));
        }
    }

    public void loginHandler(Context ctx) throws BadRequestException, DataAccessException {
        try {
            RR.LoginRequest request = gson.fromJson(ctx.body(), RR.LoginRequest.class);
            RR.LoginResult result = userService.login(request);
            ctx.status(200);
            ctx.json(result);
        } catch (BadRequestException e) {
            ctx.status(400);
            ctx.json(Map.of("message", e.getMessage()));
        } catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.json(Map.of("message", e.getMessage()));
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.json(Map.of("message", e.getMessage()));
        }
    }

    public void logoutHandler(Context ctx){
        try {
            String authToken = ctx.header("authorization");
            userService.logout(new RR.LogoutRequest(authToken));
            ctx.status(200);
            ctx.result("{}");
        } catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.json(Map.of("message", e.getMessage()));
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.json(Map.of("message", e.getMessage()));
        }
    }
}

