import java.util.ArrayList;

/**
 * Created by alexsanchez on 2016-12-20.
 */
public class Chessboard {
    private static final int NUM_OF_SQUARES = 8;
    private Square[][] boardSquares = new Square[NUM_OF_SQUARES][NUM_OF_SQUARES];
    private ArrayList<Piece> whitePieces = new ArrayList<>();
    private ArrayList<Piece> blackPieces = new ArrayList<>();
    //private King whiteKing;
    //private King blackKing;


    public Chessboard() {
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
            /*
            //add pawns
            for (int i=0; i < 8; i++) {
                addNewPiece(i, 1, PieceType.PAWN, Colour.WHITE);
                addNewPiece(i, 6, PieceType.PAWN, Colour.BLACK);
            }
            //add rooks
            addNewPiece(0, 0, PieceType.ROOK, Colour.WHITE);
            addNewPiece(7, 0, PieceType.ROOK, Colour.WHITE);
            addNewPiece(0, 7, PieceType.ROOK, Colour.BLACK);
            addNewPiece(7, 7, PieceType.ROOK, Colour.BLACK);
            //add knights

            addNewPiece(1, 0, PieceType.KNIGHT, Colour.WHITE);
            addNewPiece(6, 0, PieceType.KNIGHT, Colour.WHITE);
            addNewPiece(1, 7, PieceType.KNIGHT, Colour.BLACK);
            addNewPiece(6, 7, PieceType.KNIGHT, Colour.BLACK);
*/
            //add bishops
            addNewPiece(2, 0, Colour.WHITE, PieceType.BISHOP);
            addNewPiece(5, 0, Colour.WHITE, PieceType.BISHOP);
            addNewPiece(2, 7, Colour.BLACK, PieceType.BISHOP);
            addNewPiece(5, 7, Colour.BLACK, PieceType.BISHOP);
  /*
            //add kings
            addNewPiece(4, 0, PieceType.KING, Colour.WHITE);
            addNewPiece(4, 7, PieceType.KING, Colour.BLACK);
            //add queens
            addNewPiece(3, 0, PieceType.QUEEN, Colour.WHITE);
            addNewPiece(3, 7, PieceType.QUEEN, Colour.BLACK);
        }
        */

    }

    public Piece addNewPiece(int x, int y, Colour colour, PieceType pieceType) {
        Piece piece = null;
        switch (pieceType) {
            /*case KING: {
                piece = new King(x, y, colour, this);
                if(colour == Colour.WHITE) whiteKing = piece;
                else blackKing = piece;
                break;
            }
            case QUEEN: {
                piece = new Queen(x, y, colour, this);
                break;
            }
            case ROOK: {
                piece = new Rook(x, y, colour, this);
                break;
            }*/
            case BISHOP: {
                piece = new Bishop(colour, this);
                break;
            }
            /*
            case KNIGHT: {
                piece = new Knight(x, y, colour, this);
                break;
            }
            case PAWN: {
                piece = new Pawn(x, y, colour, this);
                break;
            }*/
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
}
