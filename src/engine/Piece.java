/**
 * Created by alexsanchez on 2016-12-20.
 */

package engine;
import java.util.ArrayList;

public abstract class Piece {

    private PieceLocation location;
    private Colour pieceColour;
    private PieceType pieceType;
    protected Position position;
    private boolean hasMoved = false;
    protected ArrayList<Move> possibleMoves = new ArrayList<>();

    public Piece(PieceType pieceType, Colour pieceColour, Position position) {
        this.pieceColour = pieceColour;
        this.pieceType = pieceType;
        this.position = position;
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
        //chessboard.makeMove(move);
        Position newPosition = new Position(position, move);

        boolean isValid = !newPosition.isKingInCheck(pieceColour);
        //chessboard.undoMove();
        return isValid;
    }

    /*
     * describes movement for pieces that can move over several squares in a single direction
     * this method is ONLY to be used by the ROOK, QUEEN, and BISHOP
     */
    public void searchDistanceMoves(int xDir, int yDir) {
        int x = location.getX() + xDir;
        int y = location.getY() + yDir;
        PieceLocation targetLocation;

        while (x >= 0 && x < Chessboard.NUM_OF_SQUARES && y >= 0 && y < Chessboard.NUM_OF_SQUARES) {
            targetLocation = new PieceLocation(x, y);
            boolean wasSuccessful = addMoveToPossibleMoves(new Move(this, location, targetLocation));
            if(!wasSuccessful || position.getPiece(targetLocation) != null) {
                break;
            }
            x += xDir;
            y += yDir;
        }
    }

    /*
     * add a move to the possibleMoves arraylist
     * this method also returns whether or not appending the move to the list was successful
     */
    public boolean addMoveToPossibleMoves(Move move) {

        Piece finalSquarePiece = position.getPiece(move.getFinalLocation());
        if(finalSquarePiece == null || finalSquarePiece.getPieceColour() != pieceColour) {
            possibleMoves.add(move);
            return true;
        }
        return false;
    }

    public Move getMove(PieceLocation finalLocation) {
        if(finalLocation == null) {
            return null;
        }
        for(Move move : possibleMoves) {
            if(move.getFinalLocation().equals(finalLocation)) {
                return move;
            }
        }
        return null;
    }

    public void clearPossibleMoves() {
        possibleMoves.clear();
    }

    public void setPosition(PieceLocation location) {
        this.location = location;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public PieceLocation getLocation() {
        return location;
    }

    public Colour getPieceColour() {
        return pieceColour;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    public ArrayList<Move> getPossibleMoves() {
        return possibleMoves;
    }
}
