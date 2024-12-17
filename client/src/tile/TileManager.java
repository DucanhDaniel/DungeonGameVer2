package tile;

import entities.Sprite;
import java.awt.*;
import static utils.Constants.Screen.*;
import entities.Player;

public class TileManager {

    public Boolean[][] tile;
    public Rectangle[][] solidArea;

    public TileManager(Player player) {
        tile = new Boolean[MAX_WORLD_ROW][MAX_WORLD_COL];
        for (int i = 0; i < MAX_WORLD_ROW; i++)
            for (int j = 0; j < MAX_WORLD_COL; j++)
                tile[i][j] = false;
        solidArea = new Rectangle[MAX_WORLD_ROW][MAX_WORLD_COL];
    }

    public boolean isWall(int row, int col) {
        return tile[row][col];
    }
}
