/**
 * Created by alexsanchez on 2017-01-03.
 */

package engine;

public class Rook extends Piece {

    public Rook(Colour pieceColour, Position position) {
        super(PieceType.ROOK, pieceColour, position);

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
