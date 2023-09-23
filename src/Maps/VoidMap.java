package Maps;

import Level.*;
import Tilesets.CommonTileset;
import Utils.Point;

public class VoidMap extends Map{

    public VoidMap(){

        super("VoidMap.txt", new CommonTileset());
        this.playerStartPosition = new Point(1, 11);

    }

}
