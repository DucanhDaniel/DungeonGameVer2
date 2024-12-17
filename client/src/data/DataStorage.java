package data;

import entities.Entity;
import entities.Sprite;
import entities.monsters.Monster;
import entities.npc.Npc;
import objects.Door;
import objects.MonsterArea;
import system.MonsterAreaSystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class DataStorage implements Serializable {
    // Player stats
    private static final long serialVersionUID = -4102511965978810115L;
    int worldX, worldY, currentHealth, maxHealth, currentMana, maxMana, currentArmor, maxArmor;
    String direction;
    String currentWeapon;

    // Playing variables
    String currentLevel;

    int[] monstersWorldX;
    int[] monstersWorldY;
    String[] monstersName;
    int[] monstersCurrentHealth;

    String[] npcName;
    int[] npcWorldX;
    int[] npcWorldY;

}
