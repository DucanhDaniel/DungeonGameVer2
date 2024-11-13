package gamestates;

import inputs.KeyboardInputs;
import main.Game;
import utils.ImageLoader;
import utils.ImageManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static utils.Constants.Screen.*;
import static utils.HelpMethods.*;

public class Menu extends State implements Statemethods {
    public Menu(Game game) {
        super(game);
    }

    boolean previousResult = false;
    public boolean isPressedValid(boolean keyPressed) {
        if (keyPressed) {
            if (previousResult) return false;
            previousResult = true;
            return true;
        } 
        previousResult = false;
        return false;
    }

    public void update() {
        KeyboardInputs keyboardInputs = game.getKeyboardInputs();
        if (isPressedValid(keyboardInputs.upPressed || keyboardInputs.downPressed || keyboardInputs.enterPressed)) {
            if (keyboardInputs.downPressed) commandNumber++;
            else if (keyboardInputs.upPressed) commandNumber--;
            else if (keyboardInputs.enterPressed) {
                switch (commandNumber) {
                    case 0, 1:
                        Gamestate.state = Gamestate.PLAYING;
                        break;
                    case 2:
                        System.exit(0);
                        break;
                }
            }
            commandNumber = min(commandNumber, 2);
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
        BufferedImage image = imageManager.getPlayerImage("WALK", "NORMAL", direction, animationIndex + 1);
        g2.drawImage(image, x - 16 * 4, y - 16 * 4, 48 * 5, 64 * 5, null);

        // Menu
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
        String text = "New game";
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

        text = "Quit";
        x = getXForCenterText(text, g2);
        y += TILE_SIZE;
        g2.drawString(text, x, y);
        if (commandNumber == 2) {
            g2.drawString("->", x - TILE_SIZE, y);
        }
    }


}
