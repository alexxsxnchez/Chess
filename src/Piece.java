import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by alexsanchez on 2016-12-20.
 */
public abstract class Piece {

    protected Point position = new Point();
    protected Colour pieceColour;
    protected PieceType pieceType;
    protected Chessboard chessboard;
    private boolean isSelectable = false;
    protected boolean hasMoved = false;
    private Image image = null;
    protected ArrayList<Move> possibleMoves = new ArrayList<>();

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

    public void findPossibleMoves() {
        findPotentialMoves();
        removeImpossibleMoves();
    }
    public abstract void findPotentialMoves();

    public void removeImpossibleMoves() {
        ArrayList<Move> invalidMoves = new ArrayList<>();
        for(Move potentialMove : possibleMoves) {
            if(!stopsCheck(potentialMove)) {
                invalidMoves.add(potentialMove);
            }
        }
        possibleMoves.removeAll(invalidMoves);
    }

    public boolean stopsCheck(Move move) {
        chessboard.makeMove(move);
        King king = chessboard.getKing(pieceColour == Colour.WHITE);
        boolean isValid = !king.isInCheck();
        chessboard.undoMove();
        return isValid;
    }
    /*
     * describes movement for pieces that can move over several squares in a single direction
     * this method is ONLY to be used by the ROOK, QUEEN, and BISHOP
     */
    public void searchDistanceMoves(int xDir, int yDir) {
        int x = position.x + xDir;
        int y = position.y + yDir;
        Square targetSquare;

        while (x >= 0 && x < Chessboard.NUM_OF_SQUARES && y >= 0 && y < Chessboard.NUM_OF_SQUARES) {
            targetSquare = chessboard.getSquare(x, y);
            boolean wasSuccessful = addMoveToPossibleMoves(new Move(this, getSquare(), targetSquare));
            if(!wasSuccessful || targetSquare.getHeldPiece() != null) break;
            x += xDir;
            y += yDir;
        }
    }

    /*
     * add a move to the possibleMoves arraylist
     * this method also returns whether or not appending the move to the list was successful
     */
    public boolean addMoveToPossibleMoves(Move move) {
        Piece finalSquarePiece = move.getCapturedPiece();
        if(finalSquarePiece == null || finalSquarePiece.getPieceColour() != pieceColour) {
            possibleMoves.add(move);
            return true;
        }
        return false;
    }

    public Move getMove(Square finalSquare) {
        if(finalSquare == null) return null;
        for(Move move : possibleMoves) {
            if(move.getFinalSquare() == finalSquare) return move;
        }
        return null;
    }

    public void clearPossibleMoves() {
        possibleMoves.clear();
    }

    public void highlightPossibleSquares(boolean highlightOn) {
        for(Move move : possibleMoves) {
            Square square = move.getFinalSquare();
            if(highlightOn) {
                square.setHighlightColour(Color.PINK);
            }
            else {
                square.setHighlightColour(null);
            }
        }
    }

    public void setPosition(int x, int y) {
        position.x = x;
        position.y = y;
    }
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
    public void setSelectable(boolean isSelectable) {
        this.isSelectable = isSelectable;
    }
    public boolean isSelectable() {
        return isSelectable;
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
    public boolean getHasMoved() {
        return hasMoved;
    }
    public ArrayList<Move> getPossibleMoves() {
        return possibleMoves;
    }
}
