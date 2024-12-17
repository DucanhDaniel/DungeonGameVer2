package components;

public class AnimationComponent {
    public int totalAnimationFrame;
    public int numAnimationFrame;
    public int frameDuration;
    public int frameCounter;

    private boolean completeAnimation;

    public AnimationComponent() {}

    public AnimationComponent(int totalAnimationFrame, int frameDuration) {
        this.totalAnimationFrame = totalAnimationFrame;
        this.frameDuration = frameDuration;
        this.numAnimationFrame = 1;
        this.frameCounter = 0;
        this.completeAnimation = false;
    }

    public void playAnAnimation() {
        if (numAnimationFrame == totalAnimationFrame) return;
        frameCounter++;
        if (frameCounter >= frameDuration) {
            frameCounter = 0;
            numAnimationFrame += 1;
        }
    }

    public void playAnAnimationReverse() {
        if (numAnimationFrame == 1) return;
        frameCounter++;
        if (frameCounter >= frameDuration) {
            frameCounter = 0;
            numAnimationFrame -= 1;
        }
    }

    public void updateAnimation() {
        frameCounter++;
        if (frameCounter >= frameDuration) {
            frameCounter = 0;
            numAnimationFrame += 1;
            if (numAnimationFrame > totalAnimationFrame) {
                numAnimationFrame -= totalAnimationFrame;
            }
        }
    }
    public int getCurrentFrame() {
        return numAnimationFrame;
    }

}
