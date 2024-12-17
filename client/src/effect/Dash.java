package effect;

import java.awt.*;
import java.awt.image.BufferedImage;
import entities.Player;
import entities.Sprite;
import utils.HelpMethods;

import static utils.Constants.Player.*;

public class Dash {
    BufferedImage[] dashImages;
    int[] dashImageX, dashImageY;
    int[] dashAlpha;
    Sprite entity;
    int dashImagesSize;
    int duration;

    public Dash(Sprite entity, int duration) {
        this.entity = entity;
        this.duration = duration;
        dashImagesSize = duration/5;

        alphaDiff = 255 / (duration / dashImagesSize);
        dashImages = new BufferedImage[dashImagesSize];
        dashImageX = new int[dashImagesSize];
        dashImageY = new int[dashImagesSize];
        dashAlpha = new int[dashImagesSize];

        for (int i = 0; i < dashImagesSize; i++)
            dashAlpha[i] = 255;
    }

    int alphaDiff;
    int frameCounter = 0;
    public void update() {
        for (int i = 0; i < dashImages.length; i++) {
            if (frameCounter == i * duration / dashImagesSize) {
                dashImages[i] = HelpMethods.makeWhiteExceptTransparent(entity.image);
                dashImageX[i] = entity.worldX;
                dashImageY[i] = entity.worldY;
            }
        }
        frameCounter++;
        if (frameCounter % (duration / dashImagesSize) == 0) {
            for (int i = 0; i < dashImages.length; i++) {
                if (dashImages[i] != null && dashAlpha[i] > 0) {
                    dashAlpha[i] -= alphaDiff;
                    dashImages[i] = HelpMethods.makeMoreTransparent(dashImages[i], dashAlpha[i]);
                }
            }
        }

        if (dashAlpha[dashImagesSize - 1] < alphaDiff) {
            entity.removeDash();
        }

    }

    public void draw(Graphics2D g2) {
        for (int i = 0; i < dashImages.length; i++) {
            if (dashImages[i] != null) {
                int screenX = HelpMethods.getScreenX(dashImageX[i], entity.getPlaying().getPlayer());
                int screenY = HelpMethods.getScreenY(dashImageY[i], entity.getPlaying().getPlayer());
                g2.drawImage(dashImages[i], screenX, screenY, null);
            }
        }
    }
}
