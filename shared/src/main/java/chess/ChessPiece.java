package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


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

    private List<ChessMove> diagonal_move_helper(ChessBoard board, int row, int col) {
        ChessPiece target;
        List<ChessMove> list_diagonal = new ArrayList<>();
        ChessPosition start_pos = new ChessPosition(row, col);
        int R,C;


        //   row-i, col+i
        for (int i = 1; i < 8; i++) {
            R = row - i;
            C = col + i;
            //this inside_checker will kill all invalid moves before it slip in to move_lists.
            //boundary check
            if (R >= 9 || C >= 9 ||
                    R <= 0 || C <= 0) {
                break;
            }
            ChessPosition pos = new ChessPosition(R,C);
            target = board.getPiece(pos);
            //BLOCKED BY ALLEY (arrest me before you add!)
            if (target != null && target.getTeamColor() == this.getTeamColor()) {
                break;
            }
            list_diagonal.add(new ChessMove(start_pos,pos,null));
            //blocked by enemy (arrest me after you add!)
            if (target != null && target.getTeamColor() != this.getTeamColor()) {
                break;
            }
        }

//   row+i, col+i
        for (int i = 1; i < 8; i++) {
            R = row + i;
            C = col + i;
            //this inside_checker will kill all invalid moves before it slip in to move_lists.
            //boundary check
            if (R >= 9 || C >= 9 ||
                    R <= 0 || C <= 0) {
                break;
            }
            ChessPosition pos = new ChessPosition(R,C);
            target = board.getPiece(pos);
            //BLOCKED BY ALLEY (arrest me before you add!)
            if (target != null && target.getTeamColor() == this.getTeamColor()) {
                break;
            }
            list_diagonal.add(new ChessMove(start_pos,pos,null));
            //blocked by enemy (arrest me after you add!)
            if (target != null && target.getTeamColor() != this.getTeamColor()) {
                break;
            }
        }




        //   row-i, col-i
        for (int i = 1; i < 8; i++) {
            R = row - i;
            C = col - i;
            //this inside_checker will kill all invalid moves before it slip in to move_lists.
            //boundary check
            if (R >= 9 || C >= 9 ||
                    R <= 0 || C <= 0) {
                break;
            }
            ChessPosition pos = new ChessPosition(R,C);
            target = board.getPiece(pos);
            //BLOCKED BY ALLEY (arrest me before you add!)
            if (target != null && target.getTeamColor() == this.getTeamColor()) {
                break;
            }
            list_diagonal.add(new ChessMove(start_pos,pos,null));
            //blocked by enemy (arrest me after you add!)
            if (target != null && target.getTeamColor() != this.getTeamColor()) {
                break;
            }
        }



//   row+i, col-i
        for (int i = 1; i < 8; i++) {
            R = row + i;
            C = col - i;
            //this inside_checker will kill all invalid moves before it slip in to move_lists.
            //boundary check
            if (R >= 9 || C >= 9 ||
                    R <= 0 || C <= 0) {
                break;
            }
            ChessPosition pos = new ChessPosition(R,C);
            target = board.getPiece(pos);
            //BLOCKED BY ALLEY (arrest me before you add!)
            if (target != null && target.getTeamColor() == this.getTeamColor()) {
                break;
            }
            list_diagonal.add(new ChessMove(start_pos,pos,null));
            //blocked by enemy (arrest me after you add!)
            if (target != null && target.getTeamColor() != this.getTeamColor()) {
                break;
            }
        }

        return list_diagonal;
    }





    private List<ChessMove> straight_move_helper(ChessBoard board, int row, int col) {
        ChessPiece target;
        List<ChessMove> list_straight = new ArrayList<>();
        ChessPosition start_pos = new ChessPosition(row, col);
        int R,C;

        //   row-i, col+0
        for (int i = 1; i < 8; i++) {
            R = row - i;
            C = col;
            //this inside_checker will kill all invalid moves before it slip in to move_lists.
            //boundary check
            if (R >= 9 || C >= 9 ||
                    R <= 0 || C <= 0) {
                break;
            }
            ChessPosition pos = new ChessPosition(R,C);
            target = board.getPiece(pos);
            //BLOCKED BY ALLEY (arrest me before you add!)
            if (target != null && target.getTeamColor() == this.getTeamColor()) {
                break;
            }
            list_straight.add(new ChessMove(start_pos,pos,null));
            //blocked by enemy (arrest me after you add!)
            if (target != null && target.getTeamColor() != this.getTeamColor()) {
                break;
            }
        }

//   row, col+i
        for (int i = 1; i < 8; i++) {
            R = row;
            C = col + i;
            //this inside_checker will kill all invalid moves before it slip in to move_lists.
            //boundary check
            if (R >= 9 || C >= 9 ||
                    R <= 0 || C <= 0) {
                break;
            }
            ChessPosition pos = new ChessPosition(R,C);
            target = board.getPiece(pos);
            //BLOCKED BY ALLEY (arrest me before you add!)
            if (target != null && target.getTeamColor() == this.getTeamColor()) {
                break;
            }
            list_straight.add(new ChessMove(start_pos,pos,null));
            //blocked by enemy (arrest me after you add!)
            if (target != null && target.getTeamColor() != this.getTeamColor()) {
                break;
            }
        }

        //   row+i, col
        for (int i = 1; i < 8; i++) {
            R = row + i;
            C = col;
            //this inside_checker will kill all invalid moves before it slip in to move_lists.
            //boundary check
            if (R >= 9 || C >= 9 ||
                    R <= 0 || C <= 0) {
                break;
            }
            ChessPosition pos = new ChessPosition(R,C);
            target = board.getPiece(pos);
            //BLOCKED BY ALLEY (arrest me before you add!)
            if (target != null && target.getTeamColor() == this.getTeamColor()) {
                break;
            }
            list_straight.add(new ChessMove(start_pos,pos,null));
            //blocked by enemy (arrest me after you add!)
            if (target != null && target.getTeamColor() != this.getTeamColor()) {
                break;
            }
        }



//   row, col-i
        for (int i = 1; i < 8; i++) {
            R = row;
            C = col - i;
            //this inside_checker will kill all invalid moves before it slip in to move_lists.
            //boundary check
            if (R >= 9 || C >= 9 ||
                    R <= 0 || C <= 0) {
                break;
            }
            ChessPosition pos = new ChessPosition(R,C);
            target = board.getPiece(pos);
            //BLOCKED BY ALLEY (arrest me before you add!)
            if (target != null && target.getTeamColor() == this.getTeamColor()) {
                break;
            }
            list_straight.add(new ChessMove(start_pos,pos,null));
            //blocked by enemy (arrest me after you add!)
            if (target != null && target.getTeamColor() != this.getTeamColor()) {
                break;
            }
        }

        return list_straight;
    }





//////////////////////////////////UNDER CONSTRUCTION//////////////////////////////////////////////
    private List<ChessMove> KNIGHT_HELPER(ChessBoard board, int row, int col){
    List<ChessMove> LIST_KNIGHT= new ArrayList<>();
        ChessPosition start_pos = new ChessPosition(row,col);

        int R,C;
        R=row-1;
        C=col+2;
        if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
            ChessPosition pos = new ChessPosition(R,C);
            ChessPiece target = board.getPiece(pos);
            if(target==null || target.getTeamColor() != this.getTeamColor()) {
                ChessMove MOVE= new ChessMove(start_pos,pos,null);
                LIST_KNIGHT.add(MOVE);
            }
        }
        R=row+1;
        C=col+2;
        if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
            ChessPosition pos = new ChessPosition(R,C);
            ChessPiece target = board.getPiece(pos);
            if(target==null || target.getTeamColor() != this.getTeamColor()) {
                ChessMove MOVE= new ChessMove(start_pos,pos,null);
                LIST_KNIGHT.add(MOVE);
            }
        }
        R=row-2;
        C=col+1;
        if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
            ChessPosition pos = new ChessPosition(R,C);
            ChessPiece target = board.getPiece(pos);
            if(target==null || target.getTeamColor() != this.getTeamColor()) {
                ChessMove MOVE= new ChessMove(start_pos,pos,null);
                LIST_KNIGHT.add(MOVE);
            }
        }
        R=row+2;
        C=col+1;
        if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
            ChessPosition pos = new ChessPosition(R,C);
            ChessPiece target = board.getPiece(pos);
            if(target==null || target.getTeamColor() != this.getTeamColor()) {
                ChessMove MOVE= new ChessMove(start_pos,pos,null);
                LIST_KNIGHT.add(MOVE);
            }
        }
        R=row-2;
        C=col-1;
        if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
            ChessPosition pos = new ChessPosition(R,C);
            ChessPiece target = board.getPiece(pos);
            if(target==null || target.getTeamColor() != this.getTeamColor()) {
                ChessMove MOVE= new ChessMove(start_pos,pos,null);
                LIST_KNIGHT.add(MOVE);
            }
        }
        R=row+2;
        C=col-1;
        if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
            ChessPosition pos = new ChessPosition(R,C);
            ChessPiece target = board.getPiece(pos);
            if(target==null || target.getTeamColor() != this.getTeamColor()) {
                ChessMove MOVE= new ChessMove(start_pos,pos,null);
                LIST_KNIGHT.add(MOVE);
            }
        }
        R=row-1;
        C=col-2;
        if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
            ChessPosition pos = new ChessPosition(R,C);
            ChessPiece target = board.getPiece(pos);
            if(target==null || target.getTeamColor() != this.getTeamColor()) {
                ChessMove MOVE= new ChessMove(start_pos,pos,null);
                LIST_KNIGHT.add(MOVE);
            }
        }
        R=row+1;
        C=col-2;
        if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
            ChessPosition pos = new ChessPosition(R,C);
            ChessPiece target = board.getPiece(pos);
            if(target==null || target.getTeamColor() != this.getTeamColor()) {
                ChessMove MOVE= new ChessMove(start_pos,pos,null);
                LIST_KNIGHT.add(MOVE);
            }
        }
        return LIST_KNIGHT;
///////////////////////////////////////////////////////////////////////////////////////////////////

}





//////////////////////////////////UNDER CONSTRUCTION//////////////////////////////////////////////
private List<ChessMove> KING_HELPER(ChessBoard board, int row, int col){
    List<ChessMove> KING_LIST= new ArrayList<>();
    ChessPosition start_pos = new ChessPosition(row,col);
    int R,C;

    R=row-1;
    C=col+1;
    if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
        ChessPosition pos = new ChessPosition(R,C);
        ChessPiece target = board.getPiece(pos);
        ChessMove KING_MOVE=new ChessMove(start_pos,pos,null);

        if(target==null || target.getTeamColor()!=this.getTeamColor()) {
            KING_LIST.add(KING_MOVE);
        }
    }

    R=row+0;
    C=col+1;
    if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
        ChessPosition pos = new ChessPosition(R,C);
        ChessPiece target = board.getPiece(pos);
        ChessMove KING_MOVE=new ChessMove(start_pos,pos,null);

        if(target==null || target.getTeamColor()!=this.getTeamColor()) {
            KING_LIST.add(KING_MOVE);
        }
    }
    R=row+1;
    C=col+1;
    if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
        ChessPosition pos = new ChessPosition(R,C);
        ChessPiece target = board.getPiece(pos);
        ChessMove KING_MOVE=new ChessMove(start_pos,pos,null);

        if(target==null || target.getTeamColor()!=this.getTeamColor()) {
            KING_LIST.add(KING_MOVE);
        }
    }
    R=row-1;
    C=col+0;
    if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
        ChessPosition pos = new ChessPosition(R,C);
        ChessPiece target = board.getPiece(pos);
        ChessMove KING_MOVE=new ChessMove(start_pos,pos,null);

        if(target==null || target.getTeamColor()!=this.getTeamColor()) {
            KING_LIST.add(KING_MOVE);
        }
    }
    R=row+1;
    C=col+0;
    if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
        ChessPosition pos = new ChessPosition(R,C);
        ChessPiece target = board.getPiece(pos);
        ChessMove KING_MOVE=new ChessMove(start_pos,pos,null);

        if(target==null || target.getTeamColor()!=this.getTeamColor()) {
            KING_LIST.add(KING_MOVE);
        }
    }
    R=row-1;
    C=col-1;

    if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
        ChessPosition pos = new ChessPosition(R,C);
        ChessPiece target = board.getPiece(pos);
        ChessMove KING_MOVE=new ChessMove(start_pos,pos,null);

        if(target==null || target.getTeamColor()!=this.getTeamColor()) {
            KING_LIST.add(KING_MOVE);
        }
    }
    R=row+0;
    C=col-1;
    if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
        ChessPosition pos = new ChessPosition(R,C);
        ChessPiece target = board.getPiece(pos);
        ChessMove KING_MOVE=new ChessMove(start_pos,pos,null);

        if(target==null || target.getTeamColor()!=this.getTeamColor()) {
            KING_LIST.add(KING_MOVE);
        }
    }
    R=row+1;
    C=col-1;
    if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
        ChessPosition pos = new ChessPosition(R,C);
        ChessPiece target = board.getPiece(pos);
        ChessMove KING_MOVE=new ChessMove(start_pos,pos,null);

        if(target==null || target.getTeamColor()!=this.getTeamColor()) {
            KING_LIST.add(KING_MOVE);
        }
    }
    return KING_LIST;
}

private List<ChessMove> Castling_HELPER(ChessBoard board, int row, int col){
        List<ChessMove> KING_LIST= new ArrayList<>();
        ChessPosition start_pos = new ChessPosition(row,col);
        int R,C;

        R=row-1;
        C=col+1;
        if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
            ChessPosition pos = new ChessPosition(R,C);
            ChessPiece target = board.getPiece(pos);
            ChessMove KING_MOVE=new ChessMove(start_pos,pos,null);

            if(target==null || target.getTeamColor()!=this.getTeamColor()) {
                KING_LIST.add(KING_MOVE);
            }
        }

        R=row+0;
        C=col+1;
        if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
            ChessPosition pos = new ChessPosition(R,C);
            ChessPiece target = board.getPiece(pos);
            ChessMove KING_MOVE=new ChessMove(start_pos,pos,null);

            if(target==null || target.getTeamColor()!=this.getTeamColor()) {
                KING_LIST.add(KING_MOVE);
            }
        }
        R=row+1;
        C=col+1;
        if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
            ChessPosition pos = new ChessPosition(R,C);
            ChessPiece target = board.getPiece(pos);
            ChessMove KING_MOVE=new ChessMove(start_pos,pos,null);

            if(target==null || target.getTeamColor()!=this.getTeamColor()) {
                KING_LIST.add(KING_MOVE);
            }
        }
        R=row-1;
        C=col+0;
        if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
            ChessPosition pos = new ChessPosition(R,C);
            ChessPiece target = board.getPiece(pos);
            ChessMove KING_MOVE=new ChessMove(start_pos,pos,null);

            if(target==null || target.getTeamColor()!=this.getTeamColor()) {
                KING_LIST.add(KING_MOVE);
            }
        }
        R=row+1;
        C=col+0;
        if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
            ChessPosition pos = new ChessPosition(R,C);
            ChessPiece target = board.getPiece(pos);
            ChessMove KING_MOVE=new ChessMove(start_pos,pos,null);

            if(target==null || target.getTeamColor()!=this.getTeamColor()) {
                KING_LIST.add(KING_MOVE);
            }
        }
        R=row-1;
        C=col-1;

        if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
            ChessPosition pos = new ChessPosition(R,C);
            ChessPiece target = board.getPiece(pos);
            ChessMove KING_MOVE=new ChessMove(start_pos,pos,null);

            if(target==null || target.getTeamColor()!=this.getTeamColor()) {
                KING_LIST.add(KING_MOVE);
            }
        }
        R=row+0;
        C=col-1;
        if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
            ChessPosition pos = new ChessPosition(R,C);
            ChessPiece target = board.getPiece(pos);
            ChessMove KING_MOVE=new ChessMove(start_pos,pos,null);

            if(target==null || target.getTeamColor()!=this.getTeamColor()) {
                KING_LIST.add(KING_MOVE);
            }
        }
        R=row+1;
        C=col-1;
        if(!(R >= 9 || C >= 9 || R <= 0 || C <= 0)){
            ChessPosition pos = new ChessPosition(R,C);
            ChessPiece target = board.getPiece(pos);
            ChessMove KING_MOVE=new ChessMove(start_pos,pos,null);

            if(target==null || target.getTeamColor()!=this.getTeamColor()) {
                KING_LIST.add(KING_MOVE);
            }
        }
        return KING_LIST;
    }



/// /////////////////////////////////////////////////////////////////////////////////////////////////
private List<ChessMove> PAWN_HELPER(ChessBoard board, int row, int col) {
    List<ChessMove> LIST_PAWN = new ArrayList<>();
    ChessPosition start_pos = new ChessPosition(row, col);

    boolean IS_WHITE = (this.getTeamColor() == ChessGame.TeamColor.WHITE);
    //oppsite site
    int DIR = IS_WHITE ? 1 : -1;
    int START_ROW = IS_WHITE ? 2 : 7;
    int PROMOTE_ROW = IS_WHITE ? 8 : 1;

    int R, C;

    R = row + DIR;
    C = col;
    if (!(R >= 9 || C >= 9 || R <= 0 || C <= 0)) {
        ChessPosition pos = new ChessPosition(R, C);
        ChessPiece target = board.getPiece(pos);
        if (target == null) {//
            if (R == PROMOTE_ROW) {
                LIST_PAWN.add(new ChessMove(start_pos, pos, PieceType.QUEEN));
                LIST_PAWN.add(new ChessMove(start_pos, pos, PieceType.ROOK));
                LIST_PAWN.add(new ChessMove(start_pos, pos, PieceType.BISHOP));
                LIST_PAWN.add(new ChessMove(start_pos, pos, PieceType.KNIGHT));
            } else {
                LIST_PAWN.add(new ChessMove(start_pos, pos, null));
            }
        }
    }

    if (row == START_ROW) {
        int R1 = row + DIR;
        int R2 = row + 2 * DIR;
        if (!(R2 >= 9 || col >= 9 || R2 <= 0 || col <= 0)) {
            ChessPosition mid = new ChessPosition(R1, col);
            ChessPosition end = new ChessPosition(R2, col);
            if (board.getPiece(mid) == null && board.getPiece(end) == null) {
                LIST_PAWN.add(new ChessMove(start_pos, end, null));
            }
        }
    }


//eat
    R = row + DIR;
    C = col - 1;
    if (!(R >= 9 || C >= 9 || R <= 0 || C <= 0)) {
        ChessPosition pos = new ChessPosition(R, C);
        ChessPiece target = board.getPiece(pos);
        if (target != null && target.getTeamColor() != this.getTeamColor()) {
            if (R == PROMOTE_ROW) {
                LIST_PAWN.add(new ChessMove(start_pos, pos, PieceType.QUEEN));
                LIST_PAWN.add(new ChessMove(start_pos, pos, PieceType.ROOK));
                LIST_PAWN.add(new ChessMove(start_pos, pos, PieceType.BISHOP));
                LIST_PAWN.add(new ChessMove(start_pos, pos, PieceType.KNIGHT));
            } else {
                LIST_PAWN.add(new ChessMove(start_pos, pos, null));
            }
        }
    }


//eat
    R = row + DIR;
    C = col + 1;
    if (!(R >= 9 || C >= 9 || R <= 0 || C <= 0)) {
        ChessPosition pos = new ChessPosition(R, C);
        ChessPiece target = board.getPiece(pos);
        if (target != null && target.getTeamColor() != this.getTeamColor()) {
            if (R == PROMOTE_ROW) {
                LIST_PAWN.add(new ChessMove(start_pos, pos, PieceType.QUEEN));
                LIST_PAWN.add(new ChessMove(start_pos, pos, PieceType.ROOK));
                LIST_PAWN.add(new ChessMove(start_pos, pos, PieceType.BISHOP));
                LIST_PAWN.add(new ChessMove(start_pos, pos, PieceType.KNIGHT));
            } else {
                LIST_PAWN.add(new ChessMove(start_pos, pos, null));
            }
        }
    }

    return LIST_PAWN;
}



        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        if(piece==null){
            return List.of();
        }
        int row =  myPosition.getRow();
        int col = myPosition.getColumn();


        if(piece.getPieceType() == PieceType.BISHOP){
            return   diagonal_move_helper(board, row, col);
        }

        if(piece.getPieceType() == PieceType.QUEEN){
            List<ChessMove> QUEEN_MOVES = new ArrayList<>();
            QUEEN_MOVES.addAll(diagonal_move_helper(board, row, col));
            QUEEN_MOVES.addAll(straight_move_helper(board, row, col));
            return   QUEEN_MOVES;
        }

        if(piece.getPieceType()==PieceType.ROOK){
            return straight_move_helper(board, row, col);
        }


        if(piece.getPieceType() == PieceType.KNIGHT){
            return  KNIGHT_HELPER(board, row, col);
        }


        if(piece.getPieceType() == PieceType.KING){
            return KING_HELPER(board, row, col);
        }


        if(piece.getPieceType() == PieceType.PAWN){
            return PAWN_HELPER(board, row, col);
        }

            return List.of();
    }


    @Override
    public boolean equals(Object o){ /////////////////////////////////////Object VS Objects
        if(this == o) return true;

        if(o == null) return false;

        if(getClass() !=o.getClass()) return false;

        ChessPiece that = (ChessPiece) o;

        return this.pieceColor == that.pieceColor && this.type == that.type;
    }
    @Override
    public int hashCode(){
        return Objects.hash(pieceColor, type);
    }


}


