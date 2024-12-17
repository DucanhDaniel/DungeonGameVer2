package objects;

import components.*;

import static utils.Constants.Screen.TILE_SIZE;

public class Door {
    public String name;
    public AnimationComponent animation;
    public PositionComponent position;
    public RenderComponent render;
    public HitboxComponent hitbox;
    public int direction; // up: -1, down: 1, left: -2, right: 2
    public boolean isOpen;
    public boolean isLocked;
    public boolean playerPassed;

    public Door() {}

    public Door(String name, int worldX, int worldY, int direction,
                int renderWidth, int renderHeight,
                int hitboxWidth, int hitboxHeight) {
        this.name = "object/door/" + name;
        position = new PositionComponent(worldX, worldY);
        this.direction = direction;
        render = new RenderComponent(renderWidth, renderHeight);
        hitbox = new HitboxComponent(hitboxWidth, hitboxHeight);
        animation = new AnimationComponent(8, 10);
        isOpen = false;
        isLocked = false;
        playerPassed = false;
    }

}
