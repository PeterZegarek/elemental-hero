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
import Level.PlayerState;
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;

import java.util.HashMap;

// This class is for the Angry Slime that throws sticks
// It walks back and forth between two set points (startLocation and endLocation)
// Every so often (based on shootTimer) it will throw a stick enemy
public class SlimeEnemy extends Enemy {

    // start and end location defines the two points that it walks between
    // is only made to walk along the x axis and has no air ground state logic, so make sure both points have the same Y value
    protected Point startLocation;
    protected Point endLocation;

    protected float movementSpeed = 1f;
    private Direction startFacingDirection;
    protected Direction facingDirection;
    protected AirGroundState airGroundState;

    //Flag
    protected boolean isInvincible = false; // if true, player cannot be hurt by enemies (good for testing)
    protected int isInvincibleCounter; // Invincible for a couple seconds after being hit

    // can be either WALK or SHOOT based on what the enemy is currently set to do
    protected SlimeState SlimeState;
    protected SlimeState previousSlimeState;
    protected PlayerState playerState;

    public SlimeEnemy(Point startLocation, Point endLocation, Direction facingDirection) {
        super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("SlimeEnemy.png"), 31, 24), "WALK_RIGHT");
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startFacingDirection = facingDirection;
        this.initialize();
    }

    @Override
    public void initialize() {
        // Add the Slime enemy as an enemy to listen for elemental abilities
        ElementalAbilityListenerManager.addEnemyListener(this);
        super.initialize();
        SlimeState = SlimeState.WALK;
        previousSlimeState = SlimeState;
        facingDirection = startFacingDirection;
        if (facingDirection == Direction.RIGHT) {
            currentAnimationName = "WALK_RIGHT";
        } else if (facingDirection == Direction.LEFT) {
            currentAnimationName = "WALK_LEFT";
        }
        airGroundState = AirGroundState.GROUND;
}

    @Override
    public void update(Player player) {
        
        if (isInvincibleCounter > 0){           
            isInvincibleCounter--;
            handleSlimeState();
            if (isInvincibleCounter == 0){                                             
                isInvincible = false;               
                this.mapEntityStatus = MapEntityStatus.REMOVED;             
            }
        } 
 
        handleSlimeState();   
        super.update(player);

    }

    protected void handleSlimeState() {
        switch (SlimeState) {
            case WALK: 
                slimeWalk();                             
                break;
            case EXPLODE:
                slimeExplode();
                break;
            }
    }


    public void slimeWalk(){
        // This is just to calculate when it should turn around
        float startBound = startLocation.x;
        float endBound = endLocation.x;

        // Need it to actually move
        // When changing direction he changes the way it is facing
        if (facingDirection == Direction.RIGHT) {
                currentAnimationName = "WALK_RIGHT";
                moveXHandleCollision(movementSpeed);
        } else {
                currentAnimationName = "WALK_LEFT";
                moveXHandleCollision(-movementSpeed);
        }

        // if SlimeEnemy reaches the start or end location, it turns around and the direction changes
        // SlimeEnemy may end up going a bit past the start or end location depending on movement speed
        // this calculates the difference and pushes the enemy back a bit so it ends up right on the start or end location
        if (getX1() + getWidth() >= endBound) {
                float difference = endBound - (getX2());
                moveXHandleCollision(-difference);
                facingDirection = Direction.LEFT;
        } 
        else if (getX1() <= startBound) {
                float difference = startBound - getX1();
                moveXHandleCollision(difference);
                facingDirection = Direction.RIGHT;
        }
    }

    public void slimeExplode(){
        // This is just to calculate when it should turn around
        float startBound = startLocation.x;
        float endBound = endLocation.x;
        if (getX1() + getWidth() >= endBound) {
                float difference = endBound - (getX2());
                moveXHandleCollision(-difference);
                facingDirection = Direction.LEFT;
        } 
        else if (getX1() <= startBound) {
                float difference = startBound - getX1();
                moveXHandleCollision(difference);
                facingDirection = Direction.RIGHT;
        }
        // Need it to actually move
        // When changing direction he changes the way it is facing
        if (facingDirection == Direction.RIGHT) {
                currentAnimationName = "EXPLODE_RIGHT";
        } 
        else {
                currentAnimationName = "EXPLODE_LEFT";
        }
    }
    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // if SlimeEnemy enemy collides with something on the x axis, it turns around and walks the other way
        if (hasCollided) {
            if (direction == Direction.RIGHT) {
                facingDirection = Direction.LEFT;
                currentAnimationName = "WALK_RIGHT";
            } else {
                facingDirection = Direction.RIGHT;
                currentAnimationName = "WALK_LEFT";
            }
        } 
    }

    @Override
    public void touchedPlayer(Player player) {
        super.touchedPlayer(player);
        isInvincible = true;
        isInvincibleCounter = 20;
        SlimeState = SlimeState.EXPLODE; 
        }


    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
                put("WALK_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                            .withScale(2)
                            .withBounds(5, 8, 20, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                            .withScale(2)
                            .withBounds(5, 8, 20, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(2)
                            .withBounds(5, 8, 20, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(2)
                            .withBounds(5, 8, 20, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(2)
                            .withBounds(5, 8, 20, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 8)
                            .withScale(2)
                            .withBounds(5, 8, 20, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 6), 8)
                            .withScale(2)
                            .withBounds(5, 8, 20, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 7), 8)
                            .withScale(2)
                            .withBounds(5, 8, 20, 14)
                            .build()
            });

                put("WALK_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(5, 8, 20, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(5, 8, 20, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(5, 8, 20, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(5, 8, 20, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(5, 8, 20, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 8)
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(5, 8, 20, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 6), 8)
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(5, 8, 20, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 7), 8)
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(5, 8, 20, 14)
                            .build()
            });
                put("EXPLODE_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(2, 0), 8)
                            .withScale(2)
                            .withBounds(6, 6, 6, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 1), 8)
                            .withScale(2)
                            .withBounds(6, 6, 6, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 2), 8)
                            .withScale(2)
                            .withBounds(6, 6, 6, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 3), 8)
                            .withScale(2)
                            .withBounds(6, 6, 6, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 4), 8)
                            .withScale(2)
                            .withBounds(6, 6, 6, 14)
                            .build()
            });
                put("EXPLODE_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(2, 0), 8)
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(6, 6, 6, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 1), 8)
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(6, 6, 6, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 2), 8)
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(6, 6, 6, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 3), 8)
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(6, 6, 6, 14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 4), 8)
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(6, 6, 6, 14)
                            .build()
            });
        }};
    }

    public enum SlimeState {
        WALK, EXPLODE
    }

}
