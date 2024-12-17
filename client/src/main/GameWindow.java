package main;
import javax.swing.JFrame;
public class GameWindow {
    private JFrame window;

    public GameWindow(GamePanel gamePanel) {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false); // Make window not resizable
        window.setTitle("2D adventure");

        window.add(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

    }
}
