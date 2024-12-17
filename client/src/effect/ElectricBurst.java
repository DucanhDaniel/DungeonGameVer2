package effect;

import entities.monsters.Monster;
import entities.monsters.bosses.SkeletonReaper;

import java.awt.*;

import static utils.Constants.Screen.SCALE;

public class ElectricBurst extends EffectMethod{
    SkeletonReaper entity;
    public ElectricBurst(Monster entity, int worldX, int worldY, int index) {
        super("ElectricBurst", 27, entity,
                entity.getPlaying().getPlayer(),
                worldX,
                worldY, 72 * SCALE, 72 * SCALE, index);
        this.entity = (SkeletonReaper) entity;
        effectRect = new Rectangle(0, 0, 72 * SCALE * 3 / 2, 72 *  SCALE * 3 / 2);

        // Make the center of effectRect has position (worldX, worldY)
        this.worldX -= effectRect.x + effectRect.width / 2;
        this.worldY -= effectRect.y + effectRect.height / 2;
        effectRect.x += this.worldX;
        effectRect.y += this.worldY;

        frameDuration = 4;
    }

    public void draw(Graphics2D g2) {
        super.draw(g2, 1.5f);
    }

    public void update() {
        frameCounter++;
        if (frameCounter >= frameDuration * totalAnimationFrames) {
            removeEffect(index);
            return;
        }
        if (frameCounter == 11 * frameDuration)
            entity.getPlaying().soundtrack.playSE(9);

        if (frameCounter % frameDuration == 0 &&
            frameCounter / frameDuration >= 14 &&
            frameCounter / frameDuration <= 21) {
            if (isIntersect())
                player.getHurt(1);
        }

    }

    @Override
    public void removeEffect(int index) {
        entity.removeElectricBurst(index);
    }
}
