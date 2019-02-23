/**
 * Created by alexsanchez on 2019-02-22.
 */

package view;
import engine.Colour;
import engine.PieceType;

import javax.swing.*;
import java.awt.*;

public class Piece {

    private int x;
    private int y;
    private boolean isSelectable;
    private PieceType pieceType;
    private Colour pieceColour;
    private Image image = null;

    Piece(int x, int y, PieceType pieceType, Colour pieceColour) {
        this.x = x;
        this.y = y;
        this.pieceColour = pieceColour;
        this.pieceType = pieceType;
        setImage();
    }

    private void setImage() {
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
            image = new ImageIcon(this.getClass().getResource("../Resources/" + fileName)).getImage();
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error, there was a problem with getting the image.");
        }
    }

    public Image getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) { // needed for castling
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public Colour getPieceColour() {
        return pieceColour;
    }

    public void setSelectable(boolean isSelectable) {
        this.isSelectable = isSelectable;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

}
