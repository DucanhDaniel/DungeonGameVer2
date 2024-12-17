package entities.monsters;

import enitystates.*;
import gamestates.Playing;
import utils.Constants;
import entities.Player;

import java.awt.*;

import static utils.Constants.Screen.TILE_SIZE;

public class Sickle extends Monster{
    public Sickle(Playing playing, int worldX, int worldY) {
        super("Sickle", playing, 8 * TILE_SIZE, 4 * TILE_SIZE);
        this.worldX = worldX;
        this.worldY = worldY;
        solidArea = new Rectangle(3 * TILE_SIZE + TILE_SIZE / 2, 2 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        attackBox = new Rectangle(3 * TILE_SIZE / 2, 0, 5 * TILE_SIZE, 3 * TILE_SIZE + TILE_SIZE / 2);
        visionBox = new Rectangle(-2 * TILE_SIZE, -4 * TILE_SIZE, 12 * TILE_SIZE, 12 * TILE_SIZE);
        hitBox = (Rectangle) solidArea.clone();

        maxHealth = 30;
        currentHealth = maxHealth;
        attackRate = 110;
        attackPoints = 2;
        speed = 4;

        attack = new Attack(this, 11, 5);
        idle = new Idle(this, 6, 5);
        walk = new Walk(this, 5, 5);
        death = new Death(this, 6, 5);
    }

    int frameCounter = 0;
    public void attack() {
        getDirectionForAttacking();
        frameCounter++;
        Player player = playing.getPlayer();
        if (frameCounter == 1) {
            playing.soundtrack.playSE(16);
        }
        if (frameCounter == attack.frameDuration * 2 ||
            frameCounter == attack.frameDuration * 5 ||
            frameCounter == attack.frameDuration * 9) {
            if (canAttack(false)) player.getHurt(attackPoints);
        }

        if (frameCounter == attack.totalAnimationFrames * attack.frameDuration) {
            currentState = EntityState.IDLE;
            frameCounter = 0;
        }
    }

    @Override
    public int getWorldY() {
        return worldY + solidArea.y + solidArea.height / 2;
    }

    @Override
    public void move() {
        getDirectionForAttacking();
        super.move();
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        // Draw health bar
        int healthBarWidth = (int) (20 * 3 * ((double) currentHealth / maxHealth));
        if (healthBarWidth > 0) {
            g2.setColor(Color.RED);
            g2.fillRect(getScreenX() + 3 * TILE_SIZE + TILE_SIZE /2, getScreenY() + TILE_SIZE / 2, healthBarWidth, 4 * 3);
        }

        // Draw auto LockOn
        super.drawLockOn(g2, TILE_SIZE * 5 / 2, TILE_SIZE * 3, 0, - TILE_SIZE / 2);
    }
}
