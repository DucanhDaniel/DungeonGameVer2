package entities.npc;

import enitystates.Idle;
import gamestates.Playing;

import java.awt.*;

import static utils.Constants.Screen.TILE_SIZE;

public class WhiteSamurai extends Npc{
    public WhiteSamurai(Playing playing, int worldX, int worldY) {
        super("White_Samurai",
                playing, worldX, worldY, 6 * TILE_SIZE, 4 * TILE_SIZE);
        solidArea = new Rectangle(5 * TILE_SIZE / 2, 2 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        idle = new Idle(this, 10, 5);

        dialogues = new String[]{
                "Oh, another soul!",
                "But this time, this soul is really different...",
                "Looks like you are stuck in this... \nweak creature.",
                "Don't worry, your body still remains... \nif you come back on time.",
                "Haha, just climb up this dungeon again \nand become the strongest!",
                "But can you make it without your power \nin the past?",
                "This creature have some weapons, you can \npress L for changing weapons. \nAnd press J for attacking.",
                "Spear have great damage but low attack \nrange and gun deal smaller but it has \ngreater attack range.",
                "However, gun consume 1 mana per shot \nso use it wisely.",
                "Since you can still remain some memory \nfrom the past, I can collect and convert \nthem to a dash skill.",
                "Now you can press K to dash. \nWhen dashing, you will be immune to \ndamage, knockback and some other stuff.",
                "Remember, this dungeon has 3 levels, you \nmust defeat all of these monsters to get \nto the next level.",
                "Good luck!"
        };
    }


}
