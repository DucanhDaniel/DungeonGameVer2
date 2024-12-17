package data;

import entities.npc.Npc;
import entities.npc.WhiteSamurai;
import gamestates.Playing;

import java.util.ArrayList;

public class NPCsData {
    public static class NPCData {
        public String name;
        public int worldX, worldY;

        public NPCData() {}
        public NPCData(String name, int worldX, int worldY) {
            this.name = name;
            this.worldX = worldX;
            this.worldY = worldY;
        }
    }

    public ArrayList<NPCData> npcsData;

    public NPCsData() {
        npcsData = new ArrayList<>();
    }

    public void saveData(Npc[] npcArray) {
        for (Npc npc : npcArray) {
            if (npc != null) {
                npcsData.add(new NPCData(npc.name, npc.worldX, npc.worldY));
            }
        }
    }

    public void loadData(Playing playing) {
        playing.npcArray = new Npc[npcsData.size()];

        int id = 0;
        for (NPCData npcData : npcsData) {
            Npc npc = createNpc(playing, npcData.name,
                    npcData.worldX, npcData.worldY);
            playing.npcArray[id] = npc;
            id++;
        }
    }
    public Npc createNpc(Playing playing, String name, int worldX, int worldY) {
        Npc npc = null;
        if (name.equals("White_Samurai")) {
            npc = new WhiteSamurai(playing, worldX, worldY);
        }
        return npc;
    }
}
