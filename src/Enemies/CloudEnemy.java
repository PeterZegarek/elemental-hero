package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.ElementalAbilityListenerManager;
import Level.Enemy;
import Level.MapEntity;
import Level.Player;
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;

import java.util.HashMap;

public class CloudEnemy extends Enemy {
    
    // start and end location defines the two points that it walks between
    // is only made to move along the x axis and has no air ground state logic, so make sure both points have the same Y value
    protected Point startLocation;
    protected Point endLocation;

    protected float movementSpeed;
    private Direction startFacingDirection;
    protected Direction facingDirection;
    protected AirGroundState airGroundState;

    // timer is used to determine how long cloud freezes in place before expanding
    protected int expandWaitTimer;

    // timer is used to determine when cloud expands
    protected int expandTimer;

    // can be either MOVE or EXPAND based on what the enemy is currently set to do
    protected CloudState cloudState;
    protected CloudState previousCloudState;

    public CloudEnemy(Point startLocation, Point endLocation, Direction facingDirection, float movementSpeed) {
        super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("CloudEnemy.png"), 32, 32), "MOVE_RIGHT");
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startFacingDirection = facingDirection;
        this.movementSpeed = movementSpeed;
        this.initialize();
    }

    @Override
    public void initialize() {
        // Add the cloud as an enemy to listen for elemental abilities
        ElementalAbilityListenerManager.addEnemyListener(this);
        super.initialize();
        cloudState = CloudState.MOVE;
        previousCloudState = cloudState;
        facingDirection = startFacingDirection;
        if (facingDirection == Direction.RIGHT) {
            currentAnimationName = "MOVE_RIGHT";
        } else if (facingDirection == Direction.LEFT) {
            currentAnimationName = "MOVE_LEFT";
        }
        airGroundState = AirGroundState.GROUND;

        // every certain number of frames, the cloud expands
        expandWaitTimer = 200;
    }

    @Override
    public void update(Player player) {
        float startBound = startLocation.x;
        float endBound = endLocation.x;

        // if expand timer is up and cloud is not currently expanding, set its state to WAIT
        if (expandWaitTimer == 0 && cloudState != CloudState.EXPAND) {
            cloudState = CloudState.WAIT;
        }
        else {
            expandWaitTimer--;
        }

        // if dinosaur is walking, determine which direction to walk in based on facing direction
        if (cloudState == CloudState.MOVE) {
            if (facingDirection == Direction.RIGHT) {
                currentAnimationName = "MOVE_RIGHT";
                moveXHandleCollision(movementSpeed);
            } else {
                currentAnimationName = "MOVE_LEFT";
                moveXHandleCollision(-movementSpeed);
            }

            // if cloud reaches the start or end location, it turns around
            // cloud may end up going a bit past the start or end location depending on movement speed
            // this calculates the difference and pushes the enemy back a bit so it ends up right on the start or end location
            if (getX1() + getWidth() >= endBound) {
                float difference = endBound - (getX2());
                moveXHandleCollision(-difference);
                facingDirection = Direction.LEFT;
            } else if (getX1() <= startBound) {
                float difference = startBound - getX1();
                moveXHandleCollision(difference);
                facingDirection = Direction.RIGHT;
            }
        }

        // if cloud is waiting to expand, it first waits for a set number of frames
        // after this waiting period is over, the cloud expands
        if (cloudState == CloudState.WAIT) {
            if (previousCloudState == CloudState.MOVE) {
                expandTimer = 100;
                currentAnimationName = facingDirection == Direction.RIGHT ? "EXPAND_RIGHT" : "EXPAND_LEFT";
            } else if (expandTimer == 0) {
                cloudState = CloudState.EXPAND;
            }
            else {
                expandTimer--;
            }
        }

        // this is for resetting the cloud
        if (cloudState == CloudState.EXPAND) {
            // change cloud back to its MOVE state after shooting, reset expandTimer to wait a certain number of frames before expanding again
            cloudState = CloudState.MOVE;

            // reset shoot wait timer so the process can happen again (cloud moves around, then waits, then expand)
            expandWaitTimer = 200;
        }

        super.update(player);

        previousCloudState = cloudState;

        if (activeRockAttack != null){
            if (intersects(activeRockAttack)){
                enemyAttacked(this);
                // broadcast so the fireball disappears
                ElementalAbilityListenerManager.rockAttackKilledEnemy();
            }
        }

    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // if cloud enemy collides with something on the x axis, it turns around and walks the other way
        if (hasCollided) {
            if (direction == Direction.RIGHT) {
                facingDirection = Direction.LEFT;
                currentAnimationName = "MOVE_LEFT";
            } else {
                facingDirection = Direction.RIGHT;
                currentAnimationName = "MOVE_RIGHT";
            }
        }
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("MOVE_RIGHT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                            .withScale(3)
                            .withBounds(10, 16, 11, 11)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                            .withScale(3)
                            .withBounds(10, 15, 12, 13)
                            .build()
            });

            put("MOVE_LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(10, 16, 11, 11)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(10, 15, 12, 13)
                            .build()
            });

            put("EXPAND_RIGHT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 7)
                            .withScale(3)
                            .withBounds(9, 13, 16, 15)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 7)
                            .withScale(3)
                            .withBounds(8, 10, 17, 17)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 7)
                            .withScale(3)
                            .withBounds(5, 5, 24, 22)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 55)
                            .withScale(4)
                            .withBounds(5, 5, 24, 24)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(3)
                            .withBounds(5, 5, 24, 22)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(3)
                            .withBounds(8, 10, 17, 17)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(3)
                            .withBounds(9, 13, 16, 15)
                            .build(),
            });

            put("EXPAND_LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 7)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(9, 13, 16, 15)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 7)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 10, 17, 17)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 7)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(5, 5, 24, 22)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 55)
                            .withScale(4)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(5, 5, 24, 24)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(5, 5, 24, 22)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 10, 17, 17)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(9, 13, 16, 15)
                            .build(),
            });
        }};
    }

    public enum CloudState {
        MOVE, EXPAND, WAIT
    }

}
