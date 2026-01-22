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




    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);

        List<String> list_diagonal = new ArrayList<>();
        boolean inside_checker(piece.myPosition){
            //this inside_checker will kill all invalid moves before it slip in to move_lists.

        //boundary check
            if (myPosition.getRow()>=9  |  myPosition.getColumn()>=9 |
                myPosition.getRow()<=0  |  myPosition.getColumn()>=9 ){
                return false;
            }

        //blocked by enemy (arrest me after you add!)
            if (board.getPiece(myPosition)!= null && piece.getTeamColor()!=this.getTeamColor()){
                return false;
            };

        //BLOCKED BY ALLEY (arrest me before you add!)
            if (board.getPiece(myPosition)!= null && piece.getTeamColor()!=this.getTeamColor()){
                return false;
            };

        }

        for (int i=0; i<8; i++){

            for (int j=0; j<8; j++){

                if ChessPiece(i,j)
            }

        }


        if(piece.getPieceType() == PieceType.BISHOP){
            return   List.of(new ChessMove(new ChessPosition(5,4), new ChessPosition(1,8),null));
        }
        return List.of();
    }
}
