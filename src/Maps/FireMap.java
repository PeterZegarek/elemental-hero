package Maps;

import Level.*;
import Tilesets.EarthTileset; // change to FireTileset
import Utils.Point;

public class FireMap extends Map{
    
    public FireMap(){

        super("FireMap.txt", new EarthTileset()); // change to FireTileset
        this.playerStartPosition = new Point(1, 11);

    }

}
