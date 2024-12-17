package objects;

import components.*;
import entities.Player;
import static utils.Constants.Screen.TILE_SIZE;

public class OBJ_Heart extends Collectible {

    public OBJ_Heart() {}

    @Override
    public void interact(Player player) {
        player.increaseHealth(item.value);
    }
    public void update() {
        animation.updateAnimation();
    }
}
