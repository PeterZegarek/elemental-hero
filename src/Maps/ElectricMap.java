package Maps;

import java.util.ArrayList;

import Enemies.InvisibleEnemy;
import EnhancedMapTiles.ElectricShooter;
import EnhancedMapTiles.EndLevelBox;
import EnhancedMapTiles.FastLightningCloud;
import EnhancedMapTiles.LightningCloud;
import Level.*;
import NPCs.UnknownTraveler;
import Tilesets.ElectricTileset;

public class ElectricMap extends Map{

    public ElectricMap(){

        super("ElectricMap.txt", new ElectricTileset()); 
        this.playerStartPosition = getMapTile(1, 26).getLocation();

    }

    @Override
    public ArrayList<Enemy> loadEnemies(){
        ArrayList<Enemy> enemies = new ArrayList<>();

        InvisibleEnemy invisEnemy1 = new InvisibleEnemy(getMapTile(8, 28).getLocation());
        enemies.add(invisEnemy1);

        InvisibleEnemy invisEnemy2 = new InvisibleEnemy(getMapTile(9, 28).getLocation());
        enemies.add(invisEnemy2);

        InvisibleEnemy invisEnemy3 = new InvisibleEnemy(getMapTile(10, 28).getLocation());
        enemies.add(invisEnemy3);

        InvisibleEnemy invisEnemy4 = new InvisibleEnemy(getMapTile(11, 28).getLocation());
        enemies.add(invisEnemy4);

        for (int x = 18; x <= 41; x++) {
            InvisibleEnemy invisEnemy = new InvisibleEnemy(getMapTile(x, 28).getLocation());
            enemies.add(invisEnemy);
        }


        return enemies;
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        // starting cloud
        LightningCloud lightningCloud1 = new LightningCloud(getMapTile(4,22).getLocation());
        enhancedMapTiles.add(lightningCloud1);

        // Two clouds on the slope running up and left
        LightningCloud lightningCloud2 = new LightningCloud(getMapTile(9,12).getLocation());
        enhancedMapTiles.add(lightningCloud2);

        // Sky clouds that are above you / you jump on
        LightningCloud lightningCloud3 = new LightningCloud(getMapTile(11,13).getLocation());
        enhancedMapTiles.add(lightningCloud3);

        LightningCloud lightningCloud4 = new LightningCloud(getMapTile(24,15).getLocation());
        enhancedMapTiles.add(lightningCloud4);

        //FastLightningCloud lightningCloud5 = new FastLightningCloud(getMapTile(20,9).getLocation());
        //enhancedMapTiles.add(lightningCloud5);
        
        LightningCloud lightningCloud6 = new LightningCloud(getMapTile(31,14).getLocation());
        enhancedMapTiles.add(lightningCloud6);

        //FastLightningCloud lightningCloud7 = new FastLightningCloud(getMapTile(27,9).getLocation());
        //enhancedMapTiles.add(lightningCloud7);

        FastLightningCloud lightningCloud8 = new FastLightningCloud(getMapTile(37,9).getLocation());
        enhancedMapTiles.add(lightningCloud8);

        FastLightningCloud lightningCloud9 = new FastLightningCloud(getMapTile(20,16).getLocation());
        enhancedMapTiles.add(lightningCloud9);

        // These are in the first area where you have to climb up
        ElectricShooter electricShooter1 = new ElectricShooter(getMapTile(14, 22).getLocation());
        enhancedMapTiles.add(electricShooter1);

        ElectricShooter electricShooter2 = new ElectricShooter(getMapTile(14, 19).getLocation());
        enhancedMapTiles.add(electricShooter2);


        // These are the ones in the chasm where you fall
        ElectricShooter electricShooter3 = new ElectricShooter(getMapTile(43, 15).getLocation());
        enhancedMapTiles.add(electricShooter3);

        ElectricShooter electricShooter4 = new ElectricShooter(getMapTile(43, 18).getLocation());
        enhancedMapTiles.add(electricShooter4);

        ElectricShooter electricShooter5 = new ElectricShooter(getMapTile(43, 21).getLocation());
        enhancedMapTiles.add(electricShooter5);

        ElectricShooter electricShooter6 = new ElectricShooter(getMapTile(43, 24).getLocation());
        enhancedMapTiles.add(electricShooter6);

        EndLevelBox endLevelBox = new EndLevelBox(getMapTile(50, 24).getLocation());
        enhancedMapTiles.add(endLevelBox);


        return enhancedMapTiles;

    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        UnknownTraveler UnknownTraveler = new UnknownTraveler(getMapTile(5, 13).getLocation().subtractY(42), "STANDING_RIGHT");
        npcs.add(UnknownTraveler);

        return npcs;
    }
}
