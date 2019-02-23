/**
 * Created by alexsanchez on 2017-01-03.
 */

package engine;
import view.Presenter;

public class Human extends Player {
    public Human (Colour colour, Presenter presenter) {
        super(colour, PlayerType.HUMAN, presenter);
    }

    public void makeMove() {
        presenter.setPieceInteractivity(getColour());
    }

}
