package main;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import gamestates.*;
import gamestates.Menu;
import utils.HelpMethods;
import utils.ImageLoader;
import utils.ImageManager;
import entities.Player;

import static utils.Constants.Screen.*;

public class UI {
    Playing playing;
    Menu menu;
    Selection selection;
    Pause pause;
    Setting setting;
    GameOver gameOver;
    public Font maruMonica, purisaBold;

    public UI(Game game) {
        this.playing = game.getPlaying();
        this.menu = game.getMenu();
        this.selection = game.getSelection();
        this.pause = game.getPause();
        this.setting = game.getSetting();
        this.gameOver = game.getGameOver();

        try {
            InputStream is = getClass().getResourceAsStream("/font/MaruMonica.ttf");
            assert is != null;
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);

            is = getClass().getResourceAsStream("/font/PurisaBold.ttf");
            assert is != null;
            purisaBold = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    int frameCounter = 0;
    String previousText = null;
    int lineCounter = 0;

    public void drawDialogueScreen(String currentDialogue, Graphics2D g2) {
        // Window
        int x = TILE_SIZE * 2, y = TILE_SIZE / 2;
        int width = SCREEN_WIDTH - TILE_SIZE * 4, height = TILE_SIZE * 4;

        drawSubWindow(x, y, width, height, g2);

        x += TILE_SIZE;
        y += TILE_SIZE;
        g2.setFont(maruMonica);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        if (currentDialogue == null) {
            playing.npcTalking = null;
            return;
        }
        if (!currentDialogue.equals(previousText)) {
            lineCounter = 0;
            previousText = currentDialogue;
            frameCounter = 0;
        }

        frameCounter++;
        String[] lines = currentDialogue.split(("\n"));

        for (int i = 0; i < lineCounter; i++) {
            g2.drawString(lines[i], x, y + 40 * i);
        }

        for (int j = lines[lineCounter].length(); j >= 0; j--) {
            if (j * 1.75 < frameCounter) {
                g2.drawString(lines[lineCounter].substring(0, j), x, y + 40 * lineCounter);
                if (j == lines[lineCounter].length() && lineCounter != lines.length - 1) {
                    lineCounter++;
                    frameCounter = 0;
                }
                break;
            }
        }


    }

    public void drawSubWindow(int x, int y, int width, int height, Graphics2D g2) {
        Color c = new Color(0, 0, 0, 200);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public void drawPlayerUI(Graphics2D g2) {
        Player player = playing.getPlayer();
        // Draw player status GUI
        ImageLoader.initialize();
        ImageManager imageManager = ImageLoader.imageManager;
        BufferedImage ui_bar_decor = imageManager.getGuiImage("UI_BAR_DECORATION");
        ui_bar_decor = HelpMethods.scaleImage(ui_bar_decor, 0.25);
        g2.drawImage(ui_bar_decor, 0, 0, null);

        if (player.currentHealth > 0) {
            BufferedImage player_health_bar = imageManager.getGuiImage("HEALTH_BAR");
            player_health_bar = HelpMethods.scaleImage(player_health_bar, 0.25);
            player_health_bar = HelpMethods.getBarImage(player_health_bar, 1.0 * player.currentHealth / player.maxHealth);
            g2.drawImage(player_health_bar, 49, 10, null);
        }

        if (player.currentArmor > 0) {
            BufferedImage player_armor_bar = imageManager.getGuiImage("ARMOR_BAR");
            player_armor_bar = HelpMethods.scaleImage(player_armor_bar, 0.25);
            player_armor_bar = HelpMethods.getBarImage(player_armor_bar, 1.0 * player.currentArmor / player.maxArmor);
            g2.drawImage(player_armor_bar, 49, 43, null);
        }

        if (player.currentMana > 0) {
            BufferedImage player_mana_bar = imageManager.getGuiImage("MANA_BAR");
            player_mana_bar = HelpMethods.scaleImage(player_mana_bar, 0.25);
            player_mana_bar = HelpMethods.getBarImage(player_mana_bar, 1.0 * player.currentMana / player.maxMana);
            g2.drawImage(player_mana_bar, 49, 75, null);
        }

        Font pixelFont = HelpMethods.loadFont("PixelFont");
        String text = player.currentHealth + "/" + player.maxHealth;
        g2.setFont(pixelFont);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        g2.setColor(Color.WHITE);
        g2.drawString(text, 107, 27);

        text = player.currentArmor + "/" + player.maxArmor;
        g2.drawString(text, 107, 60);

        text = player.currentMana + "/" + player.maxMana;
        g2.drawString(text, 90, 93);
    }

    public void drawSettingScreen(Graphics2D g2) {
        switch (setting.currentPanel) {
            case Setting.mainPanel:
                drawSettingOptionsPanel(g2, setting.commandIndex);
                break;
            case Setting.controlPanel:
                drawKeyControlsPanel(g2);
                break;
            case Setting.volumeControlPanel:
                drawVolumeOptionsPanel(g2, setting.commandIndex);
                break;
            default:
        }
    }

    private void drawSettingOptionsPanel(Graphics2D g2, int currentCommand) {
        int x = TILE_SIZE * 4, y = 5 * TILE_SIZE / 2;
        int width = SCREEN_WIDTH - 2 * x, height = SCREEN_HEIGHT - 2 * y;

        drawSubWindow(x, y, width, height, g2);
        drawSettingOptions(g2, x, y, currentCommand);
    }

    private void drawSettingOptions(Graphics2D g2, int boxX, int boxY, int currentCommand) {
        int width = SCREEN_WIDTH - 2 * boxX, height = SCREEN_HEIGHT - 2 * boxY;
        String text = "SETTING";
        g2.setFont(maruMonica);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
        g2.setColor(Color.WHITE);
        int x = HelpMethods.getXForCenterText(text, g2), y = boxY + 3 * TILE_SIZE / 2;
        g2.drawString(text, x, y);

        String[] leftStr = {"Key controls", "Volume options", "Return to menu"};
        x = boxX + TILE_SIZE;
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
        for (int i = 0; i < leftStr.length; i++) {
            y += TILE_SIZE / 2 + HelpMethods.getTextHeight(leftStr[i], g2);
            g2.drawString(leftStr[i], x, y);
            if (i == currentCommand) {
                g2.drawString("<--", boxX + width - TILE_SIZE * 2, y);
            }
        }

    }

    public void drawPauseScreen(Graphics2D g2) {
        playing.draw(g2);
        switch (pause.currentPanel) {
            case Pause.mainPanel:
                drawPauseOptionsPanel(g2, pause.commandIndex);
                break;
            case Pause.controlPanel:
                drawKeyControlsPanel(g2);
                break;
            case Pause.savePanel:
                drawSavePanel(g2);
                break;
            case Pause.volumeControlPanel:
                drawVolumeOptionsPanel(g2, pause.commandIndex);
                break;
            default:
        }
    }

    private void drawSavePanel(Graphics2D g2) {
        int x = TILE_SIZE * 4, y = 9 * TILE_SIZE / 2;
        int width = SCREEN_WIDTH - 2 * x, height = SCREEN_HEIGHT - 2 * y;

        drawSubWindow(x, y, width, height, g2);

        String text = "Progress saved!";
        g2.setFont(maruMonica);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
        g2.drawString(text,
                HelpMethods.getXForCenterText(text, g2),
                HelpMethods.getYForCenterText(text, g2) + 3 * TILE_SIZE / 4);
    }

    private void drawPauseOptionsPanel(Graphics2D g2, int currentCommand) {
        int x = TILE_SIZE * 4, y = 5 * TILE_SIZE / 2;
        int width = SCREEN_WIDTH - 2 * x, height = SCREEN_HEIGHT - 2 * y;

        drawSubWindow(x, y, width, height, g2);
        drawPauseOptions(g2, x, y, currentCommand);
    }

    private void drawPauseOptions(Graphics2D g2, int boxX, int boxY, int currentCommand) {
        int width = SCREEN_WIDTH - 2 * boxX, height = SCREEN_HEIGHT - 2 * boxY;
        String text = "PAUSED";
        g2.setFont(maruMonica);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
        g2.setColor(Color.WHITE);
        int x = HelpMethods.getXForCenterText(text, g2), y = boxY + 3 * TILE_SIZE / 2;
        g2.drawString(text, x, y);

        String[] leftStr = {"Key controls", "Save game", "Volume options", "Return to menu"};
        x = boxX + TILE_SIZE;
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
        for (int i = 0; i < leftStr.length; i++) {
            y += TILE_SIZE / 2 + HelpMethods.getTextHeight(leftStr[i], g2);
            g2.drawString(leftStr[i], x, y);
            if (i == currentCommand) {
                g2.drawString("<--", boxX + width - TILE_SIZE * 2, y);
            }
        }

    }

    public void drawKeyControlsPanel(Graphics2D g2) {
        int x = TILE_SIZE * 3, y = 3 * TILE_SIZE / 2;
        int width = SCREEN_WIDTH - 2 * x, height = SCREEN_HEIGHT - 2 * y;
        drawSubWindow(x, y, width, height, g2);
        drawKeyControls(g2, x, y);
    }

    private void drawKeyControls(Graphics2D g2, int boxX, int boxY) {
        int boxWidth = SCREEN_WIDTH - 2 * boxX, boxHeight = SCREEN_HEIGHT - 2 * boxY;
        String text = "Controls";
        g2.setFont(maruMonica);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
        g2.setColor(Color.WHITE);

        int x = HelpMethods.getXForCenterText(text, g2), y = boxY + 3 * TILE_SIZE / 2;
        g2.drawString(text, x, y);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30f));

        String[] leftStr = {"W, A, S, D", "J", "K", "L", "Shift", "Enter", "P"};
        String[] rightStr = {"Moving", "Attack", "Dash", "Change weapon", "Run", "Show entity box/Interact", "Pause/Unpause"};

        for (String str : leftStr) {
            x = boxX + TILE_SIZE;
            y += TILE_SIZE / 4 + HelpMethods.getTextHeight(str, g2);
            g2.drawString(str, x, y);
        }

        y = boxY + 3 * TILE_SIZE / 2;
        for (String str : rightStr) {
            x = boxX + boxWidth - HelpMethods.getTextWidth(str, g2) - TILE_SIZE;
            y += TILE_SIZE / 4 + HelpMethods.getTextHeight(str, g2);
            g2.drawString(str, x, y);
        }
    }

    private void drawVolumeOptionsPanel(Graphics2D g2, int commandIndex) {
        int x = TILE_SIZE * 2, y = 6 * TILE_SIZE / 2;
        int width = SCREEN_WIDTH - 2 * x, height = SCREEN_HEIGHT - 2 * y;
        drawSubWindow(x, y, width, height, g2);

        drawVolumeOptions(g2, x, y, commandIndex);
    }

    private void drawVolumeOptions(Graphics2D g2, int boxX, int boxY, int commandIndex) {
        int boxWidth = SCREEN_WIDTH - 2 * boxX, boxHeight = SCREEN_HEIGHT - 2 * boxY;
        String text = "Volume options";
        g2.setFont(maruMonica);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
        g2.setColor(Color.WHITE);
        int x = HelpMethods.getXForCenterText(text, g2), y = boxY + 3 * TILE_SIZE / 2;
        g2.drawString(text, x, y);

        String[] leftText = {"Volume", "Sound effect", "Soundtrack"};
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30f));
        x = boxX + TILE_SIZE;
        y += TILE_SIZE / 2;
        int rectX = x + HelpMethods.getTextWidth(leftText[0], g2) + TILE_SIZE * 2, rectY;
        for (int i = 0; i < leftText.length; i++) {
            y += TILE_SIZE / 4 + HelpMethods.getTextHeight(leftText[i], g2);
            g2.drawString(leftText[i], x, y);
            if (i == commandIndex) {
                g2.drawString("<--", boxX + boxWidth - TILE_SIZE * 2, y);
            }
            switch (i) {
                case 0:
                    rectY = y - 4 * 4 + 2;
                    g2.setStroke(new BasicStroke(1));
                    g2.drawRect(rectX, rectY, 4 * TILE_SIZE, 4 * 3);
                    if (Gamestate.state == Gamestate.PAUSE)
                        g2.fillRect(rectX, rectY, 4 * TILE_SIZE * pause.currentVolume / pause.maxVolume, 4 * 3);
                    else g2.fillRect(rectX, rectY, 4 * TILE_SIZE * setting.currentVolume / setting.maxVolume, 4 * 3);
                    break;
                case 1:
                    rectY = y - 4 * 4 + 2;
                    g2.drawRect(rectX, rectY, 4 * 3, 4 * 3);
                    if (Gamestate.state == Gamestate.PAUSE) {
                        if (pause.isSoundEffectOn)
                            g2.fillRect(rectX, rectY, 4 * 3, 4 * 3);
                    } else {
                        if (setting.isSoundEffectOn)
                            g2.fillRect(rectX, rectY, 4 * 3, 4 * 3);
                    }
                    break;
                case 2:
                    rectY = y - 4 * 4 + 2;
                    g2.drawRect(rectX, rectY, 4 * 3, 4 * 3);
                    if (Gamestate.state == Gamestate.PAUSE) {
                        if (pause.isSoundtrackOn)
                            g2.fillRect(rectX, rectY, 4 * 3, 4 * 3);
                    } else {
                        if (setting.isSoundtrackOn)
                            g2.fillRect(rectX, rectY, 4 * 3, 4 * 3);
                    }
                    break;
            }
        }
    }

    public void drawGameOverScreen(Graphics2D g2) {
        playing.draw(g2);
        Color c = new Color(0, 0, 0, 200);
        g2.setColor(c);
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        g2.setFont(maruMonica);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
        g2.setColor(Color.WHITE);
        int x = HelpMethods.getXForCenterText("Game Over", g2), y = SCREEN_HEIGHT / 2 - 48;
        g2.drawString("Game Over", x, y);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30f));
        String[] str = {"Restart", "Menu"};
        for (int i = 0; i < str.length; i++) {
            x = HelpMethods.getXForCenterText(str[i], g2);
            y += TILE_SIZE / 2 + HelpMethods.getTextHeight(str[i], g2);
            if (i == gameOver.commandIndex) {
                g2.drawString("<--", SCREEN_WIDTH - 6 * TILE_SIZE, y);
            }
            g2.drawString(str[i], x, y);
        }
    }
}
