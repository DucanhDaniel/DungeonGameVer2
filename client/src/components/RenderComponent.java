package components;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.awt.image.BufferedImage;

public class RenderComponent {
    public RenderComponent() {
    }

    @JsonIgnore
    public BufferedImage image;

    public int width, height;

    public RenderComponent(int width, int height) {
        this.width = width;
        this.height = height;
    }

}
