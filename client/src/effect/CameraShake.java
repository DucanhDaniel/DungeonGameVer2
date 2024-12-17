package effect;

import java.util.Random;
import static utils.Constants.Player.*;

public class CameraShake {
    private int duration;
    private int elapsedTime;
    private boolean isShaking;
    private final Random random;
    private int tempX;
    private int tempY;
    public CameraShake(int duration) {
        this.duration = duration;
        this.elapsedTime = 0;
        this.isShaking = false;
        this.random = new Random();
        tempX = PLAYER_SCREEN_X;
        tempY = PLAYER_SCREEN_Y;
    }

    public void startShake() {
        this.elapsedTime = 0;
        this.isShaking = true;
        tempX = PLAYER_SCREEN_X;
        tempY = PLAYER_SCREEN_Y;
    }

    public void update() {
        if (!isShaking) {
            return;
        }

        elapsedTime++;

        if (elapsedTime >= duration) {
            isShaking = false;
            PLAYER_SCREEN_X = tempX;
            PLAYER_SCREEN_Y = tempY;
            return;
        }

        int offsetX = random.nextInt(10) - 5;
        int offsetY = random.nextInt(10) - 5;

        PLAYER_SCREEN_X = tempX + offsetX;
        PLAYER_SCREEN_Y = tempY + offsetY;
    }

    public boolean isShaking() {
        return isShaking;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
