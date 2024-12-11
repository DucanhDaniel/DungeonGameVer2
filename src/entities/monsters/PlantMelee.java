package entities.monsters;

import enitystates.Attack;
import enitystates.Death;
import enitystates.EntityState;
import enitystates.Idle;
import gamestates.Playing;
import entities.*;

import java.awt.*;

import static utils.Constants.Screen.SCALE;
import static utils.Constants.Screen.TILE_SIZE;

public class PlantMelee extends Monster {
    public int knockbackDuration = 15;
    public PlantMelee(Playing playing, int worldX, int worldY) {
        super("PlantMelee", playing, 96 * SCALE, 96 * SCALE);

        maxHealth = 30;
        currentHealth = maxHealth;
        attackPoints = 3;
        attackRate = 180;

        attack = new Attack(this, 11, 5);
        idle = new Idle(this, 14, 10);
        death = new Death(this, 6, 10);

        attackBox = new Rectangle(0, 0, width, height);
        solidArea = new Rectangle(2 * TILE_SIZE, 2 * TILE_SIZE, 2 * TILE_SIZE, 2 * TILE_SIZE);
        hitBox = (Rectangle) solidArea.clone();
        visionBox = (Rectangle) attackBox.clone();

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        this.worldX = worldX;
        this.worldY = worldY;
    }

    @Override
    public int getWorldX() {
        return worldX + TILE_SIZE * 3;
    }

    @Override
    public int getWorldY() {
        return worldY + TILE_SIZE * 3;
    }

    int frameCounter = 0;
    boolean attackSuccess = false;
    public void attack() {
        Player player = playing.getPlayer();

        frameCounter++;
        int totalFrame = attack.totalAnimationFrames * attack.frameDuration;
        if (frameCounter == 5 * attack.frameDuration) {
//            System.out.println("sadasdasdasdsadsad");
            playing.soundtrack.playSE(14);
        }
        if (frameCounter >= totalFrame - knockbackDuration) {
            getDirectionForAttacking();
            if (player.dash == null && canAttack(false)) {
                player.knock_back(5, direction);
                attackSuccess = true;
            }
            if (frameCounter == 9 * attack.frameDuration && attackSuccess) {
                player.getHurt(attackPoints);
                attackSuccess = false;
            }
            if (frameCounter == totalFrame) {
                currentState = EntityState.IDLE;
                frameCounter = 0;
            }
        }

    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        // Draw health bar
        int healthBarWidth = (int) ( 2 * TILE_SIZE * ((double) currentHealth / maxHealth));
        if (healthBarWidth > 0) {
            g2.setColor(Color.RED);
            g2.fillRect(getScreenX() + 2 * TILE_SIZE, getScreenY() + TILE_SIZE, healthBarWidth, 4 * 3);
        }

        super.drawLockOn(g2, TILE_SIZE * 5 / 2, TILE_SIZE * 5 / 2, 0, 0);

    }


}
