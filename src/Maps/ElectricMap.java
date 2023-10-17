package Maps;

import java.util.ArrayList;

import Enemies.VerticalElectricity;
import EnhancedMapTiles.LightningCloud;
import Level.*;
import Tilesets.ElectricTileset;
import Utils.Point;

public class ElectricMap extends Map{

    public ElectricMap(){

        super("ElectricMap.txt", new ElectricTileset()); 
        this.playerStartPosition = getMapTile(0, 14).getLocation();

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

        LightningCloud lightningCloud1 = new LightningCloud(getMapTile(4,11).getLocation());
        enhancedMapTiles.add(lightningCloud1);


        return enhancedMapTiles;

    }
}
