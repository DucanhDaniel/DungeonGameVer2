package effect;

import entities.monsters.Monster;
import entities.monsters.bosses.BringerOfDeath;

import java.awt.*;

import static utils.Constants.Screen.TILE_SIZE;

public class DeathHand extends EffectMethod {
    Monster bringerOfDeath;
    public DeathHand(Monster bringerOfDeath, int worldX, int worldY, int index) {
        super("DeathHand",15,  bringerOfDeath, bringerOfDeath.getPlaying().getPlayer(), worldX, worldY,
                4 * TILE_SIZE, 4 * TILE_SIZE, index);

        frameDuration = 5;
        effectRect = new Rectangle(worldX + 5 * TILE_SIZE / 2, worldY + 5 * TILE_SIZE, 4 * TILE_SIZE, 4 * TILE_SIZE);
        this.bringerOfDeath = bringerOfDeath;
    }

    public void draw(Graphics2D g2) {
        super.draw(g2, 2);
    }
    public void update() {
        super.update(8, true);
        if (frameCounter == 2 * frameDuration) {
            entity.getPlaying().soundtrack.playSE(9);
        }
    }

    @Override
    public void removeEffect(int index) {
        bringerOfDeath.removeEffect(index);
    }
}
