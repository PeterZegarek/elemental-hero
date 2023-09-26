package Maps;

import Level.*;
import Tilesets.EarthTileset; //change to AirTileset
import Utils.Point;

public class AirMap extends Map{

    public AirMap(){

        super("AirMap.txt", new EarthTileset()); // change to AirTileset
        this.playerStartPosition = new Point(1, 11);

    }  
}
