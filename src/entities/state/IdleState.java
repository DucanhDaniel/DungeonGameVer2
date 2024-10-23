package entities.state;

import entities.Sprite;
import utils.HelpMethods;

import java.awt.*;

public class IdleState extends EntityState {

    public IdleState(String imageFolderPath, int numAnimationsFrames, int numType) {
        super(imageFolderPath, numAnimationsFrames, numType);
        loadImages();
    }

    public void loadImages() {
        for (int type = 0; type < numType; type++)
            for (int i = 0; i < numAnimationsFrames; i++) {
                up[i][type] = HelpMethods.setUp(imageFolderPath + "idle_up_" + (i + 1), currentEntity.width, currentEntity.height);
                down[i][type] = HelpMethods.setUp(imageFolderPath + "idle_down_" + (i + 1), currentEntity.width, currentEntity.height);
                left[i][type] = HelpMethods.setUp(imageFolderPath + "idle_left_" + (i + 1), currentEntity.width, currentEntity.height);
                right[i][type] = HelpMethods.setUp(imageFolderPath + "idle_right_" + (i + 1), currentEntity.width, currentEntity.height);
                up_left[i][type] = HelpMethods.setUp(imageFolderPath + "idle_up_left_" + (i + 1), currentEntity.width, currentEntity.height);
                up_right[i][type] = HelpMethods.setUp(imageFolderPath + "idle_up_right_" + (i + 1), currentEntity.width, currentEntity.height);
                down_left[i][type] = HelpMethods.setUp(imageFolderPath + "idle_down_left_" + (i + 1), currentEntity.width, currentEntity.height);
                down_right[i][type] = HelpMethods.setUp(imageFolderPath + "idle_down_right_" + (i + 1), currentEntity.width, currentEntity.height);
            }
    }

    @Override
    public void handleInput(Sprite sprite) {
        // Handle state changing using KeyHandler or something else

    }

    @Override
    public void render(Graphics2D g2) {

    }
}
