/**
 * Created by alexsanchez on 2017-01-03.
 */
public class Game {

    private Canvas canvas;
    private Chessboard chessboard;
    private Player whitePlayer;
    private Player blackPlayer;
    private boolean isWhiteToMove = true;
    private boolean isGameFinished = false;

    public Game(Canvas canvas) {
        this.canvas = canvas;
        setupNewGame();
    }
    //TODO: add new game sound
    private void setupNewGame() {
        chessboard = new Chessboard(this);
        canvas.setChessboard(chessboard);
        createPlayers();
        nextPlayerMove();
    }

    public void createPlayers() {
        whitePlayer = new Human(Colour.WHITE, this, chessboard);
        blackPlayer = new Human(Colour.BLACK, this, chessboard);
    }

    public void nextPlayerMove() {
        getPlayer(isWhiteToMove).makeMove();
    }
    public void newTurn() {
        isWhiteToMove = !isWhiteToMove;
        canvas.flipBoard();
        nextPlayerMove();
    }

    public Player getPlayer(boolean isWhite) {
        if(isWhite) {
            return whitePlayer;
        }
        return blackPlayer;
    }
    public boolean isWhiteToMove() {
        return isWhiteToMove;
    }

}
