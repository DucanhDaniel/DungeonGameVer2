package entities;

import effect.Dash;
import enitystates.*;
import entities.monsters.Monster;
import gamestates.Gamestate;
import gamestates.Playing;
import gamestates.Selection;
import inputs.KeyboardInputs;
import main.CollisionChecker;
import utils.Constants;
import utils.HelpMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

import static utils.Constants.Player.*;
import static utils.Constants.Screen.*;

public class Player extends Sprite {
    // Player's states
    public Attack attack;
    Idle idle;
    public Run run;
    Walk walk;
    Death death;

    // Player's attributes
    public int maxHealth, maxArmor, maxMana, currentArmor, currentHealth, currentMana;
    public int attackPointSpear, attackPointGun, manaCostPerShot;
    public int spearAttackRange, gunAttackRange;
    public int outOfCombatCounter, armorGenTime, outOfCombatTime;

    // Player's weapons
    public String currentWeapon = "NORMAL";

    // Player weapons attackBox
    public Rectangle spearAttackBox, gunAttackBox;

    //Player's skills
    public Dash dash = null;

    public Player(Playing playing) {
        super("Player", playing, PLAYER_IMAGE_WIDTH, PLAYER_IMAGE_HEIGHT);
        setDefaultValues();
        attack = new Attack(this);
        idle = new Idle(this);
        run = new Run(this);
        walk = new Walk(this);
        death = new Death(this);

        spearAttackBox = new Rectangle(0, 0, 3 * TILE_SIZE, 4 * TILE_SIZE);
        gunAttackBox = new Rectangle(-7 * TILE_SIZE / 2, -3 * TILE_SIZE, 10 * TILE_SIZE, 10 * TILE_SIZE);
        solidArea = new Rectangle();
        solidArea.setBounds(18 * SCALE, 32 * SCALE, 13 * SCALE, 12 * SCALE);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public void setDefaultValues() {
        worldX = 1136;
        worldY = 1693;


//        speed = 4;
//        maxArmor = 10;
//        maxHealth = 12;
//        maxMana = 200;
//        currentArmor = maxArmor;
//        currentHealth = maxHealth;
//        currentMana = maxMana;
//        attackPointSpear = 5;
//        attackPointGun = 5;
        armorGenTime = 90; // 1.5 seconds between increasing 1 armor point
        outOfCombatTime = 360 - armorGenTime; // After 6 seconds out of combat, armor will be generated
        spearAttackRange = 3 * TILE_SIZE;
        gunAttackRange = 10 * TILE_SIZE;
        manaCostPerShot = 1;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (dash != null) {
            dash.draw(g2);
            g2.drawImage(HelpMethods.makeMoreTransparent(image, 100), PLAYER_SCREEN_X, PLAYER_SCREEN_Y, null);
        } else g2.drawImage(image, PLAYER_SCREEN_X, PLAYER_SCREEN_Y, null);

        g2.setColor(Color.RED);
        if (currentWeapon.equals("GUN")) {
            g2.drawRect(gunAttackBox.x + PLAYER_SCREEN_X,
                    gunAttackBox.y + PLAYER_SCREEN_Y,
                    gunAttackBox.width, gunAttackBox.height);
        }
        else if (currentWeapon.equals("SPEAR")) {
            g2.drawRect(spearAttackBox.x + PLAYER_SCREEN_X,
                    spearAttackBox.y + PLAYER_SCREEN_Y,
                    spearAttackBox.width, spearAttackBox.height);
        }

        if (playing.getGame().getKeyboardInputs().enterPressed) {
            int screenX = PLAYER_SCREEN_X;
            int screenY = PLAYER_SCREEN_Y;
            g2.setColor(Color.WHITE);
            g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }

    int frameCounter = 0;
    int weaponSwitchDelayed = 60;
    int cooldownCounter = 0;
    int skillCooldown = 60;

    @Override
    public void update() {
        // Switch weapons
        KeyboardInputs keyboardInputs = playing.getGame().getKeyboardInputs();
        frameCounter++;
        if (keyboardInputs.changWeaponPressed && frameCounter > weaponSwitchDelayed) {
            switch (currentWeapon) {
                case "NORMAL" -> currentWeapon = "SPEAR";
                case "SPEAR" -> currentWeapon = "GUN";
                case "GUN" -> currentWeapon = "NORMAL";
            }
            frameCounter = 0;
            if (currentWeapon.equals("NORMAL") && currentState == EntityState.ATTACK) {
                currentState = EntityState.IDLE;
            }
        }

        // Skill cooldown
        cooldownCounter++;
        if (currentState == EntityState.WALK || currentState == EntityState.RUN)
            if (keyboardInputs.skillActivePressed && cooldownCounter > skillCooldown) {
                dash = new Dash(this, 20);
                cooldownCounter = 0;
            }

        // Update state
        switch (currentState) {
            case IDLE:
                idle.update(this, keyboardInputs);
                image = idle.getImage();
                break;
            case RUN:
                run.update(this, keyboardInputs);
                image = run.getImage();
                break;
            case WALK:
                walk.update(this, keyboardInputs);
                image = walk.getImage();
                break;
            case ATTACK:
                attack.update(this, keyboardInputs);
                image = attack.getImage();
                break;
            case DEATH:
                death.update(this);
                image = death.getImage();
                break;
        }

        // Update skill
        if (dash != null) dash.update();

        // Increase armor when out of combat
        outOfCombatCounter++;
        if (outOfCombatCounter > outOfCombatTime + armorGenTime) {
            if (currentArmor < maxArmor) currentArmor++;
            outOfCombatCounter = outOfCombatTime;
        }
    }

    public void lockOn() {
        int angle = getAngleAuto();
//        if (angle == 181) return;
        if (angle <= 15 && angle >= -15) this.direction = "left";
        else if (angle < -15 && angle > -75) this.direction = "left_down";
        else if (angle <= -75 && angle >= -105) this.direction = "down";
        else if (angle <= -105 && angle >= -170) this.direction = "right_down";
        else if (angle > 105 && angle < 170) this.direction = "right_up";
        else if (angle >= 75 && angle <= 105) this.direction = "up";
        else if (angle > 15 && angle < 75) this.direction = "left_up";
        else if (angle != 181) this.direction = "right";
//        System.out.println(direction + " " + angle);
        changeScreenPosition(angle, 2 * TILE_SIZE);
    }


    public void transition(int x1, int y1, int x2, int y2) {
        float dx = x2 - PLAYER_SCREEN_X;
        float dy = y2 - PLAYER_SCREEN_Y;
        float speed = 4f; // Tốc độ di chuyển (pixels/frame)
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance < speed) {
            PLAYER_SCREEN_X = x2;
            PLAYER_SCREEN_Y = y2;
            return;
        }

        float stepX = (dx / distance) * speed;
        float stepY = (dy / distance) * speed;

        PLAYER_SCREEN_X += (int) stepX;
        PLAYER_SCREEN_Y += (int) stepY;
    }


    String lastDirection;
    public void changeScreenPosition(int angle, int distance) {
        int normalPlayerScreenX = 312;
        int normalPlayerScreenY = 216;
        int newX = normalPlayerScreenX, newY = normalPlayerScreenY;
        if (angle != 181) {
            switch (this.direction) {
                case "left":
                    newX = normalPlayerScreenX + distance;
                    break;
                case "left_down":
                    newX = normalPlayerScreenX + (int)(distance * 0.7f);
                    newY = normalPlayerScreenY - (int)(distance * 0.7f);
                    break;
                case "down":
                    newY = normalPlayerScreenY - distance;
                    break;
                case "right":
                    newX = normalPlayerScreenX - distance;
                    break;
                case "right_down":
                    newX = normalPlayerScreenX - (int)(distance * 0.7f);
                    newY = normalPlayerScreenY - (int)(distance * 0.7f);
                    break;
                case "right_up":
                    newX = normalPlayerScreenX - (int)(distance * 0.7f);
                    newY = normalPlayerScreenY + (int)(distance * 0.7f);
                    break;
                case "up":
                    newY = normalPlayerScreenY + distance;
                    break;
                case "left_up":
                    newX = normalPlayerScreenX + (int)(distance * 0.7f);
                    newY = normalPlayerScreenY + (int)(distance * 0.7f);
                    break;
            }
        }
//        System.out.println(newX + " " + newY);
//        System.out.println(normalPlayerScreenX + " " + normalPlayerScreenY + " asdasd");
        if (PLAYER_SCREEN_X != newX || PLAYER_SCREEN_Y != newY) {
//            System.out.println(angle + " " + PLAYER_SCREEN_X + " " + PLAYER_SCREEN_Y + " " + newX + " " + newY);
            transition(PLAYER_SCREEN_X, PLAYER_SCREEN_Y, newX, newY);
        }
        lastDirection = direction;
    }
    public void resetScreenPosition() {
        PLAYER_SCREEN_X = 312;
        PLAYER_SCREEN_Y = 216;
//        normalPlayerScreenY = 312;
//        normalPlayerScreenX = 216;
    }

    public int getAngleMouse() {
        KeyboardInputs keyboardInputs = playing.getGame().getKeyboardInputs();
        int mouseX = keyboardInputs.getMouseX();
        int mouseY = keyboardInputs.getMouseY();
        int angle;
        int dx = -mouseX + PLAYER_SCREEN_X + TILE_SIZE * 3 / 2;
        int dy = -mouseY + PLAYER_SCREEN_Y + TILE_SIZE * 3 / 2;
        angle = (int) (Math.atan2(dy, dx) * 180 / Math.PI);
        return angle;
    }

    private int getAngleAuto() {
        int distance = Integer.MAX_VALUE;
        int angle = 0;
        Monster lockedMonster = null;
        ArrayList<Monster> entities = this.getPlaying().monsters;
        for (Monster entity : entities)
            if (entity != null && entity.isOnTheScreen()) {
                entity.isBeingLockOn = false;
                if (entity.currentState != EntityState.DEATH && HelpMethods.canSeeEntity(playing, this, entity)) {

                    int newDistance = (this.getWorldY() - entity.getWorldY()) * (this.getWorldY() - entity.getWorldY()) +
                            (this.getWorldX() - entity.getWorldX()) * (this.getWorldX() - entity.getWorldX());
                    if (!canAttackMonster(entity)) continue;

                    if (newDistance < distance && entity.isOnTheScreen()) {
                        lockedMonster = entity;
                        distance = newDistance;
                        int dx = -entity.getWorldX() + this.getWorldX();
                        int dy = -entity.getWorldY() + this.getWorldY();
                        angle = (int) (Math.atan2(dy, dx) * 180 / Math.PI);
                    }
                }
            }
        if (lockedMonster == null) return 181;
        lockedMonster.isBeingLockOn = true;
        return angle;
    }

    @Override
    public int getWorldX() {
        return worldX + TILE_SIZE * 3 / 2;
    }

    @Override
    public int getWorldY() {
        return worldY + TILE_SIZE * 2;
    }

    public int getSpeed() {
        int walkSpeed = 0, runSpeed = 0;
        switch (Selection.playerType) {
            case 0: // Xa thu
                walkSpeed = 4;
                runSpeed = 5;
                break;
            case 1: // Dau si
                walkSpeed = 3;
                runSpeed = 4;
                break;
            case 2: // Sat thu
                walkSpeed = 5;
                runSpeed = 6;
                break;
            default:
                break;
        }

        if (currentState == EntityState.WALK) {
            if (dash != null) return runSpeed;
            return walkSpeed;
        } else if (currentState == EntityState.RUN) {
            if (dash != null) return runSpeed;
            return runSpeed;
        }
        return 0;
    }

    public void getHurt(int damage) {
        if (dash != null) return;
        outOfCombatCounter = 0;

        if (currentArmor > damage) {
            currentArmor -= damage;
            return;
        } else {
            damage -= currentArmor;
            currentArmor = 0;
        }

        currentHealth -= damage;
        if (currentHealth <= 0) {
            currentHealth = 0;
            currentState = EntityState.DEATH;
        }
    }

    public void increaseHealth(int health) {
        currentHealth += health;
        if (currentHealth > maxHealth) currentHealth = maxHealth;
    }

    public void increaseMana(int mana) {
        currentMana += mana;
        if (currentMana > maxMana) currentMana = maxMana;
    }

    public boolean canAttackMonster(Monster monster) {
        int currentHitBoxX = monster.hitBox.x;
        int currentHitBoxY = monster.hitBox.y;
        int spearX = spearAttackBox.x;
        int spearY = spearAttackBox.y;
        int gunX = gunAttackBox.x;
        int gunY = gunAttackBox.y;

        spearAttackBox.x += worldX;
        spearAttackBox.y += worldY;
        gunAttackBox.x += worldX;
        gunAttackBox.y += worldY;
        monster.hitBox.x += monster.worldX;
        monster.hitBox.y += monster.worldY;

        boolean result = false;
        if (currentWeapon.equals("SPEAR")) {
            if (monster.hitBox.intersects(spearAttackBox)) result = true;
        } else if (currentWeapon.equals("GUN")) {
            if (monster.hitBox.intersects(gunAttackBox)) result = true;
        }
        monster.hitBox.x = currentHitBoxX;
        monster.hitBox.y = currentHitBoxY;
        spearAttackBox.x = spearX;
        spearAttackBox.y = spearY;
        gunAttackBox.x = gunX;
        gunAttackBox.y = gunY;
        return result;
    }

    @Override
    public void move() {
        CollisionChecker cc = playing.getGame().getCollisionChecker();
        if (dash != null) {
            cc.checkTile(this);
            if (collisionOn) return;
            goAlongDirection();
            return;
        }
//        cc.checkTile(this);
        cc.checkEntity(this, playing.entityList);
        if (collisionOn) return;
        goAlongDirection();

    }
    @Override
    public void removeDash() {
        dash = null;
    }
}
