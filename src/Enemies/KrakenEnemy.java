package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.ElementalAbilityListenerManager;
import Level.Enemy;
import Level.MapEntity;
import Level.MapEntityStatus;
import Level.Player;
import Utils.Direction;
import Utils.Point;

import java.util.HashMap;

// This class is for the fireball enemy that the DinosaurEnemy class shoots out
// it will travel in a straight line (x axis) for a set time before disappearing
// it will disappear early if it collides with a solid map tile
public class KrakenEnemy extends Enemy {
    private int existenceFrames;

    public KrakenEnemy(Point location, int existenceFrames) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("Kraken.png"), 271, 221), "DEFAULT");

        // how long the fireball will exist for before disappearing
        this.existenceFrames = existenceFrames;

        initialize();
    }

    @Override
    public void initialize() {
        // Add the fireball as an enemy to listen for elemental abilities
        ElementalAbilityListenerManager.addEnemyListener(this);
        super.initialize();
    }

    @Override
    public void update(Player player) {
        // if timer is up, set map entity status to REMOVED
        // the camera class will see this next frame and remove it permanently from the map
        if (existenceFrames == 0) {
            this.mapEntityStatus = MapEntityStatus.REMOVED;
        }
        else{
                super.update(player);
        }
        existenceFrames--;
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // if fireball collides with anything solid on the x axis, it is removed
        if (hasCollided) {
            this.mapEntityStatus = MapEntityStatus.REMOVED;
        }
    }

    @Override
    public void touchedPlayer(Player player) {
        // if fireball touches player, it disappears
        super.touchedPlayer(player);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        float scale = 1.5f;
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                            .withScale(scale)
                            .withBounds(120, 170, 20, 20)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                            .withScale(scale)
                            .withBounds(120, 135, 20, 60)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2),14)
                            .withScale(scale)
                            .withBounds(120, 115, 20, 90)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 14)
                            .withScale(scale)
                            .withBounds(110, 90, 45, 110)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 14)
                            .withScale(scale)
                            .withBounds(95, 75, 65, 135)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 14)
                            .withScale(scale)
                            .withBounds(65, 45, 130, 135)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 6), 14)
                            .withScale(scale)
                            .withBounds(45, 25, 150, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 7), 14)
                            .withScale(scale)
                            .withBounds(45, 25, 150, 175)
                            .build(), 
                    new FrameBuilder(spriteSheet.getSprite(0, 8), 14)
                            .withScale(scale)
                            .withBounds(15, 25, 235, 175)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 9), 14)
                            .withScale(scale)
                            .withBounds(15, 25, 235, 175)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 10), 14)
                            .withScale(scale)
                            .withBounds(5, 25, 235, 175)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 0), 14)
                            .withScale(scale)
                            .withBounds(5, 25, 235, 175)
                            .build(),  
                    new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                            .withScale(scale)
                            .withBounds(5, 25, 235, 175)
                            .build()                          
                });
            }
        };
    }
}
