package Maps;

import java.util.ArrayList;

import Enemies.DinosaurEnemy;
import Level.*;
import Tilesets.FireTileset; // change to FireTileset
import Utils.Direction;
import Utils.Point;

public class FireMap extends Map{
    
    public FireMap(){

        super("FireMap.txt", new FireTileset()); // change to FireTileset
        this.playerStartPosition = new Point(1, 11);
    }
    
    @Override
    public ArrayList<Enemy> loadEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

        DinosaurEnemy dinosaurEnemy1 = new DinosaurEnemy(getMapTile(10, 8).getLocation().addY(2), getMapTile(10,8).getLocation().addY(2), Direction.LEFT);
        enemies.add(dinosaurEnemy1);

        DinosaurEnemy dinosaurEnemy2 = new DinosaurEnemy(getMapTile(10, 14).getLocation().addY(2), getMapTile(10, 14).getLocation().addY(2), Direction.LEFT);
        enemies.add(dinosaurEnemy2);

        return enemies;
    }
    
}
