/**
 * Created by alexsanchez on 2016-12-20.
 */

package engine;

public class Bishop extends Piece {

    public Bishop(Colour pieceColour, Position position) {
        super(PieceType.BISHOP, pieceColour, position);

    }

    public void findPotentialMoves() {
        //search diagonally up and left
        searchDistanceMoves(-1, -1);
        //search diagonally up and right
        searchDistanceMoves(1, -1);
        //search diagonally down and right
        searchDistanceMoves(1, 1);
        //search diagonally down and left
        searchDistanceMoves(-1, 1);
    }
}
