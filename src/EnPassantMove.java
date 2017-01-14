/**
 * Created by alexsanchez on 2017-01-14.
 */
public class EnPassantMove extends Move {

    private Square capturedPawnSquare;

    public EnPassantMove(Pawn pawn, Square initSquare, Square finalSquare, Pawn capturedPawn) {
        super(pawn, initSquare, finalSquare);
        capturedPiece = capturedPawn;
        capturedPawnSquare = capturedPawn.getSquare();
    }

    public Square getCapturedPawnSquare() {
        return capturedPawnSquare;
    }
}
