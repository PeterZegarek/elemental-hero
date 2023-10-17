package Maps;

import Level.*;
import Tilesets.WaterTileset; // change to WaterTileset
import Utils.Point;

public class WaterMap extends Map{

    public WaterMap(){

        super("WaterMap.txt", new WaterTileset()); // change to WaterTileset
        this.playerStartPosition = new Point(1, 11);

    }

}
