package gamestates;

import inputs.KeyboardInputs;
import main.Game;

import java.awt.*;

public class Setting extends State implements Statemethods {
    public int commandIndex = 0;

    public static final int mainPanel = 0;
    public static final int controlPanel = 1;
    public static final int savePanel = 2;
    public static final int volumeControlPanel = 3;
    public int currentPanel = 0;

    public Setting(Game game) {
        super(game);
        playing = game.getPlaying();
    }

    public void update() {
        KeyboardInputs keyboardInputs = game.getKeyboardInputs();
        switch (currentPanel) {
            case mainPanel:
                if (KeyboardInputs.isPressedValid("up", keyboardInputs.upPressed)) {
                    commandIndex--;
                    if (commandIndex < 0) commandIndex = 0;
//                    System.out.println(commandIndex);
                } else if (KeyboardInputs.isPressedValid("down", keyboardInputs.downPressed)) {
                    commandIndex++;
                    if (commandIndex > 2) commandIndex = 2;
//                    System.out.println(commandIndex);
                } else if (KeyboardInputs.isPressedValid("enter", keyboardInputs.enterPressed)) {
                    switch (commandIndex) {
                        case 0:
                            currentPanel = controlPanel;
                            break;
                        case 1:
                            currentPanel = volumeControlPanel;
                            break;
                        case 2:
                            Gamestate.state = Gamestate.MENU;
                            break;
                    }
                    commandIndex = 0;
                } else if (KeyboardInputs.isPressedValid("escape", keyboardInputs.escapePressed)) {
                    Gamestate.state = Gamestate.MENU;
                }
                break;
            case volumeControlPanel:
                if (KeyboardInputs.isPressedValid("up", keyboardInputs.upPressed)) {
                    if (commandIndex > 0) commandIndex--;
//                    System.out.println(commandIndex);
                } else if (KeyboardInputs.isPressedValid("down", keyboardInputs.downPressed)) {
                    if (commandIndex < 2) commandIndex++;
//                    System.out.println(commandIndex);
                } else if (KeyboardInputs.isPressedValid("left", keyboardInputs.leftPressed)) {
                    if (currentVolume >= 10 && commandIndex == 0) {
                        currentVolume -= 10;
                        playing.soundtrack.setVolume(currentVolume / 100f);
//                        System.out.println(currentVolume);
                    }
                } else if (KeyboardInputs.isPressedValid("right", keyboardInputs.rightPressed)) {
                    if (currentVolume <= 90 && commandIndex == 0) {
                        currentVolume += 10;
                        playing.soundtrack.setVolume(currentVolume / 100f);
//                        System.out.println(currentVolume);
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
                else if (KeyboardInputs.isPressedValid("escape", keyboardInputs.escapePressed)) {
                    Gamestate.state = Gamestate.SETTING;
                    currentPanel = mainPanel;
                }
            case controlPanel:
                if(KeyboardInputs.isPressedValid("escape", keyboardInputs.escapePressed)) {
                    Gamestate.state = Gamestate.SETTING;
                    currentPanel = mainPanel;
                }
        }

    }

    public void draw(Graphics2D g) {
        game.getUI().drawSettingScreen(g);
    }
}
