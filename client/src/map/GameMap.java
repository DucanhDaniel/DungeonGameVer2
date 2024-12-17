package map;

import tile.TileLayer;
import tile.TileManager;
import tile.TileSet;

import java.awt.*;
import java.util.ArrayList;

import entities.Player;

import static utils.Constants.Screen.*;

public class GameMap {

    private final int mapWidth;
    private final int mapHeight;

    ArrayList<TileSet> tileSetList;
    public ArrayList<TileLayer> map;

    public GameMap(int mapWidth , int mapHeight)
    {
        map = new ArrayList<>();
        tileSetList = new ArrayList<>();

        this.mapWidth = mapWidth * SCALE;
        this.mapHeight = mapHeight * SCALE;
    }

    public void render(Graphics2D g2, Player player)
    {
        for (int i = 0; i < map.size() - 1; i++) {
            TileLayer layer = map.get(i);
            layer.render(g2, player);

        }
    }
//    int startRow, startCol, endRow, endCol;
//    public void render2(Graphics2D g2, Sprite entity, Player player) {
//        TileLayer layer = map.get(1);
//        startCol = (entity.worldX + entity.solidArea.x) / TILE_SIZE;
//        endCol = (entity.worldX + entity.solidArea.x + entity.solidArea.width) / TILE_SIZE;
//        startRow = (entity.worldY + entity.solidArea.y) / TILE_SIZE;
//        endRow = (entity.worldY + entity.solidArea.y + entity.solidArea.height) / TILE_SIZE;
//
//        int worldX, worldY, screenX, screenY;
//        for (int i = startRow; i <= endRow; i++) for (int j = startCol; j <= endCol; j++) {
//            if (i >= 0 && i < MAX_WORLD_ROW && j >= 0 && j < MAX_WORLD_COL) {
//                if (layer.tileLayerData[i][j] == null) continue;
//                worldX = j * TILE_SIZE;
//                worldY = i * TILE_SIZE;
//                screenX = (worldX - player.worldX + PLAYER_SCREEN_X);
//                screenY = (worldY - player.worldY + PLAYER_SCREEN_Y);
//                if (worldY > entity.getWorldY())
//                    g2.drawImage(layer.tileLayerData[i][j].image , screenX  , screenY , TILE_SIZE , TILE_SIZE , null);
//            }
//        }
//
//    }

    public void buildTileManager(TileManager tileManager) {
        TileLayer layer = map.get(map.size() - 1);
        for (int i = 0; i < MAX_WORLD_ROW; i++)
            for (int j = 0; j < MAX_WORLD_COL; j++) {
                if (layer.getTileData(i, j) != 0) {
                    tileManager.tile[i][j] = true;
                    tileManager.solidArea[i][j] = new Rectangle(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
                else {
                    tileManager.tile[i][j] = false;
                    tileManager.solidArea[i][j] = null;
                }
            }
    }


    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }
}