/**
 * Created by alexsanchez on 2016-12-20.
 */
public enum Colour {
    BLACK,
    WHITE;

    public Colour getOpposite() {
        switch(this) {
            case BLACK: return WHITE;
            case WHITE: return BLACK;
            default: return null;
        }

    }
}
