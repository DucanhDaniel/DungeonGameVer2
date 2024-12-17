package entities.monsters.bosses;

import effect.DeathHand;
import enitystates.*;
import entities.Player;
import entities.monsters.Monster;
import gamestates.Playing;
import utils.HelpMethods;
import utils.ImageLoader;

import java.awt.*;
import java.util.Random;

import static enitystates.EntityState.DEATH;
import static utils.Constants.Screen.*;

public class BringerOfDeath extends Boss {
    Attack normalAttack, castAttack;
    String currentAttackType = "NORMAl";
    DeathHand[] deathHand;

    public BringerOfDeath(Playing playing, int worldX, int worldY) {
        super("BringerOfDeath", playing, 13 * TILE_SIZE, 6 * TILE_SIZE);
        this.worldX = worldX;
        this.worldY = worldY;

        solidArea = new Rectangle(6 * TILE_SIZE, 4 * TILE_SIZE, TILE_SIZE, 2 * TILE_SIZE);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        attackBox = new Rectangle(0, TILE_SIZE, 13 * TILE_SIZE, 6 * TILE_SIZE);
        visionBox = new Rectangle(- 3 * TILE_SIZE, -3 * TILE_SIZE, 19 * TILE_SIZE, 14 * TILE_SIZE);
        hitBox = new Rectangle(88 * SCALE, 41 * SCALE, 32 * SCALE, 51 * SCALE);
        attackRate = 120;

        maxHealth = 100;
        currentHealth = maxHealth;
        attackPoints = 5;
        speed = 4;

        normalAttack = new Attack(this, 10, 5, "ATTACK");
        castAttack = new Attack(this, 9, 5, "CAST");
        death = new Death(this, 10, 10);
        idle = new Idle(this, 8, 10);
        walk = new Walk(this, 8, 10);

        deathHand = new DeathHand[4];
        bossThemeId = 2;
    }

    int skillCooldownCounter = 0;
    @Override
    public void update() {
        playBossTheme();
        skillCooldownCounter++;
        switch (currentState) {
            case IDLE:
                idle.update(this);
                image = idle.getImage();
                break;
            case WALK:
                walk.update(this);
                image = walk.getImage();
                break;
            case ATTACK:
                if (currentAttackType.equals("CAST")) {
                    castAttack();
                    image = castAttack.getImage();
                } else {
                    normalAttack();
                    image = normalAttack.getImage();
                }

                break;
            case DEATH:
                death.update(this);
                image = death.getImage();
                break;
        }
        for (DeathHand hand : deathHand)
            if (hand != null)
                hand.update();
    }

    int frameCounter = 0;
    public void normalAttack() {
        Player player = playing.getPlayer();
        frameCounter++;
        getDirectionForAttacking();
        int totalFrame = normalAttack.totalAnimationFrames * normalAttack.frameDuration;
        if (frameCounter == 5 * normalAttack.frameDuration) {
            if (canAttack(false))
                player.getHurt(attackPoints);
        }

        if (frameCounter == 2 * normalAttack.frameDuration) {
            playing.soundtrack.playSE(8);
        }
        if (frameCounter == totalFrame) {
            if (currentHealth < maxHealth * 3 / 4) {
                Random random = new Random();
                int x = random.nextInt(2);
                if (x == 0) currentAttackType = "CAST";
            }
            currentState = EntityState.IDLE;
            frameCounter = 0;
        }
    }

    public void castAttack() {
        Player player = playing.getPlayer();
        frameCounter++;
        getDirectionForAttacking();
        int totalFrame = castAttack.totalAnimationFrames * castAttack.frameDuration;
        if (frameCounter == totalFrame) {
            if (deathHand[0] == null) {
                int deathHandWorldX = player.worldX - 5 * TILE_SIZE / 2;
                int deathHandWorldY = player.worldY - TILE_SIZE * 5;
                deathHand[0] = new DeathHand(this, deathHandWorldX - 5 * TILE_SIZE / 2 , deathHandWorldY - 5 * TILE_SIZE / 2, 0);
                deathHand[1] = new DeathHand(this, deathHandWorldX, deathHandWorldY, 1);
                deathHand[2] = new DeathHand(this, deathHandWorldX + 5 * TILE_SIZE, deathHandWorldY - 5 * TILE_SIZE / 2, 2);
                deathHand[3] = new DeathHand(this, deathHandWorldX, deathHandWorldY + 5 * TILE_SIZE, 3);
            }
            currentState = EntityState.IDLE;
            currentAttackType = "NORMAL";
            frameCounter = 0;
        }
    }

    @Override
    public void move() {
        getDirectionForAttacking();
        super.move();
    }

    @Override
    public int getWorldY() {
        return worldY + height - 3 * TILE_SIZE / 2;
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        super.drawLockOn(g2, 5 * TILE_SIZE, 6 * TILE_SIZE, 0, -TILE_SIZE / 2);

        // Draw health bar
        int healthBarWidth = (int) ( 3 * TILE_SIZE * ((double) currentHealth / maxHealth));
        if (healthBarWidth > 0) {
            g2.setColor(Color.RED);
            g2.fillRect(getScreenX() + 5 * TILE_SIZE, getScreenY() + 3 * TILE_SIZE / 2, healthBarWidth, 4 * 3);
        }

        // Draw effect
        for (DeathHand hand : deathHand)
            if (hand != null)
                hand.draw(g2);


    }

    @Override
    public void drawBossIntro(Graphics2D g2) {
        if (bossImage == null) {
            bossImage = ImageLoader.imageManager.getMonsterImage(name, "Cast", "left", 4, width, height);
            bossImage = HelpMethods.scaleImage(bossImage,
                    1296f/width * 1.5);
            imageY = SCREEN_HEIGHT / 2 - bossImage.getHeight() / 2 - TILE_SIZE * 2;
            imageX = SCREEN_WIDTH - bossImage.getWidth() + TILE_SIZE * 18;
            textSize = 80f;

        }

        bossIntro(g2, "BringerOfDeath", bossImage);
    }

    public void removeEffect(int index) {
        deathHand[index] = null;
    }

}
