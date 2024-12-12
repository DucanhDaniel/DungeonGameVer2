package gamestates;

import entities.Entity;
import entities.Player;
import inputs.KeyboardInputs;
import main.Game;
import utils.Constants;
import utils.ImageLoader;
import utils.ImageManager;

import java.awt.*;
import java.awt.image.BufferedImage;

import static inputs.KeyboardInputs.isPressedValid;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static utils.Constants.Screen.*;
import static utils.HelpMethods.*;

public class Selection extends State implements Statemethods {
    Player player;
    public static int playerType = -1;

    public Selection(Game game) {
        super(game);
        player = game.getPlaying().getPlayer();
    }

    public void update() {
        KeyboardInputs keyboardInputs = game.getKeyboardInputs();
        if (isPressedValid("up", keyboardInputs.upPressed) ||
                isPressedValid("down", keyboardInputs.downPressed) ||
                isPressedValid("enter", keyboardInputs.enterPressed)) {
            if (keyboardInputs.downPressed) commandNumber++;
            else if (keyboardInputs.upPressed) commandNumber--;
            else if (keyboardInputs.enterPressed) {
                setPlayerStats(commandNumber, true);
                playerType = commandNumber;
                game.startNewGame();
            }
            commandNumber = min(commandNumber, 2);
            commandNumber = max(commandNumber, 0);
        }
    }
    public void setPlayerStats(int playerType, boolean setCurrent) {
        System.out.println(playerType);
        switch (playerType) {
            case 0: // Xa thu
                player.speed = 4;
                player.maxArmor = 10;
                player.maxHealth = 12;
                player.maxMana = 30;
                player.attackPointSpear = 50;
                player.attackPointGun = 5;
                break;
            case 1: // Dau si
                player.speed = 3;
                player.maxArmor = 10;
                player.maxHealth = 20;
                player.maxMana = 50;
                player.attackPointSpear = 10;
                player.attackPointGun = 2;
                break;
            case 2: // Sat thu
                player.speed = 5;
                player.maxArmor = 10;
                player.maxHealth = 8;
                player.maxMana = 50;
                player.attackPointSpear = 5;
                player.attackPointGun = 2;
                break;
        }
        if (setCurrent) {
            player.currentArmor = player.maxArmor;
            player.currentHealth = player.maxHealth;
            player.currentMana = player.maxMana;
        }
        Gamestate.state = Gamestate.PLAYING;

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

        // Selection
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
        String text = "Markman";
        x = getXForCenterText(text, g2);
        y = getYForCenterText(text, g2) + TILE_SIZE * 3 / 2;
        g2.drawString(text, x, y);
        if (commandNumber == 0) {
            g2.drawString("->", x - TILE_SIZE, y);
        }

        text = "Fighter";
        x = getXForCenterText(text, g2);
        y += TILE_SIZE;
        g2.drawString(text, x, y);
        if (commandNumber == 1) {
            g2.drawString("->", x - TILE_SIZE, y);
        }

        text = "Slayers";
        x = getXForCenterText(text, g2);
        y += TILE_SIZE;
        g2.drawString(text, x, y);
        if (commandNumber == 2) {
            g2.drawString("->", x - TILE_SIZE, y);
        }
    }
}
