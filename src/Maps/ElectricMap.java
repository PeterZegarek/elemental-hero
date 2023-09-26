package Maps;

import Level.*;
import Tilesets.EarthTileset; // change to ElectricTileset
import Utils.Point;

public class ElectricMap extends Map{

    public ElectricMap(){

        super("ElectricMap.txt", new EarthTileset()); // change to ElectricTileset
        this.playerStartPosition = new Point(1, 11);

    }
}
