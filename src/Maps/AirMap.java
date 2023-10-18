package Maps;

import java.util.ArrayList;
import java.util.Random;

import Engine.ImageLoader;
import EnhancedMapTiles.EndLevelBox;
import EnhancedMapTiles.MovingCloud;
import EnhancedMapTiles.Tornado;
import GameObject.Rectangle;
import Level.*;
import Tilesets.AirTileset;
import Utils.Direction;


public class AirMap extends Map{

    public AirMap(){
        super("AirMap.txt", new AirTileset()); 
        this.playerStartPosition = getMapTile(1, 44).getLocation();

    }  

    @Override
    public ArrayList<Enemy> loadEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

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
            random.nextFloat()*2+2,
            Direction.RIGHT
        );
        enhancedMapTiles.add(tornado1);

        Tornado tornado2 = new Tornado(
            getMapTile(21, 34).getLocation(), 
            getMapTile(28, 34).getLocation(),
            random.nextFloat()*2+2,
            Direction.RIGHT
        );
        enhancedMapTiles.add(tornado2);

        Tornado tornado3 = new Tornado(
            getMapTile(10, 33).getLocation(), 
            getMapTile(17, 33).getLocation(),
            random.nextFloat()*2+3, //Higher floor than other two by 1
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

        EndLevelBox endLevelBox = new EndLevelBox(getMapTile(1, 14).getLocation());
        enhancedMapTiles.add(endLevelBox);

        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        return npcs;
    }
}
