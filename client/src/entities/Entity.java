package entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import enitystates.EntityState;
import gamestates.Playing;
import utils.Constants;
import utils.HelpMethods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import static enitystates.EntityState.DEATH;
import static enitystates.EntityState.IDLE;
import static utils.Constants.Player.PLAYER_SCREEN_X;
import static utils.Constants.Player.PLAYER_SCREEN_Y;
import static utils.Constants.Screen.*;

public class Entity {
    public String name;

    @JsonIgnore
    public BufferedImage image;

    public int width, height;
    protected String image_path;
    protected boolean collision;
    public boolean collisionOn = false;
    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public int worldX, worldY;
    protected Playing playing;
    public EntityState currentState = IDLE;


    public Playing getPlaying() {return playing;}

    // For wall and super objects
    public Entity(String name, Playing Playing) {
        this.name = name;
        this.playing = Playing;

        width = Constants.Screen.TILE_SIZE;
        height = Constants.Screen.TILE_SIZE;
        image = null;
        solidArea = new Rectangle(0, 0, Constants.Screen.TILE_SIZE, Constants.Screen.TILE_SIZE);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    // For player, npc and monster
    public Entity(String name, Playing playing, int width, int height) {
        this.playing = playing;
        this.name = name;
        this.width = width;
        this.height = height;

        solidArea = new Rectangle(0, 0, Constants.Screen.TILE_SIZE, Constants.Screen.TILE_SIZE);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        image = null;
    }

    public void setSolidArea(int x, int y, int width, int height) {
        solidArea = new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g2) {
        if (isOnTheScreen()) {
            g2.drawImage(image, getScreenX(), getScreenY(), width, height, null);
        }
    }

    public void update(){

    }

    public int getWorldY() {
        return worldY + height / 2;
    }
    public int getWorldX() {
        return worldX + width / 2;
    }

    public int getScreenX() {
        return HelpMethods.getScreenX(worldX, playing.getPlayer());
    }
    public int getScreenY() {
        return HelpMethods.getScreenY(worldY, playing.getPlayer());
    }
    public boolean isOnTheScreen() {
        Player player = playing.getPlayer();
        int playerWorldX = player.worldX;
        int playerWorldY = player.worldY;
        int diff = 3 * TILE_SIZE;
        return getWorldX() > playerWorldX - PLAYER_SCREEN_X - diff &&
                getWorldX() < playerWorldX - PLAYER_SCREEN_X + SCREEN_WIDTH  + diff &&
                getWorldY() > playerWorldY - PLAYER_SCREEN_Y - diff &&
                getWorldY() < playerWorldY - PLAYER_SCREEN_Y + SCREEN_HEIGHT + diff;
    }

    public int getRenderOrder() {
        if (this.currentState == DEATH) return -100;
        if (name.equals("Demon")) return worldY + height;
        return getWorldY();
    }
}
