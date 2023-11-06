package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.ElementalAbilityListenerManager;
import Level.Enemy;
import Level.MapEntity;
import Level.MapEntityStatus;
import Level.Player;
import Utils.Direction;
import Utils.Point;

import java.util.HashMap;

public class Arrow extends Enemy {
    private float movementSpeed;
    // change this to change how quickly it's going to fall
    private float gravity = 5f; //.2f
    // to calculate thrown height
    private float terminalVelocity = 3f; //6f
    private float momentumY;

    public Arrow(Point location, float movementSpeed, float momentumY, String startingAnimation) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("Arrow.png"), 40, 40), startingAnimation);
        this.movementSpeed = movementSpeed;
        isUpdateOffScreen = true;
        this.momentumY = momentumY;
        initialize();
    }

    @Override
    public void initialize() {
        // Add the stick as an enemy to listen for elemental abilities
        ElementalAbilityListenerManager.addEnemyListener(this);
        super.initialize();
    }

    @Override
    public void update(Player player) {
        // move arrow forward
        moveXHandleCollision(movementSpeed);
        // Apply gravity
        momentumY = Math.min(momentumY + gravity, terminalVelocity);
        moveYHandleCollision(momentumY);

        super.update(player);

        // if it gets hit by a rock it gets killed
        if (activeRockAttack != null) {
            if (intersects(activeRockAttack)) {
                enemyAttacked(this);

                // Comment this if you don't want the rock to despawn when killing a stick
                ElementalAbilityListenerManager.rockAttackKilledEnemy();

            }
        }
    }

    @Override
    public void onEndCollisionCheckY(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // if arrow collides with anything solid on the x axis, it is removed
        if (hasCollided) {
            this.mapEntityStatus = MapEntityStatus.REMOVED;
        }
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // if arrow collides with anything solid on the x axis, it is removed
        if (hasCollided) {
            this.mapEntityStatus = MapEntityStatus.REMOVED;
        }
    }

    @Override
    public void touchedPlayer(Player player) {
        // if arrow touches player, it disappears
        super.touchedPlayer(player);
        this.mapEntityStatus = MapEntityStatus.REMOVED;
    }


    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(2)
                            .withBounds(13, 13, 14, 15)
                            .build()
            });
            put("RIGHT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(13, 13, 14, 15)
                            .build()
            });
        }};
    }
}
