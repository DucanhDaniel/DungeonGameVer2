package effect;

import entities.Player;
import entities.monsters.Monster;
import entities.monsters.bosses.SkeletonReaper;

import java.awt.*;

import static utils.Constants.Screen.SCALE;
import static utils.Constants.Screen.TILE_SIZE;

public class Brushed extends EffectMethod{
    SkeletonReaper skeletonReaper;
    public Brushed(SkeletonReaper entity, int worldX, int worldY) {
        super("Brushed", 3, entity, entity.getPlaying().getPlayer()
                , worldX, worldY, 106 * SCALE, 32 * SCALE, 0);
        effectRect.width = 4 * TILE_SIZE;
        effectRect.height = 2 * TILE_SIZE;
        this.skeletonReaper = entity;
    }
    public void draw(Graphics2D g2) {
        super.draw(g2, 1);
    }

    public void update() {
        frameCounter++;
        this.worldX = skeletonReaper.worldX + 58 * SCALE;
        this.worldY = skeletonReaper.worldY + 62 * SCALE;
        effectRect.x = worldX;
        effectRect.y = worldY;
    }

    @Override
    public void removeEffect(int index) {

    }
}
