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
        this.playerStartPosition = getMapTile(1,3).getLocation();

    }
    @Override
    public ArrayList<Enemy> loadEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

        SlimeEnemy SlimeEnemy1 = new SlimeEnemy(getMapTile(4, 6).getLocation(), getMapTile(9, 6).getLocation(), Direction.LEFT);
        enemies.add(SlimeEnemy1);

        SlimeEnemy SlimeEnemy2 = new SlimeEnemy(getMapTile(3, 38).getLocation(), getMapTile(23, 38).getLocation(), Direction.LEFT);
        enemies.add(SlimeEnemy2);

        SlimeEnemy SlimeEnemy3 = new SlimeEnemy(getMapTile(5, 36).getLocation(), getMapTile(19, 36).getLocation(), Direction.LEFT, "FLIP");
        enemies.add(SlimeEnemy3);

        SlimeEnemy SlimeEnemy4 = new SlimeEnemy(getMapTile(40, 18).getLocation(), getMapTile(47, 18).getLocation(), Direction.LEFT);
        enemies.add(SlimeEnemy4);

        SlimeEnemy SlimeEnemy5 = new SlimeEnemy(getMapTile(32, 11).getLocation(), getMapTile(39, 11).getLocation(), Direction.LEFT, "FLIP");
        enemies.add(SlimeEnemy5);

        SlimeEnemy SlimeEnemy6 = new SlimeEnemy(getMapTile(44, 16).getLocation(), getMapTile(50, 16).getLocation(), Direction.LEFT, "FLIP");
        enemies.add(SlimeEnemy6);

        SlimeEnemy SlimeEnemy7 = new SlimeEnemy(getMapTile(41, 33).getLocation(), getMapTile(49, 33).getLocation(), Direction.RIGHT);
        enemies.add(SlimeEnemy7);

        SlimeEnemy SlimeEnemy8 = new SlimeEnemy(getMapTile(51, 33).getLocation(), getMapTile(63, 33).getLocation(), Direction.RIGHT);
        enemies.add(SlimeEnemy8);

        SlimeEnemy SlimeEnemy10 = new SlimeEnemy(getMapTile(44, 29).getLocation(), getMapTile(51, 29).getLocation(), Direction.LEFT, "FLIP");
        enemies.add(SlimeEnemy10);

        WaterWizardEnemy wizard2 = new WaterWizardEnemy(getMapTile(4, 30).getLocation(), getMapTile(13, 30).getLocation(), Direction.LEFT);
        enemies.add(wizard2);

        WaterWizardEnemy wizard3 = new WaterWizardEnemy(getMapTile(6, 24).getLocation(), getMapTile(14, 24).getLocation(), Direction.LEFT);
        enemies.add(wizard3);

        WaterWizardEnemy wizard5 = new WaterWizardEnemy(getMapTile(22, 15).getLocation(), getMapTile(29, 15).getLocation(), Direction.LEFT);
        enemies.add(wizard5);

        WaterWizardEnemy wizard6 = new WaterWizardEnemy(getMapTile(26, 26).getLocation(), getMapTile(29, 26).getLocation(), Direction.LEFT);
        enemies.add(wizard6);

        WaterWizardEnemy wizard7 = new WaterWizardEnemy(getMapTile(3, 16).getLocation(), getMapTile(7, 16).getLocation(), Direction.LEFT);
        enemies.add(wizard7);

        WaterWizardEnemy wizard9 = new WaterWizardEnemy(getMapTile(41, 23).getLocation(), getMapTile(47, 23).getLocation(), Direction.RIGHT);
        enemies.add(wizard9);

        WaterWizardEnemy wizard10 = new WaterWizardEnemy(getMapTile(42, 16).getLocation(), getMapTile(48, 16).getLocation(), Direction.RIGHT);
        enemies.add(wizard10);

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
}
