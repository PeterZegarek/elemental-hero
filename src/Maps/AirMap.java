package Maps;

import java.util.ArrayList;
import java.util.Random;

import Enemies.AngelBoss;
import Enemies.CloudEnemy;
import Enemies.InvisibleEnemy;
import Engine.ImageLoader;
import EnhancedMapTiles.EndLevelBox;
import EnhancedMapTiles.MovingCloud;
import EnhancedMapTiles.Tornado;
import GameObject.Rectangle;
import Level.*;
import NPCs.UnknownTraveler;
import Tilesets.AirTileset;
import Utils.Direction;


public class AirMap extends Map{

    public AirMap(){
        super("AirMap.txt", new AirTileset()); 
        this.playerStartPosition = getMapTile(1, 47).getLocation();

    }  

    @Override
    public ArrayList<Enemy> loadEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

        CloudEnemy cloudEnemy1 = new CloudEnemy(
            getMapTile(4, 26).getLocation(), 
            getMapTile(10, 26).getLocation(), 
            Direction.RIGHT,
            2f
            );
        enemies.add(cloudEnemy1);

        CloudEnemy cloudEnemy2 = new CloudEnemy(
            getMapTile(8, 25).getLocation(), 
            getMapTile(13, 25).getLocation(), 
            Direction.RIGHT,
            1f
            );
        enemies.add(cloudEnemy2);

        CloudEnemy cloudEnemy3 = new CloudEnemy(
            getMapTile(12, 24).getLocation(), 
            getMapTile(18, 24).getLocation(), 
            Direction.RIGHT,
            1.5f
            );
        enemies.add(cloudEnemy3);

        CloudEnemy cloudEnemy4 = new CloudEnemy(
            getMapTile(19, 25).getLocation(), 
            getMapTile(25, 25).getLocation(), 
            Direction.RIGHT,
            2.5f
            );
            enemies.add(cloudEnemy4);

        CloudEnemy cloudEnemy5 = new CloudEnemy(
            getMapTile(16, 45).getLocation(), 
            getMapTile(22, 45).getLocation(), 
            Direction.RIGHT,
            2.5f
            );
            enemies.add(cloudEnemy5);

        for (int x = 6; x <= 29; x++) {
            InvisibleEnemy invisEnemy = new InvisibleEnemy(getMapTile(x, 49).getLocation());
            enemies.add(invisEnemy);
        }

        AngelBoss angelBoss = new AngelBoss(getMapTile(8, 12).getLocation(), 
                                            getMapTile(15, 12).getLocation(),
                                            Direction.RIGHT
                                            );
        enemies.add(angelBoss);
        return enemies;
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        //To randomize the tornado's speed
        Random random = new Random();

        Tornado tornado1 = new Tornado(
            getMapTile(17, 34).getLocation(), 
            getMapTile(24, 34).getLocation(),
            random.nextFloat()*2+3,
            Direction.RIGHT
        );
        enhancedMapTiles.add(tornado1);

        Tornado tornado2 = new Tornado(
            getMapTile(21, 34).getLocation(), 
            getMapTile(28, 34).getLocation(),
            random.nextFloat()*2+3,
            Direction.RIGHT
        );
        enhancedMapTiles.add(tornado2);

        Tornado tornado3 = new Tornado(
            getMapTile(10, 33).getLocation(), 
            getMapTile(17, 33).getLocation(),
            random.nextFloat()*2+3, 
            Direction.RIGHT
        );
        enhancedMapTiles.add(tornado3);


        MovingCloud movingCloud =  new MovingCloud(
            ImageLoader.load("MovingCloud.png"), 
            getMapTile(27, 25).getLocation(), 
            getMapTile(27, 19).getLocation(),
            TileType.NOT_PASSABLE, 
            3, 
            new Rectangle(0, 0, 16, 16),
            Direction.UP
            );
        enhancedMapTiles.add(movingCloud);


        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        UnknownTraveler UnknownTraveler = new UnknownTraveler(getMapTile(4, 47).getLocation().subtractY(42), "STANDING_LEFT");
        npcs.add(UnknownTraveler);

        return npcs;
    }
}
