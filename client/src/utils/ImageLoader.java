package utils;

public class ImageLoader {
    public static ImageManager imageManager = null;
    public static void initialize() {
        if (imageManager == null)
            imageManager = ImageManager.getInstance();
    }
}
