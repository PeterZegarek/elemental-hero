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
import NPCs.UnknownTraveler;
import Tilesets.VoidTileset;
import Utils.Direction;

public class VoidMap extends Map {

    FinalBoss finalBoss = null;
    ArrayList<LightningCloud> lightningClouds;

    public VoidMap() {

        super("VoidMap.txt", new VoidTileset());
        this.playerStartPosition = getMapTile(1, 13).getLocation();
    }

    @Override
    public ArrayList<Enemy> loadEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

        finalBoss = new FinalBoss(getMapTile(22, 13).getLocation().subtractY(410).subtractX(160),
                getMapTile(22, 13).getLocation().subtractX(160).subtractY(410), Direction.LEFT);
        enemies.add(finalBoss);

        return enemies;

    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();
        UnknownTraveler UnknownTraveler = new UnknownTraveler(
                getMapTile(0, 7).getLocation().subtractY(45).subtractX(15), "STANDING_RIGHT");
        npcs.add(UnknownTraveler);

        return npcs;
    }

}
