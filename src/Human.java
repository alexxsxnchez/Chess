import java.util.ArrayList;

/**
 * Created by alexsanchez on 2017-01-03.
 */
public class Human extends Player {
    public Human (Colour colour, Game game, Chessboard chessboard) {
        super(colour, game, chessboard);
        playerType = PlayerType.HUMAN;
    }

    public void makeMove() {
        ArrayList<Piece> pieces = chessboard.getPieces();
        for (Piece piece : pieces) {
            piece.setSelectable(colour == piece.getPieceColour());
        }
    }
}
