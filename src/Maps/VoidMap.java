package Maps;

import Level.*;
import Tilesets.EarthTileset; // change to VoidTileset
import Utils.Point;

public class VoidMap extends Map{

    public VoidMap(){

        super("VoidMap.txt", new EarthTileset()); // change to VoidTileset
        this.playerStartPosition = new Point(1, 11);

    }

}
