/**
 * Created by alexsanchez on 2016-12-16.
 */

import javax.swing.*;
import java.awt.*;

public class Canvas extends JPanel {

    private Window window;
    private int width;
    private int height;
    private double angle = 0;
    private static final int NUM_OF_SQUARES = 8;
    private static final int PADDING = 50;
    private Chessboard chessboard;

    public Canvas(Window window) {
        this.window = window;
        resetSize();
        setBackground(Color.DARK_GRAY);
        chessboard = new Chessboard();
    }
    public void paint(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        //drawLine(g);
    }
    public void drawLine(Graphics g) {
        g.setColor(Color.RED);
        g.drawLine(70, 60, 500, 500);
    }
    public void drawBoard(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();

        int smallestDimension = Math.min(width, height);
        int squareSize = (smallestDimension - PADDING * 2) / NUM_OF_SQUARES;
        int borderThickness = 5;
        int borderLength = squareSize * NUM_OF_SQUARES + borderThickness * 2;
        int startX = (width - borderLength) / 2;
        int startY = (height - borderLength) / 2;
        g.rotate(angle, width / 2, height / 2);
        // draw border
        g.setColor(Color.BLACK);
        g.fillRect(startX, startY, borderLength, borderLength);
        // draw squares
        Square[][] boardSquares = chessboard.getBoardSquares();
        for(int i = 0; i < NUM_OF_SQUARES; i++) {
            for(int j = 0; j < NUM_OF_SQUARES; j++) {
                Square square = boardSquares[j][i];
                if(square.getColour() == Colour.WHITE) g.setColor(Color.WHITE);
                else g.setColor(Color.BLUE);
                g.fillRect(startX + borderThickness + squareSize * j, startY + borderThickness + squareSize * i, squareSize, squareSize);
                // draw piece for square
                Piece piece = square.getHeldPiece();
                if(piece != null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.rotate(-angle, startX + borderThickness + squareSize * (j + 0.5), startY + borderThickness + squareSize * (i + 0.5));
                    g2.drawImage(piece.getImage(), startX + borderThickness + 3 + squareSize * j, startY + borderThickness + 3 + squareSize * i, squareSize - 6, squareSize - 6, null);
                }
            }
        }
/*
        Graphics2D g2 = (Graphics2D) g.create();
        g2.rotate(-angle, startX + borderThickness + squareSize * (0 + 0.5), startY + borderThickness + squareSize * (1 + 0.5));
        g2.drawImage(getImage("resources/whiteknight.png"), startX + borderThickness + squareSize * 0, startY + borderThickness + squareSize * 1, squareSize, squareSize, null);
*/
    }

    public void rotateBoard() {
        //int smallestDimension = Math.min(width, height);
        long initTime = System.currentTimeMillis();
        while(Math.abs(angle - Math.PI) > 0.0001) {
            long timeNow = System.currentTimeMillis();
            if(timeNow - initTime > 30) {
                angle += Math.PI / 40;
                System.out.println("Rotating...");
                repaint();
                initTime = timeNow;
            }

        }

        //g.rotate()
        //while(angle)
        //g.rotate(Math.PI / 4, width / 2, height / 2);
    }


    public void resetSize() {
        Dimension size = window.getDimension();
        setSize(size);
        width = size.width;
        height = size.height;
    }

    private Image getImage(String fileName) {
        try {
            return new ImageIcon(this.getClass().getResource(fileName)).getImage();
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error, there was a problem with getting the image.");
            return null;
        }
    }

}
