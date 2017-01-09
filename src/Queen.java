/**
 * Created by alexsanchez on 2017-01-03.
 */
public class Queen extends Piece {

    public Queen(Colour pieceColour, Chessboard chessboard) {
        super(PieceType.QUEEN, pieceColour, chessboard);

    }

    public void findPotentialMoves() {
        //search left
        searchDistanceMoves(-1, 0);
        //search diagonally up and left
        searchDistanceMoves(-1, -1);
        //search up
        searchDistanceMoves(0, -1);
        //search diagonally up and right
        searchDistanceMoves(1, -1);
        //search right
        searchDistanceMoves(1, 0);
        //search diagonally down and right
        searchDistanceMoves(1, 1);
        //search down
        searchDistanceMoves(0, 1);
        //search diagonally down and left
        searchDistanceMoves(-1, 1);
    }
}
