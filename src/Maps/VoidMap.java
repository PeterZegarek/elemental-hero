package Maps;

import java.util.ArrayList;

import Enemies.FinalBoss;
import Enemies.InvisibleEnemy;
import Engine.ImageLoader;
import EnhancedMapTiles.EndLevelBox;
import EnhancedMapTiles.FireCannon;
import EnhancedMapTiles.HorizontalMovingPlatform;
import EnhancedMapTiles.LightningCloud;
import GameObject.Rectangle;
import Level.*;
import Tilesets.VoidTileset;
import Utils.Direction;

public class VoidMap extends Map{

    FinalBoss finalBoss = null;
    ArrayList<LightningCloud> lightningClouds;

    public VoidMap(){

        super("VoidMap.txt", new VoidTileset()); 
        this.playerStartPosition = getMapTile(1, 13).getLocation();
    }
    @Override
    public ArrayList<Enemy> loadEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

        finalBoss = new FinalBoss(getMapTile(22, 13).getLocation().subtractY(410).subtractX(160),getMapTile(22, 13).getLocation().subtractX(160).subtractY(410), Direction.LEFT);
        enemies.add(finalBoss);



        return enemies;


    }

    // @Override
    // public ArrayList<EnhancedMapTile> loadEnhancedMapTiles(){
    //     ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();
    //     lightningClouds = new ArrayList<LightningCloud>(10);
    //     LightningCloud lightningCloud = new LightningCloud(getMapTile(17, 1).getLocation());
    //     enhancedMapTiles.add(lightningCloud);
    //     lightningClouds.add(lightningCloud);
    //     return enhancedMapTiles;
    // }

    // In this function, add all the enhanced map tiles as a list of listeners to within the boss class
    // the boss will store these listeners and then call functions on them, this will be similar to the listener manager but within the boss
    // @Override
    // protected void postLoad() {

    //     for (LightningCloud lightningCloud: lightningClouds){
    //         finalBoss.addToArrayList(lightningCloud);
    //     }
        
    // }
}
