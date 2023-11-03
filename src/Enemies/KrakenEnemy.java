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
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("Kraken.png"), 273, 223), "DEFAULT");

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
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 6))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 7))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build(), 
                    new FrameBuilder(spriteSheet.getSprite(0, 8))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 9))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 10))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 0))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build(),  
                    new FrameBuilder(spriteSheet.getSprite(1, 1))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build(),          
                    new FrameBuilder(spriteSheet.getSprite(1, 2))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 3))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build(), 
                    new FrameBuilder(spriteSheet.getSprite(1, 4))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build(),  
                    new FrameBuilder(spriteSheet.getSprite(1, 5))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 6))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build(),  
                    new FrameBuilder(spriteSheet.getSprite(1, 7))
                            .withScale(3)
                            .withBounds(1, 1, 5, 5)
                            .build()                   
                });
            }
        };
    }
}
