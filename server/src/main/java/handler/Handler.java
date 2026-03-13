package handler;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.*;
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
            ctx.result("{}");
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
        }
    }

    public void createGameHandler(Context ctx){
        try{
            RR.CreateGameRequest body = gson.fromJson(ctx.body(), RR.CreateGameRequest.class);
            RR.CreateGameResult result = gameService.createGame(new RR.CreateGameRequest(body.gameName(), ctx.header("authorization")));

            ctx.status(200);
            ctx.result(gson.toJson(result));

        } catch (BadRequestException e) {
            ctx.status(400);
            ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
        }catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
        }
    }

    public void joinGameHandler(Context ctx){
        try{
            RR.JoinGameRequest body = gson.fromJson(ctx.body(),RR.JoinGameRequest.class);
            gameService.joinGame(new RR.JoinGameRequest(body.gameID(), body.playerColor(), ctx.header("authorization")));

            ctx.status(200);
            ctx.result("{}");
        } catch (BadRequestException e){
            ctx.status(400);
            ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
        }catch (UnauthorizedException e){
            ctx.status(401);
            ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
        }catch (AlreadyTakenException e){
            ctx.status(403);
            ctx.result(gson.toJson(Map.of("message","Error: " +  e.getMessage())));
        }catch (DataAccessException e){
            ctx.status(500);
            ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
        }
    }

    public void listGameHandler(Context ctx){
        try{
            String authToken = ctx.header("authorization");
            RR.ListGameResult result = gameService.listGame(new RR.ListGameRequest(authToken));
            ctx.status(200);
            ctx.result(gson.toJson(result));
        } catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
        }
    }

    public void registerHandler(Context ctx) {
        try {
            RR.RegisterRequest request = gson.fromJson(ctx.body(), RR.RegisterRequest.class);
            RR.RegisterResult result = userService.register(request);
            ctx.status(200);
            ctx.result(gson.toJson(result));
        } catch (BadRequestException e) {
            ctx.status(400);
            ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
        } catch (AlreadyTakenException e) {
            ctx.status(403);
            ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
        }
    }

    public void loginHandler(Context ctx){
        try {
            RR.LoginRequest request = gson.fromJson(ctx.body(), RR.LoginRequest.class);
            RR.LoginResult result = userService.login(request);
            ctx.status(200);
            ctx.result(gson.toJson(result));
        } catch (BadRequestException e) {
            ctx.status(400);
            ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
        } catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
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
            ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
        }
    }
}

