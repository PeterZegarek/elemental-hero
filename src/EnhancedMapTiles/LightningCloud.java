package EnhancedMapTiles;

import Builders.FrameBuilder;
import Enemies.VerticalElectricity;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.Player;
import Level.TileType;
import Utils.Point;

import java.util.HashMap;


// This class is for the cloud that shoots out lightning
public class LightningCloud extends EnhancedMapTile {

    private Point startLocation;
    private int lightningCounter = 0;

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
        // 10/17 it takes 225 frames for the first lightning bolt, and 300 for every future lightning bolt.
        if ((lightningCounter % 225 == 0) && (lightningCounter != 0)){
            VerticalElectricity lightning1 = new VerticalElectricity(new Point(this.startLocation.x - 10, this.startLocation.y + 50));
            map.addEnemy(lightning1);
            VerticalElectricity lightning2 = new VerticalElectricity(new Point(this.startLocation.x + 50, this.startLocation.y + 50));
            map.addEnemy(lightning2);
            // This is just to make the math work out.
            lightningCounter = -75;
        }

        super.update(player);

    }


     @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 75)
                        .withScale(3)
                        .withBounds(1, 1, 32, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 0), 75)
                        .withScale(3)
                        .withBounds(1, 1, 32, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(2, 0), 75)
                        .withScale(3)
                        .withBounds(1, 1, 32, 14)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(3, 0), 75)
                        .withScale(3)
                        .withBounds(1, 1, 32, 14)
                        .build()
            });
        }};
    }

}
