package entities.monsters.bosses;

import effect.Explosion;
import enitystates.*;
import static enitystates.EntityState.*;

import entities.monsters.Monster;
import gamestates.Playing;
import entities.Player;
import utils.HelpMethods;
import utils.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static utils.Constants.Screen.*;

public class Demon extends Boss {
    public int currentPhase = 1;
    public Idle transform, idlePhase1, idlePhase2;
    public Walk walkPhase1, walkPhase2;
    public Attack attackPhase1, attackPhase2;
    public Attack cast, fire_breath;
    public Death deathPhase2;
    private String attackPhase2Type = "CAST";

    private final Explosion[] explosion;

    private boolean isSoundtrackPlayed = false;

    public Demon(Playing playing, int worldX, int worldY) {
        super("Demon", playing, 288 * SCALE, 160 * SCALE);

        // Phase1's boxes
        solidArea = new Rectangle(8 * TILE_SIZE, 9 * TILE_SIZE, 2 * TILE_SIZE, TILE_SIZE);
        visionBox = new Rectangle(3 * TILE_SIZE, 5 * TILE_SIZE, 12 * TILE_SIZE, 9 * TILE_SIZE);
        hitBox = new Rectangle(8 * TILE_SIZE, 17 * TILE_SIZE / 2, 2 * TILE_SIZE, 3 * TILE_SIZE / 2);
        attackBox = new Rectangle(7 * TILE_SIZE, 7 * TILE_SIZE, 4 * TILE_SIZE, 5 * TILE_SIZE);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        this.worldX = worldX;
        this.worldY = worldY;

        maxHealth = 10;
        currentHealth = maxHealth;
        attackPoints = 2;
        speed = 4;
        attackRate = 60;

        transform = new Idle(this, 32, 10, "Transform");
        idlePhase1 = new Idle(this, 6, 5, "Phase1_idle");
        idlePhase2 = new Idle(this, 6, 10, "Phase2_idle");

        walkPhase1 = new Walk(this, 8, 5, "Phase1_walk");
        walkPhase2 = new Walk(this, 12, 10, "Phase2_walk");

        // Physical attacking
        attackPhase1 = new Attack(this, 8, 5, "Phase1_attack");
        attackPhase2 = new Attack(this, 15, 5, "Phase2_attack");

        // Magic
        cast = new Attack(this, 6, 10, "Phase2_cast");
        explosion = new Explosion[5];
        fire_breath = new Attack(this, 21, 5, "Phase2_fire_breath");

        deathPhase2 = new Death(this, 23, 10, "Phase2_death");

    }

    @Override
    public void update() {
        if (canSeePlayer() && !isSoundtrackPlayed && currentPhase == 2 && currentState != DEATH) {
            playing.soundtrack.playMusic(1);
            isSoundtrackPlayed = true;
        } else if ((!canSeePlayer() || currentState == EntityState.DEATH) && isSoundtrackPlayed) {
            isSoundtrackPlayed = false;
            playing.setLevelTheme();
        }
        if (currentPhase == 1) {
            switch (currentState) {
                case IDLE:
                    idlePhase1.update(this);
                    image = idlePhase1.getImage();
                    break;
                case WALK:
                    walkPhase1.update(this);
                    image = walkPhase1.getImage();
                    break;
                case ATTACK:
                    attackPhase1();
                    image = attackPhase1.getImage();
                    break;
                case DEATH:
                    image = transform.getImage();
                    if (transform.frameCounter == 0 && transform.numAnimationFrames == 1) {
                        playing.soundtrack.playSE(3);
                    }
                    if (transform.numAnimationFrames + 1 == transform.totalAnimationFrames) {
                        switchPhase();
                    }
                    break;
            }
        } else {
            switch (currentState) {
                case IDLE:
                    idlePhase2.update(this);
                    image = idlePhase2.getImage();
                    break;
                case WALK:
                    walkPhase2.update(this);
                    image = walkPhase2.getImage();
                    break;
                case ATTACK:
                    switch (attackPhase2Type) {
                        case "NORMAL":
                            attackPhase2();
                            image = attackPhase2.getImage();
                            break;
                        case "FIRE_BREATH":
                            fire_breath();
                            image = fire_breath.getImage();
                            break;
                        case "CAST":
                            cast();
                            image = cast.getImage();
                            break;
                    }
                    break;
                case DEATH:
                    image = deathPhase2.getImage();
                    if (deathPhase2.frameCounter == 0 && deathPhase2.animationIndex == 2) {
                        playing.soundtrack.playSE(3);
                    }
//                    System.out.println(deathPhase2.animationIndex + " " + deathPhase2.frameCounter);
//                    if (deathPhase2.animationIndex == 23 &&
//                            deathPhase2.frameCounter == 10) {
//                        playing.soundtrack.playMusic(0);
//                        isSoundtrackPlayed = true;
//                    }
                    break;
            }
        }

        for (Explosion value : explosion) if (value != null) value.update();
    }


    int frameCounter = 0;
    public void attackPhase1() {
        Player player = playing.getPlayer();
        frameCounter++;
        getDirectionForAttacking();
        int totalFrame = attackPhase1.totalAnimationFrames * attackPhase1.frameDuration;

        if (frameCounter == totalFrame) {
            if (canAttack(false)) player.getHurt(attackPoints);
            currentState = IDLE;
            frameCounter = 0;
        }
    }

    public void attackPhase2() {
        Player player = playing.getPlayer();
        frameCounter++;
        getDirectionForAttacking();
        int totalFrame = attackPhase2.totalAnimationFrames * attackPhase2.frameDuration;
        if (frameCounter == attackPhase2.frameDuration * 10) {
            playing.cameraShake.startShake();
            if (canAttack(false)) player.getHurt(attackPoints);
            playing.soundtrack.playSE(2);
        }
        if (frameCounter == totalFrame) {
            currentState = IDLE;
            frameCounter = 0;
            changeAttackType();
        }
    }

    public void fire_breath() {
        Player player = playing.getPlayer();
        frameCounter++;
        getDirectionForAttacking();
        int totalFrame = fire_breath.totalAnimationFrames * fire_breath.frameDuration;
        int attackFrameMin = fire_breath.frameDuration * 9;
        int attackFrameMax = fire_breath.frameDuration * 16;
        if (frameCounter == attackFrameMin) {
            playing.soundtrack.playSE(1);
        }
        if (frameCounter >= attackFrameMin && frameCounter <= attackFrameMax) {
            if (canAttack(false) && frameCounter % fire_breath.frameDuration == 0) {
                player.getHurt(1);
            }
        }
        if (frameCounter == totalFrame) {
            currentState = IDLE;
            frameCounter = 0;
            attackPhase2Type = "NORMAL";
        }
    }

    public void cast() {
        Player player = playing.getPlayer();
        frameCounter++;
        getDirectionForAttacking();
        int totalFrame = fire_breath.totalAnimationFrames * fire_breath.frameDuration;

        if (frameCounter == totalFrame) {
            int playerWorldX = player.getWorldX();
            int playerWorldY = player.getWorldY();
            explosion[0] = new Explosion(this, playerWorldX - 5 * TILE_SIZE,  playerWorldY - 5 * TILE_SIZE, 0);
            explosion[1] = new Explosion(this, playerWorldX + 5 * TILE_SIZE, playerWorldY - 5 * TILE_SIZE, 1);
            explosion[2] = new Explosion(this, playerWorldX - 5 * TILE_SIZE, playerWorldY + 5 * TILE_SIZE, 2);
            explosion[3] = new Explosion(this, playerWorldX + 5 * TILE_SIZE, playerWorldY + 5 * TILE_SIZE, 3);
            explosion[4] = new Explosion(this, playerWorldX, playerWorldY, 4);
            currentState = IDLE;
            frameCounter = 0;
            attackPhase2Type = "NORMAL";
        }
    }


    @Override
    public void move() {
        getDirectionForAttacking();
        super.move();
    }

    @Override
    public int getWorldY() {
        return worldY + solidArea.y + solidArea.height / 2;
    }


    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        // Draw health bar
        if (currentPhase == 1) {
            // Health bar phase 1
            int healthBarWidth = (int) ( 2 * TILE_SIZE * ((double) currentHealth / maxHealth));
            if (healthBarWidth > 0) {
                g2.setColor(Color.RED);
                g2.fillRect(getScreenX() + 8 * TILE_SIZE, getScreenY() + 7 * TILE_SIZE, healthBarWidth, 4 * 3);
            }
        } else {
            // Health bar phase 2
            int healthBarWidth = (int) ( 4 * TILE_SIZE * ((double) currentHealth / maxHealth));
            if (healthBarWidth > 0) {
                g2.setColor(Color.RED);
                g2.fillRect(getScreenX() + 7 * TILE_SIZE, getScreenY() + 4 * TILE_SIZE, healthBarWidth, 4 * 3);
            }
        }

        // Draw lockOn
        if (currentPhase == 1) {
            super.drawLockOn(g2, 7 * TILE_SIZE / 2, 7 * TILE_SIZE / 2, 0, - TILE_SIZE / 4);
        }
        else super.drawLockOn(g2, 8 * TILE_SIZE, 8 * TILE_SIZE, 0,  - TILE_SIZE);

        // Draw explosions
        for (Explosion value : explosion) {
            if (value != null) {
                value.draw(g2);
            }
        }
    }

    @Override
    public void drawBossIntro(Graphics2D g2) {
        if (currentPhase == 2) {
            if (bossImage == null) {
                textSize = 90f;
            }
            bossIntro(g2, "Demon Slime",
                    HelpMethods.scaleImage(ImageLoader.imageManager.getMonsterImage("Demon", "Phase2_Idle", "left", 1, width, height), 1.5f));
        }
    }

    private void switchPhase() {
        currentPhase = 2;
        currentState = IDLE;

        // Phase 2's attributes
        maxHealth = 100;
        currentHealth = maxHealth;

        solidArea = new Rectangle(7 * TILE_SIZE, 7 * TILE_SIZE, 4 * TILE_SIZE, 3 * TILE_SIZE);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        attackBox = new Rectangle(TILE_SIZE, 5 * TILE_SIZE, 16 * TILE_SIZE, 6 * TILE_SIZE);
        visionBox = new Rectangle(0, 0, 18 * TILE_SIZE, 16 * TILE_SIZE);
        hitBox = (Rectangle) solidArea.clone();

        attackPoints = 5;
        attackRate = 100;

    }

    public void removeEffect(int index) {
        explosion[index] = null;
    }

    Random random = new Random();
    int x;
    public void changeAttackType() {
        x = random.nextInt(3);
        switch (x) {
            case 0:
                attackPhase2Type = "FIRE_BREATH";
                break;
            case 1:
                attackPhase2Type = "CAST";
                break;
            case 2:
                attackPhase2Type = "NORMAL";
                break;
        }
    }


}
