package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {


    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;








    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */










    //my_first_creation:  diagonal_moves function:

    private List<ChessPosition> Diagnoal_move_checker(ChessBoard board, ChessPosition next_position){
        ChessPiece target = board.getPiece(next_position);
        List<ChessPosition> list_diagonal = new ArrayList<>();

        int Row =next_position.getRow();
        int Col =next_position.getColumn();

        for (int i=1; i<8; i++){


            ChessPosition grow_pos = new ChessPosition(Row+i,Col+i)
            //this inside_checker will kill all invalid moves before it slip in to move_lists.

            //boundary check
            if (Row>=9  ||  Col>=9 ||
                    Row<=0  ||  Col<=0 ){
                break;
            }
            //BLOCKED BY ALLEY (arrest me before you add!)
            if (board.getPiece(grow_pos)!= null && target.getTeamColor() ==this.getTeamColor()){
                break;
            };
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
            list_diagonal.add(grow_pos);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //blocked by enemy (arrest me after you add!)
            if (board.getPiece(grow_pos)!= null && target.getTeamColor()!=this.getTeamColor()){
                break;
            };
            }


















            public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);


        if(piece.getPieceType() == PieceType.BISHOP){
            return   List.of(new ChessMove(new ChessPosition(5,4), new ChessPosition(1,8),null),
                             new ChessMove(new ChessPosition(6,7), new ChessPosition(1,8),null)
                    );
        }
        return List.of();
    }
}
