package EnhancedMapTiles;

import Builders.FrameBuilder;
import Enemies.VerticalElectricity;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.BossLivesListener;
import Level.EnhancedMapTile;
import Level.MapEntityStatus;
import Level.Player;
import Level.TileType;
import Utils.Point;

import java.util.HashMap;


// This class is for the cloud that shoots out lightning
public class LightningCloud extends EnhancedMapTile implements BossLivesListener {

    protected Point startLocation;
    private int lightningCounter = 0;

    // Default constructor
    public LightningCloud(Point startLocation) {
        super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("LightningCloud.png"), 33, 16),TileType.NOT_PASSABLE);
        isUpdateOffScreen = true;
        this.startLocation = startLocation;
        this.initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void update(Player player) {
        lightningCounter++;
        // This timing helps it spawn on animation 4 every time. If you change the frame delay you have to change this number too. 
        // 10/18 it takes 150 frames for the first lightning bolt, and 200 for every future lightning bolt.
        if ((lightningCounter % 150 == 0) && (lightningCounter != 0)){
            VerticalElectricity lightning1 = new VerticalElectricity(new Point(this.startLocation.x - 10, this.startLocation.y + 50));
            map.addEnemy(lightning1);
            VerticalElectricity lightning2 = new VerticalElectricity(new Point(this.startLocation.x + 40, this.startLocation.y + 50));
            map.addEnemy(lightning2);
            // This is just to make the math work out.
            lightningCounter = -50;
        }

        super.update(player);

    }

    // This is active if the boss is in the electric phase
    @Override
    public void getBossLives(int bossLives){
        if (bossLives <= 4){
            this.mapEntityStatus = MapEntityStatus.REMOVED;
        }
    }


    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 50)
                        .withScale(3)
                        .withBounds(1, 1, 32, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 0), 50)
                        .withScale(3)
                        .withBounds(1, 1, 32, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(2, 0), 50)
                        .withScale(3)
                        .withBounds(1, 1, 32, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(3, 0), 50)
                        .withScale(3)
                        .withBounds(1, 1, 32, 14)
                        .build()
            });
        }};
    }

}
