package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client {
    public enum State {
        Prelogin,
        Postlogin,
        Ingame
    }

    private final ServerFacade server;
    private final WebSocketFacade socket;
    private final NotificationHandler notificationHandler;
    private State state = State.Prelogin;
    private String visitorName = null;
    private String auth = null;
    private List<GameData> gameList;
    private ChessGame localGame;
    private ChessBoardRenderer render;
    ChessGame.TeamColor teamColor = ChessGame.TeamColor.WHITE;

    private int gameId = -10;

    public Client(ServerFacade server, WebSocketFacade socket, NotificationHandler notificationHandler) {
        this.server = server;
        this.socket = socket;
        this.notificationHandler = notificationHandler;
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
                System.out.print("Error: invalid command\n");

            }
        }
        System.out.println();
    }

    public String eval(String input) {
        try {
            String[] tokens = input.split(" ");
            if (tokens.length == 0) {return "God bless you";}
            String cmd = tokens[0];
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

                //in game
                case "move" -> makeMove(params);
                case "redraw" -> drawBoard();

                case "BAGAYALU" -> {
                    server.clear();
                    yield "deleted";
                }
                default -> help();
            };
        } catch (ResponseException | IOException ex) {
            return ex.getMessage();
        } catch (InterruptedException e) {
            return "interrupted";
        }
    }


    public String drawBoard() throws InterruptedException {
        if(notificationHandler.getGame() != null) {
            //现在假设拿到回执，就开始render
            localGame = notificationHandler.getGame();
            render = new ChessBoardRenderer(localGame.getBoard(), teamColor);
            render.drawBoard();
            state = State.Ingame;
        }return "we don't have a game to draw";
    }

    private String makeMove(String[] params) throws ResponseException, IOException, InterruptedException {
        assertJoined();
        if(params.length == 4){
            String compact = params[0] + params[1] + params[2] + params[3];
            if (!compact.matches("(?i)[a-h][1-8][a-h][1-8]")) {
                throw new ResponseException(ResponseException.Code.BadRequest, "Out of range!");
            }

            ChessPosition startPos = parse(params[0].charAt(0), Integer.parseInt(params[1]));
            ChessPosition endPos = parse(params[2].charAt(0), Integer.parseInt(params[3]));

            socket.makeMove(UserGameCommand.CommandType.MAKE_MOVE,
                    auth,
                    gameId,
                    new ChessMove(startPos, endPos, null));

            drawBoard();
            return "nice move!";
        }
        throw new ResponseException(
                ResponseException.Code.BadRequest, "Invalid!");
    }

    public static ChessPosition parse(char file, int rank) {
        char f = Character.toLowerCase(file);
        int col = f - 'a' + 1;
        int row = rank;
        return new ChessPosition(row, col);
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
                gameId = gameList.get(inputId).gameID();
                String myColor = params[1].toUpperCase();
                if (!myColor.equals("WHITE") && !myColor.equals("BLACK")) {
                    return String.format("%s, Expected: join <ID> [WHITE|BLACK]", visitorName);
                }
                teamColor =
                        myColor.equals("WHITE") ?ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;

                server.joinGame(new RR.JoinGameRequest(gameId, myColor, auth));
                notificationHandler.setGame(null);
                //同时进行Connect
                socket.connect(UserGameCommand.CommandType.CONNECT,
                        auth,
                        gameId);

                drawBoard();
                return String.format("%s, you have joined in!", visitorName);

            } catch (NumberFormatException e) {
                throw new ResponseException(ResponseException.Code.BadRequest, "Expected a integer");
            }  catch (IndexOutOfBoundsException e){
                throw new ResponseException(ResponseException.Code.BadRequest, "Out of range");
            }catch (NullPointerException | IOException e){
                throw new ResponseException(ResponseException.Code.BadRequest, "Game list is empty /" +
                        " Game can not be joined");
            } catch (InterruptedException e) {
                throw new ResponseException(ResponseException.Code.BadRequest, "something went wrong!!");
            }
        }
        throw new ResponseException(ResponseException.Code.BadRequest, "Expected: join <ID> [WHITE|BLACK]");
    }



    private String observe(String... params) throws ResponseException {
        assertLoggedIn();
        if(params.length == 1){
            try {
            int inputId = Integer.parseInt(params[0]) - 1;
            gameId = gameList.get(inputId).gameID();

            //同时进行Connect
            socket.connect(UserGameCommand.CommandType.CONNECT,
                    auth,
                    gameId);


            //现在假设拿到回执，就开始render
            ChessGame returnedGame = notificationHandler.getGame();
            if(returnedGame !=null){
                localGame = returnedGame;
                render = new ChessBoardRenderer(localGame.getBoard(), ChessGame.TeamColor.WHITE);
                render.drawBoard();

            }else {return "something went wrong";}

            state = State.Ingame;
            teamColor = ChessGame.TeamColor.WHITE;
            return String.format("%s, you are observing the game now", visitorName);

            } catch (NumberFormatException e) {
                throw new ResponseException(ResponseException.Code.BadRequest, "Expected a integer");
            }  catch (IndexOutOfBoundsException e){
                throw new ResponseException(ResponseException.Code.BadRequest, "Out of range");
            }catch (NullPointerException | IOException e){
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

        if (state == State.Postlogin) {
            return """
                    - create <NAME> - a game
                    - list - games
                    - join <ID> [WHITE|BLACK] - a game
                    - observe <ID> - a game
                    - logout - when you are done
                    - quit - playing chess
                    - help - with possible commands
                    """;
        }else {
            return """
                    - redraw - game
                    - leave  - you will left the game
                    - move  x x  x x - must have space between input
                    - resign  - you will lose the game
                    - highlight  - to highlight legal moves
                    -
                    - create <NAME> - a game
                    - list - games
                    - join <ID> [WHITE|BLACK] - a game
                    - observe <ID> - a game
                    - logout - when you are done
                    - quit - playing chess
                    - help - with possible commands
                    """;
        }
    }


    private void printPrompt() {
        //后面用
    }

    private void assertLoggedIn() throws ResponseException {
        if (state == State.Prelogin) {
            throw new ResponseException(ResponseException.Code.Forbidden, "You must login");
        }
    }

    private void assertJoined() throws ResponseException {
        if (state != State.Ingame){
            throw new ResponseException(ResponseException.Code.Forbidden, "You must in a game");
        }
    }

}
