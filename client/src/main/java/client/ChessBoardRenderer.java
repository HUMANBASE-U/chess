package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;


import static ui.EscapeSequences.*;

public class ChessBoardRenderer {
    private ChessBoard board;
    private boolean isWhite;
    private boolean colorFliper;

    public ChessBoardRenderer(ChessBoard board, ChessGame.TeamColor color) {
        this.board = board;
        this.isWhite = (color == ChessGame.TeamColor.WHITE);
        this.colorFliper = true;
    }

    public void drawBoard(){
        System.out.print(ERASE_SCREEN);
        //如果是黑色的，r,c全都
        for(int r=1; r<=8; r++){
            int rankNumber = isWhite? 9 - r : r;
            System.out.print(SET_BG_COLOR_BLACK + rankNumber + RESET_BG_COLOR + RESET_TEXT_COLOR);

            for(int c=1; c<=8; c++){
                //双层for循环意味着每个方块打印一个 bg + target + reset + reset txt

                int projR = isWhite? 9-r : r;
                int projC = isWhite? c : 9-c;
                ChessPiece target = board.getPiece(new ChessPosition(projR, projC));
                //如果是白的，那第一个绘制的left bottom背景是深色的，这样白棋的queen正好是light色
                String bg = colorFliper?  SET_BG_COLOR_LIGHT_GREY : SET_BG_COLOR_DARK_GREEN;
                String p = setPiece(target);
                colorFliper = !colorFliper;

                System.out.print(bg + p + RESET_BG_COLOR + RESET_TEXT_COLOR);
            }
            colorFliper = !colorFliper;//行与行之间会有两个块同色，这里修复了这个bug
            System.out.println(); //换行
        }

        String topFiles = isWhite
                ? "   a  b   c   d  e   f   g  h"
                : "   h  g   f   e  d   c   b  a";
        System.out.println(topFiles);
    }

    private String setPiece(ChessPiece target) {
        if (target == null) {
            return EMPTY;
        }
        boolean isTargetWhite = target.getTeamColor() == ChessGame.TeamColor.WHITE;

        return switch (target.getPieceType()){
            case KING -> isTargetWhite? WHITE_KING : BLACK_KING;
            case QUEEN -> isTargetWhite? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP -> isTargetWhite? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> isTargetWhite? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK -> isTargetWhite? WHITE_ROOK : BLACK_ROOK;
            case PAWN -> isTargetWhite? WHITE_PAWN : BLACK_PAWN;
        };
    }


}
