/**
 * Created by alexsanchez on 2019-02-22.
 */

package engine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Position {

    private HashMap<PieceLocation, Piece> pieces = new HashMap<>();
    private ArrayList<Piece> whitePieces = new ArrayList<>();
    private ArrayList<Piece> blackPieces = new ArrayList<>();
    private King blackKing;
    private King whiteKing;
    private Move moveJustMade;

    public Position() {
        moveJustMade = null;
        addStartingPieces();
    }

    private void addStartingPieces() {
        //add pawns
        for (int i = 0; i < 8; i++) {
            createNewPiece(i, 6, Colour.WHITE, PieceType.PAWN, this);
            createNewPiece(i, 1, Colour.BLACK, PieceType.PAWN, this);
        }
        
        //add rooks
        createNewPiece(0, 7, Colour.WHITE, PieceType.ROOK, this);
        createNewPiece(7, 7, Colour.WHITE, PieceType.ROOK, this);
        createNewPiece(0, 0, Colour.BLACK, PieceType.ROOK, this);
        createNewPiece(7, 0, Colour.BLACK, PieceType.ROOK, this);
        
        //add knights
        createNewPiece(1, 7, Colour.WHITE, PieceType.KNIGHT, this);
        createNewPiece(6, 7, Colour.WHITE, PieceType.KNIGHT, this);
        createNewPiece(1, 0, Colour.BLACK, PieceType.KNIGHT, this);
        createNewPiece(6, 0, Colour.BLACK, PieceType.KNIGHT, this);

        //add bishops
        createNewPiece(2, 7, Colour.WHITE, PieceType.BISHOP, this);
        createNewPiece(5, 7, Colour.WHITE, PieceType.BISHOP, this);
        createNewPiece(2, 0, Colour.BLACK, PieceType.BISHOP, this);
        createNewPiece(5, 0, Colour.BLACK, PieceType.BISHOP, this);

        //add kings
        createNewPiece(4, 7, Colour.WHITE, PieceType.KING, this);
        createNewPiece(4, 0, Colour.BLACK, PieceType.KING, this);

        //add queens
        createNewPiece(3, 7, Colour.WHITE, PieceType.QUEEN, this);
        createNewPiece(3, 0, Colour.BLACK, PieceType.QUEEN, this);
    }

    private Piece createNewPiece(int x, int y, Colour colour, PieceType pieceType, Position position) {
        Piece piece = null;
        switch (pieceType) {
            case KING: {
                King king = new King(colour, position);
                if(colour == Colour.WHITE) {
                    whiteKing = king;
                } else {
                    blackKing = king;
                }
                piece = king;
                break;
            }
            case QUEEN: {
                piece = new Queen(colour, position);
                break;
            }
            case ROOK: {
                piece = new Rook(colour, position);
                break;
            }
            case BISHOP: {
                piece = new Bishop(colour, position);
                break;
            }
            case KNIGHT: {
                piece = new Knight(colour, position);
                break;
            }
            case PAWN: {
                piece = new Pawn(colour, position);
                break;
            }
        }
        pieces.put(new PieceLocation(x, y), piece);
        if(colour == Colour.WHITE) {
            whitePieces.add(piece);
        } else {
            blackPieces.add(piece);
        }

        piece.setPosition(new PieceLocation(x, y));
        return piece;
    }
    
    
    public Position(Position oldPosition, Move move) {
        this.moveJustMade = move;
        // get move data
        Piece piece = move.getPiece();
        PieceLocation initLocation = move.getInitLocation();
        PieceLocation finalLocation = move.getFinalLocation();
        oldPosition.pieces.get(finalLocation);

        PieceLocation castlingRookLocation = null;
        PieceLocation capturedEnPassantLocation = null;
        if(move instanceof PromotionMove) {
            PromotionMove promotionMove = (PromotionMove) move;
            PieceType promotionType = promotionMove.getPromotionType();
            Piece newPiece = createNewPiece(finalLocation.getX(), finalLocation.getY(),
                    piece.getPieceColour(), promotionType, this);
            newPiece.setHasMoved(true);
        } else {
            // handle piece movement
            Piece newPiece = createNewPiece(finalLocation.getX(), finalLocation.getY(), piece.getPieceColour(), piece.getPieceType(), this);
            newPiece.setHasMoved(true);
            if(newPiece instanceof Pawn) {
                ((Pawn) newPiece).setLastMoveMade(moveJustMade); // shouldn't matter here since this pawn will never get to do enpassant
            }

            // handle special moves
            if(move instanceof CastleMove) {
                castlingRookLocation = ((CastleMove) move).getCastlingRookLocation();
                PieceLocation castlingRookEndLocation = ((CastleMove) move).getCastlingRookEndLocation();
                Piece newRook = createNewPiece(castlingRookEndLocation.getX(), castlingRookEndLocation.getY(), piece.getPieceColour(), PieceType.ROOK, this);
                newRook.setHasMoved(true);
            } else if(move instanceof EnPassantMove) {
                capturedEnPassantLocation = ((EnPassantMove) move).getCapturedPawnLocation();
            }
        }

        for (Map.Entry<PieceLocation, Piece> entry : oldPosition.pieces.entrySet()) {
            PieceLocation location = entry.getKey();
            Piece oldPiece = entry.getValue();
            if(!location.equals(initLocation) && !location.equals(castlingRookLocation) && !location.equals(finalLocation) && !location.equals(capturedEnPassantLocation)) {
                Piece newPiece = createNewPiece(location.getX(), location.getY(), oldPiece.getPieceColour(), oldPiece.getPieceType(), this);
                newPiece.setHasMoved(oldPiece.getHasMoved());
                if(newPiece instanceof Pawn) {
                    ((Pawn) newPiece).setLastMoveMade(moveJustMade);
                }
            }
        }
    }

    public boolean isKingInCheck(Colour colour) {
        ArrayList<Move> enemyMoves = new ArrayList<>();
        King king = blackKing;
        ArrayList<Piece> pieceList = whitePieces;
        if(colour == Colour.WHITE) {
            king = whiteKing;
            pieceList = blackPieces;
        }
        for(Piece enemyPiece : pieceList) {
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
            if(enemyMove.getFinalLocation().equals(king.getLocation())) {
                return true;
            }
        }
        return false;

    }

    public King getKing(Colour colour) {
        return colour == Colour.WHITE ? whiteKing : blackKing;
    }

    public Piece getPiece(PieceLocation location) {
        return pieces.get(location);
    }

    public ArrayList<Piece> getPieces(Colour colour) {
        return colour == Colour.WHITE ? whitePieces : blackPieces;
    }

    public Move getMove(PieceLocation init, PieceLocation dest) {
        Piece piece = getPiece(init);
        if(piece != null) {
            for(Move move : piece.getPossibleMoves()) {
                if(move.getFinalLocation().equals(dest)) {
                    return move;
                }
            }
        }
        return null;
    }

}
