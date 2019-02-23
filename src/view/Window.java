/**
 * Created by alexsanchez on 2016-12-16.
 */

package view;
import engine.Chessboard;
import engine.Game;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Window extends JFrame {

    private static final String NAME = "CHESS";
    //22 pixels compensates for the height of the header bar of a standard MacOS window
    private static final int FRAME_HEADER_HEIGHT = 22;
    private static final int MIN_FRAME_WIDTH = 500;
    private static final int MIN_FRAME_HEIGHT = MIN_FRAME_WIDTH + FRAME_HEADER_HEIGHT;
    private static final int INIT_FRAME_WIDTH = 800;
    private static final int INIT_FRAME_HEIGHT = INIT_FRAME_WIDTH + FRAME_HEADER_HEIGHT;
    private ArrayList<JMenuItem> menuItems = new ArrayList<>();

    public Window() {
        super(NAME);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT));
        setSize(new Dimension(INIT_FRAME_WIDTH, INIT_FRAME_HEIGHT));


        Presenter presenter = new Presenter();

        Canvas canvas = new Canvas(this, presenter, INIT_FRAME_WIDTH, INIT_FRAME_HEIGHT);
        presenter.setView(canvas);
        add(canvas);
        addMenuBar(presenter);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                int width = getContentPane().getSize().width;
                int height = getContentPane().getSize().height;
                canvas.recalculateSizes(width, height);
            }
        });
        presenter.startNewGame();
        setVisible(true);
    }

    private void addMenuBar(Presenter presenter) {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newGameItem = new JMenuItem("New Game");
        menuItems.add(newGameItem);
        fileMenu.add(newGameItem);
        newGameItem.addActionListener(e -> {
            presenter.newGameButtonClicked();
        });
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem undoItem = new JMenuItem("Undo");
        menuItems.add(undoItem);
        editMenu.add(undoItem);
        undoItem.addActionListener(e -> {
            presenter.undoButtonClicked();
        });
        menuBar.add(editMenu);
        setJMenuBar(menuBar);
    }

    public void setMenuItemsEnabled(boolean enabled) {
        for(JMenuItem menuItem : menuItems) {
            menuItem.setEnabled(enabled);
        }
    }

}
