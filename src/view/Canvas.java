/**
 * Created by alexsanchez on 2016-12-16.
 */

package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import engine.Colour;
import engine.PieceType;

public class Canvas extends JPanel implements View, MouseListener, MouseMotionListener{

    private Window window;
    private int width;
    private int height;
    private double angle = 0;
    private static final int PADDING = 50;
    private static final int NUM_OF_SQUARES = 8;
    
    private int boardX;
    private int boardY;
    private int borderLength;
    private static final int BORDER_THICKNESS = 5;
    private int squareSize;
    private int piecePadding;
    private int pieceSize;

    private Presenter presenter;
    private ArrayList<Square> highlightSquares = null;
    private Square initSquare = null;
    private Piece selectedPiece = null;
    private boolean selectedPieceIsClicked = false;
    private Square focusedSquare = null;
    private Square clickedSquare = null;
    private Point mousePosition = new Point();
    private Point selectedPiecePosition = new Point();
    private boolean isBoardFlipped = false;
    private boolean mouseEventsEnabled = true;

    private String msg = "";

    private static final int FPS = 30;

    public Canvas(Window window, Presenter presenter, int width, int height) {
        this.window = window;
        this.presenter = presenter;
        recalculateSizes(width, height);
        setBackground(Color.DARK_GRAY);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void update() {
        repaint();
    }

    @Override
    public void setEventsEnabled(boolean enabled) {
        mouseEventsEnabled = enabled;
    }

    @Override
    public void recalculateSizes(int width, int height) {
        this.width = width;
        this.height = height;
        calculateBoardSizes();
    }

    @Override
    public void highlightSquares(ArrayList<Square> squares, boolean highlight, boolean lastMove) {
        for(Square square : squares) {
            if(highlight) {
                if(lastMove) {
                    square.setHighlightColour(Color.ORANGE);
                } else {
                    square.setHighlightColour(Color.PINK);
                }
            } else {
                square.setHighlightColour(null);
            }
        }
        if(highlight) {
            highlightSquares = squares;
        } else {
            highlightSquares = null;
        }
    }

    public void unhighlightMoves() {
        if(highlightSquares != null) {
            for(Square square : highlightSquares) {
                square.setHighlightColour(null);
            }
        }
    }

    @Override
    public void setDisplayMessage(String msg) {
        this.msg = msg;
    }

    //=======================================================
    // DRAWING FUNCTIONS
    @Override
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
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                drawSquare(presenter.getSquares()[j][i], gBoard.create(), i, j);
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
        g.fillRect(squareCoor.x, squareCoor.y, squareSize, squareSize);

        // draw the square highlight
        Color highlightColour = square.getHighlightColour();
        if(highlightColour != null) {
            Composite original = g.getComposite();
            g.setComposite(square.getComposite());
            g.setColor(highlightColour);
            g.fillRect(squareCoor.x, squareCoor.y, squareSize, squareSize);
            g.setComposite(original);
        }
        // draw square border
        Color borderColour = square.getBorderColour();
        if(borderColour != null) {
            g.setColor(borderColour);
            int thickness = 3;
            g.setStroke(new BasicStroke(thickness));
            // rotating the board for some reason causes an offset when drawing the borders
            int offset = 1;
            if(isBoardFlipped) {
                offset = 2;
            }
            g.drawRect(squareCoor.x + offset, squareCoor.y + offset, squareSize - thickness, squareSize - thickness);
        }
    }

    private void drawPieces(Graphics g) {

        // draw other board pieces
        Graphics2D g2SquarePieces = (Graphics2D) g.create();
        Composite originalComposite = g2SquarePieces.getComposite();
        g2SquarePieces.rotate(angle, width / 2, height / 2);
        for(Piece piece : presenter.getPieces()) {
            if(piece != selectedPiece) {
                Graphics2D g2SinglePiece = (Graphics2D) g2SquarePieces.create();
                Point pieceCoor = getSquareCoor(piece.getX(), piece.getY());
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

            // draw shadow
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
        if(msg.equals("")) {
            return;
        }
        int size = (int) (Math.min(width, height) * 0.1);
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, size));
        g.drawString(msg, width / 2 - (msg.length() / 3 * size), height / 2);
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

    private void calculateBoardSizes() {
        squareSize = (Math.min(width, height) - PADDING * 2) / NUM_OF_SQUARES;
        borderLength = squareSize * NUM_OF_SQUARES + BORDER_THICKNESS * 2;
        boardX = (width - borderLength) / 2 + BORDER_THICKNESS;
        boardY = (height - borderLength) / 2 + BORDER_THICKNESS;
        piecePadding = (int) (0.1 * squareSize); // make image padding a percentage amount of the image size
        pieceSize = squareSize - piecePadding * 2;
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
        if(x < 0 || x >= NUM_OF_SQUARES * (squareSize) || y < 0 || y >= NUM_OF_SQUARES * (squareSize)) {
            return null;
        }
        else {
            x /= squareSize;
            y /= squareSize;
            if(isBoardFlipped) {
                return presenter.getSquares()[NUM_OF_SQUARES - 1 - x][NUM_OF_SQUARES - 1 - y];
            }
            // if the board is rotated 180 degrees, there is a slight offset by one pixel that is not accounted for (error)
            else {
                return presenter.getSquares()[x][y];
            }
        }
    }

    private void setNewMousePosition(MouseEvent e) {
        int yOffset = 2; // offset by two pixels due to the way the mouse interacts with the JFrame
        mousePosition.move(e.getX(), e.getY() - yOffset);
    }

    public void setSelectedPiece(Piece piece) {
        selectedPiece = piece;
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
        window.setMenuItemsEnabled(false);
        long initTime = System.currentTimeMillis();
        double initAngle = angle;
        int rotations = 0;
        int rotationNum = 30;//40
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
        window.setMenuItemsEnabled(true);
    }

    public void setBoardRotation(boolean flipped) {
        if(flipped) {
            angle = Math.PI;
        } else {
            angle = 0;
        }
        isBoardFlipped = flipped;
        repaint();
    }

    //public void translatePiece(Piece piece, Square dest)
    public void translateSelectedPiece(Square dest) {
        long initTime = System.currentTimeMillis();
        window.setMenuItemsEnabled(false);
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
        window.setMenuItemsEnabled(true);

    }

    public void moveFailed() {
        translateSelectedPiece(initSquare);
        initSquare = null;
        selectedPiece = null;
    }

    public void moveSuccess(Square destination) {
        translateSelectedPiece(destination);
        initSquare = null;
        selectedPiece = null;
    }

    @Override
    public PieceType askWhichPromotion() {
        Object[] promotionOptions = { PieceType.QUEEN, PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP };
        Object selectedOption = JOptionPane.showInputDialog(this, "Choose One", "PROMOTION",
                JOptionPane.PLAIN_MESSAGE, null, promotionOptions, promotionOptions[0]);
        if(selectedOption == null) {
            return PieceType.QUEEN; //if the JOptionPane was closed without an option being selected
        }
        return (PieceType) selectedOption;
    }

    public void movePiece(Piece piece, Square init, Square destination) {
        Piece temp = selectedPiece;
        Point tempPosition = selectedPiecePosition;
        selectedPiece = piece;
        selectedPiecePosition = getPieceCoorForSquare(init);
        translateSelectedPiece(destination);
        selectedPiece = temp;
        selectedPiecePosition = tempPosition;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if(!mouseEventsEnabled) {
            return;
        }
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
            clickOnSquare(squareAtMouse);
            unhighlightMoves();
            presenter.movedPiece(initSquare, squareAtMouse);
            initSquare = null;
            window.setMenuItemsEnabled(true);

        } else {
            selectedPiece = presenter.selectedSquare(squareAtMouse);
            if(selectedPiece != null) {
                initSquare = squareAtMouse;
                selectedPieceIsClicked = true;
                window.setMenuItemsEnabled(false);
                updateSelectedPiecePosition();
                clickOnSquare(squareAtMouse);
                repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!mouseEventsEnabled) {
            return;
        }
        if(selectedPiece != null && !selectedPieceIsClicked) {
            unhighlightMoves();
            presenter.movedPiece(initSquare, getSquareAtMouse());
            initSquare = null;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(!mouseEventsEnabled) {
            return;
        }
        if(selectedPiece != null && !selectedPieceIsClicked) {
            setNewMousePosition(e);
            updateSelectedPiecePosition();
            repaint();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(!mouseEventsEnabled) {
            return;
        }
        if(selectedPiece != null && !selectedPieceIsClicked) {
            mousePosition.move(width + squareSize, height + squareSize);
            updateSelectedPiecePosition();
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(!mouseEventsEnabled) {
            return;
        }
        selectedPieceIsClicked = false;
        if(selectedPiece != null) {
            setNewMousePosition(e);
            updateSelectedPiecePosition();
        }
        focusOnSquare(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!mouseEventsEnabled) {
            return;
        }
        focusOnSquare(e);
    }

}
