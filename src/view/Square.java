/**
 * Created by alexsanchez on 2016-12-20.
 */

package view;
import engine.Colour;
import java.awt.*;

public class Square {

    private Point position;
    private Colour colour;
    private Color borderColour = null;
    private Color highlightColour = null;

    public Square(int x, int y, Colour colour) {
        position = new Point(x, y);
        this.colour = colour;
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

    public void isClicked(boolean isClicked) {
        if(isClicked) {
            borderColour = Color.PINK;
        }
        else {
            borderColour = null;
        }

    }
    public void setHighlightColour(Color colour) {
        highlightColour = colour;
    }

    public AlphaComposite getComposite() {
        int rule = AlphaComposite.SRC_OVER;
        float alpha = 0.4f;
        return(AlphaComposite.getInstance(rule, alpha));
    }

    public Color getHighlightColour() { return highlightColour; }

    public Colour getColour() {
        return colour;
    }

    public Point getPosition() {
        return position;
    }

    public Color getBorderColour() { return borderColour; }

}
