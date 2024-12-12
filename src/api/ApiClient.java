package api;

import entities.Player;
import main.Game;

public class ApiClient {
    private final HttpClient httpClient;
    private String username;

    private Game game;

    public ApiClient(Game game) {
        this.game = game;
        this.httpClient = new HttpClient();

    }

    public boolean authenticate(String username, String password) {
        String endpoint = "/auth/login";
        String jsonInputString = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        try {
            String response = httpClient.sendPost(endpoint, jsonInputString);
            if (response.equalsIgnoreCase("Login successful")) {
                System.out.println("Login Successful!");
                this.username = username;
                return true;
            } else {
                System.out.println("Invalid credentials!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to authenticate!");
        }
        return false;
    }

    public String getSavedGameByUserName(String username) {
        String endpoint = "/save/" + username;
        try {
            return httpClient.sendGet(endpoint);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to get user info";
        }
    }

    public void saveGame(String username, String data) {
        String endpoint = "/save/" + username;
        try {
            String response = httpClient.sendPost(endpoint, data);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to authenticate!");
        }
    }

// ------------- Record api -------------
    public void saveRecord(Player player) {
        String endpoint = "/record/save";
        String jsonInputString = "{\"username\": \"" + username +
                                    "\", \"time\": \"" + game.totalElapsedTime +
                                    "\", \"health\": \"" + player.currentHealth +
                                    "\", \"mana\": \"" + player.currentMana  + "\"}";
        System.out.println(jsonInputString);
        try {
            String response = httpClient.sendPost(endpoint, jsonInputString);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to save!");
        }
    }

// --------------------------------------
    public String getRank() {
        String endpoint = "/record/rank";
        try {
            String response = httpClient.sendGet(endpoint);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
        }

        return "Failed";
    }
}
