package Enemies;

import Builders.FrameBuilder;
import Enemies.SkeletonBoss.SkeletonState;
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

// This class is for the electric boss
// it will fly around
public class ElectricBoss extends Enemy {

    // start and end location defines the two points that it flies between
    protected Point startLocation;
    protected Point endLocation;

    protected float movementSpeed = 1f;
    private Direction startFacingDirection;
    protected Direction facingDirection;
    protected AirGroundState airGroundState;

    protected InvisibleHitbox electricHitbox = null;
    protected boolean isElectricHitboxActive = false;

    protected int lives = 3;
    // 0 is not hurt, 1 is hurt
    protected int bossState = 0;
    protected int hurtTimer = 0;

    public ElectricBoss(Point startLocation, Point endLocation, Direction facingDirection) {
        super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("ElectricMiniboss.png"), 31, 32),
                "FLY_RIGHT");
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startFacingDirection = facingDirection;
        this.initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        facingDirection = startFacingDirection;

    }

    @Override
    public void update(Player player) {

        if (!isElectricHitboxActive) {
            createElectricHitbox();
        }
        // if it is hurt decrease timer
        if (bossState == 1){
            hurtTimer--;
            if (hurtTimer == 0){
                bossState = 0;
                if (facingDirection == Direction.RIGHT){
                    currentAnimationName = "FLY_RIGHT";
                }
                else {
                    currentAnimationName = "FLY_LEFT";
                }
            }
        }
        // This is just to calculate when it should turn around
        float startBound = startLocation.x;
        float endBound = endLocation.x;

        // Need him to actually move
        // When changing direction he changes the way he is facing
        if (facingDirection == Direction.RIGHT) {
            // currentAnimationName = "FLY_RIGHT";
            moveXHandleCollision(movementSpeed);
        } else {
            // currentAnimationName = "FLY_LEFT";
            moveXHandleCollision(-movementSpeed);
        }

        // if boss reaches the start or end location, it turns around and the direction
        // changes
        // this makes sure it doesn't go far over the end bound or start bound
        if (getX1() + getWidth() >= endBound) {
            float difference = endBound - (getX2());
            moveXHandleCollision(-difference);
            facingDirection = Direction.LEFT;
            if (bossState == 0){
                    currentAnimationName = "FLY_LEFT";
            }
        } else if (getX1() <= startBound) {
            float difference = startBound - getX1();
            moveXHandleCollision(difference);
            facingDirection = Direction.RIGHT;
            if (bossState == 0){
                    currentAnimationName = "FLY_RIGHT";
            }
        }

        super.update(player);

        // if it hits the hitbox and the boss is not currently hurt
        if (electricHitbox.isTouched() && bossState == 0){
            enemyAttacked(this);
        }
    }

    // function to create new hitbox that will damage the boss
    public void createElectricHitbox() {
        electricHitbox = new InvisibleHitbox(startLocation, 1f, startLocation, endLocation);
        map.addEnemy(electricHitbox);
        System.out.println(electricHitbox);
        isElectricHitboxActive = true;
    }

    @Override
    public void enemyAttacked(Enemy enemy) {
        if (lives > 1) {
            lives--;
            // put it into hurt state
            bossState = 1;
            if (facingDirection == Direction.LEFT) {
                currentAnimationName = "HURT_LEFT";
            } else {
                currentAnimationName = "HURT_RIGHT";
            }
            hurtTimer = 60;
        } else if (lives <= 1) {
            isOnMap = false;
            // This makes the enemy disappear
            enemy.setMapEntityStatus(MapEntityStatus.REMOVED);
            electricHitbox.setMapEntityStatus(MapEntityStatus.REMOVED);

        }
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction,
            MapEntity entityCollidedWith) {
        // if boss enemy collides with something on the x axis, it turns around
        // and walks the other way
        if (hasCollided) {

            // When colliding it changes direction and animation
            if (direction == Direction.RIGHT) {
                facingDirection = Direction.LEFT;
                if (bossState == 0){
                    currentAnimationName = "FLY_LEFT";
                }
            } else {
                facingDirection = Direction.RIGHT;
                if (bossState == 0){
                    currentAnimationName = "FLY_LEFT";
                }
            }
        }
    }

    // Note: delay is the number of frames it holds an animation for
    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {
            {
                put("FLY_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(0, 0), 10)
                                .withScale(3)
                                .withBounds(0, 10, 31, 22)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 1), 10)
                                .withScale(3)
                                .withBounds(0, 10, 31, 22)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 2), 10)
                                .withScale(3)
                                .withBounds(0, 10, 31, 22)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 3), 10)
                                .withScale(3)
                                .withBounds(0, 10, 31, 22)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 4), 10)
                                .withScale(3)
                                .withBounds(0, 10, 31, 22)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 5), 10)
                                .withScale(3)
                                .withBounds(0, 10, 31, 22)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 6), 10)
                                .withScale(3)
                                .withBounds(0, 10, 31, 22)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 7), 10)
                                .withScale(3)
                                .withBounds(0, 10, 31, 22)
                                .build(),
                });
                put("FLY_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(0, 0), 10)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(0, 10, 31, 22)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 1), 10)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(0, 10, 31, 22)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 2), 10)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(0, 10, 31, 22)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 3), 10)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(0, 10, 31, 22)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 4), 10)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(0, 10, 31, 22)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 5), 10)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(0, 10, 31, 22)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 6), 10)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(0, 10, 31, 22)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 7), 10)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(0, 10, 31, 22)
                                .build(),
                });
                put("HURT_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(0, 8), 60)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(0, 10, 31, 22)
                                .build(),
                });
                put("HURT_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(0, 8), 60)
                                .withScale(3)
                                .withBounds(0, 10, 31, 22)
                                .build(),
                });

            }
        };
    }

}
