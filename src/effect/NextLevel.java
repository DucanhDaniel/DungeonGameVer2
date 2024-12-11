package effect;

import gamestates.Playing;
import inputs.KeyboardInputs;
import utils.HelpMethods;
import utils.ImageLoader;
import utils.ImageManager;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.Screen.*;

public class NextLevel extends EffectMethod{

    int currentStage = 0;
    Playing playing;
    public NextLevel(Playing playing, int worldX, int worldY) {
        super("PortalNextLevel", 32, null, playing.getPlayer(), worldX, worldY,
                112 * SCALE, 80 * SCALE, 0);

        this.playing = playing;
        setCenter();
    }

    public void setCenter() {
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
        BufferedImage image;
        if (currentStage <= 2) {
            if (currentStage == 0) {
                totalAnimationFrames = 13;
                if (frameCounter % frameDuration == 0) {
                    numAnimationFrames = (numAnimationFrames + 1) % totalAnimationFrames;
                }
                if (frameCounter == frameDuration * totalAnimationFrames) {
                    frameCounter = 0;
                    currentStage++;
                    numAnimationFrames = 0;
                }
            } else {
                totalAnimationFrames = 19;
                if (frameCounter % frameDuration == 0) {
                    numAnimationFrames = (numAnimationFrames + 1) % totalAnimationFrames;
                }
            }
            ImageLoader.initialize();
            ImageManager imageManager = ImageLoader.imageManager;
            if (currentStage == 0)
                image = imageManager.getEffectImage(name, numAnimationFrames, width, height);
            else image = imageManager.getEffectImage(name, numAnimationFrames + 13, width, height);
        } else {
            totalAnimationFrames = 32;
            if (frameCounter % frameDuration == 0) {
                numAnimationFrames = (numAnimationFrames + 1) % totalAnimationFrames;
            }
            if (frameCounter == frameDuration * (totalAnimationFrames - 1)) {
                removeEffect(0);
            }
            image = imageManager.getEffectImage(name, 31 - numAnimationFrames, width, height);
        }
//        System.out.println(currentStage + " " + worldX + " " + worldY);
        image = HelpMethods.scaleImage(image, scaleFactor);
        return image;
    }

    public boolean isTransition = false, isBgPlayed = false;
    public void update() {
        KeyboardInputs kb = playing.getGame().getKeyboardInputs();
//        System.out.println(currentStage);
        if (currentStage <= 1 && !isBgPlayed) {
            isBgPlayed = true;
            playing.soundtrack.playMusic(9);
        }
        if (isIntersect()) {
            if (KeyboardInputs.isPressedValid("enter", kb.enterPressed) && currentStage == 1 && !isTransition) {
                currentStage++;
                isTransition = true;
                playing.soundtrack.playSE(22);
            }
        }
    }
    public void changeLevel() {
        switch (playing.currentLevel) {
            case "level1":
                playing.currentLevel = "level2";
                break;
            case "level2":
                playing.currentLevel = "level3";
                break;
            case "level3":
                playing.currentLevel = "level4";
                break;
        }
        playing.getSaveLoadSystem().loadNewGame(playing.currentLevel);
        worldX = player.getWorldX();
        worldY = player.getWorldY();
        playing.setLevelTheme();
        setCenter();
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
            else if (frameCnt2 < 70) {
                g2.setColor(new Color(255, 255, 255));
                g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                if (frameCnt2 == 69) changeLevel();


            } else if (frameCnt2 < 130) {
                playing.cameraShake.startShake();
                unFillScreen(60, g2);
                if (frameCnt2 == 129) {
                    frameCounter =  -1;
                    numAnimationFrames = -1;
                    currentStage++;
                    playing.soundtrack.playSE(20);
                }
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

    int cnt4 = 0;
    public void unFillScreen(int duration, Graphics2D g2) {
        cnt4++;
        Color c = new Color(255, 255, 255, 255 - cnt4 * (255 / duration));
        g2.setColor(c);
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        if (cnt4 >= duration) cnt4 = 0;
    }

    @Override
    public void removeEffect(int index) {
        playing.nextLevel = null;
    }
}
