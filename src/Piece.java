import javax.swing.*;
import java.awt.*;

/**
 * Created by alexsanchez on 2016-12-20.
 */
public abstract class Piece {

    protected Point position = new Point();
    protected Colour pieceColour;
    protected PieceType pieceType;
    protected Chessboard chessboard;
    private Image image = null;

    public Piece(PieceType pieceType, Colour pieceColour, Chessboard chessboard) {
        this.pieceColour = pieceColour;
        this.pieceType = pieceType;
        this.chessboard = chessboard;
        storeImage();
    }

    private void storeImage() {
        String fileName = "";
        switch(pieceType) {
            case BISHOP:
                if (pieceColour == Colour.BLACK) {
                    fileName = "blackbishop.png";
                }
                else {
                    fileName = "whitebishop.png";
                }
                break;
            case KING:
                if (pieceColour == Colour.BLACK) {
                    fileName = "blackking.png";
                }
                else {
                    fileName = "whiteking.png";
                }
                break;
            case KNIGHT:
                if (pieceColour == Colour.BLACK) {
                    fileName = "blackknight.png";
                }
                else {
                    fileName = "whiteknight.png";
                }
                break;
            case PAWN:
                if (pieceColour == Colour.BLACK) {
                    fileName = "blackpawn.png";
                }
                else {
                    fileName = "whitepawn.png";
                }
                break;
            case ROOK:
                if (pieceColour == Colour.BLACK) {
                    fileName = "blackrook.png";
                }
                else {
                    fileName = "whiterook.png";
                }
                break;
            case QUEEN:
                if (pieceColour == Colour.BLACK) {
                    fileName = "blackqueen.png";
                }
                else {
                    fileName = "whitequeen.png";
                }
                break;
        }
        try {
            image = new ImageIcon(this.getClass().getResource("resources/" + fileName)).getImage();
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error, there was a problem with getting the image.");
        }
    }

    public void setPosition(int x, int y) {
        position.x = x;
        position.y = y;
    }
    public Point getPosition() {
        return position;
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
    public Square getSquare() {
        return chessboard.getSquare(position.x, position.y);
    }
}
