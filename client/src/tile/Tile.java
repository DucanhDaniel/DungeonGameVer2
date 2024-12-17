package tile;

import java.awt.image.BufferedImage;

public class Tile {
    public BufferedImage image;
    public boolean collision;

    public Tile() {

    }

    public Tile(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getTileImg() {return image;}

    public boolean isSolid() {
        return collision;
    }

}
