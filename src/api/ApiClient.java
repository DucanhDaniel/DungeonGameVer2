package api;

import main.Game;

public class ApiClient {
    private final HttpClient httpClient;

    public ApiClient() {
        this.httpClient = new HttpClient();
    }

    public boolean authenticate(String username, String password) {
        String endpoint = "/auth/login";
        String jsonInputString = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        try {
            String response = httpClient.sendPost(endpoint, jsonInputString);
            if (response.equalsIgnoreCase("Login successful")) {
                System.out.println("Login Successful!");
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

    public int getCurrentLevelByUsername(String username) {
        String endpoint = "/current-level/" + username;
        try {
            String response = httpClient.sendGet(endpoint);
            return Integer.parseInt(response);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
