/**
 * Created by alexsanchez on 2017-01-03.
 */
public abstract class Player {

    protected Game game;
    protected Chessboard chessboard;
    protected Colour colour;
    protected PlayerType playerType;

    public Player(Colour colour,  Game game, Chessboard chessboard) {
            this.colour = colour;
            this.game = game;
            this.chessboard = chessboard;
    }

    public abstract void makeMove();

}
