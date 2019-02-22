/**
 * Created by alexsanchez on 2017-01-03.
 */
public class Game {

    private Canvas canvas;
    private Window window;
    private Chessboard chessboard;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player playerToMove;
    private GameType gameType;
    private GameState gameState = GameState.IN_PROGRESS;
    private int fiftyMoveDrawCount = 0;

    public Game(Canvas canvas, Window window) {
        this.canvas = canvas;
        this.window = window;
        setupNewGame();
    }
    //TODO: add new game sound
    private void setupNewGame() {
        createPlayers();
        chessboard = new Chessboard(this);
        canvas.setChessboard(chessboard);
        gameType = GameType.getGameType(whitePlayer.getPlayerType(), blackPlayer.getPlayerType());
        getPlayer(Colour.WHITE).setChessboard(chessboard);
        getPlayer(Colour.BLACK).setChessboard(chessboard);
        //playerToMove.makeMove();
    }

    public void createPlayers() {
        blackPlayer = new Human(Colour.BLACK);
        //blackPlayer = new Human(Colour.BLACK);
        whitePlayer = new Human(Colour.WHITE);
        //gameType = GameType.getGameType(whitePlayer.getPlayerType(), blackPlayer.getPlayerType());
        playerToMove = whitePlayer;
    }

    public void newTurn() {
        setOtherPlayerToMove(); // computer would only call this method, not newTurn entirely
        updateGameState();
        //if(playerToMove instanceof Computer) {}
        if(gameState == GameState.IN_PROGRESS) {
            if(gameType == GameType.HUMAN_VS_HUMAN) {
                canvas.flipBoard();
            }
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
    public Window getWindow() {
        return window;
    }
    public Canvas getCanvas() {
        return canvas;
    }
}
