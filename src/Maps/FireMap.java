package Maps;

import java.util.ArrayList;

import Enemies.FireDinosaurEnemy;
import Enemies.Firewisp;
import Enemies.InvisibleEnemy;
import Engine.ImageLoader;
import EnhancedMapTiles.EndLevelBox;
import EnhancedMapTiles.HorizontalMovingPlatform;
import GameObject.Rectangle;
import Level.*;
import NPCs.UnknownTraveler;
import Tilesets.FireTileset; // change to FireTileset
import Utils.Direction;

public class FireMap extends Map{
    
    public FireMap(){

        super("FireMap.txt", new FireTileset()); // change to FireTileset
        this.playerStartPosition = getMapTile(2, 20).getLocation();
    }
    
    @Override
    public ArrayList<Enemy> loadEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

        FireDinosaurEnemy fireDinosaurEnemy1 = new FireDinosaurEnemy(getMapTile(49, 12).getLocation().addY(7), getMapTile(49,12).getLocation().addY(7), Direction.LEFT);
        enemies.add(fireDinosaurEnemy1);

        FireDinosaurEnemy fireDinosaurEnemy2 = new FireDinosaurEnemy(getMapTile(49, 16).getLocation().addY(7), getMapTile(49, 16).getLocation().addY(7), Direction.LEFT);
        enemies.add(fireDinosaurEnemy2);

        Firewisp firewisp1 = new Firewisp(getMapTile(11, 23).getLocation().addY(5), getMapTile(21,23).getLocation().addY(5), Direction.LEFT);
        enemies.add(firewisp1);

        Firewisp firewisp2 = new Firewisp(getMapTile(24, 23).getLocation().addY(5), getMapTile(33,23).getLocation().addY(5), Direction.LEFT);
        enemies.add(firewisp2);

        for (int x = 10; x <= 31; x++) {
            InvisibleEnemy invisEnemy = new InvisibleEnemy(getMapTile(x, 25).getLocation());
            enemies.add(invisEnemy);
        }

        return enemies;
    }

    

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        HorizontalMovingPlatform hmp1 = new HorizontalMovingPlatform(
                ImageLoader.load("16x16FloatingRock.png"),
                getMapTile(10, 24).getLocation(),
                getMapTile(20, 24).getLocation(),
                TileType.JUMP_THROUGH_PLATFORM,
                5,
                new Rectangle(1, 6,14,8),
                Direction.RIGHT
        );
        enhancedMapTiles.add(hmp1);

        HorizontalMovingPlatform hmp2 = new HorizontalMovingPlatform(
                ImageLoader.load("16x16FloatingRock.png"),
                getMapTile(21, 24).getLocation(),
                getMapTile(33, 24).getLocation(),
                TileType.JUMP_THROUGH_PLATFORM,
                5,
                new Rectangle(1, 6,14,8),
                Direction.LEFT
        );
        enhancedMapTiles.add(hmp2);

        EndLevelBox endLevelBox = new EndLevelBox(getMapTile(0, 5).getLocation());
        enhancedMapTiles.add(endLevelBox);

        return enhancedMapTiles;
    }
    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        //Added Argument to control sprite direction (TO FLIP, REPLACE "LEFT" with "RIGHT")
        UnknownTraveler UnknownTraveler = new UnknownTraveler(getMapTile(0, 20).getLocation().subtractY(42), "STANDING_RIGHT");       
        npcs.add(UnknownTraveler);

        return npcs;
    }
     
}
