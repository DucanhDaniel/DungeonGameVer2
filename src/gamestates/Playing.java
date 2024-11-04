package gamestates;

import enitystates.EntityState;
import entities.*;
import entities.monsters.Monster;
import entities.monsters.Slime;
import main.Game;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import tile.TileManager;
import utils.HelpMethods;
import utils.ImageLoader;
import utils.ImageManager;

import static utils.Constants.Screen.TILE_SIZE;

public class Playing extends State implements Statemethods {

    private final Player player;
    private final TileManager tileManager;

    // Array of monsters
    public Monster[] monsters;

    // List and array of entities
    public ArrayList<Entity> entityList;
    public Entity[] entityArray;

    public Playing(Game game) {
        super(game);
        player = new Player(this);
        tileManager = new TileManager(player);

        monsters = new Monster[5];
        monsters[0] = new Slime(this, 5 * TILE_SIZE, 7 * TILE_SIZE);
        monsters[1] = new Slime(this,  5 * TILE_SIZE, 3 * TILE_SIZE);
        monsters[2] = new Slime(this, 7 * TILE_SIZE, 3 * TILE_SIZE);
        monsters[3] = new Slime(this, 6 * TILE_SIZE, 3 * TILE_SIZE);
        monsters[4] = new Slime(this, 8 * TILE_SIZE, 3 * TILE_SIZE);

        entityList = new ArrayList<>();
        entityList.add(player);
        entityList.addAll(Arrays.asList(monsters));

        entityArray = entityList.toArray(new Entity[0]);
    }

    public Game getGame() {
        return game;
    }
    public Player getPlayer() {
        return player;
    }

    public TileManager getTileManager() {
        return tileManager;
    }

    @Override
    public void update() {

        for (Entity entity : entityArray) {
            if (entity != null){
                entity.update();
            }
        }
        if (player.currentState != EntityState.DEATH)
            player.lockOn();
    }

    @Override
    public void draw(Graphics2D g2) {

        tileManager.draw(g2);
        entityList.sort(Comparator.comparingDouble(Entity::getWorldY));

        for (Entity entity : entityList) {
            entity.draw(g2);
        }

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


}
