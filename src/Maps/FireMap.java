package Maps;

import Level.*;
import Tilesets.FireTileset; // change to FireTileset
import Utils.Point;

public class FireMap extends Map{
    
    public FireMap(){

        super("FireMap.txt", new FireTileset()); // change to FireTileset
        this.playerStartPosition = new Point(1, 11);

    }

}
