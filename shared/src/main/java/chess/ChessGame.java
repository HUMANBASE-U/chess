package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private  ChessBoard board;
    private TeamColor team_turn;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        team_turn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.team_turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        team_turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece target = board.getPiece(startPosition);
        Collection<ChessMove> posible_move = new ArrayList<>();
        Collection<ChessMove> valid_list = new ArrayList<>();
        if(target == null){
            return List.of();
        }
        posible_move = target.pieceMoves(board, startPosition);
//先 MAKE MOVE下一步
        for(ChessMove move : posible_move){
            if(isInCheck()){
            }else valid_list.add(move);
        }
        return valid_list;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor enemy_color;
        boolean Check = false;
        ChessPosition King_pos;
        List<ChessPosition> threat_map = new ArrayList<>();
        if(teamColor == TeamColor.BLACK){
            enemy_color = TeamColor.WHITE;
        }else{enemy_color = TeamColor.BLACK;}

//search over the whole board
        for(int i=1; i<=8; i++){
            for(int j=1; j<=8; j++){
                ChessPosition search_pos = new ChessPosition(i, j);
                ChessPiece target = board.getPiece(search_pos);
                //Found our king!
                if(target!=null && target.getTeamColor() != enemy_color && target.getPieceType()== ChessPiece.PieceType.KING){
                    King_pos = search_pos;
                }

                // Found the enemy!
                if(target!=null && target.getTeamColor() == enemy_color){
                    for (ChessMove moves:target.pieceMoves(board, search_pos)) {
                        threat_map.add(moves.getEndPosition());
                    }
                }
            }
        }
//flags has been rooted, now lets take a look of king to see if he is safe
        for(ChessPosition flag_position : threat_map)

        return Check;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        throw new RuntimeException("Not implemented");
    }
}
