package objects;

import entities.Entity;
import gamestates.Playing;

public class Object extends Entity {
    public Object(String name, Playing playing, int worldX, int worldY, int width, int height) {
        super(name, playing, width, height);
        this.worldX = worldX;
        this.worldY = worldY;
    }

    public void updateImage() {

    }
}
