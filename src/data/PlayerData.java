package data;

import entities.Player;
import gamestates.Selection;

public class PlayerData {
    public int playerType;
    public int worldX, worldY, currentHealth, maxHealth, currentMana, maxMana, currentArmor, maxArmor;
    public String direction;
    public String currentWeapon;
    public int attackPointSpear, attackPointGun;

    public PlayerData() {
    }

    public void loadData(Player player, boolean isSaveFile) {
        if (isSaveFile) {
            Selection.playerType = this.playerType;
            player.worldX = this.worldX;
            player.worldY = this.worldY;
            player.currentHealth = this.currentHealth;
            player.maxHealth = this.maxHealth;
            player.currentMana = this.currentMana;
            player.maxMana = this.maxMana;
            player.currentArmor = this.currentArmor;
            player.maxArmor = this.maxArmor;
            player.direction = this.direction;
            player.currentWeapon = this.currentWeapon;
            player.attackPointGun = this.attackPointGun;
            player.attackPointSpear = this.attackPointSpear;
        }
        else {
//            if (player.getPlaying().currentLevel.equals("level1")) {
//                player.getPlaying().getGame().getSelection().setPlayerStats(Selection.playerType, true);
//            }
            player.worldX = this.worldX;
            player.worldY = this.worldY;
            player.direction = this.direction;
        }


    }

    public void saveData(Player player) {
        this.playerType = Selection.playerType;
        this.worldX = player.worldX;
        this.worldY = player.worldY;
        this.currentHealth = player.currentHealth;
        this.maxHealth = player.maxHealth;
        this.currentMana = player.currentMana;
        this.maxMana = player.maxMana;
        this.currentArmor = player.currentArmor;
        this.maxArmor = player.maxArmor;
        this.direction = player.direction;
        this.currentWeapon = player.currentWeapon;
        this.attackPointGun = player.attackPointGun;
        this.attackPointSpear = player.attackPointSpear;
    }
}
