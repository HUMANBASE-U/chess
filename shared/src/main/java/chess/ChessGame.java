package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private  ChessBoard board;
    private TeamColor team_turn;
    //////////////////////////castling, do not generate hashcode again/////////////////
    private boolean blackKingMoved;
    private boolean blackLeftRookMoved;
    private boolean blackRightRookMoved;

    private boolean whiteKingMoved;
    private boolean whiteLeftRookMoved;
    private boolean whiteRightRookMoved;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        team_turn = TeamColor.WHITE;
    }

    public boolean leftIsEmpty(int row){
        ChessPiece L1 =board.getPiece( new ChessPosition(row, 2));
        ChessPiece L2 =board.getPiece( new ChessPosition(row, 3));
        ChessPiece L3 =board.getPiece( new ChessPosition(row, 4));
        return (L1==null && L2 == null && L3==null);
    }

    public boolean rightIsEmpty(int row){
        ChessPiece R1 =board.getPiece( new ChessPosition(row, 6));
        ChessPiece R2 =board.getPiece( new ChessPosition(row, 7));
        return (R1==null && R2 == null);
    }
    //这个函数会检查王车之间是不是都是空位


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
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // R O W
        int left_row = startPosition.getRow();
        ChessPiece King = board.getPiece(startPosition);
        ChessPiece leftRook = board.getPiece(new ChessPosition(left_row,1));
        TeamColor color = King.getTeamColor();
        boolean KingMoved = (color == TeamColor.WHITE)? whiteKingMoved:blackKingMoved;
        boolean leftRookMoved = (color == TeamColor.WHITE)? whiteLeftRookMoved:blackLeftRookMoved;

        ///////////////////////////////////////////////////////////////////////////////////////////
        //before detect the validity, let me check if you are a king
        if(target.getPieceType() == ChessPiece.PieceType.KING && !isInCheck(color)){
                if(!KingMoved && !leftRookMoved && leftRook!= null && leftRook.getTeamColor() == color && leftIsEmpty(left_row)){
                    board.addPiece(new ChessPosition(left_row,4), King);   //AAA
                    board.deletePiece(new ChessPosition(left_row,5));// delete old king                                        //
                    if(!isInCheck(color)){
                        board.addPiece(new ChessPosition(left_row, 3), King);                    //BBB
                        board.deletePiece(new ChessPosition(left_row,4));// delete old king         //AAA
                        if(!isInCheck(color)){
                            //if second place is still not threatened
                            //reset the board
                            board.addPiece(startPosition, King);
                            board.deletePiece(new ChessPosition(left_row, 3));// delete old king                                          //BBB
                            posible_move.add(new ChessMove(startPosition, new ChessPosition(left_row,3), null));//add the numberTwo_block move to king_list
                        }else {
                            board.addPiece(startPosition, King);
                            board.deletePiece(new ChessPosition(left_row,3));
                        }
                    }else {
                        board.addPiece(startPosition, King);
                        board.deletePiece(new ChessPosition(left_row,4));
                    }
                }
        }

/// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // R O W
        int right_row = startPosition.getRow();
        ChessPiece rightRook = board.getPiece(new ChessPosition(right_row,8));
        boolean rightRookMoved = (color == TeamColor.WHITE)? whiteRightRookMoved:blackRightRookMoved;

        ///////////////////////////////////////////////////////////////////////////////////////////
        //before detect the validity, let me check if you are a king
        if(target.getPieceType() == ChessPiece.PieceType.KING && !isInCheck(color)){
            if(!KingMoved && !rightRookMoved && rightRook != null && rightRook.getTeamColor() == color && rightIsEmpty(right_row)){
                board.addPiece(new ChessPosition(right_row,6), King);   //AAA
                board.deletePiece(new ChessPosition(right_row,5));// delete old king                                        //
                if(!isInCheck(color)){
                    board.addPiece(new ChessPosition(right_row, 7), King);                    //BBB
                    board.deletePiece(new ChessPosition(right_row,6));// delete old king         //AAA
                    if(!isInCheck(color)){
                        //if second place is still not threatened
                        //reset the board
                        board.addPiece(startPosition, King);
                        board.deletePiece(new ChessPosition(right_row, 7));// delete old king                                          //BBB
                        posible_move.add(new ChessMove(startPosition, new ChessPosition(right_row,7), null));//add the numberTwo_block move to king_list
                    }else {
                        board.addPiece(startPosition, King);
                        board.deletePiece(new ChessPosition(right_row,7));
                    }
                }else {
                    board.addPiece(startPosition, King);
                    board.deletePiece(new ChessPosition(right_row,6));
                }
            }
        }







        for(ChessMove move : posible_move){
            ChessPiece possible_enemy = board.getPiece(move.getEndPosition());

            board.addPiece(move.getEndPosition(),target);
            board.deletePiece(move.getStartPosition());
            if(!isInCheck(target.getTeamColor())) valid_list.add(move);//未改BUG：未升变的兵，虽然是模拟但可能出错
            board.addPiece(move.getStartPosition(),target);// BUG 先加再删
            board.addPiece(move.getEndPosition(), possible_enemy);
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
        ChessPiece target = board.getPiece(move.getStartPosition());
        if(target == null) throw new InvalidMoveException();
        if(team_turn != target.getTeamColor()) throw new InvalidMoveException();
        if(! validMoves(move.getStartPosition()).contains(move)) throw new InvalidMoveException();
        TeamColor color = target.getTeamColor();

        //if nothing wrong, you may make the movement now(if pawn, pay attention)

        if(target.getPieceType() == ChessPiece.PieceType.KING){
            if(color == TeamColor.WHITE){
                whiteKingMoved=true;
            }else blackKingMoved = true;
        }

        if(target.getPieceType() == ChessPiece.PieceType.ROOK && move.getStartPosition().getColumn()==1){
            if(color == TeamColor.WHITE){
                whiteLeftRookMoved=true;
            }else blackLeftRookMoved = true;
        }

        if(target.getPieceType() == ChessPiece.PieceType.ROOK && move.getStartPosition().getColumn()==8){
            if(color == TeamColor.WHITE){
                whiteRightRookMoved=true;
            }else blackRightRookMoved = true;
        }


        if(move.getPromotionPiece()!=null){
            board.addPiece(move.getEndPosition(), new ChessPiece(target.getTeamColor(), move.getPromotionPiece()));
        }else board.addPiece(move.getEndPosition(), target);
        board.deletePiece(move.getStartPosition());
        /// ////////////////////////////////IF CASTLING//////////////////////////////////////////////

        int dif = move.getStartPosition().getColumn()-move.getEndPosition().getColumn();

        if(target.getPieceType() == ChessPiece.PieceType.KING && Math.abs(dif)==2){
            int leftRow = move.getStartPosition().getRow();
        // L E F T

            if(dif > 0) {
                ChessPiece leftROOK = board.getPiece(new ChessPosition(leftRow, 1));

                board.addPiece(new ChessPosition(leftRow, 4), leftROOK);
                board.deletePiece(new ChessPosition(leftRow, 1));

                if(color == TeamColor.WHITE){
                    whiteKingMoved=true;
                    whiteLeftRookMoved=true;
                }else{blackKingMoved=true;
                    blackLeftRookMoved=true;}
            }




        // R I G H T
            if(dif < 0) {
                ChessPiece rightRook = board.getPiece(new ChessPosition(leftRow, 8));

                board.addPiece(new ChessPosition(leftRow, 6), rightRook);
                board.deletePiece(new ChessPosition(leftRow, 8));

                if(color == TeamColor.WHITE){
                    whiteKingMoved=true;
                    whiteRightRookMoved=true;
                }else{blackKingMoved=true;
                    blackRightRookMoved=true;}
            }
        }






        team_turn = target.getTeamColor()==TeamColor.WHITE ?TeamColor.BLACK: TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor enemy_color;
        ChessPosition King_pos = null;//default king_pos
        if(teamColor == TeamColor.BLACK){
            enemy_color = TeamColor.WHITE;
        }else{enemy_color = TeamColor.BLACK;}

//search over the whole board
        for(int i=1; i<=8; i++) {  //已解决BUG：先扫王再扫enemy
            for (int j = 1; j <= 8; j++) {
                ChessPosition search_pos = new ChessPosition(i, j);
                ChessPiece target = board.getPiece(search_pos);
                //Found our king!
                if (target != null && target.getTeamColor() != enemy_color && target.getPieceType() == ChessPiece.PieceType.KING) {
                    King_pos = search_pos;
                }
            }
        }

        for(int i=1; i<=8; i++){
            for(int j=1; j<=8; j++){
                ChessPosition search_pos = new ChessPosition(i, j);
                ChessPiece target = board.getPiece(search_pos);
                // Found the enemy!
                if(target!=null && target.getTeamColor() == enemy_color){
                    for (ChessMove moves:target.pieceMoves(board, search_pos)) {
                            if(moves.getEndPosition().equals(King_pos)) return true;//and king has been threatened
                        }
                    }
                }
            }
        return false;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)) return false;  // have to be in danger first
        for(int i=1; i<=8; i++){
            for(int j=1; j<=8; j++){
                ChessPosition target_pos = new ChessPosition(i, j);
                ChessPiece target = board.getPiece(target_pos);
                if(target!=null && target.getTeamColor()==teamColor && !validMoves(target_pos).isEmpty()) return false;
            }//if you are an alley and you do have some moves , return false;
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)) return false;
        for(int i=1; i<=8; i++){
            for(int j=1; j<=8; j++){
                ChessPosition target_pos = new ChessPosition(i, j);
                ChessPiece target = board.getPiece(target_pos);
                if(target!=null && target.getTeamColor()==teamColor && !validMoves(target_pos).isEmpty()) return false;
            }//if you are an alley and you do have some moves , return false;
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && team_turn == chessGame.team_turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, team_turn);
    }
}
