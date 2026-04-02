package websocket.commands;

import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    private final CommandType commandType;

    private final String authToken;

    private final Integer gameID;

    private final ChessMove move;

    public UserGameCommand(CommandType commandType, String authToken, Integer gameID, ChessMove move) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.move = move;
    }

    public UserGameCommand(CommandType commandType, String authToken, Integer gameID) {
        this(commandType, authToken, gameID, null);
    }


    public enum CommandType {
        CONNECT,  //这里我要
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }


    //注意这个hash我都改过了
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserGameCommand command = (UserGameCommand) o;
        return commandType == command.commandType && Objects.equals(authToken, command.authToken) && Objects.equals(gameID, command.gameID) && Objects.equals(move, command.move);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandType, authToken, gameID, move);
    }
}
