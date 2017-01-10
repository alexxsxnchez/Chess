/**
 * Created by alexsanchez on 2017-01-09.
 */
public class CastleMove extends Move {

    private Rook castlingRook;

    public CastleMove(King king, Square initSquare, Square finalSquare, Rook castlingRook) {
        super(king, initSquare, finalSquare);
        this.castlingRook = castlingRook;
    }

    public Rook getCastlingRook() {
        return castlingRook;
    }
}
