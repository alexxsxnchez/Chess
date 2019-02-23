/**
 * Created by alexsanchez on 2019-02-22.
 */

package view;
import engine.PieceType;

import java.util.ArrayList;

public interface View {

    void update();

    void moveFailed();

    void moveSuccess(Square destination);

    PieceType askWhichPromotion();

    void movePiece(Piece piece, Square init, Square destination);

    void setEventsEnabled(boolean enabled);

    void recalculateSizes(int width, int height);

    void highlightSquares(ArrayList<Square> squares, boolean highlight, boolean lastMove);

    void setDisplayMessage(String msg);

    void flipBoard();

    void setBoardRotation(boolean flipped);

}
