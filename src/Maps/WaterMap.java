package Maps;

import java.util.ArrayList;

import Enemies.SlimeEnemy;
import Level.*;
import NPCs.UnknownTraveler;
import Tilesets.WaterTileset; // change to WaterTileset
import Utils.Direction;
import Utils.Point;

public class WaterMap extends Map{

    public WaterMap(){

        super("WaterMap.txt", new WaterTileset()); // change to WaterTileset
        this.playerStartPosition = new Point(5, 6);

    }
    @Override
    public ArrayList<Enemy> loadEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

        SlimeEnemy SlimeEnemy1 = new SlimeEnemy(getMapTile(3, 6).getLocation(), getMapTile(9, 6).getLocation(), Direction.LEFT);
        enemies.add(SlimeEnemy1);

        SlimeEnemy SlimeEnemy2 = new SlimeEnemy(getMapTile(2, 33).getLocation(), getMapTile(9, 33).getLocation(), Direction.LEFT);
        enemies.add(SlimeEnemy2);

        SlimeEnemy SlimeEnemy3 = new SlimeEnemy(getMapTile(15, 33).getLocation(), getMapTile(29, 33).getLocation(), Direction.LEFT);
        enemies.add(SlimeEnemy3);

        return enemies;

    }
    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        UnknownTraveler UnknownTraveler = new UnknownTraveler(getMapTile(3, 11).getLocation().subtractY(42));
        npcs.add(UnknownTraveler);

        return npcs;
    }
}
