package gamestates.auth;

import gamestates.Gamestate;
import gamestates.State;
import gamestates.Statemethods;
import inputs.KeyboardInputs;
import main.Game;
import utils.ImageLoader;
import utils.ImageManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.Screen.*;
import static utils.HelpMethods.*;

public class Login extends State implements Statemethods{
    private String username = "";
    private String password = "";
    private boolean enteringPassword = false;
    private String message = "Press Enter to Login or Signup";

    private KeyboardInputs keyboardInputs;

    public Login(Game game) {
        super(game);
        keyboardInputs = game.getKeyboardInputs();
    }

    public void handleKeyboardInputs(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_ENTER) {
            if (!keyboardInputs.enterPressed) {
                handleSubmit();
                keyboardInputs.enterPressed = false;
            }
            else keyboardInputs.enterPressed = true;
        } else if (code == KeyEvent.VK_BACK_SPACE) {
            if (!keyboardInputs.deletePressed) {
                if (enteringPassword) {
                    if (!password.isEmpty()) {
                        password = password.substring(0, password.length() - 1);
                    }
                } else {
                    if (!username.isEmpty()) {
                        username = username.substring(0, username.length() - 1);
                    }
                }
            }
            keyboardInputs.deletePressed = true;
        } else if (code == KeyEvent.VK_TAB) {
            if (!keyboardInputs.tabPressed) enteringPassword = !enteringPassword;
            keyboardInputs.tabPressed = true;
        } else {
            char prevKeyChar = keyboardInputs.keyChar;
            char keyChar = e.getKeyChar();
            if (prevKeyChar == '\0' && Character.isLetterOrDigit(keyChar)) {
                if (enteringPassword) {
                    password += keyChar;
                } else {
                    username += keyChar;
                }
                System.out.println("Username: " + username + ", Password: " + password);
            }
            keyboardInputs.keyChar = keyChar;
        }
    }

    private void handleSubmit() {
        game.getAuthSystem().authenticate(username, password);
    }

    public void update() {

    }

//    public void draw(Graphics2D g) {
//        g.setColor(Color.BLACK);
//        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
//
//        // Tiêu đề
//        g.setColor(Color.WHITE);
//        g.setFont(new Font("Arial", Font.BOLD, 24));
//        g.drawString("Login/Signup", SCREEN_WIDTH / 2 - 80, 100);
//
//        // Tên đăng nhập
//        g.setFont(new Font("Arial", Font.PLAIN, 18));
//        g.drawString("Username: " + username, 50, 200);
//
//        // Mật khẩu
//        String displayPassword = "*".repeat(password.length());
//        g.drawString("Password: " + displayPassword, 50, 250);
//
//        // Thông báo
//        g.drawString(message, 50, 300);
//    }

    int frameCounter = 0;
    int directionIndex = 0, animationIndex = 0, commandNumber = 0;
    String[] directionArray = {"down", "left_down", "left", "left_up", "up", "right_up", "right", "right_down"};

    public void draw(Graphics2D g2) {
        Font maruMonica = loadFont("MaruMonica");
        g2.setFont(maruMonica);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        g2.setColor(Color.WHITE);
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Create shadow for
        g2.setColor(Color.GRAY);
        String gameTitle = "Dungeon Game";
        int x = getXForCenterText(gameTitle, g2);
        int y = getYForCenterText(gameTitle, g2) - TILE_SIZE * 3;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80F));
        g2.drawString(gameTitle, x + 5, y + 3);

// Title name
        g2.setColor(Color.WHITE);
        g2.drawString(gameTitle, x, y);

        // Display main character
        x = SCREEN_WIDTH / 2 - TILE_SIZE;
        y += TILE_SIZE / 2;
        String direction;
        frameCounter++;
        if (frameCounter >= 60) {
            directionIndex = (directionIndex + 1) % 8;
            frameCounter = 0;
        }
        if (frameCounter % 5 == 0) {
            animationIndex = (animationIndex + 1) % 8;
        }
        direction = directionArray[directionIndex];

        ImageLoader.initialize();
        ImageManager imageManager = ImageLoader.imageManager;
        BufferedImage image = imageManager.getPlayerImage("WALK", "NORMAL", direction, animationIndex, 3 * TILE_SIZE, 4 * TILE_SIZE);
        g2.drawImage(image, x - 16 * 4, y - 16 * 4, 48 * 5, 64 * 5, null);


        y += TILE_SIZE * 2 + 110;
        String usernamePrompt = "Username: " + username;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 26F));
        g2.drawString(usernamePrompt, SCREEN_WIDTH / 2 - 200, y);

        y += TILE_SIZE;
        String displayPassword = "*".repeat(password.length());
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 26F));
        g2.drawString("Password: " + displayPassword, SCREEN_WIDTH / 2 - 200, y);


    }
}
