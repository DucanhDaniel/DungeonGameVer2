package data;

import enitystates.EntityState;
import entities.monsters.*;
import entities.monsters.bosses.BringerOfDeath;
import entities.monsters.bosses.Demon;
import entities.monsters.bosses.Samurai;
import entities.monsters.bosses.SkeletonReaper;
import gamestates.Playing;

import java.util.ArrayList;

import static java.lang.Math.min;

public class MonstersData {
    public static class MonsterData {
        public String name;
        public int worldX, worldY, currentHealth;

        public MonsterData() {}
        public MonsterData(String name, int worldX, int worldY, int currentHealth) {
            this.name = name;
            this.worldX = worldX;
            this.worldY = worldY;
            this.currentHealth = currentHealth;
        }
    }

    public ArrayList<MonsterData> monstersData;

    public MonstersData() {
        monstersData = new ArrayList<>();
    }

    public void saveData(ArrayList<Monster> monsters) {
        for (Monster monster : monsters) {
            if (monster != null) {
                monstersData.add(new MonsterData(monster.name, monster.worldX, monster.worldY, monster.currentHealth));
            }
        }
    }

    public void loadData(Playing playing) {
//        playing.monsters = new Monster[monstersData.size()]
        playing.monsters = new ArrayList<>();
//        System.out.println(monstersData.size());

        for (MonsterData monsterData : monstersData) {
            Monster monster = createMonster(playing, monsterData.name,
                    monsterData.worldX, monsterData.worldY, monsterData.currentHealth);
            System.out.println(monster.name);
            playing.monsters.add(monster);
        }
    }

    private Monster createMonster(Playing playing, String name,
                                 int worldX, int worldY, int currentHealth) {
        if (name == null) return null;
        Monster monster = switch (name) {
            case "Slime" -> new Slime(playing, worldX, worldY);
            case "BringerOfDeath" -> new BringerOfDeath(playing, worldX, worldY);
            case "Demon" -> new Demon(playing, worldX, worldY);
            case "Mage" -> new Mage(playing, worldX, worldY);
            case "Morph" -> new Morph(playing, worldX, worldY);
            case "PlantMelee" -> new PlantMelee(playing, worldX, worldY);
            case "Samurai" -> new Samurai(playing, worldX, worldY);
            case "Sickle" -> new Sickle(playing, worldX, worldY);
            case "Sword_Knight" -> new SwordKnight(playing, worldX, worldY);
            case "SkeletonReaper" -> new SkeletonReaper(playing, worldX, worldY);
            default -> null;
        };
        assert monster != null;
        monster.currentHealth = min(currentHealth, monster.maxHealth);
        return monster;
    }
}
