/**
 * Created by alexsanchez on 2016-12-16.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Window extends JFrame {

    //22 pixels compensates for the height of the header bar of a standard MacOS window
    private static final int FRAME_HEADER_HEIGHT = 22;
    private static final int MIN_FRAME_WIDTH = 500;
    private static final int MIN_FRAME_HEIGHT = MIN_FRAME_WIDTH + FRAME_HEADER_HEIGHT;
    private static final int INIT_FRAME_WIDTH = 800;
    private static final int INIT_FRAME_HEIGHT = INIT_FRAME_WIDTH + FRAME_HEADER_HEIGHT;
    private int width;
    private int height;

    public Window() {
        super("Chess");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT));
        setSize(new Dimension(INIT_FRAME_WIDTH, INIT_FRAME_HEIGHT));
        Canvas canvas = new Canvas(this);
        add(canvas);
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
        //canvas.flipBoard(); // testing functionality
    }

    public Dimension getDimensions() {
        return new Dimension(width, height);
    }

}
