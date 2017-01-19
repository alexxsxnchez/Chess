/**
 * Created by alexsanchez on 2016-12-16.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener{

    private Window window;
    private int width;
    private int height;
    private double angle = 0;
    private static final int PADDING = 50;
    
    private int boardX;
    private int boardY;
    private int borderLength;
    private static final int BORDER_THICKNESS = 5;
    private int squareSize;
    private int piecePadding;
    private int pieceSize;

    private Chessboard chessboard;
    private Game game;
    private Piece selectedPiece = null;
    private boolean selectedPieceIsClicked = false;
    private Square focusedSquare = null;
    private Square clickedSquare = null;
    private Point mousePosition = new Point();
    private Point selectedPiecePosition = new Point();
    private boolean isBoardFlipped = false;

    private static final int FPS = 30;

    public Canvas(Window window) {
        this.window = window;
        calculateSize();
        setBackground(Color.DARK_GRAY);
        addMouseListener(this);
        addMouseMotionListener(this);
        this.game = new Game(this);
    }

    //=======================================================
    // DRAWING FUNCTIONS
    public void paint(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawPieces(g);
        drawMessage(g);
    }

    private void drawBoard(Graphics graphics) {
        Graphics2D gBoard = (Graphics2D) graphics.create();
        // rotate board for when flip board is called. Usually, angle has a value of 0 or PI
        gBoard.rotate(angle, width / 2, height / 2);
        // draw border
        gBoard.setColor(Color.PINK);
        gBoard.drawRect(boardX - BORDER_THICKNESS, boardY - BORDER_THICKNESS, borderLength, borderLength);
        gBoard.fillRect(boardX - BORDER_THICKNESS, boardY - BORDER_THICKNESS, borderLength, borderLength);
        // draw squares
        for(int i = 0; i < Chessboard.NUM_OF_SQUARES; i++) {
            for(int j = 0; j < Chessboard.NUM_OF_SQUARES; j++) {
                drawSquare(chessboard.getBoardSquares()[j][i], gBoard.create(), i, j);
            }
        }
    }

    private void drawSquare(Square square, Graphics gr, int i, int j) {
        Graphics2D g = (Graphics2D) gr;
        Point squareCoor = getSquareCoor(j, i);
        Point gradientEnd = new Point(squareCoor.x + squareSize, squareCoor.y + squareSize);
        GradientPaint colour;
        if(square.getColour() == Colour.WHITE) {
            colour = new GradientPaint(squareCoor, new Color(240, 240, 240), gradientEnd, new Color(230, 230, 230));
        }
        else {
            colour = new GradientPaint(squareCoor, Color.LIGHT_GRAY, gradientEnd, new Color(150, 150, 180));
        }
        g.setPaint(colour);

        g.drawRect(squareCoor.x, squareCoor.y, squareSize - 1, squareSize - 1);
        g.fillRect(squareCoor.x, squareCoor.y, squareSize - 1, squareSize - 1);

        // draw the square highlight
        Color highlightColour = square.getHighlightColour();
        if(highlightColour != null) {
            Composite original = g.getComposite();
            g.setComposite(square.getComposite());
            g.setColor(highlightColour);
            g.drawRect(squareCoor.x, squareCoor.y, squareSize - 1, squareSize - 1);
            g.fillRect(squareCoor.x, squareCoor.y, squareSize - 1, squareSize - 1);
            g.setComposite(original);
        }
        // draw square border
        Color borderColour = square.getBorderColour();
        if(borderColour != null) {
            g.setColor(borderColour);
            int thickness = 3;
            for(int stroke = 0; stroke < thickness; stroke++) {
                g.drawRect(squareCoor.x + stroke, squareCoor.y + stroke, squareSize - 1 - stroke * 2, squareSize - 1 - stroke * 2);
            }
        }
    }

    private void drawPieces(Graphics g) {

        // draw other board pieces
        Graphics2D g2SquarePieces = (Graphics2D) g.create();
        Composite originalComposite = g2SquarePieces.getComposite();
        g2SquarePieces.rotate(angle, width / 2, height / 2);
        for(Piece piece : chessboard.getAllPieces()) {
            if(piece != selectedPiece) {
                Graphics2D g2SinglePiece = (Graphics2D) g2SquarePieces.create();
                Point pieceCoor = getSquareCoor(piece.getPosition().x, piece.getPosition().y);
                g2SinglePiece.rotate(-angle, pieceCoor.x + squareSize * 0.5, pieceCoor.y + squareSize * 0.5);
                if(!piece.isSelectable()) {
                    g2SinglePiece.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                }
                g2SinglePiece.drawImage(piece.getImage(), pieceCoor.x + piecePadding, pieceCoor.y + piecePadding, pieceSize, pieceSize, null);
                g2SinglePiece.setComposite(originalComposite);
            }
        }
        // draw piece grabbed by mouse
        if(selectedPiece != null) {
            Graphics2D g2MousePiece = (Graphics2D) g.create();
            originalComposite = g2MousePiece.getComposite();
            g2MousePiece.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

            g2MousePiece.drawImage(getShadow(selectedPiece.getImage()), selectedPiecePosition.x - piecePadding, selectedPiecePosition.y - piecePadding * 2, pieceSize, pieceSize, null);

            g2MousePiece.setComposite(originalComposite);
            g2MousePiece.drawImage(selectedPiece.getImage(), selectedPiecePosition.x - pieceSize / 2, selectedPiecePosition.y - pieceSize / 2, pieceSize, pieceSize, null);
        }
    }

    private Image getShadow(Image pieceImage) {
        BufferedImage shadow = new BufferedImage(pieceImage.getWidth(null), pieceImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = shadow.createGraphics();
        g.drawImage(pieceImage, 0, 0, null);
        g.dispose();
        for(int i = 0; i < shadow.getWidth(); i++) {
            for(int j = 0; j < shadow.getHeight(); j++) {
                if(shadow.getRGB(i, j) != 0) {
                    shadow.setRGB(i, j, Color.BLACK.getRGB());
                }
            }
        }
        return shadow;
    }

    private void drawMessage(Graphics g) {
        GameState gameState = game.getGameState();
        String text;
        switch(gameState) {
            case IN_PROGRESS:
                return;
            case DRAW:
                text = "Draw";
                break;
            default:
                text = "Checkmate";
        }
        int size = (int) (Math.min(width, height) * 0.1);
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, size));
        g.drawString(text, width / 2 - (text.length() / 3 * size), height / 2);
    }

    //========================================================
    // PIXEL CALCULATIONS

    private Point getSquareCoor(int x, int y) {
        return new Point(x * squareSize + boardX, y * squareSize + boardY);
    }

    private Point getPieceCoorForSquare(Square square) {
        Point squareCoor = getSquareCoor(square.getPosition().x, square.getPosition().y);
        int paddingDistance = piecePadding + pieceSize / 2;
        int x = squareCoor.x + paddingDistance;
        int y = squareCoor.y + paddingDistance;
        // if board is flipped
        if(isBoardFlipped) {
            x = width - x;
            y = height - y;
        }
        return new Point(x, y);
    }

    public void calculateSize() {
        Dimension size = window.getDimensions();
        setSize(size);
        width = size.width;
        height = size.height;
        calculateBoardSizes();
    }
    private void calculateBoardSizes() {
        squareSize = (Math.min(width, height) - PADDING * 2) / Chessboard.NUM_OF_SQUARES;
        borderLength = squareSize * Chessboard.NUM_OF_SQUARES + BORDER_THICKNESS * 2;
        boardX = (width - borderLength) / 2 + BORDER_THICKNESS;
        boardY = (height - borderLength) / 2 + BORDER_THICKNESS;
        piecePadding = (int) (0.1 * squareSize); // make image padding a percentage amount of the image size
        pieceSize = squareSize - piecePadding * 2;
    }

    public void setChessboard(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public Chessboard getChessboard() {
        return chessboard;
    }
    /*
     * used for testing
     */
    private void printSizes() {
        System.out.println("Width: " + width);
        System.out.println("Height: " + height);
        System.out.println("Smallest Dimension: " + Math.min(width, height));
        System.out.println("Square size: " + squareSize);
        System.out.println("boardX: " + boardX);
        System.out.println("boardY " + boardY);
        System.out.println();
    }


    //==========================================================
    //MOUSE EVENTS AND ANIMATIONS

    private Square getSquareAtMouse() {
        int x = mousePosition.x - boardX - 1; // minus one because boardx includes the first pixel of board
        int y = mousePosition.y - boardY - 1;
        if(x < 0 || x >= Chessboard.NUM_OF_SQUARES * (squareSize) || y < 0 || y >= Chessboard.NUM_OF_SQUARES * (squareSize)) {
            return null;
        }
        else {
            x /= squareSize;
            y /= squareSize;
            if(isBoardFlipped) {
                return chessboard.getSquare(Chessboard.NUM_OF_SQUARES - 1 - x, Chessboard.NUM_OF_SQUARES - 1 - y);
            }
            // if the board is rotated 180 degrees, there is a slight offset by one pixel that is not accounted for (error)
            else {
                return chessboard.getSquare(x, y);
            }
        }
    }

    private void setNewMousePosition(MouseEvent e) {
        int yOffset = 2; // offset by two pixels due to the way the mouse interacts with the JFrame
        mousePosition.move(e.getX(), e.getY() - yOffset);
    }

    private void updateSelectedPiecePosition() {
        selectedPiecePosition.x = mousePosition.x;
        selectedPiecePosition.y = mousePosition.y;
    }

    private void clickOnSquare(Square square) {
        if(clickedSquare != null) {
            clickedSquare.isClicked(false);
            clickedSquare = null;
        }
        if(selectedPieceIsClicked) {
            clickedSquare = square;
            if(focusedSquare == square) {
                focusedSquare = null;
            }
            square.isClicked(true);
        }
    }

    private void focusOnSquare(MouseEvent e) {
        if(focusedSquare != null && focusedSquare != clickedSquare) {
            focusedSquare.setIsFocused(false);
            focusedSquare = null;
        }
        setNewMousePosition(e);
        Square squareAtMouse = getSquareAtMouse();
        if(squareAtMouse != null && squareAtMouse != clickedSquare) {
            focusedSquare = squareAtMouse;
            squareAtMouse.setIsFocused(true);
        }
        repaint(); //TODO: repaint only squares
    }

    public void flipBoard() {
        window.setUndoButtonEnabled(false);
        long initTime = System.currentTimeMillis();
        double initAngle = angle;
        int rotations = 0;
        int rotationNum = 40;
        while(rotations < rotationNum) {
            long timeNow = System.currentTimeMillis();
            if(timeNow - initTime > FPS) {
                angle += Math.PI / rotationNum;
                repaint();
                paintImmediately(0, 0, width, height);
                initTime = timeNow;
                rotations++;
            }
        }
        angle = Math.PI - initAngle;
        repaint();
        isBoardFlipped = !isBoardFlipped;
        window.setUndoButtonEnabled(true);
    }

    private void translateSelectedPiece(Square dest) {
        long initTime = System.currentTimeMillis();
        window.setUndoButtonEnabled(false);
        double paddingDistance = piecePadding + pieceSize / 2;
        Point pieceCoor = getPieceCoorForSquare(dest);
        double x = pieceCoor.getX();
        double y = pieceCoor.getY();
        double xDiff = x - selectedPiecePosition.getX();
        double yDiff = y - selectedPiecePosition.getY();

        // time of animation in millis
        double translateTime = 500;
        if(Math.abs(xDiff) < paddingDistance && Math.abs(yDiff) < paddingDistance) {
            translateTime = 200;
        }
        while(selectedPiecePosition.x != x || selectedPiecePosition.y != y) {
            long timeNow = System.currentTimeMillis();
            if(timeNow - initTime > FPS) {
                translateTime -= timeNow - initTime;
                double xSpeed = xDiff / translateTime;
                double ySpeed = yDiff / translateTime;
                if(translateTime > FPS) {
                    selectedPiecePosition.x += xSpeed * FPS;
                    selectedPiecePosition.y += ySpeed * FPS;
                }
                else {
                    selectedPiecePosition.x = (int) x;
                    selectedPiecePosition.y = (int) y;
                }
                repaint();
                paintImmediately(0, 0, width, height);
                initTime = timeNow;
                xDiff = x - selectedPiecePosition.x;
                yDiff = y - selectedPiecePosition.y;
            }
        }
        window.setUndoButtonEnabled(true);
    }

    private void mousePutDown() {
        boolean moveSuccessful = false;
        selectedPiece.highlightPossibleSquares(false);
        Square squareAtMouse = getSquareAtMouse();
        Move move = selectedPiece.getMove(squareAtMouse);
        if(squareAtMouse != null && move != null) {
            translateSelectedPiece(squareAtMouse);
            if(move instanceof PromotionMove) {
                PromotionMove promotionMove = (PromotionMove) move;
                Pawn pawn = (Pawn) promotionMove.getPiece();
                promotionMove.setPromotionType(pawn.askHumanWhichPromotion(this));
            }
            chessboard.makeMove(move);
            if(move instanceof CastleMove) {
                Rook rook = ((CastleMove) move).getCastlingRook();
                selectedPiece = rook;
                translateSelectedPiece(rook.getSquare());
            }
            moveSuccessful = true;
        }
        else {
            translateSelectedPiece(selectedPiece.getSquare());
        }
        selectedPiece = null;
        paintImmediately(0, 0, width, height);
        if(moveSuccessful) {
            game.newTurn();
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        Square squareAtMouse = getSquareAtMouse();
        if(squareAtMouse == null) {
            return;
        }
        Point pieceCoor = getPieceCoorForSquare(squareAtMouse);
        int visualOffset = 5;
        mousePosition.x = pieceCoor.x + visualOffset;
        mousePosition.y = pieceCoor.y - visualOffset;


        if(selectedPiece != null) {
            selectedPieceIsClicked = false;
            window.setUndoButtonEnabled(true);
            mousePutDown();
        }

        Piece piece = squareAtMouse.getHeldPiece();
        if(piece != null && piece.isSelectable()) {
            if(selectedPiece == null) {
                selectedPieceIsClicked = true;
                window.setUndoButtonEnabled(false);
            }

            selectedPiece = squareAtMouse.getHeldPiece();
            updateSelectedPiecePosition();
            selectedPiece.clearPossibleMoves();
            selectedPiece.findPossibleMoves();
            selectedPiece.highlightPossibleSquares(true);
        }
        clickOnSquare(squareAtMouse);
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(selectedPiece != null && !selectedPieceIsClicked) {
            mousePutDown();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(selectedPiece != null && !selectedPieceIsClicked) {
            setNewMousePosition(e);
            updateSelectedPiecePosition();
            repaint();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(selectedPiece != null && !selectedPieceIsClicked) {
            mousePosition.move(width + squareSize, height + squareSize);
            updateSelectedPiecePosition();
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        selectedPieceIsClicked = false;
        focusOnSquare(e);
        if(selectedPiece != null) {
            setNewMousePosition(e);
            updateSelectedPiecePosition();
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        focusOnSquare(e);
    }
}