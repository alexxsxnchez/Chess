/**
 * Created by alexsanchez on 2017-01-03.
 */
public class King extends Piece {

    public King(Colour pieceColour, Chessboard chessboard) {
        super(PieceType.KING, pieceColour, chessboard);

    }

    public void findPotentialMoves() {
        findNormalMoves();
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
}
