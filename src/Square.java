import java.awt.*;

/**
 * Created by alexsanchez on 2016-12-20.
 */
public class Square {

    private int x;
    private int y;
    private Colour colour;

    public Square(int x, int y, Colour colour) {
        this.x = x;
        this.y = y;
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }
    public Dimension getLocation() {
        return new Dimension(x, y);
    }
    public Piece getHeldPiece() {
        if((x == 2 || x == 5) && (y == 0 || y == 7)) {
            Colour colour = Colour.WHITE;
            if (y == 0) colour = Colour.BLACK;
            return new Bishop(x, y, colour);
        }
        return null;
    }
}
