/**
 * Created by alexsanchez on 2017-01-03.
 */

package engine;

import view.Presenter;

public class Game {

    private Presenter presenter;
    private Chessboard chessboard;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player playerToMove;
    private GameType gameType;
    private GameState gameState = GameState.IN_PROGRESS;
    private int fiftyMoveDrawCount = 0;

    public Game(Presenter presenter) {
        this.presenter = presenter;
        setupNewGame();
    }
    //TODO: add new game sound
    private void setupNewGame() {
        createPlayers();
        chessboard = new Chessboard(this);
        gameType = GameType.getGameType(whitePlayer.getPlayerType(), blackPlayer.getPlayerType());
        getPlayer(Colour.WHITE).setChessboard(chessboard);
        getPlayer(Colour.BLACK).setChessboard(chessboard);
    }

    public void start() {
        playerToMove.makeMove();
    }

    public void createPlayers() {
        blackPlayer = new Human(Colour.BLACK, presenter);
        //blackPlayer = new Human(Colour.BLACK);
        whitePlayer = new Human(Colour.WHITE, presenter);
        //gameType = GameType.getGameType(whitePlayer.getPlayerType(), blackPlayer.getPlayerType());
        playerToMove = whitePlayer;
    }

    public void newTurn() {
        setOtherPlayerToMove(); // computer would only call this method, not newTurn entirely
        updateGameState();
        presenter.updateState();
        //if(playerToMove instanceof Computer) {}
        if(gameState == GameState.IN_PROGRESS) {
            if(gameType == GameType.HUMAN_VS_HUMAN) {
                presenter.humanVsHumanTurnEnded();
            }
            playerToMove.makeMove();
        }
        else {
            presenter.gameFinish(gameState);
        }
    }

    public void undoOccurred() {
        presenter.updateState();
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
            if(chessboard.getCurrentPosition().isKingInCheck(playerToMove.getColour())) {
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

    public Chessboard getChessboard() {
        return chessboard;
    }

}
