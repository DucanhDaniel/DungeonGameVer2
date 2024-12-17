package map;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tile.TileLayer;
import tile.TileSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MapParser {

    public static void loadMap(String id , String filepath)
    {
        parseMap(id , filepath);
    }

    private static void parseMap(String id , String filepath)
    {
        try
        {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(new File(filepath));
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();

            int tilewidth  , tileheight , numrows , numcols ;

            tilewidth = Integer.parseInt(root.getAttribute("tilewidth")) ;
            tileheight = Integer.parseInt(root.getAttribute("tileheight")) ;
            numrows = Integer.parseInt(root.getAttribute("height"));
            numcols = Integer.parseInt(root.getAttribute("width"));

            GameMap mp = new GameMap(tilewidth * numcols , tileheight * numrows);

            ArrayList<TileSet> tilesetlist = new ArrayList<>();
            NodeList list = doc.getElementsByTagName("tileset");
            for(int i = 0; i < list.getLength() ; i++)
            {
                Element eElement = (Element) list.item(i);
                tilesetlist.add(getTileSet(eElement));
            }

            list = doc.getElementsByTagName("layer");

            int layers = list.getLength();

            for(int i = 0 ; i < layers ; i++)
            {
                Element eElement = (Element) list.item(i);
                String data = eElement.getElementsByTagName("data").item(0).getTextContent();
                mp.map.add(parseTileLayer(data , numrows , numcols , tilesetlist));
            }

            MapManager.appendGameMap(id , mp);

        } catch(Exception e)
        {
            System.out.println("Cannot parse map from path: " + filepath);
            e.printStackTrace();
        }
    }

    private static TileLayer parseTileLayer(String data , int numRows , int numCols , ArrayList<TileSet> tilesetlist)
    {
        String[] values = data.trim().split(",");

        int index = 0;

        // Initialize the 2D array
        int[][] array = new int[numRows][numCols];

        // Populate the 2D array
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                array[i][j] = Integer.parseInt(values[index].trim());
                index++;
            }
        }

        return new TileLayer(numRows , numCols , array , tilesetlist);
    }



    private static TileSet getTileSet(Element eElement)
    {
        try {
            return parsePNGfile(eElement);
        }
        catch (NullPointerException e) {
            return parseTSXfile(eElement);
        }
    }


    private static TileSet parsePNGfile(Element eElement) throws NullPointerException
    {
        NodeList imageSourceList = eElement.getElementsByTagName("image");
        Element e = (Element) imageSourceList.item(0);
        String source = e.getAttribute("source");
        String filepath = source.substring(2);

        int tilewidth = Integer.parseInt(eElement.getAttribute("tilewidth"));
        int tileheight = Integer.parseInt(eElement.getAttribute("tilewidth"));

        int tilecount = Integer.parseInt(eElement.getAttribute("tilecount"));
        int firstid = Integer.parseInt(eElement.getAttribute("firstgid"));
        int lastid = firstid + tilecount - 1;

        int numcols = Integer.parseInt(eElement.getAttribute("columns"));
        int numrows = tilecount / numcols;

        return new TileSet(firstid , lastid , tilewidth , tileheight , numrows , numcols , filepath);
    }

    private static TileSet parseTSXfile(Element eElement)
    {
        String tmp_file = eElement.getAttribute("source");
        String filepath = "res_tile" + tmp_file.substring(2);

        TileSet tileSet = null;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(new File(filepath));
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();

            //LẤY RA SỐ Ô VÀ SỐ HÀNG CỘT TRONG FILE TSX
            int tilewidth = Integer.parseInt(root.getAttribute("tilewidth"));
            int tileheight = Integer.parseInt(root.getAttribute("tileheight"));

            int tilecount = Integer.parseInt(root.getAttribute("tilecount"));
            int firstid =  Integer.parseInt(eElement.getAttribute("firstgid"));
            int lastid = firstid + tilecount - 1;

            int numcols = Integer.parseInt(root.getAttribute("columns"));
            int numrows = tilecount / numcols;

            //LẤY RA ĐƯỜNG DẪN CỦA ẢNH TRONG TILESET
            NodeList list_tmp = doc.getElementsByTagName("image");
            Element element_tmp = (Element) list_tmp.item(0);
            //Element element_tmp = (Element) doc.getElementsByTagName("image");
            String tilename = element_tmp.getAttribute("source");

            //LẤY RA NHỮNG Ô CÓ OBJECT VÀ CHO VÀO HASHMAP
            NodeList list = doc.getElementsByTagName("tile");

            HashMap<Integer , Rectangle> object = new HashMap<>();
            for(int i = 0 ; i < list.getLength() ; i++)
            {
                Element element = (Element) list.item(i);
                int tileID = Integer.parseInt(element.getAttribute("id"));

                NodeList tmp = doc.getElementsByTagName("object");
                Element element1 = (Element) tmp.item(0);

                int x = Integer.parseInt(element1.getAttribute("x"));
                int y = Integer.parseInt(element1.getAttribute("y"));
                int width = Integer.parseInt(element1.getAttribute("width"));
                int height = Integer.parseInt(element1.getAttribute("height"));

                object.put(tileID , new Rectangle(x , y , width , height));
            }

            tileSet = new TileSet(firstid , lastid , tilewidth ,tileheight ,numrows , numcols ,  object,tilename);

        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return tileSet;
    }
}
