import javax.swing.*;

/**
 * Created by alexsanchez on 2017-01-03.
 */
public class Pawn extends Piece {

    public Pawn(Colour pieceColour, Chessboard chessboard) {
        super(PieceType.PAWN, pieceColour, chessboard);

    }

    public void findPotentialMoves() {
        int dir = 1;
        if(pieceColour == Colour.WHITE) {
            dir = -1;
        }

        searchNormalMoves(dir);
        searchAttackingMoves(dir);
    }

    private void searchNormalMoves(int dir) {
        Square targetSquare;
        targetSquare = chessboard.getSquare(position.x, position.y + dir);
        if(targetSquare.getHeldPiece() == null) {
            // if pawn is in a position to be promoted
            Move move;

            if(targetSquare.getPosition().y == 3.5 + (3.5 * dir)) {
                move = new PromotionMove(this, getSquare(), targetSquare);
            }
            else {
                move = new Move(this, getSquare(), targetSquare);
            }
            addMoveToPossibleMoves(move);

            if(!hasMoved) {
                targetSquare = chessboard.getSquare(position.x, position.y + dir * 2);
                if(targetSquare.getHeldPiece() == null) {
                    addMoveToPossibleMoves(new Move(this, getSquare(), targetSquare));
                }
            }
        }
    }

    private void searchAttackingMoves(int dir) {
        for(int i = -1; i <= 1; i += 2) {
            Square targetSquare;
            int x = position.x + i;
            if(x >= 0 && x < Chessboard.NUM_OF_SQUARES) {
                targetSquare = chessboard.getSquare(x, position.y + dir);
                Move move;
                if(targetSquare.getPosition().y == 3.5 + (3.5 * dir)) {
                    move = new PromotionMove(this, getSquare(), targetSquare);
                }
                else {
                    move = new Move(this, getSquare(), targetSquare);
                }
                addAttackableSquareToPossibleMoves(move);
            }
        }
    }

    private void addAttackableSquareToPossibleMoves(Move move) {
        Piece finalSquarePiece = move.getCapturedPiece();
        if(finalSquarePiece != null && finalSquarePiece.getPieceColour() != pieceColour) {
            possibleMoves.add(move);
        }
    }

    public PieceType askHumanWhichPromotion(JPanel jPanel) {
        Object[] promotionOptions = { PieceType.QUEEN, PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP };
        Object selectedOption = JOptionPane.showInputDialog(jPanel, "Choose One", "PROMOTION",
                JOptionPane.PLAIN_MESSAGE, null, promotionOptions, promotionOptions[0]);
        if(selectedOption == null) {
            return PieceType.QUEEN; //if the JOptionPane was closed without an option being selected
        }
        return (PieceType) selectedOption;
    }
}
