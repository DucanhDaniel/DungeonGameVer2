package entities.state;

import entities.Sprite;
import utils.HelpMethods;

import java.awt.*;

public class WalkState extends EntityState{
    public WalkState(String imageFolderPath, int numAnimationsFrames, int numType) {
        super(imageFolderPath, numAnimationsFrames, numType);
        loadImages();
    }

    public void loadImages() {
        for (int type = 0; type < numType; type++) {
            for (int i = 0; i < numAnimationsFrames; i++) {
                up[i][type] = HelpMethods.setUp(imageFolderPath + "up" + (i + 1), currentEntity.width, currentEntity.height);
                up[i][type] = HelpMethods.setUp(imageFolderPath + "up" + (i + 1), currentEntity.width, currentEntity.height);
                down[i][type] = HelpMethods.setUp(imageFolderPath + "down" + (i + 1), currentEntity.width, currentEntity.height);
                left[i][type] = HelpMethods.setUp(imageFolderPath + "left" + (i + 1), currentEntity.width, currentEntity.height);
                right[i][type] = HelpMethods.setUp(imageFolderPath + "right" + (i + 1), currentEntity.width, currentEntity.height);
                up_left[i][type] = HelpMethods.setUp(imageFolderPath + "up_left" + (i + 1), currentEntity.width, currentEntity.height);
                up_right[i][type] = HelpMethods.setUp(imageFolderPath + "up_right" + (i + 1), currentEntity.width, currentEntity.height);
                down_left[i][type] = HelpMethods.setUp(imageFolderPath + "down_left" + (i + 1), currentEntity.width, currentEntity.height);
                down_right[i][type] = HelpMethods.setUp(imageFolderPath + "down_right" + (i + 1), currentEntity.width, currentEntity.height);
            }
        }
    }

    @Override
    public void handleInput(Sprite sprite) {

    }

    @Override
    public void render(Graphics2D g2) {

    }
}
