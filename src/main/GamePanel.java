package main;

import inputs.KeyboardInputs;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.Key;
import javax.swing.JPanel;

import static java.awt.Color.BLACK;
import static utils.Constants.Screen.SCREEN_WIDTH;
import static utils.Constants.Screen.SCREEN_HEIGHT;
//import inputs.KeyboardInputs;

public class GamePanel extends JPanel{
    private final Game game;
    private final KeyboardInputs keyboardInputs = new KeyboardInputs(this);
    private int mouseX, mouseY;
    public GamePanel(Game game) {
        this.game = game;
        setPanelSize();
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
//                System.out.println("Mouse position: (" + mouseX + ", " + mouseY + ")");
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

    }

    private void setPanelSize() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(BLACK);
        setDoubleBuffered(true); // Make game render better
        addKeyListener(keyboardInputs);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
//        this.eventHandler = new EventHandler(this);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render((Graphics2D)g);
    }

    public Game getGame() {
        return game;
    }
    public KeyboardInputs getKeyboardInputs() {
        return keyboardInputs;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }
}
