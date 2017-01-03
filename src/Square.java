import java.awt.*;

/**
 * Created by alexsanchez on 2016-12-20.
 */
public class Square {

    private Point position;
    private Colour colour;
    private Piece heldPiece = null;
    private Color borderColour = null;

    public Square(int x, int y, Colour colour) {
        position = new Point(x, y);
        this.colour = colour;
    }
    public void setHeldPiece(Piece piece) {
        heldPiece = piece;
    }
    public void setIsFocused(boolean isFocused) {
        Color borderColour;
        if(isFocused) {
            borderColour = Color.DARK_GRAY;
        }
        else {
            borderColour = null;
        }
        this.borderColour = borderColour;
    }
    public Colour getColour() {
        return colour;
    }
    public Point getPosition() {
        return position;
    }
    public Piece getHeldPiece() {
        return heldPiece;
    }
    public Color getBorderColour() { return borderColour; }
}
