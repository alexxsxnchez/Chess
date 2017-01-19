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
    private int fiftyMoveDrawCount = 0;

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

    public void newTurn() {
        setOtherPlayerToMove(); // computer would only call this method, not newTurn entirely
        updateGameState();
        //if(playerToMove instanceof Computer) {}
        if(gameState == GameState.IN_PROGRESS) {
            canvas.flipBoard();
            playerToMove.makeMove();
        }
        else {
            gameFinish();
        }
    }

    private void setOtherPlayerToMove() {
        if(playerToMove == whitePlayer) {
            playerToMove = blackPlayer;
        }
        else {
            playerToMove = whitePlayer;
        }
    }

    private void updateGameState() {
        boolean playerCanMove = playerToMove.canMove();
        if(!playerCanMove) {
            if(playerToMove.getKing().isInCheck()) {
                if(playerToMove == whitePlayer) {
                    gameState = GameState.BLACK_WIN;
                }
                else {
                    gameState = GameState.WHITE_WIN;
                }
            }
            else {
                gameState = GameState.DRAW;
            }
        }
        /* check for tri-fold repetition and 50-move draw and insufficient material
        else if() {

        }
        */
    }

    private void gameFinish() {
        makePiecesUnmoveable();
    }

    private void makePiecesUnmoveable() {
        for(Piece piece : chessboard.getAllPieces()) {
            piece.setSelectable(false);
        }
    }

    public void increaseFiftyMoveCount() {
        fiftyMoveDrawCount++;
    }

    public void resetFiftyMoveCount() {
        fiftyMoveDrawCount = 0;
    }

    public Player getPlayerToMove() {
        return playerToMove;
    }
    public Player getPlayer(Colour colour) {
        if(colour == Colour.WHITE) {
            return whitePlayer;
        }
        return blackPlayer;
    }

    public GameState getGameState() {
        return gameState;
    }
}
