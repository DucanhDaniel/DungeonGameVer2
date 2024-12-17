package entities.monsters;

import enitystates.*;
import entities.Player;
import gamestates.Playing;

import java.awt.*;

import static utils.Constants.Screen.TILE_SIZE;

public class Slime extends Monster {

    public Slime(Playing playing, int worldX, int worldY) {
        super("Slime", playing, TILE_SIZE, TILE_SIZE * 2);
        currentState = EntityState.WALK;
        attack = new Attack(this, 6, 10);
        idle = new Idle(this, 5, 10);
        walk = new Walk(this, 5, 10);
        death = new Death(this, 5, 10);
        this.worldX = worldX;
        this.worldY = worldY;

        setDefaultValues();

        solidArea = new Rectangle(0, TILE_SIZE * 3 / 2, TILE_SIZE, TILE_SIZE / 2);
        visionBox = new Rectangle(-TILE_SIZE, 0, 3 * TILE_SIZE, 3 * TILE_SIZE);
        hitBox = new Rectangle(0, TILE_SIZE, TILE_SIZE, TILE_SIZE);
        attackBox = (Rectangle) visionBox.clone();

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public void setDefaultValues() {
        speed = 2;
        attackPoints = 1;
        maxHealth = 10;
        currentHealth = maxHealth;
        attackRate = 30;
    }


    int frameCounter = 0;

    public void attack() {
        getDirectionForAttacking();
        speed = 4;
        move();
        frameCounter++;
        Player player = playing.getPlayer();
        if (frameCounter > attack.totalAnimationFrames * attack.frameDuration) {
            if (playing.getGame().getCollisionChecker().checkPlayer(this)) {
                player.getHurt(attackPoints);
            }
            playing.soundtrack.playSE(17);
            currentState = EntityState.IDLE;
            frameCounter = 0;
        }
        speed = 2;
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);

        // Draw health bar
        int healthBarWidth = (int) (15 * 3 * ((double) currentHealth / maxHealth));
        if (healthBarWidth > 0) {
            g2.setColor(Color.RED);
            g2.fillRect(getScreenX(), getScreenY() + 5 * 3, healthBarWidth, 4 * 3);
        }

        // Draw auto LockOn
        super.drawLockOn(g2, TILE_SIZE * 5 / 2, TILE_SIZE * 5 / 2, 0, 0);
    }

    @Override
    public int getWorldX() {
        return worldX + TILE_SIZE / 2;
    }

    @Override
    public int getWorldY() {
        return worldY + solidArea.y - TILE_SIZE / 2;
    }


}
