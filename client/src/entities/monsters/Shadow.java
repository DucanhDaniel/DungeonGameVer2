package entities.monsters;

import enitystates.*;
import gamestates.Playing;
import utils.Constants;
import entities.Player;

import java.awt.*;
import java.util.Random;

import static utils.Constants.Screen.TILE_SIZE;

public class Shadow extends Monster{
    Rectangle attack1Box, attack2Box;
    Attack attack1, attack2;
    Random random = new Random();

    public Shadow(Playing playing, int worldX, int worldY) {
        super("Shadow", playing, 8 * TILE_SIZE, 3 * TILE_SIZE);
        this.worldX = worldX;
        this.worldY = worldY;
        solidArea = new Rectangle(3 * TILE_SIZE + TILE_SIZE / 2, 2 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        attack1Box = new Rectangle(2 * TILE_SIZE, 3 * TILE_SIZE / 2, 4 * TILE_SIZE, 2 * TILE_SIZE);
        attack2Box = new Rectangle(-2 * TILE_SIZE, TILE_SIZE, 12 * TILE_SIZE, 2 * TILE_SIZE + TILE_SIZE / 2);
        attackBox = attack1Box;
        visionBox = new Rectangle(-6 * TILE_SIZE, -7 * TILE_SIZE, 20 * TILE_SIZE, 18 * TILE_SIZE);
        hitBox = (Rectangle) solidArea.clone();

        maxHealth = 30;
        currentHealth = maxHealth;
        attackRate = 60;
        attackPoints = 2;
        speed = 4;

        attack1 = new Attack(this, 16, 5, "Attack_type1");
        attack2 = new Attack(this, 6, 5, "Attack_type2");
        attack = attack1;
        idle = new Idle(this, 8, 5);
        walk = new Walk(this, 8, 5);
        death = new Death(this, 16, 5);
    }

    int frameCounter = 0;
    public void attack() {
        if(attack == attack1) attack1();
        else attack2();
    }

    int randomAttack;
    public void attack1() {
        getDirectionForAttacking();
        frameCounter++;
        Player player = playing.getPlayer();
        if (frameCounter == attack.frameDuration * 4 ||
                frameCounter == attack.frameDuration * 9) {
            if (canAttack(false)) player.getHurt(attackPoints);
        }

        if (frameCounter == attack.totalAnimationFrames * attack.frameDuration) {
            randomAttack = random.nextInt(2);
            if(randomAttack == 1) {
                changeAttackType();
            }
            currentState = EntityState.IDLE;
            frameCounter = 0;
        }
    }

    public void attack2() {
        Player player = playing.getPlayer();
        getDirectionForAttacking();
        frameCounter++;
        if(frameCounter == 2 * attack2.frameDuration) {
            if (canAttack(false)) {
                player.getHurt(attackPoints);
//                if(player.worldX > getWorldX()) this.worldX -= 9 * TILE_SIZE;
//                else this.worldX += 9 * TILE_SIZE;
                int sx = (player.getWorldX() > getWorldX()) ? 1 : -1;
                boolean haveWall = false;
                for(int i = 1; i <= 9; i++) {
                    if(playing.getTileManager().isWall(getWorldY() / TILE_SIZE, getWorldX() / TILE_SIZE + sx * i)) {
                        haveWall = true;
                        if(getWorldX() + sx * (i - 1) * TILE_SIZE == player.getWorldX()) this.worldX = this.worldX + sx * (i - 2) * TILE_SIZE;
                        else this.worldX = this.worldX + sx * (i - 1) * TILE_SIZE;
                        break;
                    }
                }
                if(!haveWall) {
                    if(getWorldX() + sx * 9 * TILE_SIZE == player.getWorldX()) this.worldX = this.worldX + sx * 8 * TILE_SIZE;
                    else this.worldX = this.worldX + sx * 9 * TILE_SIZE;
                }
            }
        }
        if(frameCounter == attack2.frameDuration * attack2.totalAnimationFrames) {
            randomAttack = random.nextInt(2);
            if(randomAttack == 0) {
                changeAttackType();
            }
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

    public void heal() {
        if (currentHealth < maxHealth) {
            currentHealth += 5;
            if (currentHealth > maxHealth) currentHealth = maxHealth;
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
            g2.fillRect(getScreenX() + 3 * TILE_SIZE + TILE_SIZE /3, getScreenY() + TILE_SIZE / 3, healthBarWidth, 4 * 3);
        }

        // Draw auto LockOn
        super.drawLockOn(g2, TILE_SIZE * 5 / 2, TILE_SIZE * 3, 0, - TILE_SIZE / 2);
    }
}
