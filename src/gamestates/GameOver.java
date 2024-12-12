package gamestates;

import enitystates.EntityState;
import inputs.KeyboardInputs;
import main.Game;
import entities.Player;

import java.awt.*;

public class GameOver extends State implements Statemethods{
    Playing playing;
    public int commandIndex = 0;
    public GameOver(Game game) {
        super(game);
        this.playing = game.getPlaying();
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics2D g2) {
        KeyboardInputs kb = game.getKeyboardInputs();
        game.getUI().drawGameOverScreen(g2);
        if (KeyboardInputs.isPressedValid("up", kb.upPressed)) {
            if (commandIndex > 0) commandIndex--;
        }
        if (KeyboardInputs.isPressedValid("down", kb.downPressed)) {
            if (commandIndex < 1) commandIndex++;
        }
        if (KeyboardInputs.isPressedValid("enter", kb.enterPressed)) {
            switch (commandIndex) {
                case 0:
                    game.getPlaying().setDefaultValues();
                    playing.getSaveLoadSystem().loadNewGame(playing.currentLevel);
                    Player player = playing.getPlayer();
                    player.currentState = EntityState.IDLE;
                    System.out.println(player.currentHealth + " " + player.maxHealth);
                    playing.getSaveLoadSystem().loadNewGame(playing.currentLevel);

                    game.getSelection().setPlayerStats(Selection.playerType, true);

                    playing.nextLevel = null;
                    playing.getPlayer().resetScreenPosition();
                    Gamestate.state = Gamestate.PLAYING;
                    break;
                case 1:
                    game.getPlaying().setDefaultValues();
                    Gamestate.state = Gamestate.MENU;
                    break;
            }
        }
    }
}
