package Maps;

import Level.*;
import Tilesets.EarthTileset; // change to WaterTileset
import Utils.Point;

public class WaterMap extends Map{

    public WaterMap(){

        super("WaterMap.txt", new EarthTileset()); // change to WaterTileset
        this.playerStartPosition = new Point(1, 11);

    }

}
