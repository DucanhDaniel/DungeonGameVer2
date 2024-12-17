package gamestates;

import data.SaveLoadSystem;
import effect.CameraShake;
import effect.EnergyOrb;
import effect.NextLevel;
import enitystates.EntityState;
import entities.*;
import entities.monsters.*;
import entities.monsters.bosses.*;
import entities.projectile.ProjectileManager;
import inputs.KeyboardInputs;
import main.Game;
import java.awt.*;
import entities.npc.Npc;

import java.util.*;

import map.GameMap;
import map.MapManager;
import map.MapParser;
import system.*;
import tile.TileManager;
import utils.ImageLoader;
import utils.ImageManager;
import main.Sound;

public class Playing extends State implements Statemethods {
    private Player player;
    private TileManager tileManager;

    // Array of monsters
    public ArrayList<Monster> monsters;

    // List and array of entities
    public ArrayList<Sprite> entityList;

    // Camera shake
    public CameraShake cameraShake;

    public CollectibleSystem collectibleSystem;
    public DoorSystem doorSystem;
    private RenderSystem renderSystem;
    public MonsterAreaSystem monsterAreaSystem;

    private SaveLoadSystem saveLoadSystem;

    private final ImageManager imageManager;

    // Game map
    public static GameMap currentMap;

    // Npc
    public Npc npcTalking = null;
    public Npc[] npcArray;

    // Level
    public String currentLevel = "level1";
    public EnergyOrb energyOrb = null;
    public NextLevel nextLevel = null;


    public Playing(Game game) {
        super(game);

        cameraShake = new CameraShake(20);

        ImageLoader.initialize();
        imageManager = ImageLoader.imageManager;
        player = new Player(this);
        setDefaultValues();

        soundtrack = new Sound();
//        setLevelTheme();

        saveLoadSystem = new SaveLoadSystem(this);
        saveLoadSystem.loadNewGame(currentLevel);
    }

    public void setDefaultValues() {
        tileManager = new TileManager(player);
        doorSystem = new DoorSystem();
        monsterAreaSystem = new MonsterAreaSystem();
        collectibleSystem = new CollectibleSystem();
        renderSystem = new RenderSystem(this);
    }

    public void loadMap() {
        MapParser.loadMap(currentLevel, "res/map/map_" + currentLevel + ".tmx");
        currentMap = MapManager.getGameMap(currentLevel);
        currentMap.buildTileManager(tileManager);
    }

    public void setUpList() {
        entityList = new ArrayList<>();
        entityList.add(player);
        for (Monster monster : monsters) {
            if (monster.currentHealth > 0) entityList.add(monster);
        }
        entityList.addAll(Arrays.asList(npcArray));
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }

    public ImageManager getImageManager() {
        return imageManager;
    }

    public RenderSystem getRenderSystem() { return renderSystem; }


    public DoorSystem getDoorSystem() { return doorSystem; }

    public MonsterAreaSystem getMonsterAreaSystem() { return monsterAreaSystem; }

    public SaveLoadSystem getSaveLoadSystem() { return saveLoadSystem; }

    public CollectibleSystem getCollectibleSystem() { return collectibleSystem; }

    public TileManager getTileManager() {
        return tileManager;
    }

    @Override
    public void update() {
//        System.out.println(player.worldX + " " + player.worldY);
        cameraShake.update();

        if (npcTalking != null) {
            npcTalking.update();
            return;
        }

        for (Sprite entity : new ArrayList<>(entityList)) {
            if (entity != null && entity.isOnTheScreen()) {
                entity.update();
            }
        }

        collectibleSystem.update();
        monsterAreaSystem.update();
        doorSystem.update();


        if (player.currentState != EntityState.DEATH)
            player.lockOn();

        if (player.currentState == EntityState.DEATH) {
            Gamestate.state = Gamestate.GAME_OVER;
        }

        if  (currentLevel.equals("level4") && energyOrb == null) {
            for (Monster monster : monsters) {
                if (monster instanceof SkeletonReaper && monster.currentState == EntityState.DEATH)
                    energyOrb = new EnergyOrb(this, 1220, 577);
            }

        }

        entityList.removeIf(entity -> entity.image == null && entity.currentState == EntityState.DEATH);
//        monsters.removeIf(monster -> monster.image == null && monster.currentState == EntityState.DEATH);

        if (energyOrb != null) energyOrb.update();


        if (!currentLevel.equals("level4") && nextLevel == null) {
            boolean allMonstersNull = true;
            for (Monster monster : monsters) {
                if (monster == null || (monster.image == null && monster.currentState == EntityState.DEATH)) continue;

                if (monster.currentState != EntityState.DEATH ||
                        (monster instanceof Demon && ((Demon) monster).currentPhase == 1) ||
                        (monster instanceof Samurai && ((Samurai) monster).currentPhase == 1)) {
                    allMonstersNull = false;
                    break;
                }
            }
            if (allMonstersNull) {
                nextLevel = new NextLevel(this, player.getWorldX(), player.getWorldY());
            }
            // For debugging
//            nextLevel = new NextLevel(this, player.getWorldX(), player.getWorldY());
        }

        if (KeyboardInputs.isPressedValid("pause", game.getKeyboardInputs().pausePressed)) {
            Gamestate.state = Gamestate.PAUSE;
            game.stopGame();
        }

        if (nextLevel != null) {
            nextLevel.update();
        }
//        for (Monster monster : monsters) {
//            if (monster.currentState == EntityState.DEATH) {
//                System.out.print(0);
//            } else {
//                System.out.print(1);
//            }
//        }
//        System.out.println();
    }

    @Override
    public void draw(Graphics2D g2) {
        currentMap.render(g2, player);

        collectibleSystem.draw(g2);
        if (doorSystem != null) doorSystem.draw(g2);

        entityList.stream()
                .sorted(Comparator.comparingDouble(Entity::getRenderOrder))
                .forEach(entity -> {
                    if (entity.isOnTheScreen() && entity.image != null) {
                        entity.draw(g2);
                        // currentMap.render2(g2, entity, player);
                    }
                });

        game.getUI().drawPlayerUI(g2);
        game.getUI().drawClock(g2);

        if (npcTalking != null) game.getUI().drawDialogueScreen(npcTalking.talk(), g2);

        for (Monster monster : monsters) {
            if (monster instanceof Demon || monster instanceof BringerOfDeath || monster instanceof Samurai ||
                monster instanceof SkeletonReaper) {
                Boss boss = (Boss) monster;
                boss.drawBossIntro(g2);
            }
            if (monster instanceof  SkeletonReaper) {
                ((SkeletonReaper) monster).drawDialogue(g2);
            }
        }

        if (energyOrb != null) energyOrb.draw(g2);
        if (nextLevel != null) nextLevel.draw(g2);
    }

    // Sound
    public Sound soundtrack;
    public void setLevelTheme() {
        switch (currentLevel) {
            case "level1":
                soundtrack.playMusic(4);
                break;
            case "level2":
                soundtrack.playMusic(5);
                break;
            case "level3":
                soundtrack.playMusic(6);
                break;
            default:
                soundtrack.playMusic(7);
                break;
        }
    }
}
