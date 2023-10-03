package Maps;

import java.util.ArrayList;

import Enemies.DinosaurEnemy;
import Enemies.Firewisp;
import Engine.ImageLoader;
import EnhancedMapTiles.EndLevelBox;
import EnhancedMapTiles.HorizontalMovingPlatform;
import GameObject.Rectangle;
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

        Firewisp firewisp1 = new Firewisp(getMapTile(10, 12).getLocation().addY(2), getMapTile(10,8).getLocation().addY(2), Direction.LEFT);
        enemies.add(firewisp1);

        return enemies;
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        EndLevelBox endLevelBox = new EndLevelBox(getMapTile(22, 11).getLocation());
        enhancedMapTiles.add(endLevelBox);

        return enhancedMapTiles;
    }
    
}
