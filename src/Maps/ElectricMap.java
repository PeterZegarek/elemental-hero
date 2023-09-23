package Maps;

import Level.*;
import Tilesets.CommonTileset;
import Utils.Point;

public class ElectricMap extends Map{

    public ElectricMap(){

        super("ElectricMap.txt", new CommonTileset());
        this.playerStartPosition = new Point(1, 11);

    }
}
