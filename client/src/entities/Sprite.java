package entities;

import enitystates.EntityState;
import gamestates.Playing;
import utils.HelpMethods;

import java.awt.*;


public class Sprite extends Entity {
    public int speed;
    public String direction = "down";
    public boolean isIdling = true;
    public Rectangle visionBox;
    public Rectangle attackBox;
    public Rectangle hitBox;



    public Sprite(String name, Playing playing, int width, int height) {
        super(name, playing, width, height);
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        // Draw box for debugging purpose
        if (playing.getGame().getKeyboardInputs().enterPressed) {
            g2.setColor(Color.GREEN);
            if (hitBox != null)
                g2.drawRect(hitBox.x + getScreenX(), hitBox.y + getScreenY(), hitBox.width, hitBox.height);

            g2.setColor(Color.YELLOW);
            if (solidArea != null)
                g2.drawRect(solidArea.x + getScreenX(), solidArea.y + getScreenY(), solidArea.width, solidArea.height);
            if (visionBox != null)
                g2.drawRect(visionBox.x + getScreenX(), visionBox.y + getScreenY(), visionBox.width, visionBox.height);
            if (attackBox != null)
                g2.drawRect(attackBox.x + getScreenX(), attackBox.y + getScreenY(), attackBox.width, attackBox.height);
        }
    }

    public void move() {
        playing.getGame().getCollisionChecker().checkTile(this);
        playing.getGame().getCollisionChecker().checkEntity(this, getPlaying().entityList);
        if (collisionOn) return;
        goAlongDirection();

    }
    public void goAlongDirection() {
        if (direction.equals("down")) {
            worldY += speed;
        }
        if (direction.equals("up")) {
            worldY -= speed;
        }
        if (direction.equals("left")) {
            worldX -= speed;
        }
        if (direction.equals("right")) {
            worldX += speed;
        }
        if (direction.equals("right_down")) {
            worldX += speed - 1;
            worldY += speed - 1;
        }
        if (direction.equals("left_up")) {
            worldX -= speed - 1;
            worldY -= speed - 1;
        }
        if (direction.equals("left_down")) {
            worldX -= speed - 1;
            worldY += speed - 1;
        }
        if (direction.equals("right_up")) {
            worldX += speed - 1;
            worldY -= speed - 1;
        }
    }
    public void goOppositeDirection() {
        if (direction.equals("down")) {
            worldY -= speed;
        }
        if (direction.equals("up")) {
            worldY += speed;
        }
        if (direction.equals("left")) {
            worldX += speed;
        }
        if (direction.equals("right")) {
            worldX -= speed;
        }
        if (direction.equals("right_down")) {
            worldX -= speed - 1;
            worldY -= speed - 1;
        }
        if (direction.equals("left_up")) {
            worldX += speed - 1;
            worldY += speed - 1;
        }
        if (direction.equals("left_down")) {
            worldX += speed - 1;
            worldY -= speed - 1;
        }
        if (direction.equals("right_up")) {
            worldX -= speed - 1;
            worldY += speed - 1;
        }
    }

    public void knock_back(int speed, String direction) {
        String temp = this.direction;
        int tempSpeed = this.speed;
        this.direction = direction;
        this.speed = speed;
        move();
        this.direction = temp;
        this.speed = tempSpeed;
    }
    public boolean canSeePlayer() {
        Player player = playing.getPlayer();
        if (player.currentState == EntityState.DEATH) return false;
        int visionDefaultX = visionBox.x;
        int visionDefaultY = visionBox.y;

        player.solidArea.x += player.worldX;
        player.solidArea.y += player.worldY;
        visionBox.x += worldX;
        visionBox.y += worldY;

        boolean result = visionBox.intersects(player.solidArea);

        player.solidArea.x = player.solidAreaDefaultX;
        player.solidArea.y = player.solidAreaDefaultY;
        visionBox.x = visionDefaultX;
        visionBox.y = visionDefaultY;

        return result && HelpMethods.canSeeEntity(playing, this, player);
    }

    public void getDirectionForAttacking() {
        int dx = playing.getPlayer().getWorldX() - getWorldX();
        int dy = playing.getPlayer().getWorldY() - getWorldY();

        double angle = (Math.atan2(dy, dx) * 180 / Math.PI);
        if (angle >= -22 && angle < 22) direction = "right";
        else if (angle >= 22 && angle < 67) direction = "right_down";
        else if (angle >= 67 && angle < 112) direction = "down";
        else if (angle >= 112 && angle < 157) direction = "left_down";
        else if (angle >= 157 || angle < -157) direction = "left";
        else if (angle >= -157 && angle < -112) direction = "left_up";
        else if (angle >= -112 && angle < -67) direction = "up";
        else direction = "right_up";
    }

    public void removeDash() {
    }
}
