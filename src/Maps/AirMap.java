package Maps;

import Level.*;
import Tilesets.CommonTileset;
import Utils.Point;

public class AirMap extends Map{

    public AirMap(){

        super("AirMap.txt", new CommonTileset());
        this.playerStartPosition = new Point(1, 11);

    }  
}
