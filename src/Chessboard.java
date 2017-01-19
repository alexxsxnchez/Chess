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
    private Stack<Move> moves = new Stack<>();
    private Stack<Integer> fiftyMoveDrawCount = new Stack<>();

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
                game.getPlayer(colour).setKing((King) piece);
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
        getPieces(colour).add(piece);
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
        getPieces(piece.getPieceColour()).remove(piece);
    }

    public void makeMove(Move move) {
        // get move data
        Piece piece = move.getPiece();
        Piece capturedPiece = move.getCapturedPiece();
        Square initSquare = move.getInitSquare();
        Square finalSquare = move.getFinalSquare();

        // handle piece movement
        if(capturedPiece != null) {
            removePiece(capturedPiece);
        }
        putPiece(piece, finalSquare);
        initSquare.setHeldPiece(null);
        piece.setHasMoved(true);

        // handle special moves
        if(move instanceof CastleMove) {
            Rook castlingRook = ((CastleMove) move).getCastlingRook();
            castlingRook.castle();
        }
        else if(move instanceof PromotionMove) {
            PromotionMove promotionMove = (PromotionMove) move;
            PieceType promotionType = promotionMove.getPromotionType();
            removePiece(piece);
            Piece promotionPiece = addNewPiece(finalSquare.getPosition().x, finalSquare.getPosition().y,
                    piece.getPieceColour(), promotionType);

            promotionMove.setPromotionPiece(promotionPiece);
        }

        moves.push(move);
        highlightLastMove(true);
    }

    public void undoMove() {
        if(moves.isEmpty()) {
            return;
        }
        highlightLastMove(false);
        Move move = moves.pop();

        Piece piece = move.getPiece();
        Piece capturedPiece = move.getCapturedPiece();
        Square initSquare = move.getInitSquare();
        Square finalSquare = move.getFinalSquare();

        if(move instanceof PromotionMove) {
            Piece promotedPiece = ((PromotionMove) move).getPromotionPiece();
            removePiece(promotedPiece);
            getPieces(piece.getPieceColour()).add(piece);
        }

        putPiece(piece, initSquare);
        finalSquare.setHeldPiece(null);

        if(capturedPiece != null) {
            if(move instanceof EnPassantMove) {
                putPiece(capturedPiece, ((EnPassantMove) move).getCapturedPawnSquare());
            }
            else {
                putPiece(capturedPiece, finalSquare);
            }
            getPieces(capturedPiece.getPieceColour()).add(capturedPiece);
        }
        if(move instanceof CastleMove) {
            undoMove();
        }

        if(move.isFirstTimePieceMoved()) {
            piece.setHasMoved(false);
        }
        highlightLastMove(true);
    }

    public void takeBackLastTwoMoves() {
        if(moves.size() < 2) {
            return;
        }
        undoMove();
        undoMove();
        game.getPlayerToMove().makeMove();
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

    public ArrayList<Piece> getAllPieces() {
        ArrayList<Piece> allPieces= new ArrayList<>();
        allPieces.addAll(getPieces(Colour.WHITE));
        allPieces.addAll(getPieces(Colour.BLACK));
        return allPieces;
    }
    public ArrayList<Piece> getPieces(Colour colour) {
        return game.getPlayer(colour).getPieces();
    }

    public King getKing(Colour colour) {
        return game.getPlayer(colour).getKing();
    }

    public Stack<Move> getMoves() {
        return moves;
    }

    public Square[][] getBoardSquares() {
        return boardSquares;
    }
}
