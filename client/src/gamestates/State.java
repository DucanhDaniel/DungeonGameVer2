package gamestates;
import main.Game;
public class State {
    // Sound properties, need to be linked with other classes in the future
    public static int currentVolume = 100, maxVolume = 100;
    public static boolean isSoundEffectOn = true;
    public static boolean isSoundtrackOn = true;

    Playing playing;

    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public void update() {

    }
}
