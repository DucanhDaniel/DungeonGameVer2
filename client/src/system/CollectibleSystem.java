package system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import gamestates.Playing;
import objects.Collectible;
import objects.OBJ_Heart;
import objects.OBJ_Mana;

import java.awt.*;
import java.util.ArrayList;
import static utils.Constants.Screen.*;


public class CollectibleSystem {

    @JsonIgnore
    public Playing playing;

    public ArrayList<Collectible> collectibleList;

    public CollectibleSystem() {
        collectibleList = new ArrayList<>();
    }

    public void update() {
        for (Collectible collectible : collectibleList) {
            if (playing.getGame().getCollisionChecker().checkPlayer(collectible.hitbox, collectible.position)) {
                collectible.interact(playing.getPlayer());
                collectible.item.isCollected = true;
            } else {
                collectible.animation.updateAnimation();
                String key = collectible.name;
                int numAnimationFrame = collectible.animation.numAnimationFrame;
                int width = collectible.render.width;
                int height = collectible.render.height;
                collectible.render.image = playing.getImageManager().getObjectImage(key, numAnimationFrame - 1, width, height);
            }
        }
        collectibleList.removeIf(collectible -> collectible.item.isCollected);
    }

    public void draw(Graphics2D g2) {
        if (collectibleList == null) return;
        for (Collectible collectible : collectibleList) {
            playing.getRenderSystem().draw(g2, collectible.position, collectible.render, collectible.hitbox);
        }
    }
}
