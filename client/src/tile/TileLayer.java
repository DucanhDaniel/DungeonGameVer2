package tile;


import java.awt.*;
import java.util.ArrayList;
import entities.Player;
import gamestates.Gamestate;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static utils.Constants.Player.PLAYER_SCREEN_X;
import static utils.Constants.Player.PLAYER_SCREEN_Y;
import static utils.Constants.Screen.*;

public class TileLayer {

    public Tile[][] tileLayerData;
    private int[][] data;
    final private ArrayList<TileSet> tileSetList;

    final private int numCols;
    final private int numRows;

    final private int tileWidth;
    final private int tileHeight;

    public TileLayer(int numrows , int numcols  , int [][] data , ArrayList<TileSet> tileSetList)
    {
        super();
        this.numRows = numrows;
        this.numCols = numcols;
        this.tileSetList = tileSetList;
        this.tileWidth = tileSetList.get(0).getTileWidth() * SCALE;
        this.tileHeight = tileSetList.get(0).getTileHeight() * SCALE;
        this.data = data;

        parseLayerData(data);
    }

    public int getTileData(int row, int col) {
        return data[row][col];
    }

    int startRow, startCol, endRow, endCol;
    int offSet = 5;
    public void render(Graphics2D g2, Player player) {
        if (Gamestate.state != Gamestate.CUTSCENE) {
            startRow = max(player.getWorldY() / TILE_SIZE - MAX_SCREEN_ROW / 2 - offSet, 0);
            endRow = min(player.getWorldY() / TILE_SIZE + MAX_WORLD_ROW / 2 + offSet, MAX_WORLD_ROW);
            startCol = max(player.getWorldX() / TILE_SIZE - MAX_SCREEN_COL / 2 - offSet, 0);
            endCol = min(player.getWorldX() / TILE_SIZE + MAX_SCREEN_COL / 2 + offSet, MAX_WORLD_COL);
        }
        else {
            startRow = 0;
            endRow = MAX_WORLD_ROW;
            startCol = 0;
            endCol = MAX_WORLD_COL;
        }
        for(int i = startRow ; i < endRow ; i++)
        {
            for(int j = startCol ; j < endCol ; j++)
            {
                if(tileLayerData[i][j] == null) continue;

                Tile tile = tileLayerData[i][j];

                int worldX = j * tileWidth;
                int worldY = i * tileHeight;

                int screenX = (worldX - player.worldX + PLAYER_SCREEN_X);
                int screenY = (worldY - player.worldY + PLAYER_SCREEN_Y);

                g2.drawImage(tile.getTileImg() , screenX  , screenY  , tileWidth , tileHeight  , null);

            }
        }

    }




    private int getIndexTileSet(int data)
    {

        int index = 0;

        for(int i = 0 ; i < tileSetList.size() ; i++)
        {
            TileSet tmp_tileSet = tileSetList.get(i);
            if(data >= tmp_tileSet.getFirstID() && data <= tmp_tileSet.getLastID())
            {
                index = i;
                break;
            }
        }
        return index;
    }

    private void parseLayerData(int [][] arrayOfIndexedTile)
    {
        this.tileLayerData = new Tile[numRows][numCols];

        for(int i = 0 ; i < numRows ; i++)
        {
            for(int j = 0 ; j < numCols ; j++)
            {
                if(arrayOfIndexedTile[i][j] == 0) continue;

                int data = arrayOfIndexedTile[i][j];
                int k = getIndexTileSet(data);
                TileSet ts = tileSetList.get(k);

                data = data + ts.getNumTiles() - ts.getLastID();

                int tileRow = data / ts.getNumCols();
                int tileCol = data - tileRow * ts.getNumCols() - 1;

                if(data % ts.getNumCols() == 0) {
                    tileRow--;
                    tileCol = ts.getNumCols() - 1;
                }


                tileLayerData[i][j] =  new Tile(ts.getTileSetSprite().getSubimage(tileCol * ts.getTileWidth(), tileRow * ts.getTileHeight() ,
                        ts.getTileWidth()  , ts.getTileHeight()));

            }
        }
    }



}
