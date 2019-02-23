/**
 * Created by alexsanchez on 2017-01-03.
 */

package engine;

public class Queen extends Piece {

    public Queen(Colour pieceColour, Position position) {
        super(PieceType.QUEEN, pieceColour, position);

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
