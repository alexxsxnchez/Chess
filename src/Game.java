/**
 * Created by alexsanchez on 2017-01-03.
 */
public class Game {

    private Canvas canvas;
    private Chessboard chessboard;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player playerToMove;
    private GameState gameState = GameState.IN_PROGRESS;

    public Game(Canvas canvas) {
        this.canvas = canvas;
        setupNewGame();
    }
    //TODO: add new game sound
    private void setupNewGame() {
        createPlayers();
        chessboard = new Chessboard(this);
        canvas.setChessboard(chessboard);
        getPlayer(Colour.WHITE).setChessboard(chessboard);
        getPlayer(Colour.BLACK).setChessboard(chessboard);
        playerToMove.makeMove();
    }

    public void createPlayers() {
        whitePlayer = new Human(Colour.WHITE);
        blackPlayer = new Human(Colour.BLACK);
        playerToMove = whitePlayer;
    }


    private void setOtherPlayerToMove() {
        if(playerToMove == whitePlayer) {
            playerToMove = blackPlayer;
        }
        else {
            playerToMove = whitePlayer;
        }
    }

    public void newTurn() {
        setOtherPlayerToMove();
        checkForGameFinish();
        if(gameState == GameState.IN_PROGRESS) {
            canvas.flipBoard();
            playerToMove.makeMove();
        }
    }

    private boolean checkForGameFinish() {
        boolean playerCanMove = playerToMove.canMove();
        if(!playerCanMove) {
            /*if(playerToMove.getKing().isInCheck()) {

            }*/
            gameState = GameState.DRAW;
            gameFinish();
        }

        return !playerCanMove;
    }

    private void gameFinish() {
        makePiecesUnmoveable();

    }

    private void makePiecesUnmoveable() {
        for(Piece piece : chessboard.getPieces()) {
            piece.setSelectable(false);
        }
    }

    public Player getPlayer(Colour colour) {
        if(colour == Colour.WHITE) {
            return whitePlayer;
        }
        return blackPlayer;
    }
}
