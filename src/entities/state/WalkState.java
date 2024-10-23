package entities.state;

import entities.Entity;
import entities.Player;
import entities.Sprite;
import utils.Constants;
import utils.HelpMethods;
import static utils.Constants.Player.*;
import static utils.Constants.Screen.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class WalkState extends EntityState{
    public WalkState(Entity currentEntity, String imageFolderPath, int numAnimationsFrames, int numType) {
        super(currentEntity, imageFolderPath, numAnimationsFrames, numType);
        loadImages();
    }

    public void loadImages() {
        for (int type = 0; type < numType; type++) {
            for (int i = 0; i < numAnimationsFrames; i++) {
                up[i][type] = HelpMethods.setUp(imageFolderPath + "up" + (i + 1), currentEntity.width, currentEntity.height);
                up[i][type] = HelpMethods.setUp(imageFolderPath + "up" + (i + 1), currentEntity.width, currentEntity.height);
                down[i][type] = HelpMethods.setUp(imageFolderPath + "down" + (i + 1), currentEntity.width, currentEntity.height);
                left[i][type] = HelpMethods.setUp(imageFolderPath + "left" + (i + 1), currentEntity.width, currentEntity.height);
                right[i][type] = HelpMethods.setUp(imageFolderPath + "right" + (i + 1), currentEntity.width, currentEntity.height);
                up_left[i][type] = HelpMethods.setUp(imageFolderPath + "up_left" + (i + 1), currentEntity.width, currentEntity.height);
                up_right[i][type] = HelpMethods.setUp(imageFolderPath + "up_right" + (i + 1), currentEntity.width, currentEntity.height);
                down_left[i][type] = HelpMethods.setUp(imageFolderPath + "down_left" + (i + 1), currentEntity.width, currentEntity.height);
                down_right[i][type] = HelpMethods.setUp(imageFolderPath + "down_right" + (i + 1), currentEntity.width, currentEntity.height);
            }
        }
    }

    @Override
    public void handleInput(Sprite sprite) {
        Player player = (Player)sprite;
        if (currentEntity.getClass().equals(Player.class)) {
            if (player.keyHandler.upPressed) {
                if (player.keyHandler.leftPressed) player.direction = "up_left";
                else if (player.keyHandler.rightPressed) player.direction = "up_right";
                else player.direction = "up";
            }
            else if (player.keyHandler.downPressed) {
                if (player.keyHandler.leftPressed) player.direction = "down_left";
                else if (player.keyHandler.rightPressed) player.direction = "down_right";
                else player.direction = "down";
            }
            else if (player.keyHandler.leftPressed) player.direction = "left";
            else if (player.keyHandler.rightPressed) player.direction = "right";
            else player.currentState = player.idleState;
        }
    }

    @Override
    public void render(Graphics2D g2) {
        // Draw sprite here
        if (currentEntity.getClass().equals(Player.class)) {
            Player player = (Player) currentEntity;
            BufferedImage image = switch (player.direction) {
                case "up" -> getImage("up", player.walkType);
                case "down" -> getImage("down", player.walkType);
                case "left" -> getImage("left", player.walkType);
                case "right" -> getImage("right", player.walkType);
                case "up_left" -> getImage("up_left", player.walkType);
                case "up_right" -> getImage("up_right", player.walkType);
                case "down_left" -> getImage("down_left", player.walkType);
                case "down_right" -> getImage("down_right", player.walkType);
                default -> null;
            };
//            if (invincible) {
//                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
//            }
            g2.drawImage(image, PLAYER_SCREEN_X, PLAYER_SCREEN_Y, null);
            g2.drawRect(PLAYER_SCREEN_X, PLAYER_SCREEN_Y, TILE_SIZE * 3, TILE_SIZE * 4);
//            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));



            // Debug
//        g2.setFont(new Font("Arial", Font.BOLD, 26));
//        g2.setColor(Color.WHITE);
//        g2.drawString("Invincible time left: " + (60 - invincibleCounter), 10, 500);
        }
    }
}
