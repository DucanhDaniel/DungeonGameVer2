package gamestates;

import inputs.KeyboardInputs;
import main.Game;
import utils.ImageLoader;
import utils.ImageManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import static inputs.KeyboardInputs.isPressedValid;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static utils.Constants.Screen.*;
import static utils.HelpMethods.*;

public class Menu extends State implements Statemethods {
    public Menu(Game game) {
        super(game);
    }

    private boolean needDisplayContinuePlaying = false;
    public void update() {
        KeyboardInputs keyboardInputs = game.getKeyboardInputs();
        if (isPressedValid("up", keyboardInputs.upPressed) ||
                isPressedValid("down", keyboardInputs.downPressed) ||
                isPressedValid("enter", keyboardInputs.enterPressed)) {
            if (keyboardInputs.downPressed) commandNumber++;
            else if (keyboardInputs.upPressed) commandNumber--;
            else if (keyboardInputs.enterPressed) {
                switch (commandNumber) {
                    case 0:
                        if(!needDisplayContinuePlaying) Gamestate.state = Gamestate.SELECTION;
                        else {
                            Gamestate.state = Gamestate.PLAYING;
                            game.continueGame();
                        }
                        needDisplayContinuePlaying = true; // Nghia la game dang duoc choi roi
                        break;
                    case 1:
                        game.getPlaying().getSaveLoadSystem().loadSavedGame(game.getAuthSystem().username);
                        Gamestate.state = Gamestate.PLAYING;
                        break;
                    case 2:
                        Gamestate.state = Gamestate.SETTING;
                        break;
                    case 3:
                        Gamestate.state = Gamestate.VIEW_RANK;
                        game.getViewRank().loadRank();
                        break;
                    case 4:
                        System.exit(0);
                        break;
                }
            }
            commandNumber = min(commandNumber, 4);
            commandNumber = max(commandNumber, 0);
        }

    }

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

        // Menu
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
        String text = "New game";
        if (needDisplayContinuePlaying)
            text = "Continue game";
        x = getXForCenterText(text, g2);
        y = getYForCenterText(text, g2) + TILE_SIZE * 3 / 2;
        g2.drawString(text, x, y);
        if (commandNumber == 0) {
            g2.drawString("->", x - TILE_SIZE, y);
        }

        text = "Load game";
        x = getXForCenterText(text, g2);
        y += TILE_SIZE;
        g2.drawString(text, x, y);
        if (commandNumber == 1) {
            g2.drawString("->", x - TILE_SIZE, y);
        }

        text = "Settings";
        x = getXForCenterText(text, g2);
        y += TILE_SIZE;
        g2.drawString(text, x, y);
        if (commandNumber == 2) {
            g2.drawString("->", x - TILE_SIZE, y);
        }

        text = "Ranking";
        x = getXForCenterText(text, g2);
        y += TILE_SIZE;
        g2.drawString(text, x, y);
        if (commandNumber == 3) {
            g2.drawString("->", x - TILE_SIZE, y);
        }

        text = "Quit";
        x = getXForCenterText(text, g2);
        y += TILE_SIZE;
        g2.drawString(text, x, y);
        if (commandNumber == 4) {
            g2.drawString("->", x - TILE_SIZE, y);
        }

        String username = game.getAuthSystem().username;
        String welcomeText = "Welcome " + (username == null ? "mock" : username) + "!";
        g2.setColor(Color.GREEN);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 25F));
        g2.drawString(welcomeText, 20, 30);
    }


}
