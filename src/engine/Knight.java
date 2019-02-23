/**
 * Created by alexsanchez on 2017-01-03.
 */

package engine;

public class Knight extends Piece {

    public Knight(Colour pieceColour, Position position) {
        super(PieceType.KNIGHT, pieceColour, position);

    }

    public void findPotentialMoves() {
        searchMoves(true);
        searchMoves(false);
    }

    private void searchMoves(boolean searchAbove) {
        int xDiff = -2;
        int x = getLocation().getX() + xDiff;
        int y;

        while(x < Chessboard.NUM_OF_SQUARES && xDiff <= 2) {
            if(x >= 0 && xDiff != 0) {
                y = getY(xDiff, searchAbove);
                if(y != -1) {
                    addMoveToPossibleMoves(new Move(this, getLocation(), new PieceLocation(x, y)));
                }
            }
            xDiff++;
            x++;
        }
    }

    private int getY(int xDiff, boolean searchAbove) {
        int dirMultiplier = 1;
        if(searchAbove) {
            dirMultiplier = -1;
        }
        int y = getLocation().getY();
        if(Math.abs(xDiff) == 1) {
            y += 2 * dirMultiplier;
        }
        else {
            y += dirMultiplier;
        }
        if(y >= 0 && y < Chessboard.NUM_OF_SQUARES) {
            return y;
        }
        else {
            return -1;
        }
    }
}
