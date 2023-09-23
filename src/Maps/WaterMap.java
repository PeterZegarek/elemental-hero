package Maps;

import Level.*;
import Tilesets.CommonTileset;
import Utils.Point;

public class WaterMap extends Map{

    public WaterMap(){

        super("WaterMap.txt", new CommonTileset());
        this.playerStartPosition = new Point(1, 11);

    }

}
