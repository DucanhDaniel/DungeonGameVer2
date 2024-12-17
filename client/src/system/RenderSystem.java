package system;

import components.HitboxComponent;
import components.PositionComponent;
import components.RenderComponent;
import entities.Player;
import gamestates.Playing;
import utils.HelpMethods;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.Player.PLAYER_SCREEN_X;
import static utils.Constants.Player.PLAYER_SCREEN_Y;
import static utils.Constants.Screen.TILE_SIZE;

public class RenderSystem {
    Player player;
    public RenderSystem(Playing playing) {
        this.player = playing.getPlayer();
    }

    public void draw(Graphics2D g2, PositionComponent position, RenderComponent render) {
        int width = render.width, height = render.height;
        int worldX = position.worldX;
        int worldY = position.worldY;
        if (isOnTheScreen(worldX, worldY)) {
            drawImage(g2, position, render.image, width, height);
        }
    }

    public void draw(Graphics2D g2, PositionComponent position, RenderComponent render, HitboxComponent hitbox) {
        int width = render.width, height = render.height;
        int worldX = position.worldX;
        int worldY = position.worldY;
        if (isOnTheScreen(worldX, worldY)) {
            drawImage(g2, position, render.image, width, height);
            drawRect(g2, position, hitbox.width, hitbox.height);
        }
    }

    public void drawImage(Graphics2D g2, PositionComponent position, BufferedImage image, int width, int height) {
        int worldX = position.worldX - width / 2;
        int worldY = position.worldY - height / 2;
        int playerWorldX = player.worldX;
        int playerWorldY = player.worldY;
        int screenX = worldX - playerWorldX + PLAYER_SCREEN_X;
        int screenY = worldY - playerWorldY + PLAYER_SCREEN_Y;
        g2.drawImage(image, screenX, screenY, width, height, null);
    }

    public void drawRect(Graphics2D g2, PositionComponent position, int width, int height) {
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1));

        int worldX = position.worldX - width / 2;
        int worldY = position.worldY - height / 2;
        int playerWorldX = player.worldX;
        int playerWorldY = player.worldY;
        int screenX = worldX - playerWorldX + PLAYER_SCREEN_X;
        int screenY = worldY - playerWorldY + PLAYER_SCREEN_Y;
//        g2.drawRect(screenX, screenY, width, height);
//        g2.setStroke(new BasicStroke(1));
    }

    public boolean isOnTheScreen(int worldX, int worldY) {
        int playerWorldX = player.worldX;
        int playerWorldY = player.worldY;
        return worldX > (playerWorldX + TILE_SIZE) - (PLAYER_SCREEN_X + TILE_SIZE) - TILE_SIZE * 5
                && worldX < (playerWorldX + TILE_SIZE) + (PLAYER_SCREEN_X + TILE_SIZE) + TILE_SIZE * 5
                && worldY > (playerWorldY + TILE_SIZE) - (PLAYER_SCREEN_Y + TILE_SIZE) - TILE_SIZE * 5
                && worldY < (playerWorldY + TILE_SIZE) + (PLAYER_SCREEN_Y + TILE_SIZE) + TILE_SIZE * 5;
    }
}
