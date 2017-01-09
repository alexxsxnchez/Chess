/**
 * Created by alexsanchez on 2017-01-06.
 */
public class Move {
    private Piece piece;
    private Piece capturedPiece;
    private Square initSquare;
    private Square finalSquare;
    private boolean firstTimePieceMoved;

    public Move (Piece piece, Square initSquare, Square finalSquare) {
        this.piece = piece;
        this.initSquare = initSquare;
        this.finalSquare = finalSquare;
        capturedPiece = finalSquare.getHeldPiece();
        firstTimePieceMoved = !piece.getHasMoved();

    }

    public Piece getPiece() {
        return piece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public Square getInitSquare() {
        return initSquare;
    }

    public Square getFinalSquare() {
        return finalSquare;
    }

    public boolean isFirstTimePieceMoved() {
        return firstTimePieceMoved;
    }
}
