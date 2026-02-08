package flipracer;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;

public class ImageCache {
	private static final Map<String, Image> imageCache = new HashMap<>();

    public static Image getImage(String path) {
        if (!imageCache.containsKey(path)) {
            Image image = new Image(path);
            imageCache.put(path, image);
        }
        return imageCache.get(path);
    }
}
