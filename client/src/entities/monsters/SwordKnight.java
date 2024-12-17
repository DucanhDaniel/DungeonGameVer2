package entities.monsters;

import enitystates.*;
import gamestates.Playing;

import java.awt.*;
import java.util.Random;

import entities.*;

import static utils.Constants.Screen.TILE_SIZE;

public class SwordKnight extends Monster{
    Rectangle attack1Box, attack2Box;
    Attack attack1, attack2;
    public SwordKnight(Playing playing, int worldX, int worldY) {
        super("Sword_Knight", playing, 12 * TILE_SIZE, 4 * TILE_SIZE);
        this.worldX = worldX;
        this.worldY = worldY;
        solidArea = new Rectangle(6 * TILE_SIZE - TILE_SIZE / 2, 2 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        attack1Box = new Rectangle(4 * TILE_SIZE, 3 * TILE_SIZE / 2, 4 * TILE_SIZE, 2 * TILE_SIZE);
        attack2Box = new Rectangle(0, TILE_SIZE, 12 * TILE_SIZE, 2 * TILE_SIZE + TILE_SIZE / 2);
        attackBox = attack1Box;
        visionBox = new Rectangle(0, - 3 * TILE_SIZE, 12 * TILE_SIZE, 10 * TILE_SIZE);
        hitBox = (Rectangle) solidArea.clone();

        // Monster's attributes
        attackRate = 90;
        attackPoints = 3;
        speed = 4;
        maxHealth = 30;
        currentHealth = maxHealth;

        // Monster's state
        attack1 = new Attack(this, 14, 5, "Attack_type1");
        attack2 = new Attack(this, 11, 5, "Attack_type2");
        attack = attack1;
        idle = new Idle(this, 7, 5);
        walk = new Walk(this, 8, 5);
        death = new Death(this, 12, 5);
    }


    int frameCounter = 0;
    public void attack() {
        if (attack == attack1) attack1();
        else attack2();
    }

    int attack1Counter = 0;
    public void attack1() {
        getDirectionForAttacking();
        Player player = playing.getPlayer();
        frameCounter++;
        if (frameCounter == attack.frameDuration * 3) {
            playing.soundtrack.playSE(18);
        }
        if (frameCounter == attack.frameDuration * 5 || frameCounter == attack.frameDuration * 9) {
            if (canAttack(false)) {
                player.getHurt(attackPoints);
            }
        }
        if (frameCounter == attack.totalAnimationFrames * attack.frameDuration) {
            attack1Counter++;
            if (attack1Counter == 2) {
                changeAttackType();
                attack1Counter = 0;
            }
            currentState = EntityState.IDLE;
            frameCounter = 0;
        }
    }
    public void attack2() {
        getDirectionForAttacking();
        Player player = playing.getPlayer();
        frameCounter++;
        if (frameCounter == attack2.frameDuration) {
            playing.soundtrack.playSE(19);
        }
        if (frameCounter == 3 * attack2.frameDuration) {
            if (canAttack(false))
                player.getHurt(attackPoints);
        }
        if (frameCounter == 11 * attack2.frameDuration) {
            if (canAttack(false))
                player.getHurt(attackPoints);
        }
        if (frameCounter == attack2.totalAnimationFrames * attack2.frameDuration) {
            changeAttackType();
            currentState = EntityState.IDLE;
            frameCounter = 0;
        }
    }

    public void changeAttackType() {
        if (attack == attack1) {
            attack = attack2;
            attackBox = attack2Box;
            attackRate = 10;
        }
        else {
            attack = attack1;
            attackBox = attack1Box;
            attackRate = 90;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        // Draw health bar
        int healthBarWidth = (int) (15 * 3 * ((double) currentHealth / maxHealth));
        if (healthBarWidth > 0) {
            g2.setColor(Color.RED);
            g2.fillRect(getScreenX() + 5 * TILE_SIZE + TILE_SIZE /2, getScreenY() + TILE_SIZE, healthBarWidth, 4 * 3);
        }

        // Draw auto LockOn
        super.drawLockOn(g2, TILE_SIZE * 5 / 2, TILE_SIZE * 5 / 2, 0, 0);
    }

    @Override
    public int getWorldY() {
        return worldY + solidArea.y + TILE_SIZE / 2;
    }

    @Override
    public void move() {
        getDirectionForAttacking();
        super.move();
    }
}
