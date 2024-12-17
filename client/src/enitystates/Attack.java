package enitystates;

import entities.monsters.*;
import entities.Sprite;
import inputs.KeyboardInputs;
import entities.Player;


public class Attack extends EntityStateMethods{
    public EntityState lastState;

    public Attack(Sprite entity,int totalAnimationFrames, int frameDuration) {
        super(entity, totalAnimationFrames, frameDuration);
        state = "ATTACK";
    }

    // This constructor used for multiple attack types
    public Attack(Sprite entity, int totalAnimationFrames, int frameDuration, String customAttackState) {
        super(entity, totalAnimationFrames, frameDuration);
        state = customAttackState;
    }

    public Attack(Sprite entity) {
        super(entity);
        state = "ATTACK";
    }

    public int frameCounter = 0;
    @Override
    public void update(Sprite entity) {

    }

    public void update(Monster monster) {
        monster.attack();
    }

    public void update(Player player, KeyboardInputs keyboardInputs) {
        // focus at the position of the mouse
        player.lockOn();
        frameCounter++;
        if (player.currentWeapon.equals("SPEAR") && frameCounter == 2 * frameDuration) {
            player.getPlaying().soundtrack.playSE(5);
        }
        if (player.currentWeapon.equals("GUN") && frameCounter == 2 * frameDuration) {
            player.getPlaying().soundtrack.playSE(4);
        }
        if (frameCounter >= totalAnimationFrames * frameDuration) {
            for (Monster monster : player.getPlaying().monsters) {
                if (monster != null) {
                    if (monster.isBeingLockOn) {
                        if (player.currentWeapon.equals("SPEAR"))
                            monster.getHurt(player.attackPointSpear);
                        else if (player.currentMana - player.manaCostPerShot < 0) {
                            player.currentState = lastState;
                            return;
                        }
                        else if (player.currentWeapon.equals("GUN")) {
                            monster.getHurt(player.attackPointGun);
                            player.currentMana -= player.manaCostPerShot;
                        }

                    }
                }
            }
            if (!keyboardInputs.attackPressed) {
                player.currentState = lastState;
            }
            frameCounter = 0;
        }
    }
}
