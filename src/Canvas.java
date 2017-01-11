/**
 * Created by alexsanchez on 2016-12-16.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Locale;

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
        this.game = new Game(this);
    }

    //=======================================================
    // DRAWING FUNCTIONS
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
        for(Piece piece : chessboard.getPieces()) {
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

    //========================================================
    // PIXEL CALCULATIONS

    private Point getSquareCoor(int x, int y) {
        return new Point(x * squareSize + boardX, y * squareSize + boardY);
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

    public void flipBoard() {
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
    }

    private void translateSelectedPiece(Square dest) {
        long initTime = System.currentTimeMillis();
        // distance at which speed of piece will become one pixel per frame
        int slowDownDistance = piecePadding + pieceSize / 2;

        Point squareCoor = getSquareCoor(dest.getPosition().x, dest.getPosition().y);
        int x = squareCoor.x + slowDownDistance;
        int y = squareCoor.y + slowDownDistance;
        // if board is flipped
        if(isBoardFlipped) {
            x = width - x;
            y = height - y;
        }
        int xDiff = x - selectedPiecePosition.x;
        int yDiff = y - selectedPiecePosition.y;
        int xDir = 0;
        int yDir = 0;
        int xDirMultiplier = 1;
        int yDirMultiplier = 1;
        int fastSpeed = 10;
        if(xDiff < 0) {
            xDirMultiplier = -1;
        }
        if(Math.abs(xDiff) > slowDownDistance) {
            xDir = fastSpeed * xDirMultiplier;
        }
        if(yDiff < 0) {
            yDirMultiplier = -1;
        }
        if(Math.abs(yDiff) > slowDownDistance) {
            yDir = fastSpeed * yDirMultiplier;
        }
        while(selectedPiecePosition.x != x || selectedPiecePosition.y != y) {
            long timeNow = System.currentTimeMillis();
            if(timeNow - initTime > FPS) {
                if(Math.abs(xDiff) <= slowDownDistance) {
                    xDir = xDirMultiplier;
                }
                if(Math.abs(yDiff) <= slowDownDistance) {
                    yDir = yDirMultiplier;
                }
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

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        setNewMousePosition(e);
        Square squareAtMouse = getSquareAtMouse();
        if(squareAtMouse != null) {
            Piece piece = squareAtMouse.getHeldPiece();
            if(piece != null && piece.isSelectable()) {
                selectedPiece = squareAtMouse.getHeldPiece();
                updateSelectedPiecePosition();
                selectedPiece.clearPossibleMoves();
                selectedPiece.findPossibleMoves();
                selectedPiece.highlightPossibleSquares(true);
            }
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(selectedPiece == null) {
            return;
        }
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
        if(moveSuccessful) {
            game.newTurn();
        }
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