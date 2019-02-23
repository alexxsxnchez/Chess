/**
 * Created by alexsanchez on 2017-01-03.
 */

package engine;

public class King extends Piece {

    public King(Colour pieceColour, Position position) {
        super(PieceType.KING, pieceColour, position);
    }

    public void findPotentialMoves() {
        findNormalMoves();
        findCastlingMoves();
    }

    public void findNormalMoves() {
        //search right
        searchMoves(1, 0);
        //search diagonally down and right
        searchMoves(1, 1);
        //search down
        searchMoves(0, 1);
        //search diagonally down and left
        searchMoves(-1, 1);
        //search left
        searchMoves(-1, 0);
        //search diagonally up and left
        searchMoves(-1, -1);
        //search up
        searchMoves(0, -1);
        //search diagonally up and right
        searchMoves(1, -1);
    }

    private void searchMoves(int xDir, int yDir) {
        int x = getLocation().getX() + xDir;
        int y = getLocation().getY() + yDir;
        if (x >= 0 && x < Chessboard.NUM_OF_SQUARES && y >= 0 && y < Chessboard.NUM_OF_SQUARES) {
            addMoveToPossibleMoves(new Move(this, getLocation(), new PieceLocation(x, y)));
        }
    }

    private void findCastlingMoves() {
        if(getHasMoved() || position.isKingInCheck(getPieceColour()))  {
            return;
        }
        for(int column = 0; column <= 7; column += 7) {
            PieceLocation castlingLocation = getCastlingSquare(column);
            if(castlingLocation != null) {
                PieceLocation rookLocation = new PieceLocation(column, getLocation().getY());
                Piece pieceOnRookLocation = position.getPiece(rookLocation);
                if (pieceOnRookLocation != null && !pieceOnRookLocation.getHasMoved()) {
                    // must be rook on rookLocation
                    addMoveToPossibleMoves(new CastleMove(this, getLocation(), castlingLocation, rookLocation));
                }
            }
        }
    }

    private PieceLocation getCastlingSquare(int column) {
        int x = getLocation().getX();
        // if castling queenside, ensure there is not a piece blocking the way of the rook
        if(column == 0 && position.getPiece(new PieceLocation(1, getLocation().getY())) != null) {
            return null;
        }
        PieceLocation castlingLocation = null;
        for (int i = 0; i < 2; i++) {
            if(column == 0) {
                x--;
            }
            else {
                x++;
            }
            castlingLocation = new PieceLocation(x, getLocation().getY());
            if(position.getPiece(castlingLocation) == null) {
                boolean isValid = stopsCheck(new Move(this, getLocation(), castlingLocation));
                if(!isValid) {
                    return null;
                }
            }
        }
        return castlingLocation;
    }

}
