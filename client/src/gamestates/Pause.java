package gamestates;

import inputs.KeyboardInputs;
import main.Game;

import java.awt.*;


public class Pause extends State implements Statemethods {
    public int commandIndex = 0;

    public static final int mainPanel = 0;
    public static final int controlPanel = 1;
    public static final int savePanel = 2;
    public static final int volumeControlPanel = 3;
    public int currentPanel = 0;

    public Pause(Game game) {
        super(game);
        playing = game.getPlaying();
    }

    @Override
    public void update() {
        KeyboardInputs keyboardInputs = playing.getGame().getKeyboardInputs();
        switch (currentPanel) {
            case mainPanel:
                if (KeyboardInputs.isPressedValid("pause", keyboardInputs.pausePressed)) {
                    Gamestate.state = Gamestate.PLAYING;
                    game.continueGame();
                } else if (KeyboardInputs.isPressedValid("up", keyboardInputs.upPressed)) {
                    commandIndex--;
                    if (commandIndex < 0) commandIndex = 0;
                } else if (KeyboardInputs.isPressedValid("down", keyboardInputs.downPressed)) {
                    commandIndex++;
                    if (commandIndex > 3) commandIndex = 3;
                } else if (KeyboardInputs.isPressedValid("enter", keyboardInputs.enterPressed)) {
                    switch (commandIndex) {
                        case 0:
                            currentPanel = controlPanel;
                            break;
                        case 1:
                            currentPanel = savePanel;
                            break;
                        case 2:
                            currentPanel = volumeControlPanel;
                            break;
                        case 3:
                            Gamestate.state = Gamestate.MENU;
                            break;
                    }
                    commandIndex = 0;
                }
                break;
            case controlPanel:
                if (KeyboardInputs.isPressedValid("pause", keyboardInputs.pausePressed)) {
                    currentPanel = mainPanel;
                    Gamestate.state = Gamestate.PLAYING;
                    game.continueGame();
                }
                break;
            case savePanel:
                playing.getSaveLoadSystem().saveGame();
                if (KeyboardInputs.isPressedValid("pause", keyboardInputs.pausePressed)) {
                    currentPanel = mainPanel;
                    Gamestate.state = Gamestate.PLAYING;
                    game.continueGame();
                }
                break;
            case volumeControlPanel:
                if (KeyboardInputs.isPressedValid("up", keyboardInputs.upPressed)) {
                    if (commandIndex > 0) commandIndex--;
                } else if (KeyboardInputs.isPressedValid("down", keyboardInputs.downPressed)) {
                    if (commandIndex < 2) commandIndex++;
                } else if (KeyboardInputs.isPressedValid("pause", keyboardInputs.pausePressed)) {
                    currentPanel = mainPanel;
                    commandIndex = 0;
                    game.continueGame();
                    Gamestate.state = Gamestate.PLAYING;
                    game.getSettings().saveSettings();
                } else if (KeyboardInputs.isPressedValid("left", keyboardInputs.leftPressed)) {
                    if (currentVolume >= 10 && commandIndex == 0) {
                        currentVolume -= 10;
                        playing.soundtrack.setVolume(currentVolume / 100f);
                    }
                } else if (KeyboardInputs.isPressedValid("right", keyboardInputs.rightPressed)) {
                    if (currentVolume <= 90 && commandIndex == 0) {
                        currentVolume += 10;
                        playing.soundtrack.setVolume(currentVolume / 100f);
                    }
                } else if (KeyboardInputs.isPressedValid("enter", keyboardInputs.enterPressed)) {
                    switch (commandIndex) {
                        case 1:
//                            if (isSoundEffectOn)
                                playing.soundtrack.toggleEffectMute();
                            isSoundEffectOn = !isSoundEffectOn;
                            break;
                        case 2:
//                            if (isSoundtrackOn)
                                playing.soundtrack.toggleSongMute();
                            isSoundtrackOn = !isSoundtrackOn;
                            break;
                    }
                }
        }

    }

    public void draw(Graphics2D g2) {
        game.getUI().drawPauseScreen(g2);
    }


}
