/**
 * Created by alexsanchez on 2016-12-20.
 */
public class Chessboard {
    private static final int NUM_OF_SQUARES = 8;
    private Square[][] boardSquares = new Square[NUM_OF_SQUARES][NUM_OF_SQUARES];

    public Chessboard() {
        createSquares();
    }
    private void createSquares() {
        for (int i = 0; i < NUM_OF_SQUARES; i++) {
            for (int j = 0; j < NUM_OF_SQUARES; j++) {
                Colour squareColour = Colour.BLACK;
                if ((i + j) % 2 == 0) squareColour = Colour.WHITE;
                boardSquares[j][i] = new Square(j, i, squareColour);
            }
        }
    }
    public Square[][] getBoardSquares() {
        return boardSquares;
    }
}
