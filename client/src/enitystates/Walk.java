package enitystates;

import effect.Dash;
import entities.Player;
import entities.monsters.*;
import entities.Sprite;
import inputs.KeyboardInputs;

import static java.lang.Math.abs;
import static utils.Constants.Screen.TILE_SIZE;

public class Walk extends EntityStateMethods{

    public Walk(Sprite entity, int totalAnimationFrames, int frameDuration, String state) {
        super(entity, totalAnimationFrames, frameDuration);
        this.state = state;
    }

    public Walk(Sprite entity, int totalAnimationFrames, int frameDuration) {
        super(entity, totalAnimationFrames, frameDuration);
        state = "WALK";
    }

    public Walk(Sprite entity) {
        super(entity);
        state = "WALK";
    }

    @Override
    public void update(Sprite entity) {


    }
    public void update(Monster monster) {
        if (monster instanceof Slime slime) {
            slime.move();
            if (slime.canAttack(true)) {
                slime.currentState = EntityState.ATTACK;
            }
            else slime.stateChanger();
        }
        else if (!monster.canSeePlayer()) {
            monster.currentState = EntityState.IDLE;
        } else {
            if (monster.canAttack(true)) monster.currentState = EntityState.ATTACK;
            else monster.move();
        }
    }

    public void update(Player player, KeyboardInputs keyboardInputs) {
        player.speed = player.getSpeed();
        player.isIdling = false;
        if (keyboardInputs.upPressed) {
            if (keyboardInputs.leftPressed)
                player.direction = "left_up";
            else if (keyboardInputs.rightPressed)
                player.direction = "right_up";
            else player.direction = "up";
        } else if (keyboardInputs.downPressed) {
            if (keyboardInputs.leftPressed)
                player.direction = "left_down";
            else if (keyboardInputs.rightPressed)
                player.direction = "right_down";
            else player.direction = "down";
        } else if (keyboardInputs.leftPressed)
            player.direction = "left";
        else if (keyboardInputs.rightPressed)
            player.direction = "right";
        else player.isIdling = true;

        player.getPlaying().getGame().getCollisionChecker().checkTile(player);

        // Condition for changing player state
        stateChanger(player, keyboardInputs);

        if (player.isIdling) player.currentState = EntityState.IDLE;
        if (!player.collisionOn && !player.isIdling) {
            player.move();
        }

    }
    public void stateChanger(Player player, KeyboardInputs keyboardInputs) {
        if (!keyboardInputs.attackPressed || player.currentWeapon.equals("NORMAL")) {
            if (player.isIdling) player.currentState = EntityState.WALK;
            if (!player.isIdling && keyboardInputs.shiftPressed) player.currentState = EntityState.RUN;
        }
        else {
            if (player.currentWeapon.equals("SPEAR") || player.currentMana - player.manaCostPerShot >= 0) {
                player.attack.lastState = EntityState.WALK;
                player.currentState = EntityState.ATTACK;
            }
        }
    }
}
