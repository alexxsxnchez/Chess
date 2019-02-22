/**
 * Created by alexsanchez on 2016-12-16.
 */
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;

public class Window extends JFrame {

    private static final String NAME = "CHESS";
    //22 pixels compensates for the height of the header bar of a standard MacOS window
    private static final int FRAME_HEADER_HEIGHT = 22;
    private static final int MIN_FRAME_WIDTH = 500;
    private static final int MIN_FRAME_HEIGHT = MIN_FRAME_WIDTH + FRAME_HEADER_HEIGHT;
    private static final int INIT_FRAME_WIDTH = 800;
    private static final int INIT_FRAME_HEIGHT = INIT_FRAME_WIDTH + FRAME_HEADER_HEIGHT;
    private int width;
    private int height;
    private boolean undoEnabled = true;

    public Window() {
        super(NAME);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT));
        setSize(new Dimension(INIT_FRAME_WIDTH, INIT_FRAME_HEIGHT));
        Canvas canvas = new Canvas(this);
        add(canvas);
        addMenuBar(canvas);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                width = getContentPane().getSize().width;
                height = getContentPane().getSize().height;
                canvas.calculateSize();
            }
        });
        setVisible(true);
        canvas.startGame();
        //canvas.flipBoard(); // testing functionality
    }

    private void addMenuBar(Canvas canvas) {
        JMenuBar menuBar = new JMenuBar();
        JMenu undoButton = new JMenu("Undo");
        undoButton.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                if(undoEnabled) {
                    canvas.getChessboard().takeBackLastTwoMoves();
                    canvas.repaint();
                }
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        menuBar.add(undoButton);
        setJMenuBar(menuBar);
    }

    public void setUndoButtonEnabled(boolean enabled) {
        undoEnabled = enabled;
    }

    public Dimension getDimensions() {
        return new Dimension(width, height);
    }

}
