package data;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import gamestates.Playing;
import gamestates.State;
import main.Game;
import system.*;

import java.io.*;

public class SaveLoadSystem {
    private Playing playing;
    private Game game;
    private DoorSystem doorSystem;

    private MonsterAreaSystem monsterAreaSystem;

    private CollectibleSystem collectibleSystem;


    private ObjectMapper objectMapper;

    public SaveLoadSystem(Game game) {
        this.game = game;
    }

    public SaveLoadSystem(Playing playing) {
        this.game = playing.getGame();
        this.playing = playing;
        this.doorSystem = playing.getDoorSystem();
        this.monsterAreaSystem = playing.getMonsterAreaSystem();
        this.collectibleSystem = playing.getCollectibleSystem();
        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.AUTO_DETECT_GETTERS, false);
    }

    public void saveGame() {
        GameData gameData = new GameData();
        gameData.isSaveFile = true;
        gameData.currentLevel = playing.currentLevel;
        gameData.timer = game.totalElapsedTime;
        gameData.player.saveData(playing.getPlayer());
        gameData.monsters.saveData(playing.monsters);
        gameData.npcsData.saveData(playing.npcArray);

        gameData.monsterAreaSystem = monsterAreaSystem;
        gameData.doorSystem = doorSystem;

        gameData.collectibleSystem = collectibleSystem;

        try {
            String jsonData = objectMapper.writeValueAsString(gameData);
            game.api.saveGame(game.getAuthSystem().username, jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadNewGame(String level) {
        try {
            GameData gameData = objectMapper.readValue(new File(level + ".json"), GameData.class);
            loadGame(gameData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSavedGame(String username) {
        String data = game.api.getSavedGameByUserName(username);
        try {
            GameData gameData = objectMapper.readValue(data, GameData.class);
            loadGame(gameData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame(GameData gameData) {
            playing.currentLevel = gameData.currentLevel;
            game.totalElapsedTime = gameData.timer;

            gameData.player.loadData(playing.getPlayer(), gameData.isSaveFile);
            gameData.monsters.loadData(playing);
            gameData.npcsData.loadData(playing);

            doorSystem = gameData.doorSystem;
            if (doorSystem != null) {
                doorSystem.playing = playing;
            }
            playing.doorSystem = doorSystem;

            monsterAreaSystem = gameData.monsterAreaSystem;
            if (monsterAreaSystem != null) {
                monsterAreaSystem.playing = playing;
            }
            playing.monsterAreaSystem = monsterAreaSystem;

            collectibleSystem = gameData.collectibleSystem;
            collectibleSystem.playing = playing;
//            InitSystem.initCollectibleObjects(collectibleSystem.collectibleList);
            playing.collectibleSystem = collectibleSystem;


            if (gameData.currentLevel != null)
                playing.currentLevel = gameData.currentLevel;

            playing.setUpList();
            playing.loadMap();
            playing.setLevelTheme();
    }

    public void saveSettings() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("settings.dat")));

            // Store data
            Settings settings = new Settings();
            settings.volume = game.getPause().currentVolume;
            settings.isSoundtrackOn = game.getPause().isSoundtrackOn;
            settings.isSoundEffectOn = game.getPause().isSoundEffectOn;

            oos.writeObject(settings);
        } catch (IOException e) {
            System.out.println("Failed to save settings!");
        }
    }
    public void loadSettings() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("settings.dat")));

            // Load data
            Settings settings = (Settings) ois.readObject();

            State.currentVolume = settings.volume;
            State.isSoundtrackOn = settings.isSoundtrackOn;
            State.isSoundEffectOn = settings.isSoundEffectOn;

            game.getPlaying().soundtrack.setVolume(settings.volume / 100f);

            if (!settings.isSoundEffectOn) game.getPlaying().soundtrack.toggleEffectMute();
            if (!settings.isSoundtrackOn) game.getPlaying().soundtrack.toggleSongMute();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
