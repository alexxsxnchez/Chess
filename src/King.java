import java.util.ArrayList;

/**
 * Created by alexsanchez on 2017-01-03.
 */
public class King extends Piece {

    public King(Colour pieceColour, Chessboard chessboard) {
        super(PieceType.KING, pieceColour, chessboard);

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
        Square targetSquare;
        int x = position.x + xDir;
        int y = position.y + yDir;
        if (x >= 0 && x < Chessboard.NUM_OF_SQUARES && y >= 0 && y < Chessboard.NUM_OF_SQUARES) {
            targetSquare = chessboard.getSquare(x, y);
            addMoveToPossibleMoves(new Move(this, getSquare(), targetSquare));
        }
    }

    private void findCastlingMoves() {
        if(hasMoved || isInCheck())  {
            return;
        }
        for(int column = 0; column <= 7; column += 7) {
            Square castlingSquare = getCastlingSquare(column);
            if(castlingSquare != null) {
                Piece pieceOnRookSquare = chessboard.getSquare(column, position.y).getHeldPiece();
                if (pieceOnRookSquare != null && !pieceOnRookSquare.getHasMoved()) {
                    Rook rook = (Rook) pieceOnRookSquare;
                    addMoveToPossibleMoves(new CastleMove(this, getSquare(), castlingSquare, rook));
                }
            }
        }
    }

    private Square getCastlingSquare(int column) {
        int x = position.x;
        // if castling queenside, ensure there is not a piece blocking the way of the rook
        if(column == 0 && chessboard.getSquare(1, position.y).getHeldPiece() != null) {
            return null;
        }
        Square castlingSquare = null;
        for (int i = 0; i < 2; i++) {
            if(column == 0) {
                x--;
            }
            else {
                x++;
            }
            castlingSquare = chessboard.getSquare(x, position.y);
            if(castlingSquare.getHeldPiece() == null) {
                chessboard.makeMove(new Move(this, getSquare(), castlingSquare));
                boolean isInvalid = isInCheck();
                chessboard.undoMove();
                if(isInvalid) {
                    return null;
                }
            }
        }
        return castlingSquare;
    }

    public boolean isInCheck() {
        ArrayList<Move> enemyMoves = new ArrayList<>();
        for(Piece enemyPiece : chessboard.getPieces(pieceColour.getOpposite())) {
            enemyPiece.clearPossibleMoves();
            if(enemyPiece instanceof King) {
                King enemyKing = (King) enemyPiece;
                enemyKing.findNormalMoves(); // do not search for castling moves
            }
            else {
                enemyPiece.findPotentialMoves(); // *NOT* findPossibleMoves
            }
            enemyMoves.addAll(enemyPiece.getPossibleMoves());
            enemyPiece.clearPossibleMoves();
        }
        for(Move enemyMove : enemyMoves) {
            if(enemyMove.getCapturedPiece() == this) {
                return true;
            }
        }
        return false;
    }
}
