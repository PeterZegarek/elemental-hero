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
public class Stick extends Enemy {

    private float movementSpeed;
    // change this to change how quickly it's going to fall
    private float gravity = .2f;
    // to calculate thrown height
    private float terminalVelocity = 6f;
    private float momentumY;

    public Stick(Point location, float movementSpeed, float momentumY) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("TreeStick.png"), 40, 40), "DEFAULT");
        this.movementSpeed = movementSpeed;
        this.momentumY = momentumY;
        initialize();
    }

    @Override
    public void initialize() {
        // Add the stick as an enemy to listen for elemental abilities
        ElementalAbilityListenerManager.addEnemyListener(this);
        super.initialize();
    }

    //this.mapEntityStatus = MapEntityStatus.REMOVED;

    @Override
    public void update(Player player) {
        // move stick forward
        moveXHandleCollision(movementSpeed);
        // Apply gravity
        momentumY = Math.min(momentumY + gravity, terminalVelocity);
        moveYHandleCollision(momentumY);

        super.update(player);

        // if it gets hit by a fireball it gets killed
        if (activeFireball != null) {
            if (intersects(activeFireball)) {
                enemyAttacked(this);

                // Comment this if you don't want the fireball to despawn when killing a stick
                ElementalAbilityListenerManager.fireballKilledEnemy();

                // For now I don't want it to reset the cooldown of fireball
                 // Player.setCooldownCounter(20);
            }
        }
    }

    @Override
    public void onEndCollisionCheckY(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // if fireball collides with anything solid on the x axis, it is removed
        if (hasCollided) {
            this.mapEntityStatus = MapEntityStatus.REMOVED;
        }
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
        this.mapEntityStatus = MapEntityStatus.REMOVED;
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(2)
                            .withBounds(13, 13, 14, 15)
                            .build()
            });
        }};
    }
}
