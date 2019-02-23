/**
 * Created by alexsanchez on 2016-12-20.
 */

package engine;

public enum PieceType {
    BISHOP,
    KING,
    KNIGHT,
    PAWN,
    ROOK,
    QUEEN;

    public int getValue() {
        switch(this) {
            case BISHOP: return 325;
            case KNIGHT: return 300;
            case KING: return Integer.MAX_VALUE;
            case QUEEN: return 1000;
            case ROOK: return 600;
            case PAWN: return 100;
            default: return 0;
        }
    }
}
