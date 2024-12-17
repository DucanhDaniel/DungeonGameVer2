package tile;



import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class TileSet {

    final private int firstID;
    final private int lastID;
    final private int tileWidth;
    final private int tileHeight;
    final private int numRows;
    final private int numCols;
    final private int numTiles;

    private String imgPath;

    private BufferedImage tileSetSprite;



    public TileSet(int firstID , int lastID , int tileWidth ,
                   int tileHeight , int numRows ,
                   int numCols , String imgPath)
    {
        this.firstID = firstID;
        this.lastID = lastID;
        this.tileWidth = tileWidth ;
        this.tileHeight = tileHeight;
        this.numRows = numRows;
        this.numCols = numCols;
        this.numTiles = lastID - firstID + 1;
        this.imgPath = imgPath;

        loadTileSheet(imgPath);
    };

    public TileSet(int firstID , int lastID , int tileWidth ,
                   int tileHeight , int numRows ,
                   int numCols , HashMap<Integer , Rectangle> objects ,String imgPath)
    {
        this.firstID = firstID;
        this.lastID = lastID;
        this.tileWidth = tileWidth ;
        this.tileHeight = tileHeight;
        this.numRows = numRows;
        this.numCols = numCols;
        this.numTiles = lastID - firstID + 1;
        this.imgPath = imgPath;

        loadTileSheet(imgPath);
    };

    private void loadTileSheet(String imgPath)
    {
        tileSetSprite = null;
        try
        {
            tileSetSprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imgPath)));
        } catch(Exception e)
        {
            System.out.println("Cannot find path: " + imgPath);
            e.printStackTrace();
        }
    }



    public int getFirstID(){return firstID;};
    public int getLastID(){return  lastID;};
    public int getNumRows(){return numRows;};
    public int getNumCols(){return numCols;};
    public int getNumTiles(){return numTiles;};

    public String getImgPath(){return imgPath;};
    public BufferedImage getTileSetSprite(){return tileSetSprite;};

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }
}
