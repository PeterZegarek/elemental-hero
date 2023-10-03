package Maps;

import java.util.ArrayList;

import Enemies.DinosaurEnemy;
import Enemies.FireDinosaurEnemy;
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

        FireDinosaurEnemy fireDinosaurEnemy1 = new FireDinosaurEnemy(getMapTile(10, 8).getLocation().addY(2), getMapTile(10,8).getLocation().addY(2), Direction.LEFT);
        enemies.add(fireDinosaurEnemy1);

        FireDinosaurEnemy fireDinosaurEnemy2 = new FireDinosaurEnemy(getMapTile(10, 14).getLocation().addY(2), getMapTile(10, 14).getLocation().addY(2), Direction.LEFT);
        enemies.add(fireDinosaurEnemy2);


        Firewisp firewisp1 = new Firewisp(getMapTile(5, 6).getLocation().addY(2), getMapTile(13,8).getLocation().addY(2), Direction.LEFT);
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
