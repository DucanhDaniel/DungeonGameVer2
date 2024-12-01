package system;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.SaveLoad;
import gamestates.Playing;
import main.Game;
import objects.Door;
import objects.MonsterArea;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SaveLoadSystem {
    private Playing playing;
    private DoorSystem doorSystem;
    private MonsterAreaSystem monsterAreaSystem;

    ObjectMapper objectMapper;

    public SaveLoadSystem(Playing playing) {
        this.playing = playing;
        this.doorSystem = playing.getDoorSystem();
        this.monsterAreaSystem = playing.getMonsterAreaSystem();
        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.AUTO_DETECT_GETTERS, false);
    }

    public void saveGame() {
        GameData gameData = new GameData();
        gameData.monsterAreaSystem = monsterAreaSystem;
        gameData.doorSystem = doorSystem;

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("save.json"), gameData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame(String filePath) {

        try {
            GameData gameData = objectMapper.readValue(new File(filePath + ".json"), GameData.class);
//            System.out.println("aaaaaaa " + doorSystem.doors.size());
            doorSystem = gameData.doorSystem;
            doorSystem.playing = playing;
            playing.doorSystem = doorSystem;

            monsterAreaSystem = gameData.monsterAreaSystem;
            monsterAreaSystem.playing = playing;
            playing.monsterAreaSystem = monsterAreaSystem;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
