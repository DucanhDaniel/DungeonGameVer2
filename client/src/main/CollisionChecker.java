package main;

import components.HitboxComponent;
import components.PositionComponent;
import enitystates.EntityState;
import entities.Entity;
import entities.monsters.Monster;
import entities.Sprite;
import entities.Player;
import objects.Door;
import tile.TileManager;
import utils.Constants;

import java.awt.*;
import java.util.ArrayList;

import static utils.Constants.Screen.*;

public class CollisionChecker {

    Game game;
    TileManager tileManager;
    public CollisionChecker(Game game) {
        this.game = game;
    }

    int entityLeftWorldX;
    int entityRightWorldX, entityTopWorldY, entityBottomWorldY, entityLeftCol, entityRightCol, entityTopRow, entityBottomRow;
    public boolean checkTile(Sprite entity) {
        entity.goAlongDirection();
        tileManager = entity.getPlaying().getTileManager();
        entityLeftWorldX = entity.worldX + entity.solidArea.x;
        entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        entityTopWorldY = entity.worldY + entity.solidArea.y;
        entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        entityLeftCol = entityLeftWorldX/TILE_SIZE;
        entityRightCol = entityRightWorldX/TILE_SIZE;
        entityTopRow = entityTopWorldY/TILE_SIZE;
        entityBottomRow = entityBottomWorldY/TILE_SIZE;


        Rectangle tileNum1 = null, tileNum2 = null;
        try {
            switch (entity.direction) {
                case "up":
                    entityTopRow = (entityTopWorldY - entity.speed) / TILE_SIZE;
                    tileNum1 = tileManager.solidArea[entityTopRow][entityLeftCol];
                    tileNum2 = tileManager.solidArea[entityTopRow][entityRightCol];
                    break;
                case "down":
                    entityBottomRow = (entityBottomWorldY + entity.speed) / TILE_SIZE;
                    tileNum1 = tileManager.solidArea[entityBottomRow][entityLeftCol];
                    tileNum2 = tileManager.solidArea[entityBottomRow][entityRightCol];
                    break;
                case "left":
                    entityLeftCol = (entityLeftWorldX - entity.speed) / TILE_SIZE;
                    tileNum1 = tileManager.solidArea[entityTopRow][entityLeftCol];
                    tileNum2 = tileManager.solidArea[entityBottomRow][entityLeftCol];
                    break;
                case "right":
                    entityRightCol = (entityRightWorldX + entity.speed) / TILE_SIZE;
                    tileNum1 = tileManager.solidArea[entityTopRow][entityRightCol];
                    tileNum2 = tileManager.solidArea[entityBottomRow][entityRightCol];
                    break;
                case "left_up":
                    entityLeftCol = (entityLeftWorldX - (entity.speed)) / TILE_SIZE;
                    entityTopRow = (entityTopWorldY - entity.speed) / TILE_SIZE;
                    tileNum1 = tileManager.solidArea[entityTopRow][entityLeftCol];
                    tileNum2 = tileManager.solidArea[entityTopRow][entityRightCol];
                    break;
                case "right_up":
                    entityRightCol = (entityRightWorldX + entity.speed) / TILE_SIZE;
                    entityTopRow = (entityTopWorldY - entity.speed) / TILE_SIZE;
                    tileNum1 = tileManager.solidArea[entityTopRow][entityLeftCol];
                    tileNum2 = tileManager.solidArea[entityTopRow][entityRightCol];
                    break;
                case "left_down":
                    entityLeftCol = (entityLeftWorldX - entity.speed) / TILE_SIZE;
                    entityBottomRow = (entityBottomWorldY + entity.speed) / TILE_SIZE;
                    tileNum1 = tileManager.solidArea[entityBottomRow][entityLeftCol];
                    tileNum2 = tileManager.solidArea[entityBottomRow][entityRightCol];
                    break;
                case "right_down":
                    entityRightCol = (entityRightWorldX + entity.speed) / TILE_SIZE;
                    entityBottomRow = (entityBottomWorldY + entity.speed) / TILE_SIZE;
                    tileNum1 = tileManager.solidArea[entityBottomRow][entityLeftCol];
                    tileNum2 = tileManager.solidArea[entityBottomRow][entityRightCol];
                    break;
            }
            entity.solidArea.x += entity.worldX;
            entity.solidArea.y += entity.worldY;
            entity.collisionOn = false;
            if (tileNum1 != null) {
                if (tileNum1.intersects(entity.solidArea)) {
                    entity.collisionOn = true;
                }
            }
            if (tileNum2!= null) {
                if (tileNum2.intersects(entity.solidArea)) {
                    entity.collisionOn = true;
                }
            }
            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            entity.goOppositeDirection();

            if (entity.currentState == EntityState.IDLE && entity.collisionOn) {
                throw new Exception("Fuck this thing!");
            }
        } catch (Exception e) {
            System.out.println("Error check tile collision");
            entity.collisionOn = false;
        }

        if (entity instanceof Player) {
            game.getPlaying().getDoorSystem().checkPlayerNextMove((Player)entity);
        }


        return entity.collisionOn;
    }

    // NPC and monster collision
    public int checkEntity(Sprite entity, ArrayList<Sprite> entityArrayList) {
        int index = -1;
        for (int i = 0; i < entityArrayList.size(); i++) {
            Entity target = entityArrayList.get(i);
            if (target instanceof Monster) {
                if (target.currentState == EntityState.DEATH) continue;
            }
            if (target != null) {
                // Get entity solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Get the target solid area position
                target.solidArea.x = target.solidArea.x + target.worldX;
                target.solidArea.y = target.solidArea.y + target.worldY;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                    case "left_up":
                        entity.solidArea.x -= entity.speed;
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "right_up":
                        entity.solidArea.x += entity.speed;
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "left_down":
                        entity.solidArea.x -= entity.speed;
                        entity.solidArea.y += entity.speed;
                        break;
                    case "right_down":
                        entity.solidArea.x += entity.speed;
                        entity.solidArea.y += entity.speed;
                        break;
                }
                if (entity.solidArea.intersects(target.solidArea) && target != entity) {
                    entity.collisionOn = true;
                    index = i;
                }

                target.solidArea.x = target.solidAreaDefaultX;
                target.solidArea.y = target.solidAreaDefaultY;
            }
            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
        }

        return index;
    }

    public boolean checkPlayer(Sprite entity) {
        Player player = game.getPlaying().getPlayer();

        boolean contactPlayer = false;

        // Get entity solid area position
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        // Get the target solid area position
        player.solidArea.x = player.solidArea.x + player.worldX;
        player.solidArea.y = player.solidArea.y + player.worldY;

        switch (entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
                break;
            case "down":
                entity.solidArea.y += entity.speed;
                break;
            case "left":
                entity.solidArea.x -= entity.speed;
                break;
            case "right":
                entity.solidArea.x += entity.speed;
                break;
            case "left_up":
                entity.solidArea.x -= entity.speed;
                entity.solidArea.y -= entity.speed;
                break;
            case "right_up":
                entity.solidArea.x += entity.speed;
                entity.solidArea.y -= entity.speed;
                break;
            case "left_down":
                entity.solidArea.x -= entity.speed;
                entity.solidArea.y += entity.speed;
                break;
            case "right_down":
                entity.solidArea.x += entity.speed;
                entity.solidArea.y += entity.speed;
                break;
        }
        if (entity.solidArea.intersects(player.solidArea)){
            entity.collisionOn = true;
            contactPlayer = true;
        }

        player.solidArea.x = player.solidAreaDefaultX;
        player.solidArea.y = player.solidAreaDefaultY;
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;

        return contactPlayer;
    }

    public boolean checkPlayer(HitboxComponent hitbox, PositionComponent position) {
        Player player = game.getPlaying().getPlayer();

        boolean contactPlayer = false;

        int x = position.worldX - hitbox.width / 2;
        int y = position.worldY - hitbox.height / 2;

        Rectangle hitboxRegion = new Rectangle(x, y, hitbox.width, hitbox.height);

        player.solidArea.x = player.solidArea.x + player.worldX;
        player.solidArea.y = player.solidArea.y + player.worldY;

        if (hitboxRegion.intersects(player.solidArea)){
            contactPlayer = true;
        }

        player.solidArea.x = player.solidAreaDefaultX;
        player.solidArea.y = player.solidAreaDefaultY;

        return contactPlayer;
    }
}
