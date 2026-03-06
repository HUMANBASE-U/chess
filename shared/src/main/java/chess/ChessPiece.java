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



    private void addRayMoves(ChessBoard board, ChessPosition startPos, int dRow, int dCol,
                             List<ChessMove> moves) {
        for (int i = 1; i < 8; i++) {
            int r = startPos.getRow() + dRow * i;
            int c = startPos.getColumn() + dCol * i;
            if (r < 1 || r > 8 || c < 1 || c > 8) {
                break;
            }
            ChessPosition pos = new ChessPosition(r, c);
            ChessPiece target = board.getPiece(pos);
            //BLOCKED BY ALLEY (arrest me before you add!)

            if (target != null && target.getTeamColor() == this.getTeamColor()) {
                break;
            }//依旧双车错
            moves.add(new ChessMove(startPos, pos, null));
            //blocked by enemy (arrest me after you add!)
            if (target != null && target.getTeamColor() != this.getTeamColor()) {
                break;
            }
        }
    }

    private List<ChessMove> diagonalMoveHelper(ChessBoard board, int row, int col) {
        List<ChessMove> moves = new ArrayList<>();
        ChessPosition startPos = new ChessPosition(row, col);
        int[][] dirs = {{-1, 1}, {1, 1}, {1, -1}, {-1, -1}};
        for (int[] d : dirs) {
            addRayMoves(board, startPos, d[0], d[1], moves);
        }
        return moves;
    }

    private List<ChessMove> straightMoveHelper(ChessBoard board, int row, int col) {
        List<ChessMove> moves = new ArrayList<>();
        ChessPosition startPos = new ChessPosition(row, col);
        int[][] dirs = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        for (int[] d : dirs) {
            addRayMoves(board, startPos, d[0], d[1], moves);
        }
        return moves;
    }

    private List<ChessMove> knightHelper(ChessBoard board, int row, int col) {
        List<ChessMove> moves = new ArrayList<>();
        ChessPosition startPos = new ChessPosition(row, col);
        int[][] dirs = {
                {-2, 1}, {-1, 2}, {1, 2}, {2, 1},
                {2, -1}, {1, -2}, {-1, -2}, {-2, -1}
        };
        for (int[] d : dirs) {
            int r = row + d[0];
            int c = col + d[1];
            if (r < 1 || r > 8 || c < 1 || c > 8) {
                continue;
            }
            ChessPosition pos = new ChessPosition(r, c);
            ChessPiece target = board.getPiece(pos);
            if (target == null || target.getTeamColor() != this.getTeamColor()) {//注意短路！短路！
                moves.add(new ChessMove(startPos, pos, null));
            }
        }
        return moves;
    }

    private List<ChessMove> kingHelper(ChessBoard board, int row, int col) {
        List<ChessMove> moves = new ArrayList<>();
        ChessPosition startPos = new ChessPosition(row, col);

        int[][] dirs = {
                {-1, 0}, {-1, 1}, {1, 1}, {0, 1},
                {1, 0}, {1, -1}, {0, -1}, {-1, -1}
        };
        for (int[] d : dirs) {
            int r = row + d[0];
            int c = col + d[1];
            if (r < 1 || r > 8 || c < 1 || c > 8) {
                continue;
            }

            ChessPosition pos = new ChessPosition(r, c);
            ChessPiece target = board.getPiece(pos);
            if (target == null || target.getTeamColor() != this.getTeamColor()) {//短 + 对
                moves.add(new ChessMove(startPos, pos, null));
            }
        }
        return moves;
    }

    private void addPawnMove(List<ChessMove> moves, ChessPosition startPos, ChessPosition endPos,
                             boolean promote) {//
        if (promote) {
            moves.add(new ChessMove(startPos, endPos, PieceType.QUEEN));
            moves.add(new ChessMove(startPos, endPos, PieceType.ROOK));
            moves.add(new ChessMove(startPos, endPos, PieceType.BISHOP));
            moves.add(new ChessMove(startPos, endPos, PieceType.KNIGHT));
        } else {
            moves.add(new ChessMove(startPos, endPos, null));
        }
    }

    private List<ChessMove> pawnHelper(ChessBoard board, int row, int col) {
        List<ChessMove> moves = new ArrayList<>();
        ChessPosition startPos = new ChessPosition(row, col);
        boolean isWhite = (this.getTeamColor() == ChessGame.TeamColor.WHITE);
        int dir = isWhite ? 1 : -1;
        int startRow = isWhite ? 2 : 7;
        int promoteRow = isWhite ? 8 : 1;
//1
        int r = row + dir;
        int c = col;
        if (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
            ChessPosition pos = new ChessPosition(r, c);
            ChessPiece target = board.getPiece(pos);
            if (target == null) {
                addPawnMove(moves, startPos, pos, r == promoteRow);
            }
        }
//往前走两步
        if (row == startRow) {
            int r1 = row + dir;
            int r2 = row + 2 * dir;
            if (r2 >= 1 && r2 <= 8) {
                ChessPosition mid = new ChessPosition(r1, col);
                ChessPosition end = new ChessPosition(r2, col);
                if (board.getPiece(mid) == null && board.getPiece(end) == null) {
                    moves.add(new ChessMove(startPos, end, null));
                }
            }
        }
//左吃
        r = row + dir;
        c = col - 1;
        if (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
            ChessPosition pos = new ChessPosition(r, c);
            ChessPiece target = board.getPiece(pos);
            if (target != null && target.getTeamColor() != this.getTeamColor()) {
                addPawnMove(moves, startPos, pos, r == promoteRow);
            }
        }
//右吃
        r = row + dir;
        c = col + 1;
        if (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
            ChessPosition pos = new ChessPosition(r, c);
            ChessPiece target = board.getPiece(pos);
            if (target != null && target.getTeamColor() != this.getTeamColor()) {
                addPawnMove(moves, startPos, pos, r == promoteRow);
            }
        }

        return moves;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        if (piece == null) {
            return List.of();
        }


        if (piece.getPieceType() == PieceType.BISHOP) {
            return diagonalMoveHelper(board, row, col);
        }
        if (piece.getPieceType() == PieceType.QUEEN) {
            List<ChessMove> moves = new ArrayList<>();
            moves.addAll(diagonalMoveHelper(board, row, col));
            moves.addAll(straightMoveHelper(board, row, col));
            return moves;
        }
        if (piece.getPieceType() == PieceType.ROOK) {
            return straightMoveHelper(board, row, col);
        }
        if (piece.getPieceType() == PieceType.KNIGHT) {
            return knightHelper(board, row, col);
        }
        if (piece.getPieceType() == PieceType.KING) {
            return kingHelper(board, row, col);
        }
        if (piece.getPieceType() == PieceType.PAWN) {
            return pawnHelper(board, row, col);
        }

        return List.of();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece piece = (ChessPiece) o;
        return pieceColor == piece.pieceColor && type == piece.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
