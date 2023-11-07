package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.BossLivesListener;
import Level.ElementalAbilityListenerManager;
import Level.Enemy;
import Level.MapEntity;
import Level.MapEntityStatus;
import Level.Player;
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;

import java.util.HashMap;

// This class is for the green dinosaur enemy that shoots fireballs
// It walks back and forth between two set points (startLocation and endLocation)
// Every so often (based on shootTimer) it will shoot a Fireball enemy
public class Firewisp extends Enemy implements BossLivesListener {

    // start and end locati implements BossLivesListener {

    // start and end location defines the two points that it walks between
    // is only made to walk along the x axis and has no air ground state logic, so
    // make sure both points have the same Y value
    protected Point startLocation;
    protected Point endLocation;

    protected float movementSpeed = 1f;
    private Direction startFacingDirection;
    protected Direction facingDirection;
    protected AirGroundState airGroundState;

    public Firewisp(Point startLocation, Point endLocation, Direction facingDirection) {
        super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("FirewispSpritesheet.png"), 150, 150),
                "FLY_RIGHT");
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startFacingDirection = facingDirection;
        this.initialize();
    }

    @Override
    public void initialize() {
        // Add the firewisp as an enemy to listen for elemental abilities
        ElementalAbilityListenerManager.addEnemyListener(this);
        super.initialize();
        facingDirection = startFacingDirection;
        // this variable seems to be useless
        airGroundState = AirGroundState.GROUND;

    }

    @Override
    public void update(Player player) {
        // This is just to calculate when it should turn around
        float startBound = startLocation.x;
        float endBound = endLocation.x;

        // Need him to actually move
        // When changing direction he changes the way he is facing
        if (facingDirection == Direction.RIGHT) {
            currentAnimationName = "FLY_RIGHT";
            moveXHandleCollision(movementSpeed);
        } else {
            currentAnimationName = "FLY_LEFT";
            moveXHandleCollision(-movementSpeed);
        }

        // if firewisp reaches the start or end location, it turns around and the direction changes
        // firewisp may end up going a bit past the start or end location depending on movement speed
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

        super.update(player);

        // if it gets hit by a wave it gets killed
        if (activeWave != null) {
            if (intersects(activeWave)) {
                enemyAttacked(this);
                ElementalAbilityListenerManager.waveKilledEnemy();
            }
        }
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction,
            MapEntity entityCollidedWith) {
        // if firewisp enemy collides with something on the x axis, it turns around
        // and walks the other way
        if (hasCollided) {
            // The move up MAY be necessary in certain use cases
            // This will help the wisp get out of weird spots. Also could be useful to have it travel up a tube or something
            // moveUp(20);
            // When colliding it changes direction and animation
            if (direction == Direction.RIGHT) {
                facingDirection = Direction.LEFT;
                currentAnimationName = "FLY_LEFT";
            } else {
                facingDirection = Direction.RIGHT;
                currentAnimationName = "FLY_RIGHT";
            }
        }
    }

    @Override
    public void getBossLives(int bossLives){
        if (bossLives < 1){
            this.mapEntityStatus = MapEntityStatus.REMOVED;
        }
    }

    // Note: delay is the number of frames it holds an animation for
    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {
            {
                put("FLY_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(0, 0), 10)
                                .withScale(.5f)
                                // .withBounds(4, 2, 5, 13)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 1), 10)
                                .withScale(.5f)
                                // .withBounds(4, 2, 5, 13)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 2), 10)
                                .withScale(.5f)
                                // .withBounds(4, 2, 5, 13)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 3), 10)
                                .withScale(.5f)
                                // .withBounds(4, 2, 5, 13)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 4), 10)
                                .withScale(.5f)
                                // .withBounds(4, 2, 5, 13)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 5), 10)
                                .withScale(.5f)
                                // .withBounds(4, 2, 5, 13)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 6), 10)
                                .withScale(.5f)
                                // .withBounds(4, 2, 5, 13)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 7), 10)
                                .withScale(.5f)
                                // .withBounds(4, 2, 5, 13)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 8), 10)
                                .withScale(.5f)
                                // .withBounds(4, 2, 5, 13)
                                .build(),
                });
                put("FLY_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(0, 0), 10)
                                .withScale(.5f)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                // .withBounds(4, 2, 5, 13)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 1), 10)
                                .withScale(.5f)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                // .withBounds(4, 2, 5, 13)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 2), 10)
                                .withScale(.5f)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                // .withBounds(4, 2, 5, 13)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 3), 10)
                                .withScale(.5f)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                // .withBounds(4, 2, 5, 13)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 4), 10)
                                .withScale(.5f)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                // .withBounds(4, 2, 5, 13)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 5), 10)
                                .withScale(.5f)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                // .withBounds(4, 2, 5, 13)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 6), 10)
                                .withScale(.5f)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                // .withBounds(4, 2, 5, 13)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 7), 10)
                                .withScale(.5f)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                // .withBounds(4, 2, 5, 13)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 8), 10)
                                .withScale(.5f)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                // .withBounds(4, 2, 5, 13)
                                .build(),
                });
            }
        };
    }

}
