package enitystates;

import effect.Dash;
import entities.Sprite;
import inputs.KeyboardInputs;
import entities.Player;

public class Run extends EntityStateMethods{
    public Run(Sprite entity, int totalAnimationFrames, int frameDuration) {
        super(entity, totalAnimationFrames, frameDuration);
        state = "RUN";
    }

    public Run(Sprite entity) {
        super(entity);
        state = "RUN";
    }

    @Override
    public void update(Sprite entity) {

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

        // Condition to change player state
        stateChanger(player, keyboardInputs);

        if (!player.collisionOn && !player.isIdling) {
            player.move();
        }

    }

    public void stateChanger(Player player, KeyboardInputs keyboardInputs) {
        if ((!keyboardInputs.attackPressed) || player.currentWeapon.equals("NORMAL")) {
            if (player.isIdling) {
                player.currentState = EntityState.IDLE;
            }
            if (!keyboardInputs.shiftPressed && !player.isIdling) {
                player.currentState = EntityState.WALK;
            }
        }
        else {
            player.attack.lastState = EntityState.RUN;
            player.currentState = EntityState.ATTACK;
        }
    }
}
