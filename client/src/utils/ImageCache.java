package utils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ImageCache {
    private final int maxSize; // Kích thước tối đa của cache
    private final Map<String, BufferedImage> cache;

    public ImageCache(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, BufferedImage> eldest) {
                return size() > maxSize; // Xóa mục cũ nhất khi vượt quá kích thước
            }
        };
    }

    public BufferedImage getImage(String path) {
        if (cache.containsKey(path)) {
            return cache.get(path); // Lấy ảnh từ cache nếu có
        }
        // Load ảnh từ file nếu chưa có trong cache
        BufferedImage image = loadImageFromFile(path);
        image = HelpMethods.scaleImage(image, Constants.Screen.SCALE);
        cache.put(path, image);
        return image;
    }

    private BufferedImage loadImageFromFile(String path) {
        // Logic load ảnh từ file
        try {
//            return javax.imageio.ImageIO.read(new java.io.File(path));
            return ImageIO.read(Objects.requireNonNull(
                    ImageManager.class.getResource(path)
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void clear() {
        cache.clear();
    }
}
