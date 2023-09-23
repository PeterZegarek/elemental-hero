package Maps;

import Level.*;
import Tilesets.CommonTileset;
import Utils.Point;

public class FireMap extends Map{
    
    public FireMap(){

        super("FireMap.txt", new CommonTileset());
        this.playerStartPosition = new Point(1, 11);

    }

}
