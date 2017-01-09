import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by alexsanchez on 2016-12-20.
 */
public class Chessboard {

    private Game game;
    public static final int NUM_OF_SQUARES = 8;
    private Square[][] boardSquares = new Square[NUM_OF_SQUARES][NUM_OF_SQUARES];
    private ArrayList<Piece> whitePieces = new ArrayList<>();
    private ArrayList<Piece> blackPieces = new ArrayList<>();
    private Stack<Move> moves = new Stack<>();
    private King whiteKing;
    private King blackKing;


    public Chessboard(Game game) {
        this.game = game;
        createSquares();
        addStartingPieces();
    }
    private void createSquares() {
        for (int i = 0; i < NUM_OF_SQUARES; i++) {
            for (int j = 0; j < NUM_OF_SQUARES; j++) {
                Colour squareColour = Colour.BLACK;
                if ((i + j) % 2 == 0) {
                    squareColour = Colour.WHITE;
                }
                boardSquares[j][i] = new Square(j, i, squareColour);
            }
        }
    }
    private void addStartingPieces() {
            //add pawns
            for (int i = 0; i < 8; i++) {
                addNewPiece(i, 6, Colour.WHITE, PieceType.PAWN);
                addNewPiece(i, 1, Colour.BLACK, PieceType.PAWN);
            }
            //add rooks
            addNewPiece(0, 7, Colour.WHITE, PieceType.ROOK);
            addNewPiece(7, 7, Colour.WHITE, PieceType.ROOK);
            addNewPiece(0, 0, Colour.BLACK, PieceType.ROOK);
            addNewPiece(7, 0, Colour.BLACK, PieceType.ROOK);
            //add knights

            addNewPiece(1, 7, Colour.WHITE, PieceType.KNIGHT);
            addNewPiece(6, 7, Colour.WHITE, PieceType.KNIGHT);
            addNewPiece(1, 0, Colour.BLACK, PieceType.KNIGHT);
            addNewPiece(6, 0, Colour.BLACK, PieceType.KNIGHT);

            //add bishops
            addNewPiece(2, 7, Colour.WHITE, PieceType.BISHOP);
            addNewPiece(5, 7, Colour.WHITE, PieceType.BISHOP);
            addNewPiece(2, 0, Colour.BLACK, PieceType.BISHOP);
            addNewPiece(5, 0, Colour.BLACK, PieceType.BISHOP);

            //add kings
            addNewPiece(4, 7, Colour.WHITE, PieceType.KING);
            addNewPiece(4, 0, Colour.BLACK, PieceType.KING);

            //add queens
            addNewPiece(3, 7, Colour.WHITE, PieceType.QUEEN);
            addNewPiece(3, 0, Colour.BLACK, PieceType.QUEEN);
    }

    public Piece addNewPiece(int x, int y, Colour colour, PieceType pieceType) {
        Piece piece = null;
        switch (pieceType) {
            case KING: {
                piece = new King(colour, this);
                if(colour == Colour.WHITE) whiteKing = (King) piece;
                else blackKing = (King) piece;
                break;
            }
            case QUEEN: {
                piece = new Queen(colour, this);
                break;
            }
            case ROOK: {
                piece = new Rook(colour, this);
                break;
            }
            case BISHOP: {
                piece = new Bishop(colour, this);
                break;
            }
            case KNIGHT: {
                piece = new Knight(colour, this);
                break;
            }
            case PAWN: {
                piece = new Pawn(colour, this);
                break;
            }
        }
        //the active pieces arraylist just holds the "alive" pieces on the board for each team
        if (colour == Colour.WHITE) {
            whitePieces.add(piece);
        }
        else {
            blackPieces.add(piece);
        }
        piece.setPosition(x, y);
        //let the square know that it holds a piece
        getSquare(x, y).setHeldPiece(piece);
        return piece;
    }

    public void putPiece(Piece piece, Square finalSquare) {
        finalSquare.setHeldPiece(piece);
        piece.setPosition(finalSquare.getPosition().x, finalSquare.getPosition().y);
    }

    public void removePiece(Piece piece) {
        piece.getSquare().setHeldPiece(null);
        piece.setPosition(-1, -1);
        if(piece.getPieceColour() == Colour.WHITE) {
            whitePieces.remove(piece);
        }
        else {
            blackPieces.remove(piece);
        }
    }

    public void makeMove(Move move) {
        Piece piece = move.getPiece();
        Piece capturedPiece = move.getCapturedPiece();
        Square initSquare = move.getInitSquare();
        Square finalSquare = move.getFinalSquare();

        if(capturedPiece != null) {
            removePiece(capturedPiece);
        }
        putPiece(piece, finalSquare);
        initSquare.setHeldPiece(null);

        piece.setHasMoved(true);
        moves.push(move);
        highlightLastMove(true);
    }

    public void undoMove() {
        if(moves.isEmpty()) return;
        highlightLastMove(false);
        Move move = moves.pop();

        Piece piece = move.getPiece();
        Piece capturedPiece = move.getCapturedPiece();
        Square initSquare = move.getInitSquare();
        Square finalSquare = move.getFinalSquare();

        putPiece(piece, initSquare);
        finalSquare.setHeldPiece(null);

        if(capturedPiece != null) {
            putPiece(capturedPiece, finalSquare);
            getPieces(capturedPiece.getPieceColour() == Colour.WHITE).add(capturedPiece);
        }
        if(move.isFirstTimePieceMoved()) {
            piece.setHasMoved(false);
        }
        highlightLastMove(true);
        //game.newTurn(); // may need this
    }

    public void highlightLastMove(boolean highlightOn) {
        if(moves.empty()) return;
        Move lastMove = moves.pop();
        Color highlightColour;
        if(highlightOn) {
            highlightLastMove(false);
            highlightColour = Color.ORANGE;
        }
        else {
            highlightColour = null;
        }
        moves.push(lastMove);
        lastMove.getInitSquare().setHighlightColour(highlightColour);
        lastMove.getFinalSquare().setHighlightColour(highlightColour);
    }

    public Square getSquare(int x, int y) {
        return boardSquares[x][y];
    }

    public Square[][] getBoardSquares() {
        return boardSquares;
    }

    public ArrayList<Piece> getPieces() {
        ArrayList<Piece> allPieces= new ArrayList<>();
        allPieces.addAll(whitePieces);
        allPieces.addAll(blackPieces);
        return allPieces;
    }

    public ArrayList<Piece> getPieces(boolean white) {
        if(white) {
            return whitePieces;
        }
        return blackPieces;
    }
    public King getKing(boolean getWhite) {
        if(getWhite) {
            return whiteKing;
        }
        return blackKing;
    }
}
