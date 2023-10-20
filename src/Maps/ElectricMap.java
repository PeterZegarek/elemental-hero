package Maps;

import java.util.ArrayList;

import Enemies.VerticalElectricity;
import EnhancedMapTiles.ElectricShooter;
import EnhancedMapTiles.LightningCloud;
import Level.*;
import NPCs.UnknownTraveler;
import Tilesets.ElectricTileset;
import Utils.Point;

public class ElectricMap extends Map{

    public ElectricMap(){

        super("ElectricMap.txt", new ElectricTileset()); 
        this.playerStartPosition = getMapTile(0, 22).getLocation();

    }

    @Override
    public ArrayList<Enemy> loadEnemies(){
        ArrayList<Enemy> enemies = new ArrayList<>();

        // VerticalElectricity testElectricity = new VerticalElectricity(getMapTile(3, 11).getLocation());
        // enemies.add(testElectricity);


        return enemies;
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        LightningCloud lightningCloud1 = new LightningCloud(getMapTile(4,22).getLocation());
        enhancedMapTiles.add(lightningCloud1);

        LightningCloud lightningCloud2 = new LightningCloud(getMapTile(9,12).getLocation());
        enhancedMapTiles.add(lightningCloud2);

        LightningCloud lightningCloud3 = new LightningCloud(getMapTile(11,13).getLocation());
        enhancedMapTiles.add(lightningCloud3);

        LightningCloud lightningCloud4 = new LightningCloud(getMapTile(24,15).getLocation());
        enhancedMapTiles.add(lightningCloud4);

        LightningCloud lightningCloud5 = new LightningCloud(getMapTile(20,9).getLocation());
        enhancedMapTiles.add(lightningCloud5);

        ElectricShooter electricShooter1 = new ElectricShooter(getMapTile(14, 22).getLocation());
        enhancedMapTiles.add(electricShooter1);

        ElectricShooter electricShooter2 = new ElectricShooter(getMapTile(14, 19).getLocation());
        enhancedMapTiles.add(electricShooter2);


        return enhancedMapTiles;

    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        UnknownTraveler UnknownTraveler = new UnknownTraveler(getMapTile(10, 11).getLocation().subtractY(42), "STANDING_LEFT");
        npcs.add(UnknownTraveler);

        return npcs;
    }
}
