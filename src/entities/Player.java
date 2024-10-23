package entities;

import entities.state.IdleState;
import entities.state.WalkState;
import gamestates.Playing;
import main.KeyHandler;

import static utils.Constants.Player.*;
import static utils.Constants.Screen.*;
public class Player extends Sprite {
    public int worldX, worldY;
    public KeyHandler keyHandler;

    public int walkType, attackType, idleType;

    public Player(String name, String image_path, Playing playing, int width, int height, int numAnimationFrames) {
        super(name, image_path, playing, width, height, numAnimationFrames);
        idleState = new IdleState(this, name, 8, 3);
        walkState = new WalkState(this, name, 8, 3);
        currentState = idleState;
        direction = "down";
        walkType = 0;
        attackType = 0;
        idleType = 0;
        setDefaultValues();
    }


    public void setDefaultValues() {
        worldX = TILE_SIZE * 23;
        worldY = TILE_SIZE * 21;
    }


}
