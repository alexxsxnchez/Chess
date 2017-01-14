import java.util.ArrayList;

/**
 * Created by alexsanchez on 2017-01-03.
 */
public class Human extends Player {
    public Human (Colour colour) {
        super(colour, PlayerType.HUMAN);
    }

    public void makeMove() {
        ArrayList<Piece> allPieces = chessboard.getPieces();
        for (Piece piece : allPieces) {
            piece.setSelectable(colour == piece.getPieceColour());
        }
    }
}
