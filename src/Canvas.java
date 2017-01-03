/**
 * Created by alexsanchez on 2016-12-16.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener{

    private Window window;
    private int width;
    private int height;
    private double angle = 0;
    private static final int NUM_OF_SQUARES = 8;
    private static final int PADDING = 50;
    
    private int boardX;
    private int boardY;
    private int borderLength;
    private static final int BORDER_THICKNESS = 5;
    private int squareSize;
    private int piecePadding;
    private int pieceSize;

    private Chessboard chessboard;
    private Piece selectedPiece = null;
    private Square focusedSquare = null;
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
        chessboard = new Chessboard();
    }
    public void paint(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawPieces(g);
    }

    private void drawBoard(Graphics graphics) {
        Graphics2D gBoard = (Graphics2D) graphics.create();
        // rotate board for when flip board is called. Usually, angle has a value of 0 or PI
        gBoard.rotate(angle, width / 2, height / 2);
        // draw border
        gBoard.setColor(Color.YELLOW);
        gBoard.drawRect(boardX - BORDER_THICKNESS, boardY - BORDER_THICKNESS, borderLength, borderLength);
        gBoard.fillRect(boardX - BORDER_THICKNESS, boardY - BORDER_THICKNESS, borderLength, borderLength);
        // draw squares
        for(int i = 0; i < NUM_OF_SQUARES; i++) {
            for(int j = 0; j < NUM_OF_SQUARES; j++) {
                drawSquare(chessboard.getBoardSquares()[j][i], gBoard.create(), i, j);
            }
        }
    }

    private void drawSquare(Square square, Graphics g, int i, int j) {
        if(square.getColour() == Colour.WHITE) {
            g.setColor(Color.WHITE);
        }
        else {
            g.setColor(Color.LIGHT_GRAY);
        }
        Point squareCoor = getSquareCoor(j, i);
        g.drawRect(squareCoor.x, squareCoor.y, squareSize - 1, squareSize - 1);
        g.fillRect(squareCoor.x, squareCoor.y, squareSize - 1, squareSize - 1);
        Color borderColour = square.getBorderColour();
        // draw square border
        if(borderColour != null) {
            g.setColor(borderColour);
            int thickness = 3;
            for(int stroke = 0; stroke < thickness; stroke++) {
                g.drawRect(squareCoor.x + stroke, squareCoor.y + stroke, squareSize - 1 - stroke * 2, squareSize - 1 - stroke * 2);
            }
        }
    }

    private void drawPieces(Graphics g) {
        // draw
        if(selectedPiece != null) {
            Graphics2D g2MousePiece = (Graphics2D) g.create();
            g2MousePiece.drawImage(selectedPiece.getImage(), selectedPiecePosition.x - pieceSize / 2, selectedPiecePosition.y - pieceSize / 2, pieceSize, pieceSize, null);
            System.out.println(selectedPiecePosition.x);
        }
        Graphics2D g2SquarePieces = (Graphics2D) g.create();
        g2SquarePieces.rotate(angle, width / 2, height / 2);
        for(Piece piece : chessboard.getPieces()) {
            if(piece != selectedPiece) {
                Graphics2D g2SinglePiece = (Graphics2D) g2SquarePieces.create();
                Point pieceCoor = getSquareCoor(piece.getPosition().x, piece.getPosition().y);
                g2SinglePiece.rotate(-angle, pieceCoor.x + squareSize * 0.5, pieceCoor.y + squareSize * 0.5);
                g2SinglePiece.drawImage(piece.getImage(), pieceCoor.x + piecePadding, pieceCoor.y + piecePadding, pieceSize, pieceSize, null);
            }
        }
    }

    public void flipBoard() {
        long initTime = System.currentTimeMillis();
        double initAngle = angle;
        int rotations = 0;
        int rotationNum = 40;
        while(rotations < rotationNum) {
            long timeNow = System.currentTimeMillis();
            if(timeNow - initTime > FPS) {
                angle += Math.PI / rotationNum;
                System.out.println("Rotating...");
                repaint();
                paintImmediately(0, 0, width, height);
                initTime = timeNow;
                rotations++;
            }
        }
        angle = Math.PI - initAngle;
        repaint();
        isBoardFlipped = !isBoardFlipped;
    }

    private void translateSelectedPiece(Square dest) {
        long initTime = System.currentTimeMillis();
        Point squareCoor = getSquareCoor(dest.getPosition().x, dest.getPosition().y);
        int x = squareCoor.x + piecePadding + pieceSize / 2;
        int y = squareCoor.y + piecePadding + pieceSize / 2;
        // if board is flipped
        if(isBoardFlipped) {
            x = width - x;
            y = height - y;
        }
        int xDiff = x - selectedPiecePosition.x;
        int yDiff = y - selectedPiecePosition.y;
        int xDir = xDiff / Math.abs(xDiff);
        int yDir = yDiff / Math.abs(yDiff);
        while(selectedPiecePosition.x != x || selectedPiecePosition.y != y) {
            long timeNow = System.currentTimeMillis();
            if(timeNow - initTime > FPS) {
                if(xDiff != 0) {
                    selectedPiecePosition.x += xDir;
                }
                if(yDiff != 0) {
                    selectedPiecePosition.y += yDir;
                }
                repaint();
                paintImmediately(0, 0, width, height);
                initTime = timeNow;
                xDiff = x - selectedPiecePosition.x;
                yDiff = y - selectedPiecePosition.y;
            }
        }
    }

    private Point getSquareCoor(int x, int y) {
        return new Point(x * squareSize + boardX, y * squareSize + boardY);
    }

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
                return chessboard.getSquare(NUM_OF_SQUARES - 1 - x, NUM_OF_SQUARES - 1 - y);
            }
            // if the board is rotated 180 degrees, there is a slight offset by one pixel that is not accounted for (error)
            else {
                return chessboard.getSquare(x, y);
            }
        }
    }

    public void calculateSize() {
        Dimension size = window.getDimensions();
        setSize(size);
        width = size.width;
        height = size.height;
        calculateBoardSizes();
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

    private void setNewMousePosition(MouseEvent e) {
        int yOffset = 2; // offset by two pixels due to the way the mouse interacts with the JFrame
        mousePosition.move(e.getX(), e.getY() - yOffset);
    }

    private void updateSelectedPiecePosition() {
        selectedPiecePosition.x = mousePosition.x;
        selectedPiecePosition.y = mousePosition.y;
    }

    private void focusOnSquare(MouseEvent e) {
        if(focusedSquare != null) {
            focusedSquare.setIsFocused(false);
        }
        setNewMousePosition(e);
        Square squareAtMouse = getSquareAtMouse();
        if(squareAtMouse != null) {
            focusedSquare = squareAtMouse;
            squareAtMouse.setIsFocused(true);
        }
        repaint(); //TODO: repaint only squares
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        setNewMousePosition(e);
        Square squareAtMouse = getSquareAtMouse();
        if(squareAtMouse != null) {
            selectedPiece = squareAtMouse.getHeldPiece();
            updateSelectedPiecePosition();
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(selectedPiece == null) {
            return;
        }
        Square squareAtMouse = getSquareAtMouse();
        if(squareAtMouse != null) {
            selectedPiece.getSquare().setHeldPiece(null);
            squareAtMouse.setHeldPiece(selectedPiece);  // !! this line is why pieces disappear "forever" when another piece goes on its square
            selectedPiece.setPosition(squareAtMouse.getPosition().x, squareAtMouse.getPosition().y);
            translateSelectedPiece(squareAtMouse);
        }
        selectedPiece = null;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(selectedPiece != null) {
            setNewMousePosition(e);
            updateSelectedPiecePosition();
            repaint();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(selectedPiece != null) {
            mousePosition.move(width + squareSize, height + squareSize);
            updateSelectedPiecePosition();
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
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