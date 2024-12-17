package entities.monsters.bosses;

import enitystates.EntityState;
import entities.monsters.Monster;
import gamestates.Playing;
import utils.HelpMethods;

import java.awt.*;
import java.awt.image.BufferedImage;

import static enitystates.EntityState.DEATH;
import static utils.Constants.Screen.*;
import static utils.Constants.Screen.SCREEN_WIDTH;

public abstract class Boss extends Monster {
    public Boss(String name, Playing playing, int width, int height) {
        super(name, playing, width, height);
    }

    public int bossThemeId;
    public boolean isSoundtrackPlayed = false;
    public void playBossTheme() {
        if (canSeePlayer() && currentState != DEATH && currentHealth != 0 && !isSoundtrackPlayed) {
            playing.soundtrack.playMusic(bossThemeId);
            isSoundtrackPlayed = true;
        } else if ((!canSeePlayer() || currentHealth == 0) && isSoundtrackPlayed) {
            isSoundtrackPlayed = false;
            playing.setLevelTheme();
        }
    }
    @Override
    public void update() {
        if (!isBossIntroDrew) currentState = EntityState.IDLE;
        super.update();
    }

    // Boss intro attributes
    public boolean isBossIntroDrew = true;
    public boolean isFirstTime = true;
    int frameCnt = 0;
    float textSize = 100f;
    public int rectangleHeight = 150;
    int bossNameX = 0, bossNameY = 0, imageX = 0, imageY = 0;
    String bossName;
    BufferedImage bossImage; // 1296 x 720

    public abstract void drawBossIntro(Graphics2D g2);

    public void initializeBossIntro(Graphics2D g2, String bossName, BufferedImage bossImage) {
        g2.setFont(playing.getGame().getUI().maruMonica);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, textSize));
        this.bossName = bossName;
        this.bossImage = bossImage;
        if (bossNameX == 0) bossNameX = TILE_SIZE;
        if (bossNameY == 0) bossNameY = HelpMethods.getYForCenterText(bossName, g2) + 3 * TILE_SIZE / 2;
        if (imageX == 0) imageX = SCREEN_WIDTH - bossImage.getWidth() + TILE_SIZE * 9;
        if (imageY == 0) imageY = SCREEN_HEIGHT / 2 - bossImage.getHeight() / 2 - TILE_SIZE  * 2;
    }

    Rectangle rect = new Rectangle(SCREEN_WIDTH / 2 - TILE_SIZE * 3, 0,
            TILE_SIZE * 6, TILE_SIZE / 2);
    public void drawBossHealthBar(Graphics2D g2, int yDiff) {
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.BLACK);
        g2.drawRect(SCREEN_WIDTH / 2 - TILE_SIZE * 3, yDiff,
                TILE_SIZE * 6, TILE_SIZE / 2);
        g2.setColor(Color.BLACK);
        g2.fillRect(rect.x + 3, rect.y + 3 + yDiff, rect.width - 6, rect.height - 6);
        g2.setColor(Color.RED);
        if (currentHealth > 0)
            g2.fillRect(rect.x + 3, rect.y + 3 + yDiff, (int) ((rect.width - 6) * (1.0f * currentHealth / maxHealth)), rect.height - 6);
    }
    public void bossIntro(Graphics2D g2, String bossName, BufferedImage bossImage) {
        if (isFirstTime) {
            if (canSeePlayer()) {
                isBossIntroDrew = false;
                isFirstTime = false;
            }
        }

        if (isBossIntroDrew) {
            if (isOnTheScreen() && currentState != EntityState.DEATH  && !isFirstTime)
                drawBossHealthBar(g2, TILE_SIZE);
            return;
        }
        initializeBossIntro(g2, bossName, bossImage);
        frameCnt++;
        if (frameCnt <= 30) {
            fillScreen(30, g2);
        }
        else if (frameCnt <= 60) {
            rectangleMoveIn(30, g2);

        } else if (frameCnt <= 90) {
            drawBackground(g2, imageX, imageY, false);
            if (frameCnt <= 85)
                textAnimationMoveLeft( g2, 25, 0, bossNameY, imageX - TILE_SIZE, imageY,
                        SCREEN_WIDTH, SCREEN_WIDTH);
            else textAnimationMoveRight( g2, 5, 0, bossNameY, imageX - TILE_SIZE, imageY,
                     bossNameX, imageX);

            g2.setColor(Color.BLACK);
            g2.fillRect(0, SCREEN_HEIGHT - rectangleHeight,
                    SCREEN_WIDTH, rectangleHeight);

        } else if (frameCnt <= 170) {
            drawBackground(g2, imageX, imageY, true);

            g2.setColor(Color.BLACK);
            g2.fillRect(0, SCREEN_HEIGHT - rectangleHeight,
                    SCREEN_WIDTH, rectangleHeight);

        } else if (frameCnt <= 210){
            drawBackground(g2,imageX, imageY, false);

            if (frameCnt <= 180)
                textAnimationMoveLeft(g2, 10, 0, bossNameY,
                        imageX - TILE_SIZE, imageY, bossNameX, imageX);
            else
                textAnimationMoveRight( g2, 30, 0, bossNameY, imageX - TILE_SIZE, imageY, SCREEN_WIDTH, SCREEN_WIDTH);

            g2.setColor(Color.BLACK);
            g2.fillRect(0, SCREEN_HEIGHT - rectangleHeight,
                    SCREEN_WIDTH, rectangleHeight);
        } else if (frameCnt <= 240) {
            rectangleMoveOut(30, g2);
        } else if (frameCnt <= 270) {
            double easeProgress = easeInOut((frameCnt - 240) / 30f);
            drawBossHealthBar(g2, (int) (easeProgress * TILE_SIZE));
        }
        else {
            isBossIntroDrew = true;
            frameCnt = 0;
        }
    }

    public void drawBackground(Graphics2D g2, int imageX, int imageY, boolean drawImage) {
        Color c = new Color(147, 114, 15, 180);
        g2.setColor(c);
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, SCREEN_WIDTH, rectangleHeight);

        if (drawImage) {
            g2.setFont(playing.getGame().getUI().maruMonica);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, textSize));

            g2.setColor(Color.GRAY);
            g2.drawString(bossName, bossNameX + 5, bossNameY + 3);
            g2.setColor(Color.WHITE);
            g2.drawString(bossName, bossNameX, bossNameY);

            g2.drawImage(bossImage, imageX, imageY, null);
        }
    }

    int cnt3 = 0;
    public void fillScreen(int duration, Graphics2D g2) {
        cnt3++;
        Color c = new Color(147, 114, 15, cnt3 * (180 / duration));
        g2.setColor(c);
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        if (cnt3 >= duration) cnt3 = 0;
    }

    int cnt4 = 0;
    public void unFillScreen(int duration, Graphics2D g2) {
        cnt4++;
        Color c = new Color(147, 114, 15, 180 - cnt4 * (180 / duration));
        g2.setColor(c);
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        if (cnt4 >= duration) cnt4 = 0;
    }

    public int cnt5 = 0;
    public void rectangleMoveIn(int duration, Graphics2D g2) {
        cnt5++;
        Color c = new Color(147, 114, 15, 180);
        g2.setColor(c);
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, SCREEN_WIDTH, cnt5 * (rectangleHeight / duration));

        float t = (float) (1.0 * cnt5 / duration);
        double easedProgress = easeInOut(t);

        g2.fillRect(0, (int) (SCREEN_HEIGHT - easedProgress * rectangleHeight),
                SCREEN_WIDTH, (int) (easedProgress * rectangleHeight));
        if (cnt5 >= duration) cnt5 = 0;
    }

    public int cnt6 = 0;
    public void rectangleMoveOut(int duration, Graphics2D g2) {
        cnt6++;
        unFillScreen(duration, g2);

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, SCREEN_WIDTH, duration * 6 - cnt6 * 6);

        float t = (float) (1.0 * cnt6 / duration);
        double easedProgress = easeInOut(t);

        g2.fillRect(0, SCREEN_HEIGHT - rectangleHeight + (int) (easedProgress * rectangleHeight),
                SCREEN_WIDTH, rectangleHeight - (int) (easedProgress * rectangleHeight));

        if (cnt6 >= duration) cnt6 = 0;
    }

    public double easeInOut(float t) {
        if (t < 0.5) {
            return 2 * Math.pow(t, 2);
        } else {
            return 1 - 2 * Math.pow(1 - t, 2);
        }
    }

    int cnt1 = 0;
    public void textAnimationMoveLeft(Graphics2D g2, int duration, int x, int y, int imageX, int imageY,
                                      int xStartText, int xStartImage) {
        String text = bossName;
        cnt1++;
        int distance;

        float t = (float) cnt1 / duration; // Normalize time (0 to 1)
        double easedProgress = easeInOut(t); // Use the desired easing function
        int currentX = (int) (xStartText + easedProgress * (x - xStartText)); // Compute position

        g2.setFont(playing.getGame().getUI().maruMonica);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, textSize));
        g2.setColor(Color.GRAY);
        g2.drawString(text, currentX + 5, y + 3);
        g2.setColor(Color.WHITE);
        g2.drawString(text, currentX, y);

        distance = xStartImage - imageX;
        int diff = distance / duration;
        g2.drawImage(bossImage, xStartImage - cnt1 * diff, imageY, null);

        if (cnt1 >= duration) cnt1 = 0;
    }

    int cnt2 = 0;
    public void textAnimationMoveRight(Graphics2D g2, int duration, int x, int y, int imageX, int imageY,
                                       int xEndText, int xEndImage) {
        String text = bossName;
        cnt2++;
        int distance = xEndText - x;
        int diff = distance / duration;
        g2.setFont(playing.getGame().getUI().maruMonica);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, textSize));
        g2.setColor(Color.GRAY);
        g2.drawString(text, x + diff * cnt2 + 5, y + 3);
        g2.setColor(Color.WHITE);
        g2.drawString(text, x + diff * cnt2, y);

        distance = xEndImage - imageX;
        diff = distance / duration;
        g2.drawImage(bossImage, imageX + cnt2 * diff, imageY, null);

        if (cnt2 >= duration) cnt2 = 0;
    }
}
