package inputs;

import java.awt.event.*;

import gamestates.Gamestate;
import main.GamePanel;

public class KeyboardInputs extends KeyAdapter implements KeyListener  {

    private final GamePanel gamePanel;
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean enterPressed;
    public boolean shiftPressed;
    public boolean spacePressed;
    public boolean mousePressed;
    public boolean skillActivePressed; // K pressed
    public boolean attackPressed; // J pressed
    public boolean changWeaponPressed; // L pressed
    public boolean pausePressed; // P pressed
    public boolean escapePressed; // Escape pressed

    public int mousePressedScreenX, mousePressedScreenY;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        mousePressedScreenX = 0;
        mousePressedScreenY = 0;
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePressed = true;
//                System.out.println("Mouse pressed at: (" + e.getX() + ", " + e.getY() + ")");
                mousePressedScreenX = e.getX();
                mousePressedScreenY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mousePressed = false;
//                System.out.println("Mouse released at: (" + e.getX() + ", " + e.getY() + ")");
                mousePressedScreenX = 0;
                mousePressedScreenY = 0;
            }
        });

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
        if (code == KeyEvent.VK_SHIFT) shiftPressed = false;
        if (code == KeyEvent.VK_SPACE) spacePressed = false;
        if (code == KeyEvent.VK_K) skillActivePressed = false;
        if (code == KeyEvent.VK_J) attackPressed = false;
        if (code == KeyEvent.VK_L) changWeaponPressed = false;
        if (code == KeyEvent.VK_P) pausePressed = false;
        if (code == KeyEvent.VK_ESCAPE) escapePressed = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if (code == KeyEvent.VK_SHIFT) shiftPressed = true;
        if (code == KeyEvent.VK_SPACE) spacePressed = true;
        if (code == KeyEvent.VK_K) skillActivePressed = true;
        if (code == KeyEvent.VK_J) attackPressed = true;
        if (code == KeyEvent.VK_L) changWeaponPressed = true;
        if (code == KeyEvent.VK_P) pausePressed = true;
        if (code == KeyEvent.VK_ESCAPE) escapePressed = true;

    }
    public int getMouseX() {
        return gamePanel.getMouseX();
    }
    public int getMouseY() {
        return gamePanel.getMouseY();
    }



    public static boolean prevUp = false, prevDown = false, prevLeft = false, prevRight = false, prevEscape = false;
    public static boolean prevPause = false, prevEnter = false, prevAttack;
    public static boolean isPressedValid(String code, boolean keyPressed) {
        switch (code) {
            case "attack" -> {
                if (keyPressed) {
                    if (prevAttack) return false;
                    prevAttack = true;
                    return true;
                }
                prevAttack = false;
                return false;
            }
            case "pause" -> {
                if (keyPressed) {
                    if (prevPause) return false;
                    prevPause = true;
                    return true;
                }
                prevPause = false;
                return false;
            }
            case "enter" -> {
                if (keyPressed) {
                    if (prevEnter) return false;
                    prevEnter = true;
                    return true;
                }
                prevEnter = false;
                return false;
            }
            case "up" -> {
                if (keyPressed) {
                    if (prevUp) return false;
                    prevUp = true;
                    return true;
                }
                prevUp = false;
                return false;
            }
            case "down" -> {
                if (keyPressed) {
                    if (prevDown) return false;
                    prevDown = true;
                    return true;
                }
                prevDown = false;
                return false;
            }
            case "left" -> {
                if (keyPressed) {
                    if (prevLeft) return false;
                    prevLeft = true;
                    return true;
                }
                prevLeft = false;
                return false;
            }
            case "right" -> {
                if (keyPressed) {
                    if (prevRight) return false;
                    prevRight = true;
                    return true;
                }
                prevRight = false;
                return false;
            }
            case "escape" -> {
                if (keyPressed) {
                    if (prevEscape) return false;
                    prevEscape = true;
                    return true;
                }
                prevEscape = false;
                return false;
            }
        }
        return false;
    }
}
