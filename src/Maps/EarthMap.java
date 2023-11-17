package Maps;

import Enemies.BugEnemy;
import Enemies.DinosaurEnemy;
import Enemies.KrakenEnemy;
import Enemies.SkeletonBoss;
import Enemies.TreeEnemy;
import Enemies.WaterBoss;
import Engine.ImageLoader;
import EnhancedMapTiles.EndLevelBox;
import EnhancedMapTiles.HorizontalMovingPlatform;
import GameObject.Rectangle;
import Level.*;
import NPCs.UnknownTraveler;
import Tilesets.EarthTileset;
import Utils.Direction;

import java.util.ArrayList;

// Represents a test map to be used in a level
public class EarthMap extends Map {

    public EarthMap() {
        super("EarthMap.txt", new EarthTileset());
        // typical is 4, 18
        // to test the skeleton boss, do 47, 25
        this.playerStartPosition = getMapTile(4, 18).getLocation();
    }

    @Override
    public ArrayList<Enemy> loadEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

        BugEnemy bugEnemy1 = new BugEnemy(getMapTile(19, 26).getLocation().subtractY(25), Direction.LEFT);
        enemies.add(bugEnemy1);

        BugEnemy bugEnemy2 = new BugEnemy(getMapTile(28, 27).getLocation().subtractY(25), Direction.LEFT);
        enemies.add(bugEnemy2);

        BugEnemy bugEnemy3 = new BugEnemy(getMapTile(16, 17).getLocation().subtractY(25), Direction.LEFT);
        enemies.add(bugEnemy3);

        BugEnemy bugEnemy4 = new BugEnemy(getMapTile(16, 17).getLocation().subtractY(25), Direction.RIGHT);
        enemies.add(bugEnemy4);

        DinosaurEnemy dinosaurEnemy1 = new DinosaurEnemy(getMapTile(10, 25).getLocation().addY(2), getMapTile(13, 25).getLocation().addY(2), Direction.LEFT);
        enemies.add(dinosaurEnemy1);

        DinosaurEnemy dinosaurEnemy2 = new DinosaurEnemy(getMapTile(31, 26).getLocation().addY(2), getMapTile(36, 26).getLocation().addY(2), Direction.LEFT);
        enemies.add(dinosaurEnemy2);

        DinosaurEnemy dinosaurEnemy3 = new DinosaurEnemy(getMapTile(32, 17).getLocation().addY(2), getMapTile(40, 13).getLocation().addY(2), Direction.LEFT);
        enemies.add(dinosaurEnemy3);

        TreeEnemy treeEnemy1 = new TreeEnemy(getMapTile(11, 3).getLocation().addY(-12), getMapTile(15, 3).getLocation().addY(-12), Direction.LEFT);
        enemies.add(treeEnemy1);

        TreeEnemy treeEnemy2 = new TreeEnemy(getMapTile(40, 3).getLocation().addY(-12), getMapTile(43, 3).getLocation().addY(-12), Direction.LEFT);
        enemies.add(treeEnemy2);

        TreeEnemy treeEnemy3 = new TreeEnemy(getMapTile(30, 5).getLocation().addY(-12), getMapTile(34, 5).getLocation().addY(-12), Direction.LEFT);
        enemies.add(treeEnemy3);

        SkeletonBoss skeletonBoss = new SkeletonBoss(getMapTile(58,24).getLocation(), Direction.LEFT);
        enemies.add(skeletonBoss);


        return enemies;
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        HorizontalMovingPlatform hmp = new HorizontalMovingPlatform(
                ImageLoader.load("GreenPlatform.png"),
                getMapTile(24, 6).getLocation(),
                getMapTile(27, 6).getLocation(),
                TileType.JUMP_THROUGH_PLATFORM,
                3,
                new Rectangle(0, 6,16,4),
                Direction.RIGHT
        );
        enhancedMapTiles.add(hmp);

        HorizontalMovingPlatform hmp2 = new HorizontalMovingPlatform(
                ImageLoader.load("GreenPlatform.png"),
                getMapTile(47, 6).getLocation(),
                getMapTile(50, 6).getLocation(),
                TileType.JUMP_THROUGH_PLATFORM,
                3,
                new Rectangle(0, 6,16,4),
                Direction.RIGHT
        );
        enhancedMapTiles.add(hmp2);

        HorizontalMovingPlatform hmp3 = new HorizontalMovingPlatform(
                ImageLoader.load("GreenPlatform.png"),
                getMapTile(49, 10).getLocation(),
                getMapTile(53, 10).getLocation(),
                TileType.JUMP_THROUGH_PLATFORM,
                3,
                new Rectangle(0, 6,16,4),
                Direction.RIGHT
        );
        enhancedMapTiles.add(hmp3);

        EndLevelBox endLevelBox = new EndLevelBox(getMapTile(64, 9).getLocation());
        enhancedMapTiles.add(endLevelBox);

        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        //Added Argument to control sprite direction (TO FLIP, REPLACE "LEFT" with "RIGHT")
        UnknownTraveler UnknownTraveler = new UnknownTraveler(getMapTile(11, 15).getLocation().subtractY(23).subtractX(15), "STANDING_LEFT");
        npcs.add(UnknownTraveler);

        return npcs;
    }
}
