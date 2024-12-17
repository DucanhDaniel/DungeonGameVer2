package entities.monsters;

import effect.MagicCircle;
import enitystates.*;
import gamestates.Playing;
import entities.Player;

import java.awt.*;

import static utils.Constants.Screen.TILE_SIZE;

public class Mage extends Monster {
    private MagicCircle magicCircle = null;
    private Player player;
    private Attack attack1, attack2;
    private Rectangle attack1Box, attack2Box;
    public Mage(Playing playing, int worldX, int worldY) {
        super("Mage", playing, 12 * TILE_SIZE, 4 * TILE_SIZE);
        this.worldX = worldX;
        this.worldY = worldY;

        solidArea = new Rectangle(5 * TILE_SIZE + TILE_SIZE / 2, 2 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        visionBox = new Rectangle(0, - 4 * TILE_SIZE, 12 * TILE_SIZE, 12 * TILE_SIZE);
        attack1Box = new Rectangle(0, 2 * TILE_SIZE, 12 * TILE_SIZE, TILE_SIZE * 3 / 2);
        attack2Box = (Rectangle) visionBox.clone();
        hitBox = (Rectangle) solidArea.clone();

        attack1 = new Attack(this, 12, 10, "attack_type1");
        attack2 = new Attack(this, 10, 10, "attack_type2");
        attack = attack1;
        attackBox = attack1Box;

        idle = new Idle(this, 7, 5);
        walk = new Walk(this, 6, 5);
        death = new Death(this, 10, 10);

        player = playing.getPlayer();

        maxHealth = 30;
        currentHealth = maxHealth;
        speed = 4;
        attackRate = 40;
        attackPoints = 4;
    }

    public void attack() {
        if (attack == attack1) attack1();
        else attack2();
    }

    int frameCounter = 0;
    int attack1Counter = 0;
    public void attack1() {
        getDirectionForAttacking();
        frameCounter++;
        if (frameCounter == attack1.frameDuration * 4) {
            playing.soundtrack.playSE(10);
        }
        if (frameCounter == 6 * attack1.frameDuration ||
            frameCounter == 7 * attack1.frameDuration ||
            frameCounter == 8 * attack1.frameDuration ||
            frameCounter == 9 * attack1.frameDuration) {
            if (canAttack(false)) player.getHurt(attackPoints / 2);
        }

        if (frameCounter == attack1.frameDuration * attack1.totalAnimationFrames) {
            attack1Counter++;
            if (attack1Counter == 2) {
                attack = attack2;
                attackBox = attack2Box;
                attack1Counter = 0;
                attackRate = 10;
            }
            currentState = EntityState.IDLE;
            frameCounter = 0;
        }
    }

    public void attack2() {
        getDirectionForAttacking();
        frameCounter++;
        if (frameCounter == attack2.frameDuration * attack2.totalAnimationFrames) {
            int playerX = player.getWorldX();
            int playerY = player.getWorldY();
            magicCircle = new MagicCircle(this, playerX, playerY, 0);

            frameCounter = 0;
            attack = attack1;
            attackBox = attack1Box;
            attackRate = 40;
            currentState = EntityState.IDLE;
        }
    }

    @Override
    public int getWorldY() {
        return worldY + solidArea.y + solidArea.height / 2;
    }

    @Override
    public int getWorldX() {
        return worldX + solidArea.x + solidArea.width / 2;
    }

    @Override
    public void move() {
        getDirectionForAttacking();
        super.move();
    }

    @Override
    public void update() {
        super.update();

        if (magicCircle != null) magicCircle.update();
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        if (magicCircle != null) magicCircle.draw(g2);

        // Draw health bar
        int healthBarWidth = (int) (15 * 3 * ((double) currentHealth / maxHealth));
        if (healthBarWidth > 0) {
            g2.setColor(Color.RED);
            g2.fillRect(getScreenX() + 5 * TILE_SIZE + TILE_SIZE /2, getScreenY() + TILE_SIZE, healthBarWidth, 4 * 3);
        }

        // Draw auto LockOn
        super.drawLockOn(g2, TILE_SIZE * 5 / 2, TILE_SIZE * 5 / 2, 0, 0);

    }

    public void removeEffect(int index) {
        magicCircle = null;
    }
}
