package Maps;

import java.util.ArrayList;

import Level.*;
import NPCs.UnknownTraveler;
import Tilesets.WaterTileset; // change to WaterTileset
import Utils.Point;

public class WaterMap extends Map{

    public WaterMap(){

        super("WaterMap.txt", new WaterTileset()); // change to WaterTileset
        this.playerStartPosition = new Point(5, 6);

    }
    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        UnknownTraveler UnknownTraveler = new UnknownTraveler(getMapTile(3, 11).getLocation().subtractY(42));
        npcs.add(UnknownTraveler);

        return npcs;
    }
}
