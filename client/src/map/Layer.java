package map;

import java.awt.*;

public abstract class Layer {

    public Layer(){}

    public abstract void render(Graphics2D g2);
    public abstract void update();
}
