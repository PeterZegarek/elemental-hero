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
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;

import java.util.HashMap;

// This class is for the Angry tree that throws sticks
// It walks back and forth between two set points (startLocation and endLocation)
// Every so often (based on shootTimer) it will throw a stick enemy
public class TreeEnemy extends Enemy {

    // start and end location defines the two points that it walks between
    // is only made to walk along the x axis and has no air ground state logic, so make sure both points have the same Y value
    protected Point startLocation;
    protected Point endLocation;

    protected float movementSpeed = 1f;
    private Direction startFacingDirection;
    protected Direction facingDirection;
    protected AirGroundState airGroundState;

    // timer is used to determine how long tree freezes in place before shooting branch
    protected int shootWaitTimer;

    // timer is used to determine when a branch is to be shot out
    protected int shootTimer;

    // can be either WALK or SHOOT based on what the enemy is currently set to do
    protected TreeState treeState;
    protected TreeState previoustreeState;


    public TreeEnemy(Point startLocation, Point endLocation, Direction facingDirection) {
        super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("TreeEnemy.png"), 44, 45), "WALK_RIGHT");
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startFacingDirection = facingDirection;
        this.isOnMap = true;
        this.initialize();
    }

    @Override
    public void initialize() {
        // Add the tree enemy as an enemy to listen for elemental abilities
        ElementalAbilityListenerManager.addEnemyListener(this);
        super.initialize();
        treeState = TreeState.WALK;
        previoustreeState = treeState;
        facingDirection = startFacingDirection;
        if (facingDirection == Direction.RIGHT) {
            currentAnimationName = "WALK_RIGHT";
        } else if (facingDirection == Direction.LEFT) {
            currentAnimationName = "WALK_LEFT";
        }
        airGroundState = AirGroundState.GROUND;

        // every certain number of frames, the fireball will be shot out
        shootWaitTimer = 65;
    }

    @Override
    public void update(Player player) {
        float startBound = startLocation.x;
        float endBound = endLocation.x;

        // if shoot timer is up and tree is not currently shooting, set its state to SHOOT
        if (shootWaitTimer == 0 && treeState != TreeState.SHOOT_WAIT) {
            treeState = TreeState.SHOOT_WAIT;
        }
        else {
            shootWaitTimer--;
        }

        // if tree is walking, determine which direction to walk in based on facing direction
        if (treeState == TreeState.WALK) {
            if (facingDirection == Direction.RIGHT) {
                currentAnimationName = "WALK_RIGHT";
                moveXHandleCollision(movementSpeed);
            } else {
                currentAnimationName = "WALK_LEFT";
                moveXHandleCollision(-movementSpeed);
            }

            // if tree reaches the start or end location, it turns around
            // dinosaur may end up going a bit past the start or end location depending on movement speed
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

        // if tree is waiting to shoot, it does its animations then throws the stick
        // after this waiting period is over, the stick is thrown
        if (treeState == TreeState.SHOOT_WAIT) {
            if (previoustreeState == TreeState.WALK) {
                shootTimer = 40;
                // This line of code takes the current direction the tree is facing and makes it shoot in that direction
                // If facingdirection is right, it shoots right, else it shoots left
                // currentAnimationName = facingDirection == Direction.RIGHT ? "SHOOT_RIGHT" : "SHOOT_LEFT";
                if (this.getX() > player.getX()){
                    currentAnimationName = "SHOOT_LEFT";
                }
                else{
                    currentAnimationName = "SHOOT_RIGHT";
                }
            } else if (shootTimer == 0) {
                treeState = TreeState.SHOOT;
            }
            else {
                shootTimer--;
            }
        }

        // this is for actually having the tree throw the stick
        if (treeState == TreeState.SHOOT) {
            // define where stick will spawn on map (x location) relative to tree's location            
            // and define its movement speed
            int stickX;
            float movementSpeed;
            if (currentAnimationName == "SHOOT_RIGHT") {
                stickX = Math.round(getX()) + getWidth() - 30;
                movementSpeed = 2f;
            } else {
                stickX = Math.round(getX() - 35);
                movementSpeed = -2f;
            }

            // define where stick will spawn on the map (y location) relative to tree's location
            int stickY = Math.round(getY()) - 16;

            //Modify how high it throws the stick
            float heightY = -6f;

            // create stick enemy
            Stick stick = new Stick(new Point(stickX, stickY), movementSpeed, heightY);

            // add stick enemy to the map for it to spawn in the level
            map.addEnemy(stick);

            // change tree back to its WALK state after shooting, reset shootTimer to wait a certain number of frames before shooting again
            treeState = TreeState.WALK;

            // reset shoot wait timer so the process can happen again (tree walks around, then waits, then shoots)
            shootWaitTimer = 130;
        }

        super.update(player);

        previoustreeState = treeState;

        // if there is a player fireball and it got hit
        if (activeFireball != null){
            if (intersects(activeFireball)){
                enemyAttacked(this);
                // broadcast so the fireball disappears
                ElementalAbilityListenerManager.fireballKilledEnemy();
            }
        }

    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // if dinosaur enemy collides with something on the x axis, it turns around and walks the other way
        if (hasCollided) {
            if (direction == Direction.RIGHT) {
                facingDirection = Direction.LEFT;
                currentAnimationName = "WALK_LEFT";
            } else {
                facingDirection = Direction.RIGHT;
                currentAnimationName = "WALK_RIGHT";
            }
        }
    }

    public MapEntityStatus getMapEntityStatus(){
        return this.mapEntityStatus;
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        float scale = 1.8f;
        return new HashMap<String, Frame[]>() {{
            put("WALK_LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 14)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 0), 14)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 2), 14)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 3), 14)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 4), 14)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 5), 14)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(1, 3, 35, 30)
                            .build()
                    
            });

            put("WALK_RIGHT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 14)
                            .withScale(scale)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 0), 14)
                            .withScale(scale)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                            .withScale(scale)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 2), 14)
                            .withScale(scale)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 3), 14)
                            .withScale(scale)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 4), 14)
                            .withScale(scale)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 5), 14)
                            .withScale(scale)
                            .withBounds(1, 3, 35, 30)
                            .build()
            });

            put("SHOOT_LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(2, 0), 15)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 1), 15)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 2), 15)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 3), 15)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 4), 15)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(1, 3, 35, 30)
                            .build()
            });

            put("SHOOT_RIGHT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(2, 0), 15)
                            .withScale(scale)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 1), 15)
                            .withScale(scale)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 2), 15)
                            .withScale(scale)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 3), 15)
                            .withScale(scale)
                            .withBounds(1, 3, 35, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 4), 15)
                            .withScale(scale)
                            .withBounds(1, 3, 35, 30)
                            .build(),
            });
        }};
    }

    public enum TreeState {
        WALK, SHOOT_WAIT, SHOOT
    }
}
