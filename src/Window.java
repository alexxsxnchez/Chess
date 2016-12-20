/**
 * Created by alexsanchez on 2016-12-16.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Window extends JFrame {


    private static final int MIN_FRAME_WIDTH = 500;
    private static final int MIN_FRAME_HEIGHT = MIN_FRAME_WIDTH + 22; //22 pixels compensates for the header of the window
    private int width;
    private int height;

    public Window() {
        super("Graphics");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT));
        Canvas canvas = new Canvas(this);
        add(canvas);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                width = getContentPane().getSize().width;
                height = getContentPane().getSize().height;
                canvas.resetSize();
            }
        });
        //pack();
        setVisible(true);
        canvas.rotateBoard();
    }

    public Dimension getDimension() {
        return new Dimension(width, height);
    }

}
