/**
 * Created by alexsanchez on 2017-01-03.
 */

package engine;

public class Pawn extends Piece {

    private Move lastMoveMade;

    public Pawn(Colour pieceColour, Position position) {
        super(PieceType.PAWN, pieceColour, position);
    }

    public void setLastMoveMade(Move move) {
        this.lastMoveMade = move; // for en passant
    }

    public void findPotentialMoves() {
        int dir = 1;
        if(getPieceColour() == Colour.WHITE) {
            dir = -1;
        }

        searchNormalMoves(dir);
        searchAttackingMoves(dir);
    }

    private void searchNormalMoves(int dir) {
        PieceLocation targetLocation = new PieceLocation(getLocation().getX(), getLocation().getY() + dir);
        if(position.getPiece(targetLocation) == null) {
            // if pawn is in a position to be promoted
            Move move;

            if(targetLocation.getY() == 3.5 + (3.5 * dir)) {
                move = new PromotionMove(this, getLocation(), targetLocation);
            }
            else {
                move = new Move(this, getLocation(), targetLocation);
            }
            addMoveToPossibleMoves(move);

            if(!getHasMoved()) {
                targetLocation = new PieceLocation(getLocation().getX(), getLocation().getY() + dir * 2);
                if(position.getPiece(targetLocation) == null) {
                    addMoveToPossibleMoves(new Move(this, getLocation(), targetLocation));
                }
            }
        }
    }

    private void searchAttackingMoves(int dir) {
        searchEnPassant(dir);
        for(int i = -1; i <= 1; i += 2) {
            int x = getLocation().getX() + i;
            if(x >= 0 && x < Chessboard.NUM_OF_SQUARES) {
                int y = getLocation().getY() + dir;
                Move move;
                PieceLocation targetLocation = new PieceLocation(x, y);
                if(y == 3.5 + (3.5 * dir)) {
                    move = new PromotionMove(this, getLocation(), targetLocation);
                }
                else {
                    move = new Move(this, getLocation(), targetLocation);
                }
                addAttackableSquareToPossibleMoves(move);
            }
        }
    }

    private void addAttackableSquareToPossibleMoves(Move move) {
        Piece finalSquarePiece = position.getPiece(move.getFinalLocation());
        if(finalSquarePiece != null && finalSquarePiece.getPieceColour() != getPieceColour()) {
            possibleMoves.add(move);
        }
    }

    private void searchEnPassant(int dir) {
        if(lastMoveMade == null) {
            return;
        }
        Piece enemyPiece = lastMoveMade.getPiece();

        if(enemyPiece instanceof Pawn && lastMoveMade.isFirstTimePieceMoved()) {

            int xDiff = Math.abs(getLocation().getX() - lastMoveMade.getFinalLocation().getX());
            int yDiff = Math.abs(getLocation().getY() - lastMoveMade.getFinalLocation().getY());

            if (xDiff == 1 && yDiff == 0 && (getLocation().getY() == 3 || getLocation().getY() == 4)) {
                PieceLocation finalLocation = new PieceLocation(lastMoveMade.getFinalLocation().getX(), lastMoveMade.getFinalLocation().getY() + dir);
                if (position.getPiece(finalLocation) == null) {
                    possibleMoves.add(new EnPassantMove(this, getLocation(), finalLocation, lastMoveMade.getFinalLocation()));
                }
            }
        }
    }

}
