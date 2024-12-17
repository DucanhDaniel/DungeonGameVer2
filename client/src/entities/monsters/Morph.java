package entities.monsters;

import enitystates.*;
import gamestates.Playing;
import entities.Player;

import java.awt.*;
import java.util.Random;

import static utils.Constants.Screen.TILE_SIZE;

public class Morph extends Monster{
    Rectangle attack1Box, attack2Box;
    Attack attack1, attack2;
    Player player;
    private Walk dash;
    private boolean isWalkDash = false;
    public Morph(Playing playing, int worldX, int worldY) {
        super("Morph", playing, 10 * TILE_SIZE, 8 * TILE_SIZE);
        this.worldX = worldX;
        this.worldY = worldY;
        solidArea = new Rectangle(4 * TILE_SIZE + TILE_SIZE / 2, 4 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        visionBox = new Rectangle(0, - TILE_SIZE, 10 * TILE_SIZE, 10 * TILE_SIZE);
        attack1Box = new Rectangle(TILE_SIZE, 3 * TILE_SIZE, 8 * TILE_SIZE, 5 * TILE_SIZE / 2);
        attack2Box = new Rectangle(3 * TILE_SIZE, 2 * TILE_SIZE, 4 * TILE_SIZE, 5 * TILE_SIZE);
        attack1 = new Attack(this, 8, 5, "Attack_type1");
        attack2 = new Attack(this, 6, 10, "Attack_type2");
        hitBox = (Rectangle) solidArea.clone();
        attackBox = attack1Box;

        attack = attack1;
        idle = new Idle(this, 6, 5);
        walk = new Walk(this,6, 5);
        death = new Death(this, 8, 10);
        dash = new Walk(this, 5, 10, "Walk_dash");

        attackPoints = 4;
        attackRate = 100;
        speed = 4;
        maxHealth = 30;
        currentHealth = maxHealth;

        player = playing.getPlayer();
    }

    int frameCounter = 0;
    public void attack() {
        if (attack == attack1) attack1();
        else attack2();
    }

    int attack1Counter = 0;
    public void attack1() {
        getDirectionForAttacking();
        frameCounter++;
        if (frameCounter == attack1.frameDuration / 2) {
            playing.soundtrack.playSE(12);
        }
        if (frameCounter == 3 * attack1.frameDuration ||
            frameCounter == 4 * attack1.frameDuration ||
            frameCounter == 5 * attack1.frameDuration) {
            if (canAttack(false))
                player.getHurt(attackPoints/3);
        }

        if (frameCounter == attack1.totalAnimationFrames * attack1.frameDuration) {
            attack1Counter++;
            if (attack1Counter == 2) {
                attackBox = attack2Box;
                attack = attack2;
                attack1Counter = 0;
            }
            currentState = EntityState.IDLE;
            frameCounter = 0;
        }
    }
    public void attack2() {
        getDirectionForAttacking();
        frameCounter++;
        if (frameCounter == 2 * attack2.frameDuration) {
            if (canAttack(false))
                player.getHurt(attackPoints);
            playing.soundtrack.playSE(13);
        }
        if (frameCounter == attack2.totalAnimationFrames * attack2.frameDuration) {
            attackBox = attack1Box;
            attack = attack1;
            currentState = EntityState.IDLE;
            frameCounter = 0;
        }
    }

    int dashFrameCounter = 0;
    public void dash() {
        getDirectionForAttacking();
        dashFrameCounter++;
        if (dashFrameCounter == dash.frameDuration * dash.totalAnimationFrames) {
            isWalkDash = false;
            currentState = EntityState.ATTACK;
            dashFrameCounter = 0;
        }
    }

    Random random = new Random();
    @Override
    public void update() {
        if (player.canAttackMonster(this) && playing.getGame().getKeyboardInputs().attackPressed &&
        currentState != EntityState.ATTACK) {
//            isWalkDash = true;
            int x = random.nextInt(3);
            isWalkDash = x == 1;
        }
        if (isWalkDash) {
            dash();
            image = dash.getImage();
            return;
        }
        super.update();
    }

    @Override
    public void getHurt(int damage) {
        if (!isWalkDash)
            super.getHurt(damage);
    }

    @Override
    public int getWorldY() {
        return worldY + solidArea.y + solidArea.height / 2;
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        // Draw health bar
        int healthBarWidth = (int) (TILE_SIZE * ((double) currentHealth / maxHealth));
        if (healthBarWidth > 0) {
            g2.setColor(Color.RED);
            g2.fillRect(getScreenX() + 4 * TILE_SIZE + TILE_SIZE /2, getScreenY() + 3 * TILE_SIZE, healthBarWidth, 4 * 3);
        }

        // Draw auto LockOn
        super.drawLockOn(g2, TILE_SIZE * 5 / 2, TILE_SIZE * 5 / 2, 0, 0);


    }

    @Override
    public void move() {
        getDirectionForAttacking();
        super.move();
    }

}
