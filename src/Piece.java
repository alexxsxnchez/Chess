import javax.swing.*;
import java.awt.*;

/**
 * Created by alexsanchez on 2016-12-20.
 */
public abstract class Piece {

    protected int x;
    protected int y;
    protected Colour pieceColour;
    protected PieceType pieceType;
    private Image image = null;

    public Piece(int x, int y, Colour pieceColour, PieceType pieceType) {
        this.x = x;
        this.y = y;
        this.pieceColour = pieceColour;
        this.pieceType = pieceType;
        storeImage();
    }

    private void storeImage() {
        String fileName = "";
        switch(pieceType) {
            case BISHOP:
                if (pieceColour == Colour.BLACK) fileName = "blackbishop.png";
                else fileName = "whitebishop.png";
                break;
            case KING:
                if (pieceColour == Colour.BLACK) fileName = "blackking.png";
                else fileName = "whiteking.png";
                break;
            case KNIGHT:
                if (pieceColour == Colour.BLACK) fileName = "blackknight.png";
                else fileName = "whiteknight.png";
                break;
            case PAWN:
                if (pieceColour == Colour.BLACK) fileName = "blackpawn.png";
                else fileName = "whitepawn.png";
                break;
            case ROOK:
                if (pieceColour == Colour.BLACK) fileName = "blackrook.png";
                else fileName = "whiterook.png";
                break;
            case QUEEN:
                if (pieceColour == Colour.BLACK) fileName = "blackqueen.png";
                else fileName = "whitequeen.png";
                break;
        }
        try {
            image = new ImageIcon(this.getClass().getResource("resources/" + fileName)).getImage();
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error, there was a problem with getting the image.");
        }
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Colour getPieceColour() {
        return pieceColour;
    }
    public PieceType getPieceType() {
        return pieceType;
    }
    public Image getImage() {
        return image;
    }

}
