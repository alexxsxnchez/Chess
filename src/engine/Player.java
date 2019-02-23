/**
 * Created by alexsanchez on 2017-01-03.
 */

package engine;
import view.Presenter;

public abstract class Player {

    protected Chessboard chessboard;
    protected Presenter presenter;
    private Colour colour;
    private PlayerType playerType;

    public Player(Colour colour, PlayerType playerType, Presenter presenter) {
        this.colour = colour;
        this.playerType = playerType;
        this.presenter = presenter;
    }

    public abstract void makeMove();

    public boolean canMove() {
        for(Piece piece : chessboard.getCurrentPosition().getPieces(colour)) {
            piece.clearPossibleMoves();
            piece.findPossibleMoves();
            if(!piece.getPossibleMoves().isEmpty()) {
                piece.clearPossibleMoves();
                return true;
            }
            piece.clearPossibleMoves();
        }
        return false;
    }

    public void setChessboard(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public Colour getColour() {
        return colour;
    }

}
