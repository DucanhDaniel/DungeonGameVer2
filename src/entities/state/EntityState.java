package entities.state;

import entities.Entity;
import entities.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class EntityState {
    public BufferedImage[][] up, down, left, right, up_right, up_left, down_right, down_left;
    public String imageFolderPath;
    public int numAnimationsFrames;
    public int numFramesToReverse = 5;
    public int numType;
    private int cnt = 0;
    public Entity currentEntity;

    public EntityState(Entity currentEntity, String imageFolderPath, int numAnimationsFrames,  int numType) {
        this.currentEntity = currentEntity;
        this.imageFolderPath = imageFolderPath;
        this.numAnimationsFrames = numAnimationsFrames;
        this.numType = numType;
        setUpImageArrays();
    }
    public void setUpImageArrays() {
        up = new BufferedImage[numAnimationsFrames][numType];
        down = new BufferedImage[numAnimationsFrames][numType];
        left = new BufferedImage[numAnimationsFrames][numType];
        right = new BufferedImage[numAnimationsFrames][numType];
        up_right = new BufferedImage[numAnimationsFrames][numType];
        up_left = new BufferedImage[numAnimationsFrames][numType];
        down_right = new BufferedImage[numAnimationsFrames][numType];
        down_left = new BufferedImage[numAnimationsFrames][numType];
    }
    public BufferedImage getImage(String direction, int type) {
        cnt++;
        for (int i = 0; i < numAnimationsFrames; i++) {
            if (cnt <= numFramesToReverse * (i + 1)) {
                return getImageFromArrays(i, direction, type);
            }
        }
        return getImageFromArrays(numAnimationsFrames - 1, direction, type);
    }

    public BufferedImage getImageFromArrays(int index, String direction, int type) {
        return switch (direction) {
            case "up" -> up[index][type];
            case "down" -> down[index][type];
            case "left" -> left[index][type];
            case "right" -> right[index][type];
            case "up_right" -> up_right[index][type];
            case "up_left" -> up_left[index][type];
            case "down_right" -> down_right[index][type];
            case "down_left" -> down_left[index][type];
            default -> null;
        };
    }

    public abstract void handleInput(Sprite sprite);
    public abstract void render(Graphics2D g2);
}
