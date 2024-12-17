package gamestates;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.RecordData;
import inputs.KeyboardInputs;
import main.Game;
import utils.HelpMethods;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static inputs.KeyboardInputs.isPressedValid;
import static utils.Constants.Screen.*;

public class ViewRank extends State implements Statemethods {
    private Game game;

    private ArrayList<RecordData> rankData;

    private ObjectMapper objectMapper;
    public ViewRank(Game game) {
        super(game);
        this.game = game;
        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.AUTO_DETECT_GETTERS, false);
    }

    public void loadRank() {
        String data = game.api.getRank();
        System.out.println(data);
        if (data.equals("Failed")) return;
        try {
            rankData = objectMapper.readValue(data,  new TypeReference<ArrayList<RecordData>>() {});
//            System.out.println(rankData.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        KeyboardInputs keyboardInputs = game.getKeyboardInputs();
        if (isPressedValid("enter", keyboardInputs.enterPressed)) {
            Gamestate.state = Gamestate.MENU;
        }
    }

    public void draw(Graphics2D g2) {
        int x = TILE_SIZE * 4 , y = 5 * TILE_SIZE / 2 - 40;
        int width = SCREEN_WIDTH - 2 * x, height = SCREEN_HEIGHT - 2 * y;

        Color c = new Color(0, 0, 0, 200);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);

        int boxX = x, boxY = y;
        width = SCREEN_WIDTH - 2 * boxX; height = SCREEN_HEIGHT - 2 * boxY;
        String text = "RANKING";
        g2.setFont(game.getUI().maruMonica);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
        g2.setColor(Color.WHITE);
        x = HelpMethods.getXForCenterText(text, g2); y = boxY + 3 * TILE_SIZE / 2;
        g2.drawString(text, x, y);

        x = boxX + TILE_SIZE + 20;
        int xTime = x + 210;

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
        g2.setColor(Color.GREEN);

        y += TILE_SIZE / 2 + 20;
        g2.drawString("Username", x, y);
        g2.drawString("Time", xTime, y);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        g2.setColor(Color.WHITE);
        for (int i = 0; i < 5; i++) {
            RecordData data = (i < rankData.size() ? rankData.get(i) : null);
            String username = (data == null ? "-" : data.username);
            String time = (data == null ? "-" : game.getUI().formatTime(data.time));
            y += TILE_SIZE / 2 + HelpMethods.getTextHeight(username, g2);
            g2.drawString(username, x, y);
            g2.drawString(time, xTime, y);
        }

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
        y += 45;
        x += 40;
        g2.drawString("Return to Menu", x, y);
        g2.drawString("-->", x - 50, y);
    }
}
