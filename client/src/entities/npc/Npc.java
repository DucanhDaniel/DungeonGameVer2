package entities.npc;

import enitystates.EntityState;
import enitystates.Idle;
import entities.Sprite;
import gamestates.Playing;
import entities.Player;
import inputs.KeyboardInputs;

import java.awt.*;
import java.io.Serializable;

import static inputs.KeyboardInputs.isPressedValid;

public class Npc extends Sprite {
    public String[] dialogues;
    Player player;
    Idle idle;
    public Npc(String name, Playing playing, int worldX, int worldY,
               int width, int height) {
        super(name, playing, width, height);
        this.worldX = worldX;
        this.worldY = worldY;
        player = playing.getPlayer();
        visionBox = new Rectangle(0, 0, width, height);
        solidArea = new Rectangle(0, 0, width, height);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public boolean canTalk() {
        KeyboardInputs kb = playing.getGame().getKeyboardInputs();
        return canSeePlayer() && isPressedValid("enter", kb.enterPressed);
    }

    @Override
    public void update() {
        getDirectionForAttacking();
        if (currentState == EntityState.IDLE && idle != null) {
            idle.update(this);
            image = idle.getImage();
        }
        if (playing.npcTalking == null) {
            if (canTalk()) playing.npcTalking = this;
        }

    }

    int dialogueCounter = 0;
    public String talk() {
        KeyboardInputs kb = playing.getGame().getKeyboardInputs();

        // Make player idle
        player.currentState = EntityState.IDLE;

        if (isPressedValid("enter", kb.enterPressed)) {
            dialogueCounter++;
            if (dialogueCounter >= dialogues.length) {
                dialogueCounter = 0;
                return null;
            }
        }
        return dialogues[dialogueCounter];
    }

}
