/**
 * Created by alexsanchez on 2019-02-22.
 */

package view;
import engine.*;

import java.util.ArrayList;

public class Presenter {

    private Game game;
    private Chessboard chessboard;
    private Square[][] boardSquares;
    private ArrayList<Piece> pieces;
    private ArrayList<Square> lastMoveSquares;
    private View view;

    public Presenter() {

    }

    public void setView(View view) {
        this.view = view;
    }

    public void startNewGame() {
        this.game = new Game(this);
        chessboard = game.getChessboard();
        boardSquares = new Square[8][8];
        pieces = new ArrayList<>();
        lastMoveSquares = new ArrayList<>();
        engine.Position position = chessboard.getCurrentPosition();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Colour squareColour = Colour.BLACK;
                if ((i + j) % 2 == 0) {
                    squareColour = Colour.WHITE;
                }
                boardSquares[j][i] = new Square(j, i, squareColour);
                engine.Piece enginePiece = position.getPiece(new PieceLocation(j, i));
                if(enginePiece != null) {
                    pieces.add(new Piece(j, i, enginePiece.getPieceType(), enginePiece.getPieceColour()));
                }
            }
        }
        game.start();
        view.update();
    }

    public void gameFinish(GameState gameState) {
        setPieceInteractivity(null);
        String text;
        switch(gameState) {
            case IN_PROGRESS:
                return;
            case DRAW:
                text = "Draw";
                break;
            default:
                text = "Checkmate";
        }
        view.setDisplayMessage(text);
    }

    public void undoButtonClicked() {
        System.out.println("undo clicked");
        chessboard.takeBackLastTwoMoves();
    }

    public void newGameButtonClicked() {
        System.out.println("new game clicked");
        view.setBoardRotation(false);
        view.setDisplayMessage("");
        startNewGame();
    }

    public void updateState() {
        Position position = chessboard.getCurrentPosition();
        pieces = new ArrayList<>();
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                engine.Piece enginePiece = position.getPiece(new PieceLocation(i, j));
                if(enginePiece != null) {
                    pieces.add(new Piece(i, j, enginePiece.getPieceType(), enginePiece.getPieceColour()));
                }
            }
        }

        view.highlightSquares(lastMoveSquares, false, true);
        Move lastMove = chessboard.getMoves().peek();
        lastMoveSquares = new ArrayList<>();
        if(lastMove != null) {
            lastMoveSquares.add(boardSquares[lastMove.getInitLocation().getX()][lastMove.getInitLocation().getY()]);
            lastMoveSquares.add(boardSquares[lastMove.getFinalLocation().getX()][lastMove.getFinalLocation().getY()]);
        }
        view.highlightSquares(lastMoveSquares, true, true);
        view.update();
    }

    public void setPieceInteractivity(Colour colour) {
        for(Piece piece : pieces) {
            piece.setSelectable(piece.getPieceColour() == colour);
        }
        view.update();
    }

    public void movedPiece(Square init, Square destination) {
        if(destination == null) {
            view.moveFailed();
            return;
        }

        Move move = chessboard.getCurrentPosition().getMove(new PieceLocation(init.getPosition().x, init.getPosition().y), new PieceLocation(destination.getPosition().x, destination.getPosition().y));
        if(move == null) {
            view.moveFailed();
            return;
        }

        if(move instanceof PromotionMove) {
            PromotionMove promotionMove = (PromotionMove) move;
            promotionMove.setPromotionType(view.askWhichPromotion());
        }

        view.moveSuccess(destination);

        if(move instanceof CastleMove) {
            PieceLocation castlingRookLocation = ((CastleMove) move).getCastlingRookLocation();
            Piece rook = null;
            Piece king = null;
            for(Piece piece : pieces) {
                if(piece.getX() == castlingRookLocation.getX() && piece.getY() == castlingRookLocation.getY()) {
                    rook = piece;
                }
                if(piece.getX() == move.getInitLocation().getX() && piece.getY() == move.getInitLocation().getY()) {
                    king = piece;
                }
                if(king != null && rook != null) {
                    break;
                }
            }
            king.setX(move.getFinalLocation().getX());
            PieceLocation castlingRookEndLocation = ((CastleMove) move).getCastlingRookEndLocation();
            view.movePiece(rook, boardSquares[castlingRookLocation.getX()][castlingRookLocation.getY()], boardSquares[castlingRookEndLocation.getX()][castlingRookEndLocation.getY()]);
        }

        chessboard.makeMove(move);
    }

    public Piece selectedSquare(Square square) { // could be just selectedSquare and do check for piece in here...
        engine.Piece enginePiece = chessboard.getCurrentPosition().getPiece(new PieceLocation(square.getPosition().x, square.getPosition().y));
        if(enginePiece == null) {
            return null;
        }

        Piece viewPiece = null;
        for(Piece piece : pieces) {
            if(piece.getX() == square.getPosition().x && piece.getY() == square.getPosition().y) {
                viewPiece = piece;
                break;
            }
        }

        if(viewPiece == null || !viewPiece.isSelectable()) {
            return null;
        }
        enginePiece.findPossibleMoves();

        ArrayList<Move> possibleMoves = enginePiece.getPossibleMoves();
        ArrayList<Square> highlightSquares = new ArrayList<>();
        for(Move move : possibleMoves) {
            Square highlightSquare = boardSquares[move.getFinalLocation().getX()][move.getFinalLocation().getY()];
            highlightSquares.add(highlightSquare);
        }
        view.highlightSquares(highlightSquares, true, false); // need also highlight last move

        return viewPiece;
    }

    public void humanVsHumanTurnEnded() {
        view.flipBoard();
    }

    public Square[][] getSquares() {
        return boardSquares;
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

}
