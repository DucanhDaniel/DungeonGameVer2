package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class ImageManager {

    private static ImageManager instance;
    private ImageCache imageCache;

    public static ImageManager getInstance() {
        if (instance == null) {
            synchronized (ImageManager.class) { // Optional: Thread-safe initialization
                if (instance == null) {
                    instance = new ImageManager(); // Create the instance lazily
                }
            }
        }
        return instance;
    }


    private ImageManager() {
        imageCache = new ImageCache(50);
    }

    public BufferedImage getPlayerImage(String state, String weapon, String direction, int numAnimationFrame, int width, int height) {
        StringBuilder sb = new StringBuilder("/Player");

        if (state.equals("ATTACK")) sb.append("/Attack");
        else if (state.equals("RUN")) sb.append("/Run");
        else if (state.equals("WALK")) sb.append("/Walk");
        else if (state.equals("IDLE")) sb.append("/Idle");
        else sb.append("/Death");

        if (weapon.equals("NORMAL")) sb.append("/Normal");
        else if (weapon.equals("SPEAR")) sb.append("/Spear");
        else sb.append("/Gun");


        sb.append("/").append(direction).append("/1-sheet.png");
        String path = sb.toString();
//        System.out.println(path);
        BufferedImage image = imageCache.getImage(path);

        if (image == null) return null;

        return image.getSubimage(width * numAnimationFrame, 0, width, height);  // Trả về ảnh từ bộ nhớ
    }


    public BufferedImage getGuiImage(String name) {
        StringBuilder pathBuilder = new StringBuilder("/Gui/");
        pathBuilder.append(name.toLowerCase()).append(".png");

        String path = pathBuilder.toString();
//        System.out.println(path);

        BufferedImage image = imageCache.getImage(path);

        return image;
    }

    public BufferedImage getMonsterImage(String name, String state, String direction, int numAnimationFrame, int width, int height) {
        StringBuilder pathBuilder = new StringBuilder("/monster/");
        pathBuilder.append(name.toLowerCase())
                .append("/")
                .append(state.toLowerCase())
                .append("/")
                .append(direction.toLowerCase())
                .append("/1-sheet.png");

        String path = pathBuilder.toString();
//         System.out.println(path);

        BufferedImage image = imageCache.getImage(path);

        if (image == null) return null;

        return image.getSubimage(width * numAnimationFrame, 0, width, height);  // Trả về ảnh từ bộ nhớ
    }

    public BufferedImage getEffectImage(String name, int numAnimationFrame, int width, int height) {
        StringBuilder pathBuilder = new StringBuilder("/Effect/");
        pathBuilder.append(name)
                .append("/")
                .append("1-sheet.png");

        String path = pathBuilder.toString();
//        System.out.println(path);
        BufferedImage image = imageCache.getImage(path);
        if (image == null) return null;
        return image.getSubimage(width * numAnimationFrame, 0, width, height);
    }
    public BufferedImage getNPCImage(String name, String state, String direction, int numAnimationFrame, int width, int height) {
        StringBuilder path = new StringBuilder();
        path.append("/npc/");
        path.append(name);
        path.append("/");
        path.append(state);
        path.append("/");
        path.append(direction);
        path.append("/");
        path.append("1-sheet.png");
        BufferedImage image = imageCache.getImage(path.toString());
        return image.getSubimage(width * numAnimationFrame, 0, width, height);
    }

    public BufferedImage getProjectileImage(String name, String state, String direction, int numAnimationFrame, int totalAnimationFrame) {
        StringBuilder pathBuilder = new StringBuilder("/projectile/");
        pathBuilder.append(name.toLowerCase())
                .append("/")
                .append(state.toLowerCase())
                .append("/")
                .append(direction)
                .append(".png");

        String path = pathBuilder.toString();
        BufferedImage image = imageCache.getImage(path);
        if (image == null) return null;

        int width = image.getWidth() / totalAnimationFrame;
        int height = image.getHeight();

        return image.getSubimage(width * (numAnimationFrame - 1), 0, width, height);  // Trả về ảnh từ bộ nhớ
    }

    public BufferedImage getObjectImage(String key, int numAnimationFrame, int width, int height) {
        StringBuilder pathBuilder = new StringBuilder("/");
        pathBuilder.append(key).append(".png");
        String path = pathBuilder.toString();
//        System.out.println(path);
        return imageCache.getImage(pathBuilder.toString()).getSubimage(width * numAnimationFrame, 0, width, height);
    }

    public BufferedImage getObjectImage(String key, int numAnimationFrame, int totalAnimationFrame) {
        StringBuilder pathBuilder = new StringBuilder("/");
        pathBuilder.append(key).append(".png");
        BufferedImage image = imageCache.getImage(pathBuilder.toString());
        int width = image.getWidth() / totalAnimationFrame, height = image.getHeight();
        return image.getSubimage(width * numAnimationFrame, 0, width, height);
    }
}
