package Maps;

import java.util.ArrayList;

import Enemies.SlimeEnemy;
import Enemies.WaterBoss;
import Enemies.WaterWizardEnemy;
import EnhancedMapTiles.EndLevelBox;
import Level.*;
import NPCs.UnknownTraveler;
import Tilesets.WaterTileset; // change to WaterTileset
import Utils.Direction;
import Utils.Point;

public class WaterMap extends Map{

    public WaterMap(){

        super("WaterMap.txt", new WaterTileset()); // change to WaterTileset
        this.playerStartPosition = new Point(5, 19);

    }
    @Override
    public ArrayList<Enemy> loadEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

        SlimeEnemy SlimeEnemy1 = new SlimeEnemy(getMapTile(3, 6).getLocation(), getMapTile(9, 6).getLocation(), Direction.LEFT);
        enemies.add(SlimeEnemy1);

        SlimeEnemy SlimeEnemy2 = new SlimeEnemy(getMapTile(2, 33).getLocation(), getMapTile(9, 33).getLocation(), Direction.LEFT);
        enemies.add(SlimeEnemy2);

        SlimeEnemy SlimeEnemy3 = new SlimeEnemy(getMapTile(15, 33).getLocation(), getMapTile(29, 33).getLocation(), Direction.LEFT);
        enemies.add(SlimeEnemy3);

        SlimeEnemy SlimeEnemy4 = new SlimeEnemy(getMapTile(29, 30).getLocation(), getMapTile(40, 30).getLocation(), Direction.LEFT, "FLIP");
        enemies.add(SlimeEnemy4);

        SlimeEnemy SlimeEnemy5 = new SlimeEnemy(getMapTile(32, 11).getLocation(), getMapTile(39, 11).getLocation(), Direction.LEFT, "FLIP");
        enemies.add(SlimeEnemy5);

        SlimeEnemy SlimeEnemy6 = new SlimeEnemy(getMapTile(29, 32).getLocation(), getMapTile(40, 32).getLocation(), Direction.RIGHT);
        enemies.add(SlimeEnemy6);

        SlimeEnemy SlimeEnemy7 = new SlimeEnemy(getMapTile(41, 33).getLocation(), getMapTile(49, 33).getLocation(), Direction.RIGHT);
        enemies.add(SlimeEnemy7);

        SlimeEnemy SlimeEnemy8 = new SlimeEnemy(getMapTile(51, 33).getLocation(), getMapTile(63, 33).getLocation(), Direction.RIGHT);
        enemies.add(SlimeEnemy8);

        SlimeEnemy SlimeEnemy9 = new SlimeEnemy(getMapTile(55, 6).getLocation(), getMapTile(60, 6).getLocation(), Direction.RIGHT);
        enemies.add(SlimeEnemy9);

        SlimeEnemy SlimeEnemy10 = new SlimeEnemy(getMapTile(60, 10).getLocation(), getMapTile(64, 10).getLocation(), Direction.LEFT, "FLIP");
        enemies.add(SlimeEnemy10);

        SlimeEnemy SlimeEnemy11 = new SlimeEnemy(getMapTile(60, 14).getLocation(), getMapTile(64, 14).getLocation(), Direction.RIGHT);
        enemies.add(SlimeEnemy11);

        WaterWizardEnemy wizard1 = new WaterWizardEnemy(getMapTile(14, 19).getLocation(), getMapTile(25, 19).getLocation(), Direction.LEFT);
        enemies.add(wizard1);

        WaterWizardEnemy wizard2 = new WaterWizardEnemy(getMapTile(3, 27).getLocation(), getMapTile(9, 27).getLocation(), Direction.LEFT);
        enemies.add(wizard2);

        WaterWizardEnemy wizard3 = new WaterWizardEnemy(getMapTile(6, 24).getLocation(), getMapTile(12, 24).getLocation(), Direction.LEFT);
        enemies.add(wizard3);

        WaterWizardEnemy wizard4 = new WaterWizardEnemy(getMapTile(17, 28).getLocation(), getMapTile(25, 28).getLocation(), Direction.RIGHT);
        enemies.add(wizard4);

        WaterWizardEnemy wizard5 = new WaterWizardEnemy(getMapTile(24, 13).getLocation(), getMapTile(29, 13).getLocation(), Direction.LEFT);
        enemies.add(wizard5);

        WaterWizardEnemy wizard6 = new WaterWizardEnemy(getMapTile(21, 3).getLocation(), getMapTile(29, 3).getLocation(), Direction.LEFT);
        enemies.add(wizard6);

        WaterWizardEnemy wizard7 = new WaterWizardEnemy(getMapTile(3, 16).getLocation(), getMapTile(10, 16).getLocation(), Direction.LEFT);
        enemies.add(wizard7);

        WaterWizardEnemy wizard8 = new WaterWizardEnemy(getMapTile(15, 23).getLocation(), getMapTile(26, 23).getLocation(), Direction.RIGHT);
        enemies.add(wizard8);

        WaterWizardEnemy wizard9 = new WaterWizardEnemy(getMapTile(40, 24).getLocation(), getMapTile(52, 24).getLocation(), Direction.RIGHT);
        enemies.add(wizard9);

        WaterWizardEnemy wizard10 = new WaterWizardEnemy(getMapTile(43, 18).getLocation(), getMapTile(51, 18).getLocation(), Direction.RIGHT);
        enemies.add(wizard10);

        WaterWizardEnemy wizard11 = new WaterWizardEnemy(getMapTile(44, 7).getLocation(), getMapTile(52, 7).getLocation(), Direction.RIGHT);
        enemies.add(wizard11);

        WaterBoss waterBoss = new WaterBoss(getMapTile(60, 29).getLocation().subtractX(70).subtractY(145), getMapTile(60, 29).getLocation().subtractX(70).subtractY(145), Direction.LEFT);
        enemies.add(waterBoss);


        return enemies;

    }
    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        //Added Argument to control sprite direction (TO FLIP, REPLACE "RIGHT" with "LEFT")
        UnknownTraveler UnknownTraveler = new UnknownTraveler(getMapTile(3, 11).getLocation().subtractY(42), "STANDING_RIGHT"); 
        npcs.add(UnknownTraveler);

        return npcs;
    }
     @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        EndLevelBox endLevelBox = new EndLevelBox(getMapTile(69, 5).getLocation());
        enhancedMapTiles.add(endLevelBox);

        return enhancedMapTiles;
    }
}
