/**
 * Created by alexsanchez on 2017-01-14.
 */

package engine;

public class EnPassantMove extends Move {

    private PieceLocation capturedPawnLocation;

    public EnPassantMove(Pawn pawn, PieceLocation initLocation, PieceLocation finalLocation, PieceLocation capturedPawnLocation) {
        super(pawn, initLocation, finalLocation);
        this.capturedPawnLocation = capturedPawnLocation;
    }

    public PieceLocation getCapturedPawnLocation() {
        return capturedPawnLocation;
    }

}
