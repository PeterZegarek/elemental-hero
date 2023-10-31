package Maps;

import Level.*;
import Tilesets.VoidTileset;

public class VoidMap extends Map{

    public VoidMap(){

        super("VoidMap.txt", new VoidTileset()); 
        this.playerStartPosition = getMapTile(0, 50).getLocation();

    }

}
