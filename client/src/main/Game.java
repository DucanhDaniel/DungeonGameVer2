package main;

import java.awt.*;
import javax.swing.Timer;

import api.ApiClient;
import data.SaveLoadSystem;
import gamestates.*;
import gamestates.Menu;
import gamestates.auth.Login;
import inputs.KeyboardInputs;
import utils.ImageManager;
import system.AuthSystem;

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

    private ViewRank viewRank;
    private Setting setting;
    private UI ui;

    private Login login;

    public ApiClient api;
    private AuthSystem authSystem;

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

    public ViewRank getViewRank() { return viewRank; }

    public Login getLogin() { return login; }

    public Setting getSetting() {
        return setting;
    }

    public KeyboardInputs getKeyboardInputs() {
        return gamePanel.getKeyboardInputs();
    }

    public UI getUI() {
        return ui;
    }
    public AuthSystem getAuthSystem() { return authSystem; }

    public Game() {
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        initClasses();
        settings.loadSettings();
        playing.setLevelTheme();
        ui = new UI(this);
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
        login = new Login(this);
        api = new ApiClient(this);
        authSystem = new AuthSystem(this);
        viewRank = new ViewRank(this);
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
            case LOGIN:
                login.update();
                break;
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
            case VIEW_RANK:
                viewRank.update();
                break;
            default:
                System.exit(0);
                break;
        }
    }

    public void render(Graphics2D g) {
        switch (Gamestate.state) {
            case LOGIN:
                login.draw(g);
                break;
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
            case VIEW_RANK:
                viewRank.draw(g);
                break;
            default:
                break;
        }
    }

    public GameOver getGameOver() {
        return gameOver;
    }

//    -------------- start new game --------------
    public long startTime;
    public long elapsedTime;

    public long totalElapsedTime;

    private Timer timer;

    public void startNewGame() {
        startTime = System.currentTimeMillis();
        elapsedTime = 0; // Reset thời gian

        timer = new Timer(1000, e -> {
            totalElapsedTime = elapsedTime + (System.currentTimeMillis() - startTime) / 1000;
        });
        timer.start();
    }

    public void stopGame() {
        timer.stop();
        elapsedTime += (System.currentTimeMillis() - startTime) / 1000;
    }

    public void continueGame() {
        startTime = System.currentTimeMillis();
        timer = new Timer(1000, e -> {
            long currentElapsedTime = (System.currentTimeMillis() - startTime) / 1000; // Thời gian mới
            totalElapsedTime = elapsedTime + currentElapsedTime; // Tổng thời gian
//            System.out.println("Elapsed Time: " + totalElapsedTime + " seconds");
        });
        timer.start();
    }
}
