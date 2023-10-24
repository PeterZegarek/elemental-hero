package EnhancedMapTiles;

import java.util.HashMap;

import Builders.FrameBuilder;
import Enemies.HorizontalElectricity;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.Player;
import Level.TileType;
import Utils.Point;


// This class is for the electric shoter that shoots out lightning sideways
public class ElectricShooter extends EnhancedMapTile {

    private Point startLocation;
    private int lightningCounter = 0;

    public ElectricShooter(Point startLocation) {
        super(startLocation.x, startLocation.y+25, new SpriteSheet(ImageLoader.load("ElectricShooter.png"), 4, 16),TileType.PASSABLE);
        isUpdateOffScreen = true;
        this.startLocation = new Point(startLocation.x, startLocation.y + 25);
        this.initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void update(Player player) {
        lightningCounter++;

        // As of right now, it shoots lightning every 2.2 seconds
        if ((lightningCounter % 132 == 0)){
            HorizontalElectricity lightning1 = new HorizontalElectricity(new Point(this.startLocation.x + 5, this.startLocation.y));
            map.addEnemy(lightning1);
        }
        super.update(player);

    }




     @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 50)
                        .withScale(3)
                        .withBounds(0, 0, 4, 16)
                        .build(),
            });
        }};
    }
}
