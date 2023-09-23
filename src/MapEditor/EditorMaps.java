package MapEditor;

import Level.Map;
import Maps.EarthMap;
import Maps.FireMap;
import Maps.WaterMap;
import Maps.ElectricMap;
import Maps.AirMap;
import Maps.VoidMap;
import Maps.TitleScreenMap;

import java.util.ArrayList;

public class EditorMaps {
    public static ArrayList<String> getMapNames() {
        return new ArrayList<String>() {{
            add("TitleScreen");
            add("EarthMap");
            add("FireMap");
            add("WaterMap");
            add("ElectricMap");
            add("AirMap");
            add("VoidMap");
        }};
    }

    public static Map getMapByName(String mapName) {
        switch(mapName) {
            case "TitleScreen":
                return new TitleScreenMap();
            case "EarthMap":
                return new EarthMap();
            case "FireMap":
                return new FireMap();
            case "WaterMap":
                return new WaterMap();
            case "ElectricMap":
                return new ElectricMap();
            case "AirMap":
                return new AirMap();
            case "VoidMap":
                return new VoidMap();
            default:
                throw new RuntimeException("Unrecognized map name");
        }
    }
}
