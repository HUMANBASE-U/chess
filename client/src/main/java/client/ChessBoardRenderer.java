package client;

import chess.ChessPiece;
import ui.EscapeSequences;

public class ChessBoardRenderer {
    private ChessPiece[][] squares;

    public ChessBoardRenderer(ChessPiece[][] squares) {
        this.squares = squares;
    }

    public void drawBoard{
        System.out.print(EscapeSequences.ERASE_SCREEN);

        
    }


}
