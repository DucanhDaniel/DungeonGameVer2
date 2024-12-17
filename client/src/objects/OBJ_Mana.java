package objects;

import components.*;
import entities.Player;
import static utils.Constants.Screen.TILE_SIZE;

public class OBJ_Mana extends Collectible {
    public OBJ_Mana() {}
    public OBJ_Mana(String name, int worldX, int worldY, int itemValue) {
        this.name = super.name + "/" + name;
        position = new PositionComponent(worldX, worldY);
        render = new RenderComponent(TILE_SIZE, TILE_SIZE * 2);
        hitbox = new HitboxComponent(TILE_SIZE / 2, TILE_SIZE / 2);
        item = new ItemComponent("mana", itemValue);
        animation = new AnimationComponent(12, 5);
    }

    @Override
    public void interact(Player player) {
        player.increaseMana(item.value);
    }
    public void update() {
        animation.updateAnimation();
    }
}
