package utils;

import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;

import static utils.Constants.Player.*;
import static utils.Constants.Screen.*;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import gamestates.Playing;
import entities.*;


public class HelpMethods {
    public static int getXForCenterText(String text, Graphics2D g2) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return SCREEN_WIDTH/2 - length/2;
    }
    public static int getYForCenterText(String text, Graphics2D g2) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getHeight();
        return SCREEN_HEIGHT/2 - length/2;
    }

    public static int getTextHeight(String text, Graphics2D g2) {
        return (int)g2.getFontMetrics().getStringBounds(text, g2).getHeight();
    }
    public static int getTextWidth(String text, Graphics2D g2) {
        return (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
    }

    public static Font loadFont(String fontName) {
        Font font = null;
        try {
            InputStream is = HelpMethods.class.getClassLoader().getResourceAsStream("font/" + fontName + ".ttf");
            if (is == null) {
                System.err.println("File not found: " + fontName + ".ttf");
                return null;
            }
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            System.err.println("Font format error: " + fontName + ".ttf");
        } catch (IOException e) {
            System.out.println("Failed to load font: " + fontName + ".ttf");
        }
        return font;
    }

    public static BufferedImage setUp(String imagePath, int width, int height) {
        UtilityTool utilityTool = new UtilityTool();
        BufferedImage scaledImage = null;
        try {
            scaledImage = ImageIO.read(Objects.requireNonNull(HelpMethods.class.getClassLoader().getResourceAsStream(imagePath + ".png")));
            scaledImage = scaleImage(scaledImage, width, height);

        } catch (IOException e) {
            System.out.println("Failed to load/scale image");
        }
        return scaledImage;
    }

    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(original, 0, 0, width, height, null);
        g2d.dispose();

        return resizedImage;
    }
    public static BufferedImage scaleImage(BufferedImage inputImage, double scaleFactor) {
        int width = (int) (inputImage.getWidth() * scaleFactor);
        int height = (int) (inputImage.getHeight() * scaleFactor);

        BufferedImage scaledImage = new BufferedImage(width, height, inputImage.getType());
        Graphics2D g2d = scaledImage.createGraphics();
//        AffineTransform at = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
//        g.drawRenderedImage(inputImage, at);
//        g.dispose();

        g2d.drawImage(inputImage, 0, 0, width, height, null);
        g2d.dispose();

        return scaledImage;
    }
    public static BufferedImage getBarImage(BufferedImage inputBar, double percentage) {
        int width = (int) (inputBar.getWidth() * percentage);
        int height = inputBar.getHeight();

        BufferedImage scaledImage = new BufferedImage(width, height, inputBar.getType());
        Graphics2D g = scaledImage.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(percentage, 1.0);
        g.drawRenderedImage(inputBar, at);
        g.dispose();

        return scaledImage;
    }
    
    
    public static boolean canSeeEntity(Playing playing, Sprite sprite1, Sprite entity2) {
        int x2 = entity2.getWorldX() / TILE_SIZE;
        int y2 = entity2.getWorldY() / TILE_SIZE;
        int x1 = sprite1.getWorldX() / TILE_SIZE, y1 = sprite1.getWorldY() / TILE_SIZE;

        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);

        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;

        int p;
        
        if (dx > dy) {
            p = dy * 2 - dx;
            while (x1 != x2) {
                if (playing.getTileManager().isWall(y1, x1)) return false;
                x1 += sx;
                if (p >= 0) {
                    y1 += sy;
                    p -= 2 * dx;
                }
                p += 2 * dy;
            }
        } else {
            p = dx * 2 - dy;
            while (y1!= y2) {
                if (playing.getTileManager().isWall(y1, x1)) return false;
                y1 += sy;
                if (p >= 0) {
                    x1 += sx;
                    p -= 2 * dy;
                }
                p += 2 * dx;
            }
        }
        return !playing.getTileManager().isWall(y2, x2);
    }

    public static BufferedImage makeWhiteExceptTransparent(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage whiteImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xFF;

                if (alpha == 0) {
                    // Pixel is transparent, keep the original color
                    whiteImage.setRGB(x, y, rgb);
                } else {
                    // Pixel is not transparent, make it white
                    whiteImage.setRGB(x, y, 0xFFFFFFFF);
                }
            }
        }

        return whiteImage;
    }
    public static BufferedImage makeMoreTransparent(BufferedImage image, int alpha) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage transparentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = transparentImage.createGraphics();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f));
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return transparentImage;
    }

    public static int getScreenX(int worldX, Player player) {
        return worldX - player.worldX + PLAYER_SCREEN_X;
    }
    public static int getScreenY(int worldY, Player player) {
        return worldY - player.worldY + PLAYER_SCREEN_Y;
    }
}
