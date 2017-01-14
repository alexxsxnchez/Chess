import java.util.ArrayList;

/**
 * Created by alexsanchez on 2017-01-03.
 */
public abstract class Player {

    protected Chessboard chessboard;
    protected Colour colour;
    private PlayerType playerType;
    private ArrayList<Piece> pieces = new ArrayList<>();
    private King king;

    public Player(Colour colour, PlayerType playerType) {
        this.colour = colour;
        this.playerType = playerType;
    }

    public abstract void makeMove();

    public boolean canMove() {
        for(Piece piece : pieces) {
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

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public void setKing(King king) {
        this.king = king;
    }

    public void setChessboard(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public King getKing() {
        return king;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }
}
