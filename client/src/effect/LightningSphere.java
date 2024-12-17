package effect;

import entities.Player;
import entities.monsters.Monster;
import entities.monsters.bosses.SkeletonReaper;

import java.awt.*;

import static utils.Constants.Screen.SCALE;
import static utils.Constants.Screen.TILE_SIZE;

public class LightningSphere extends EffectMethod{
    SkeletonReaper skeletonReaper;
    public LightningSphere(Monster entity, int worldX, int worldY, int index) {
        super("LightningSphere", 22, entity,
                entity.getPlaying().getPlayer(), worldX, worldY, 8 * TILE_SIZE, 12 * TILE_SIZE, index);

        skeletonReaper = (SkeletonReaper) entity;

        effectRect = new Rectangle(TILE_SIZE * 2, 9 * TILE_SIZE, 2 * TILE_SIZE * 2, 3 * TILE_SIZE);
        // Make the center of effectRect has position (worldX, worldY)
        this.worldX -= effectRect.x + effectRect.width / 2;
        this.worldY -= effectRect.y + effectRect.height / 2;
        effectRect.x += this.worldX;
        effectRect.y += this.worldY;

        frameDuration = 4;
    }

    public void draw(Graphics2D g2) {
        super.draw(g2, 1f);
    }

    public void update() {
        frameCounter++;
        if (frameCounter >= frameDuration * totalAnimationFrames) {
            removeEffect(index);
            return;
        }
        if (frameCounter % frameDuration == 0 &&
                frameCounter / frameDuration >= 7 &&
                frameCounter / frameDuration <= 19) {
            if (isIntersect())
                player.getHurt(1);
        }

    }

    @Override
    public void removeEffect(int index) {
        skeletonReaper.removeLightningSphere(index);
    }
}
