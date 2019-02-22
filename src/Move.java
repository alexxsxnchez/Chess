import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by alexsanchez on 2017-01-06.
 */
public class Move {

    private Piece piece;
    protected Piece capturedPiece;
    private Square initSquare;
    private Square finalSquare;
    private boolean firstTimePieceMoved;
    private int value;
    private boolean hasValue = false;
    private ArrayList<Move> children = new ArrayList<>();

    public Move (Piece piece, Square initSquare, Square finalSquare) {
        this.piece = piece;
        this.initSquare = initSquare;
        this.finalSquare = finalSquare;
        capturedPiece = finalSquare.getHeldPiece();
        firstTimePieceMoved = !piece.getHasMoved();
    }

    public Piece getPiece() {
        return piece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public Square getInitSquare() {
        return initSquare;
    }

    public Square getFinalSquare() {
        return finalSquare;
    }
    public String toString() {
        return piece.getPieceType() + " to " + finalSquare.getPosition().x + finalSquare.getPosition().y;
    }

    public boolean isFirstTimePieceMoved() {
        return firstTimePieceMoved;
    }

    public void setValue(int value) {
        this.value = value;
        hasValue = true;
    }

    public int getValue() {
        return value;
    }

    // ERROR: generate children creates new moves, where when a piece is selected it creates the same moves but different objects
    public void generateChildren(Chessboard chessboard) {
        Colour childrenColour = piece.getPieceColour().getOpposite();
        ArrayList<Piece> pieces = chessboard.getPieces(childrenColour);
        for(int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            piece.clearPossibleMoves();
            piece.findPossibleMoves();
            ArrayList<Move> moves = piece.getPossibleMoves();
            children.addAll(moves);
            piece.clearPossibleMoves();
        }
    }

    public void sortChildren() {
        children.sort(new Comparator<Move>() {
            @Override
            public int compare(Move child1, Move child2) {
                if(!child1.hasValue) {
                    return 1;
                }
                if(!child2.hasValue) {
                    return -1;
                }
                return child2.value - child1.value;
            }
        });
    }

    public ArrayList<Move> getChildren() {
        return children;
    }

    public boolean hasValue() {
        return hasValue;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }


}
