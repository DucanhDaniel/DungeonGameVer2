package main;

import java.awt.*;

import data.SaveLoadSystem;
import gamestates.*;
import gamestates.Menu;
import inputs.KeyboardInputs;
import utils.ImageManager;

import static utils.Constants.Screen.*;

public class Game implements Runnable {

    private Thread gameThread;
    public ImageManager imageManager;
    private GamePanel gamePanel;
    private GameWindow gameWindow;
    private Playing playing;
    private Menu menu;
    private Selection selection;
    private CutScene cutScene;
    private GameOver gameOver;
    private final CollisionChecker collisionChecker;
    private Pause pause;
    private Setting setting;
    private UI ui;

    private SaveLoadSystem settings = new SaveLoadSystem(this);

    public SaveLoadSystem getSettings() {
        return settings;
    }

    public CollisionChecker getCollisionChecker() {
        return collisionChecker;
    }

    public Playing getPlaying() {
        return playing;
    }

    public Menu getMenu() {
        return menu;
    }

    public Selection getSelection() {
        return selection;
    }

    public Pause getPause() {
        return pause;
    }

    public Setting getSetting() {
        return setting;
    }

    public KeyboardInputs getKeyboardInputs() {
        return gamePanel.getKeyboardInputs();
    }

    public UI getUI() {
        return ui;
    }

    public Game() {

        initClasses();
        settings.loadSettings();
        playing.setLevelTheme();
        ui = new UI(this);
        imageManager = ImageManager.getInstance();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        collisionChecker = new CollisionChecker(this);

        startGameLoop();
    }

    public void initClasses() {
        menu = new Menu(this);
        playing = new Playing(this);
        selection = new Selection(this);
        pause = new Pause(this);
        setting = new Setting(this);
        gameOver = new GameOver(this);
        cutScene = new CutScene(this);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
//                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }

        }
    }

    public void update() {
        switch (Gamestate.state) {
            case MENU:
                menu.update();
                break;
            case SELECTION:
                selection.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case PAUSE:
                pause.update();
                break;
            case SETTING:
                setting.update();
                break;
            case GAME_OVER:
                gameOver.update();
                break;
            case CUTSCENE:
                cutScene.update();
                break;
            default:
                System.exit(0);
                break;
        }
    }

    public void render(Graphics2D g) {
        switch (Gamestate.state) {
            case MENU:
                menu.draw(g);
                break;
            case SELECTION:
                selection.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            case PAUSE:
                pause.draw(g);
                break;
            case SETTING:
                setting.draw(g);
                break;
            case GAME_OVER:
                gameOver.draw(g);
                break;
            case CUTSCENE:
                cutScene.draw(g);
                break;
            default:
                break;
        }
    }

    public GameOver getGameOver() {
        return gameOver;
    }
}
