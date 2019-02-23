/**
 * Created by alexsanchez on 2017-01-09.
 */

package engine;

public class CastleMove extends Move {

    private PieceLocation castlingRookLocation;

    public CastleMove(King king, PieceLocation initLocation, PieceLocation finalLocation, PieceLocation castlingRookLocation) {
        super(king, initLocation, finalLocation);
        this.castlingRookLocation = castlingRookLocation;
    }

    public PieceLocation getCastlingRookLocation() {
        return castlingRookLocation;
    }

    public PieceLocation getCastlingRookEndLocation() {
        int x = (castlingRookLocation.getX() * 2) / 7 + 3;
        return new PieceLocation(x, castlingRookLocation.getY());
    }
}
