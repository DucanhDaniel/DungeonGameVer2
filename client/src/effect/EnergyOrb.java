package effect;

import entities.Player;
import entities.monsters.Monster;
import gamestates.Gamestate;
import gamestates.Playing;
import inputs.KeyboardInputs;
import utils.HelpMethods;
import utils.ImageLoader;
import utils.ImageManager;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.Screen.*;

public class EnergyOrb extends EffectMethod{
    Playing playing;
    int currentStage = 0;
    public EnergyOrb(Playing playing, int worldX, int worldY) {
        super("EnergyOrb", 22, null,
                playing.getPlayer(), worldX, worldY, 6 * TILE_SIZE, 6 * TILE_SIZE, 0);
        this.playing = playing;

        effectRect = new Rectangle(0, 0, width, height);

        // Make the center of effectRect has position (worldX, worldY)
        this.worldX -= effectRect.x + effectRect.width / 2;
        this.worldY -= effectRect.y + effectRect.height / 2;
        effectRect.x += this.worldX;
        effectRect.y += this.worldY;

    }

    @Override
    public BufferedImage getImage(float scaleFactor) {
        frameCounter++;
        if (currentStage == 0) {
            totalAnimationFrames = 13;
            if (frameCounter % frameDuration == 0) {
                numAnimationFrames = (numAnimationFrames + 1) % totalAnimationFrames;
            }
            if (frameCounter == frameDuration * totalAnimationFrames) {
                currentStage++;
            }
        } else {
            totalAnimationFrames = 9;
            if (frameCounter % frameDuration == 0) {
                numAnimationFrames = (numAnimationFrames + 1) % totalAnimationFrames;
            }
        }
        ImageLoader.initialize();
        ImageManager imageManager = ImageLoader.imageManager;
        BufferedImage image;
        if (currentStage == 0)
            image = imageManager.getEffectImage(name, numAnimationFrames, width, height);
        else image = imageManager.getEffectImage(name, numAnimationFrames + 13, width, height);

        image = HelpMethods.scaleImage(image, scaleFactor);
        return image;
    }

    public void update() {
        KeyboardInputs kb = playing.getGame().getKeyboardInputs();
        if (isIntersect()) {
            if (KeyboardInputs.isPressedValid("enter", kb.enterPressed) && currentStage == 1) {
                currentStage++;

//                ---- save Record ------
                playing.getGame().stopGame();

//                playing.getGame().api.saveRecord(playing.getPlayer());
            }
        }
    }

    int frameCnt2 = 0;
    public void draw(Graphics2D g2) {
        super.draw(g2, 1.0f);

        if (currentStage == 2)  {
            frameCnt2++;
            if (frameCnt2 < 60) {
                playing.cameraShake.startShake();
                fillScreen(60, g2);
            }
            else {
                g2.setColor(new Color(255, 255, 255));
                g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                removeEffect(0);
                Gamestate.state = Gamestate.CUTSCENE;
                playing.getGame().api.saveRecord(player);
            }
        }
    }

    int cnt3 = 0;
    public void fillScreen(int duration, Graphics2D g2) {
        cnt3++;
        Color c = new Color(255, 255, 255, cnt3 * (255 / duration));
        g2.setColor(c);
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        if (cnt3 >= duration) cnt3 = 0;
    }

    @Override
    public void removeEffect(int index) {
        playing.energyOrb = null;
    }
}
