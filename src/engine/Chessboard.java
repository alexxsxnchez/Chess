/**
 * Created by alexsanchez on 2016-12-20.
 */

package engine;

import java.util.Stack;

public class Chessboard {

    private Game game;
    public static final int NUM_OF_SQUARES = 8;
    private Stack<Move> moves = new Stack<>();
    private Stack<Position> positions = new Stack<>();
    private Stack<Integer> fiftyMoveDrawCount = new Stack<>();

    public Chessboard(Game game) {
        this.game = game;
        positions.push(new Position());
    }

    public void makeMove(Move move) {
        moves.push(move);
        positions.push(new Position(positions.peek(), move));
        game.newTurn();
    }

    public void undoMove() {
        if (moves.isEmpty()) {
            return;
        }
        moves.pop();
        positions.pop();

        // TELL GAME ??

    }

    public void takeBackLastTwoMoves() {
        if (moves.size() < 2) {
            return;
        }
        undoMove();
        undoMove();
        game.undoOccurred();
        game.getPlayerToMove().makeMove();
    }

    public Stack<Move> getMoves() {
        return moves;
    }

    public Position getCurrentPosition() {
        return positions.peek();
    }

}
