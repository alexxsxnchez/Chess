/**
 * Created by alexsanchez on 2017-01-03.
 */
public class Rook extends Piece {

    public Rook(Colour pieceColour, Chessboard chessboard) {
        super(PieceType.ROOK, pieceColour, chessboard);

    }

    public void findPotentialMoves() {
        //search left
        searchDistanceMoves(-1, 0);
        //search up
        searchDistanceMoves(0, -1);
        //search right
        searchDistanceMoves(1, 0);
        //search down
        searchDistanceMoves(0, 1);
    }
}
