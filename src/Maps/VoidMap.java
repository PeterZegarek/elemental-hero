package Maps;

import java.util.ArrayList;

import Enemies.FinalBoss;
import Enemies.InvisibleEnemy;
import Level.*;
import Tilesets.VoidTileset;
import Utils.Direction;

public class VoidMap extends Map{

    public VoidMap(){

        super("VoidMap.txt", new VoidTileset()); 
        this.playerStartPosition = getMapTile(0, 50).getLocation();

    }
    @Override
    public ArrayList<Enemy> loadEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();

        FinalBoss finalBoss = new FinalBoss(getMapTile(22, 51).getLocation().subtractY(410).subtractX(160),getMapTile(22, 51).getLocation().subtractX(160).subtractY(410), Direction.LEFT);
        enemies.add(finalBoss);



        return enemies;


    }

}
