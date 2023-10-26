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
import Enemies.Lava;

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

        //All of the lava enemy tiles
        Lava lava1 = new Lava(getMapTile(10,25).getLocation());
        enemies.add(lava1);

        Lava lava2 = new Lava(getMapTile(11,25).getLocation());
        enemies.add(lava2);

        Lava lava3 = new Lava(getMapTile(12,25).getLocation());
        enemies.add(lava3);

        Lava lava4 = new Lava(getMapTile(13,25).getLocation());
        enemies.add(lava4);

        Lava lava5 = new Lava(getMapTile(14,25).getLocation());
        enemies.add(lava5);

        Lava lava6 = new Lava(getMapTile(15,25).getLocation());
        enemies.add(lava6);

        Lava lava7 = new Lava(getMapTile(16,25).getLocation());
        enemies.add(lava7);

        Lava lava8 = new Lava(getMapTile(17,25).getLocation());
        enemies.add(lava8);

        Lava lava9 = new Lava(getMapTile(18,25).getLocation());
        enemies.add(lava9);

        Lava lava10 = new Lava(getMapTile(19,25).getLocation());
        enemies.add(lava10);

        Lava lava11 = new Lava(getMapTile(20,25).getLocation());
        enemies.add(lava1);

        Lava lava12 = new Lava(getMapTile(21,25).getLocation());
        enemies.add(lava12);

        Lava lava13 = new Lava(getMapTile(22,25).getLocation());
        enemies.add(lava13);

        Lava lava14 = new Lava(getMapTile(23,25).getLocation());
        enemies.add(lava14);

        Lava lava15 = new Lava(getMapTile(24,25).getLocation());
        enemies.add(lava15);

        Lava lava16 = new Lava(getMapTile(25,25).getLocation());
        enemies.add(lava16);

        Lava lava17 = new Lava(getMapTile(26,25).getLocation());
        enemies.add(lava17);

        Lava lava18 = new Lava(getMapTile(27,25).getLocation());
        enemies.add(lava18);

        Lava lava19 = new Lava(getMapTile(28,25).getLocation());
        enemies.add(lava19);

        Lava lava20 = new Lava(getMapTile(29,25).getLocation());
        enemies.add(lava20);

        Lava lava21 = new Lava(getMapTile(30,25).getLocation());
        enemies.add(lava21);

        Lava lava22 = new Lava(getMapTile(31,25).getLocation());
        enemies.add(lava22);

        Lava lava23 = new Lava(getMapTile(39,23).getLocation());
        enemies.add(lava23);

        Lava lava24 = new Lava(getMapTile(40,23).getLocation());
        enemies.add(lava24);

        Lava lava25 = new Lava(getMapTile(41,23).getLocation());
        enemies.add(lava25);

        Lava lava26 = new Lava(getMapTile(38,23).getLocation());
        enemies.add(lava26);

        // for (int x = 10; x <= 31; x++) {
        //     InvisibleEnemy invisEnemy = new InvisibleEnemy(getMapTile(x, 25).getLocation());
        //     enemies.add(invisEnemy);
        // }

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
