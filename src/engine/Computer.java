/**
 * Created by alexsanchez on 2017-01-28.
 */

package engine;
import view.Presenter;

import java.util.ArrayList;

public class Computer extends Player {

    private Game game;
    private long thinkingTime = 1000 * 2;
    private static final int INFINITY = 100000000;

    public Computer(Colour colour, Game game, Presenter presenter) {
        super(colour, PlayerType.COMPUTER, presenter);
        this.game = game;
    }


    @Override
    public void makeMove() {
        /*for(Piece enemyPiece : chessboard.getPieces(colour.getOpposite())) {
            enemyPiece.setSelectable(false);
        }
        game.getWindow().setUndoButtonEnabled(false);
        game.getCanvas().setMouseEventsEnabled(false);
        Move move = iterativeSearch();
        Piece piece = move.getPiece();
        game.getCanvas().repaint();

        chessboard.makeMove(move);
        game.getCanvas().setSelectedPiece(piece);
        game.getCanvas().translateSelectedPiece(move.getFinalLocation());
        game.getCanvas().setSelectedPiece(null);
        game.getWindow().setUndoButtonEnabled(true);
        game.getCanvas().setMouseEventsEnabled(true);
        game.newTurn();*/
    }

    /*
    private Move iterativeSearch() {
        Move root = null;
        if(!chessboard.getMoves().isEmpty()) {
            root = chessboard.getMoves().peek();
        }
        long timeInit = System.currentTimeMillis();
        long timePassed;
        int depth = 1;
        Move bestMove;
        do {
            System.out.println(depth);
            bestMove = alphabeta(root, -INFINITY, INFINITY, depth, colour);
            depth++;
            timePassed = System.currentTimeMillis() - timeInit;
        } while(timePassed < thinkingTime && !checkForInterrupt());
        System.out.println(bestMove);
        return bestMove;
    }

    private Move alphabeta(Move parent, int alpha, int beta, int depth, Colour playerColour) {
        if(depth == 0 || !game.getPlayer(playerColour).canMove()) {
            return null;
        }
        ArrayList<Move> moves = new ArrayList<>();
        if(parent == null) {
            for(Piece piece : chessboard.getPieces(playerColour)) {
                piece.clearPossibleMoves();
                piece.findPossibleMoves();
                moves.addAll(piece.getPossibleMoves());
                piece.clearPossibleMoves();
            }
        }
        else if(!parent.hasChildren()) {
            parent.generateChildren(chessboard);
            moves.addAll(parent.getChildren());
        }

        Move bestMove = null;
        for (Move child : moves) {
            chessboard.makeMove(child);
            int value;
            Move enemyBestMove = alphabeta(child, alpha, beta, depth - 1, playerColour.getOpposite());
            if(enemyBestMove != null) {
                value = enemyBestMove.getValue();
                child.setValue(value);
            }
            else if(!child.hasValue()) {
                value = quiesce(child);
                child.setValue(value);
            }
            else {
                value = child.getValue();
            }
            chessboard.undoMove();
            if(playerColour == Colour.WHITE) {
                alpha = Math.max(alpha, value);
            }
            else {
                beta = Math.min(beta, value);
            }
            if(bestMove == null) {
                bestMove = child;
            }
            else {
                int bestNodeValue = bestMove.getValue();
                if((playerColour == Colour.WHITE && bestNodeValue < value) ||
                        (playerColour == Colour.BLACK && bestNodeValue > value)) {
                    bestMove = child;
                }
            }

            if(alpha >= beta) {
                break;
            }
        }
        bestMove.sortChildren();
        return bestMove;
    }

    int quiesce(Move child) {
        return evaluate();
    }

    int evaluate() {
        int score = 0;
        for(Piece piece : chessboard.getAllPieces()) {
            int pieceScore;
            switch(piece.getPieceType()) {
                case PAWN:
                    pieceScore = 100;
                    break;
                case KNIGHT:
                    pieceScore = 300;
                    break;
                case BISHOP:
                    pieceScore = 315;
                    break;
                case ROOK:
                    pieceScore = 500;
                    break;
                case QUEEN:
                    pieceScore = 1000;
                    break;
                default:
                    pieceScore = INFINITY;
            }
            if(piece.getPieceColour() == Colour.WHITE) {
                score += pieceScore;
            }
            else {
                score += pieceScore * -1;
            }
        }
        return score;
    }
    private boolean checkForInterrupt() {
        return false;
    }

    */
}
