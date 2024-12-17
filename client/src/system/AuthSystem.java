package system;

import gamestates.Gamestate;
import main.Game;

public class AuthSystem {
    private Game game;
    public String username;
    public AuthSystem(Game game) {
        this.game = game;
    }

    public void authenticate(String username, String password) {
        if (game.api.authenticate(username, password)) {
            this.username = username;
            Gamestate.state = Gamestate.MENU;
        } else {
            System.out.println("Please try again!!!");
        }
    }
}
