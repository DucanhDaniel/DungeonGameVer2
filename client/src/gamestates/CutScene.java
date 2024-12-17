package gamestates;

import effect.ElectricBurst;
import enitystates.EntityState;
import entities.monsters.bosses.SkeletonReaper;
import inputs.KeyboardInputs;
import main.Game;
import entities.Player;

import java.awt.*;

import static inputs.KeyboardInputs.isPressedValid;
import static utils.Constants.Screen.*;

public class CutScene implements Statemethods {
    Playing playing;
    Game game;
    Player player;
    public SkeletonReaper skeletonReaper;

    String[][] dialogues;

    public CutScene(Game game) {
        this.game = game;
        playing = game.getPlaying();
    }

    public void initialize() {
        playing.currentLevel = "level4";

        playing.loadMap();
        player = playing.getPlayer();

        player.worldX = TILE_SIZE * 23;
        player.worldY = 10 * TILE_SIZE;
        skeletonReaper = new SkeletonReaper(playing,19 * TILE_SIZE, 10 * TILE_SIZE);
        playing.entityList.clear();
        playing.entityList.add(player);
        playing.entityList.add(skeletonReaper);

        dialogues = new String[2][];

        dialogues[0] = new String[]{
                "Finally! My power is back!!",
                "That was a long journey... At least it makes \nme feel less boring after being the lord of \nthis dungeon.",
                "But that strange magic, which extracts \nmy soul and puts it in a random human... \nI need to do some research about it."
        };

        dialogues[1] = new String[]{
                "Wha...",
                "...",
                "Hahaha, this kind of time magic is really \nINTERESTING!!",
                "Show me what you can do, challenger!!"
        };

        ebs = new ElectricBurst[4];

        frameCnt2 = 0;
        currentStage = -2;
        dialogueCounter = 0;
        frameCnt = 0;
        transparentCounter = 1;
        changeScene = false;
    }

    public int currentStage = -2;
    public int dialogueCounter = 0;

    public ElectricBurst[] ebs;
    int frameCnt = 0, transparentCounter = 1;
    boolean changeScene = false;
    @Override
    public void update() {
        if (player == null) initialize();

        KeyboardInputs kb = playing.getGame().getKeyboardInputs();

        if (currentStage == -2) {
            player.worldX = TILE_SIZE * 23 + TILE_SIZE / 2;
            player.worldY = 10 * TILE_SIZE;
            skeletonReaper.currentState = EntityState.IDLE;
            frameCnt++;
            if (frameCnt >= 30) {
                currentStage++;
                frameCnt = 0;
            }
        }
        else if (currentStage == -1) {
            player.worldX = TILE_SIZE * 23 + TILE_SIZE / 2;
            player.worldY = 10 * TILE_SIZE;
            skeletonReaper.currentState = EntityState.IDLE;
            transparentCounter++;
            if (transparentCounter > 50) {
                currentStage++;
            }
        }
        else if (currentStage == 0) {
            player.worldX = TILE_SIZE * 23 + TILE_SIZE / 2;
            player.worldY = 10 * TILE_SIZE;
            skeletonReaper.currentState = EntityState.IDLE;
            if (isPressedValid("enter", kb.enterPressed)) {
                dialogueCounter++;
                if (dialogueCounter >= dialogues[0].length) {
                    dialogueCounter = 0;
                    currentStage++;
                }
            }
        }
        else if (currentStage == 1) {
            skeletonReaper.currentState = EntityState.IDLE;
            if (player.worldY < 33 * TILE_SIZE)
                player.worldY += player.speed * 2;
            else currentStage++;
        }
        else if (currentStage == 2) {
            skeletonReaper.currentState = EntityState.IDLE;
            player.worldY = 33 * TILE_SIZE;
            currentStage++;
        } else if (currentStage == 3) {
            skeletonReaper.currentState = EntityState.IDLE;
            player.solidArea.x += player.worldX;
            player.solidArea.y += player.worldY;
            skeletonReaper.attackBox.x += skeletonReaper.worldX;
            skeletonReaper.attackBox.y += skeletonReaper.worldY;
            if (player.solidArea.intersects(skeletonReaper.attackBox)) {
                currentStage++;
            }
            player.solidArea.x = player.solidAreaDefaultX;
            player.solidArea.y = player.solidAreaDefaultY;
            skeletonReaper.attackBox.x -= skeletonReaper.worldX;
            skeletonReaper.attackBox.y -= skeletonReaper.worldY;
        }
        else if (currentStage == 4) {
            skeletonReaper.currentState = EntityState.IDLE;
            player.currentState = EntityState.IDLE;
            if (isPressedValid("enter", kb.enterPressed)) {
                dialogueCounter++;
                if (dialogueCounter >= dialogues[1].length) {
                    dialogueCounter = 0;
                    currentStage++;
                }
            }
        } else if (currentStage == 5) {
            player.currentWeapon = "SPEAR";
            player.currentState = EntityState.IDLE;
            skeletonReaper.currentState = EntityState.ATTACK;
            skeletonReaper.isDialogueDraw = true;
            skeletonReaper.currentHealth = skeletonReaper.maxHealth / 2 - 10;
            skeletonReaper.attack = skeletonReaper.castAttack;
            ebs[0] = new ElectricBurst(skeletonReaper, skeletonReaper.getWorldX() + TILE_SIZE * 2,
                    skeletonReaper.getWorldY() + TILE_SIZE * 2, 0);
            ebs[1] = new ElectricBurst(skeletonReaper, skeletonReaper.getWorldX() - TILE_SIZE * 2,
                    skeletonReaper.getWorldY() + TILE_SIZE * 2, 1);
            ebs[2] = new ElectricBurst(skeletonReaper, skeletonReaper.getWorldX() + TILE_SIZE * 2,
                    skeletonReaper.getWorldY() - TILE_SIZE * 2, 2);
            ebs[3] = new ElectricBurst(skeletonReaper, skeletonReaper.getWorldX() - TILE_SIZE * 2,
                    skeletonReaper.getWorldY() - TILE_SIZE * 2, 3);
            currentStage++;
        } else if (currentStage == 6) {
            playing.cameraShake.startShake();
            playing.cameraShake.update();
            frameCnt++;
            if (frameCnt == 60) {
                changeScene = true;
            }
        }

        if (skeletonReaper.currentState == EntityState.ATTACK) {
            for (ElectricBurst eb : ebs) if (eb != null){
                eb.update();
            }
        }
        skeletonReaper.update();
        player.update();
    }

    int frameCnt2 = 0;
    @Override
    public void draw(Graphics2D g2) {
        if (player == null)
            initialize();
        Playing.currentMap.render(g2, player);

        switch (currentStage) {
            case -2:
                g2.setColor(new Color(255, 255, 255));
                g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                break;
            case -1:
                skeletonReaper.draw(g2);
                g2.setColor(new Color(255, 255, 255, 250 - transparentCounter * (250 / 50)));
                g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                break;
            case 0:
                skeletonReaper.draw(g2);
                game.getUI().drawDialogueScreen(dialogues[0][dialogueCounter], g2);
                break;
            case 1:
                skeletonReaper.draw(g2);
                break;
            case 2:
                player.draw(g2);
                break;
            case 3, 5:
                player.draw(g2);
                skeletonReaper.draw(g2);
                break;
            case 4:
                player.draw(g2);
                skeletonReaper.draw(g2);
                game.getUI().drawDialogueScreen(dialogues[1][dialogueCounter], g2);
                break;
            case 6:
                player.getPlaying().cameraShake.startShake();
                player.draw(g2);
                skeletonReaper.draw(g2);
                for (ElectricBurst eb: ebs) eb.draw(g2);
                break;

        }
        if (changeScene) {
            skeletonReaper.rectangleHeight = SCREEN_HEIGHT / 2 + TILE_SIZE;
            frameCnt2++;
            if (frameCnt2 <= 60) {
                skeletonReaper.fillScreen(60, g2);
            }
            else {
                if (frameCnt2 <= 180)
                    skeletonReaper.rectangleMoveIn(120, g2);
                else {
                    g2.setColor(Color.BLACK);
                    g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                    if (frameCnt2 >= 240) {
                        Gamestate.state = Gamestate.MENU;
                        playing.getSaveLoadSystem().loadNewGame("level1");
                        playing.nextLevel = null;
                        player = null;
                    }
                }
            }
        }

    }
}
