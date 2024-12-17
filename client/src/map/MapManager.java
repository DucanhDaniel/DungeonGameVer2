package map;

import java.util.HashMap;
import java.util.Map;

public class MapManager {

    public static Map<String , GameMap> mapRef = new HashMap<>();

    public static void appendGameMap(String id , GameMap mp)
    {
        mapRef.put(id , mp);
    }


    public static GameMap getGameMap(String id)
    {
        return mapRef.get(id);
    }


    public static void dispose()
    {
        mapRef.clear();
    }



}