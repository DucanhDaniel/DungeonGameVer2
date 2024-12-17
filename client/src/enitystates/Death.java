package enitystates;

import entities.monsters.PlantMelee;
import entities.Sprite;
import utils.ImageLoader;
import entities.Player;

import java.awt.image.BufferedImage;

public class Death extends EntityStateMethods {

    public Death(Sprite entity, int totalAnimationFrames, int frameDuration, String state) {
        super(entity, totalAnimationFrames, frameDuration);
        this.state = state;
    }

    public Death(Sprite entity, int totalAnimationFrames, int frameDuration) {
        super(entity, totalAnimationFrames, frameDuration);
        state = "DEATH";
    }

    public Death(Sprite entity) {
        super(entity);
        state = "DEATH";
    }

    @Override
    public void update(Sprite entity) {
    }


    public int frameCounter = 0;
    public int animationIndex = 0;

    @Override
    public BufferedImage getImage() {
        ImageLoader.initialize();
        imageManager = ImageLoader.imageManager;
        frameCounter++;
        if (frameCounter >= frameDuration) {
            animationIndex++;
            if (animationIndex + 1 > totalAnimationFrames)
                return null;
            frameCounter = 0;
        }

        if (entity instanceof PlantMelee) {
            return imageManager.getMonsterImage(entity.name, state, "ALL", animationIndex, entity.width, entity.height);
        }


        if (entity instanceof Player player) {
            return imageManager.getPlayerImage(state, player.currentWeapon, player.direction, animationIndex, entity.width, entity.height);
        }
        else {
            switch (entity.direction) {
                case "up", "left_up", "left", "left_down":
                    return imageManager.getMonsterImage(entity.name, state, "left", animationIndex, entity.width, entity.height);
                case "down", "right_down", "right", "right_up":
                    return imageManager.getMonsterImage(entity.name, state, "right", animationIndex, entity.width, entity.height);
            }
        }

        return null;
    }
}
