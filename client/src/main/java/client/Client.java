package client;

import chess.ChessGame;
import model.GameData;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client {
    public enum State {
        Prelogin,
        Postlogin
    }

    private final ServerFacade server;
    private State state = State.Prelogin;
    private String visitorName = null;
    private String auth = null;
    private List<GameData> gameList;
    private ChessGame localGame;
    private ChessBoardRenderer render;

    public Client(ServerFacade server) {
        this.server = server;
    }

    public void run() {
        System.out.println(" Welcome to Chess game");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(result + "\n");
                
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public String eval(String input) {
        try {
            String[] tokens = input.split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "create" -> create(params);
                case "list" -> list(params);
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout(params);
                case "quit" -> "quit";
                case "BAGAYALU" -> {
                    server.clear();
                    yield "deleted";
                }
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }


    public String register(String... params) throws ResponseException {
        if(params.length == 3){
            auth = server.register(new RR.RegisterRequest(params[0], params[1], params[2])).authToken();
            state = State.Postlogin;
            visitorName = params[0];
            return String.format("%s, you are signed in!", visitorName);
        }
        throw new ResponseException(
                ResponseException.Code.BadRequest, "Expected: register <USERNAME> <PASSWORD> <EMAIL>");
    }


    private String login(String... params) throws ResponseException {
        if(params.length == 2){
            auth = server.login(new RR.LoginRequest(params[0], params[1])).authToken();
            visitorName = params[0];
            state = State.Postlogin;
            return String.format("%s, you are logged in!", visitorName);
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected: login <USERNAME> <PASSWORD>");
    }



    //post:

    private String create(String... params) throws ResponseException {
        assertLoggedIn();
        if(params.length == 1){
            server.createGame(new RR.CreateGameRequest(params[0], auth));
            return String.format("%s, you have created a game!", visitorName);
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected: create <NAME>");
    }

    private String list(String... params) throws ResponseException {
        assertLoggedIn();
        if(params.length == 0){
            RR.ListGameResult result = server.listGames(new RR.ListGameRequest(auth));
            gameList = result.games();

            for (int i=0; i<gameList.size(); i++){
                String output = " ";
                GameData game = gameList.get(i);

                output += "id: " + (i+1) + "   ";
                output += "gameName: " + game.gameName() + "        ";
                output += "whiteUsername: " + game.whiteUsername() + " ";
                output += "blackUsername: " + game.blackUsername() + "\n";

                System.out.print(output);
            }
            return String.format("%s, above is all the games", visitorName);
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected: list");
    }

    public String join(String... params) throws ResponseException {
        assertLoggedIn();
        if(params.length == 2){

            try { //这里用用户的input -》 倒退一位下标， 然后在list找到 倒退下标的data， 然后取出gameID
                int inputId = Integer.parseInt(params[0]) - 1;
                int gameId = gameList.get(inputId).gameID();
                String myColor = params[1].toUpperCase();
                if (!myColor.equals("WHITE") && !myColor.equals("BLACK"))
                    return String.format("%s, Expected: join <ID> [WHITE|BLACK]", visitorName);
                ChessGame.TeamColor teamColor =
                        myColor.equals("WHITE") ?ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;

                server.joinGame(new RR.JoinGameRequest(gameId, myColor, auth));
                localGame = gameList.get(inputId).game();
                render = new ChessBoardRenderer(localGame.getBoard(), teamColor);
                render.drawBoard();
                return String.format("%s, you have joined in!", visitorName);

            } catch (NumberFormatException e) {
                throw new ResponseException(ResponseException.Code.BadRequest, "Expected a integer");
            }  catch (IndexOutOfBoundsException e){
                throw new ResponseException(ResponseException.Code.BadRequest, "Out of range");
            }catch (NullPointerException e){
                throw new ResponseException(ResponseException.Code.BadRequest, "Game list is empty /" +
                        " Game can not be joined");
            }
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected: join <ID> [WHITE|BLACK]");
    }



    private String observe(String... params) throws ResponseException {
        assertLoggedIn();
        if(params.length == 1){
            try {
            int inputId = Integer.parseInt(params[0]) - 1;

            localGame = gameList.get(inputId).game();
            render = new ChessBoardRenderer(localGame.getBoard(), ChessGame.TeamColor.WHITE);
            render.drawBoard();
            return String.format("%s, you are observing the game now", visitorName);

            } catch (NumberFormatException e) {
                throw new ResponseException(ResponseException.Code.BadRequest, "Expected a integer");
            }  catch (IndexOutOfBoundsException e){
                throw new ResponseException(ResponseException.Code.BadRequest, "Out of range");
            }catch (NullPointerException e){
                throw new ResponseException(ResponseException.Code.BadRequest, "Game list is empty /" +
                        " Game can not be joined");
            }
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected: observe <ID>");
    }

    private String logout(String... params) throws ResponseException {
        assertLoggedIn();
        if(params.length == 0){
            server.logout(new RR.LogoutRequest(auth));
            state = State.Prelogin;
            auth = null;
            return String.format("%s, you have logged out", visitorName);
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected: logout");
    }



    public String help() {
        if (state == State.Prelogin) {
            return """
                    - Login as an existing user:  "login" <USERNAME> <PASSWORD>
                    - Register a new user:  "register" <USERNAME> <PASSWORD> <EMAIL>
                    - Exit the program:  "quit"
                    - Print this message:  "help"
                    """;
        }
        return """
                - create <NAME> - a game
                - list - games
                - join <ID> [WHITE|BLACK] - a game
                - observe <ID> - a game
                - logout - when you are done
                - quit - playing chess
                - help - with possible commands
                """;
    }


    private void printPrompt() {
        //后面用
    }

    private void assertLoggedIn() throws ResponseException {
        if (state == State.Prelogin) {
            throw new ResponseException(ResponseException.Code.Forbidden, "You must login");
        }
    }

}
