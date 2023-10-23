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
import Players.Wave;
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;

import java.util.HashMap;

// This class is for the Angry WaterWizard that throws sticks
// It SWIMs back and forth between two set points (startLocation and endLocation)
// Every so often (based on shootTimer) it will throw a stick enemy
public class WaterWizardEnemy extends Enemy {

    // start and end location defines the two points that it SWIMs between
    // is only made to SWIM along the x axis and has no air ground state logic, so make sure both points have the same Y value
    protected Point startLocation;
    protected Point endLocation;

    protected float movementSpeed = 1f;
    private Direction startFacingDirection;
    protected Direction facingDirection;
    protected AirGroundState airGroundState;

    // timer is used to determine how long WaterWizard freezes in place before shooting branch
    protected int shootWaitTimer;

    // timer is used to determine when a branch is to be shot out
    protected int shootTimer;

    // can be either SWIM or SHOOT based on what the enemy is currently set to do
    protected WaterWizardState WaterWizardState;
    protected WaterWizardState previousWaterWizardState;

    public WaterWizardEnemy(Point startLocation, Point endLocation, Direction facingDirection) {
        super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("WaterWizardEnemy.png"), 215, 226), "SWIM_RIGHT");
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startFacingDirection = facingDirection;
        this.initialize();
    }

    @Override
    public void initialize() {
        // Add the WaterWizard enemy as an enemy to listen for elemental abilities
        ElementalAbilityListenerManager.addEnemyListener(this);
        super.initialize();
        WaterWizardState = WaterWizardState.SWIM;
        previousWaterWizardState = WaterWizardState;
        facingDirection = startFacingDirection;
        if (facingDirection == Direction.RIGHT) {
            currentAnimationName = "SWIM_RIGHT";
        } else if (facingDirection == Direction.LEFT) {
            currentAnimationName = "SWIM_LEFT";
        }
        airGroundState = AirGroundState.GROUND;

        // every certain number of frames, the Wave will be shot out
        shootWaitTimer = 65;
    }

    @Override
    public void update(Player player) {
        float startBound = startLocation.x;
        float endBound = endLocation.x;

        // if shoot timer is up and WaterWizard is not currently shooting, set its state to SHOOT
        if (shootWaitTimer == 0 && WaterWizardState != WaterWizardState.SHOOT_WAIT) {
            WaterWizardState = WaterWizardState.SHOOT_WAIT;
        }
        else {
            shootWaitTimer--;
        }

        // if WaterWizard is SWIMMING, determine which direction to SWIM in based on facing direction
        if (WaterWizardState == WaterWizardState.SWIM) {
            if (facingDirection == Direction.RIGHT) {
                currentAnimationName = "SWIM_RIGHT";
                moveXHandleCollision(movementSpeed);
            } else {
                currentAnimationName = "SWIM_LEFT";
                moveXHandleCollision(-movementSpeed);
            }

            // if WaterWizard reaches the start or end location, it turns around
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

        // if WaterWizard is waiting to shoot, it does its animations then throws the stick
        // after this waiting period is over, the stick is thrown
        if (WaterWizardState == WaterWizardState.SHOOT_WAIT) {
            if (previousWaterWizardState == WaterWizardState.SWIM) {
                shootTimer = 40;
                // This line of code takes the current direction the WaterWizard is facing and makes it shoot in that direction
                // If facingdirection is right, it shoots right, else it shoots left
                // currentAnimationName = facingDirection == Direction.RIGHT ? "SHOOT_RIGHT" : "SHOOT_LEFT";
                if (this.getX() > player.getX()){
                    currentAnimationName = "SHOOT_LEFT";
                }
                else{
                    currentAnimationName = "SHOOT_RIGHT";
                }
            } else if (shootTimer == 0) {
                WaterWizardState = WaterWizardState.SHOOT;
            }
            else {
                shootTimer--;
            }
        }

        // this is for actually having the WaterWizard throw the stick
        if (WaterWizardState == WaterWizardState.SHOOT) {
            // define where stick will spawn on map (x location) relative to WaterWizard's location            
            // and define its movement speed
            int waveX;
            if (currentAnimationName == "SHOOT_RIGHT") {
                waveX = Math.round(getX()) + getWidth() + 35;
            } else {
                waveX = Math.round(getX() - 95);

            }

            // define where stick will spawn on the map (y location) relative to WaterWizard's location
            int waveY = Math.round(getY()) - 16;

            // create Wave enemy
            Wave waveAttackLeft = new Wave(waveX, waveY, Direction.LEFT);

            Wave waveAttackRight = new Wave(waveX, waveY, Direction.RIGHT);

            // add stick enemy to the map for it to spawn in the level
            if(previousAnimationName == "SHOOT_LEFT"){
                map.addEnemy(waveAttackLeft);
                
            }
            else{
                map.addEnemy(waveAttackRight);
            }
            // change WaterWizard back to its SWIM state after shooting, reset shootTimer to wait a certain number of frames before shooting again
            WaterWizardState = WaterWizardState.SWIM;

            // reset shoot wait timer so the process can happen again (WaterWizard SWIMs around, then waits, then shoots)
            shootWaitTimer = 150;
        }

        super.update(player);

        previousWaterWizardState = WaterWizardState;

    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // if dinosaur enemy collides with something on the x axis, it turns around and SWIMs the other way
        if (hasCollided) {
            if (direction == Direction.RIGHT) {
                facingDirection = Direction.LEFT;
                currentAnimationName = "SWIM_LEFT";
            } else {
                facingDirection = Direction.RIGHT;
                currentAnimationName = "SWIM_RIGHT";
            }
        }
    }
   /*  @Override
    public void enemyAttacked(Enemy enemy){
        
    }
*/
    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        float scale = .5f;
        return new HashMap<String, Frame[]>() {{
            put("SWIM_LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(3, 0), 14)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(25, 35, 200, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 1), 14)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(25, 35, 200, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 2), 14)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(25, 35, 200, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 3), 14)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(25, 35, 200, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 4), 14)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(25, 35, 200, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 5), 14)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(25, 35, 200, 150)
                            .build(),                
            });

            put("SWIM_RIGHT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(3, 0), 14)
                            .withScale(scale)
                            .withBounds(25, 35, 200, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 1), 14)
                            .withScale(scale)
                            .withBounds(25, 35, 200, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 2), 14)
                            .withScale(scale)
                            .withBounds(25, 35, 200, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 3), 14)
                            .withScale(scale)
                            .withBounds(25, 35, 200, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 4), 14)
                            .withScale(scale)
                            .withBounds(25, 35, 200, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 5), 14)
                            .withScale(scale)
                            .withBounds(25, 35, 200, 150)
                            .build(),
            });

            put("SHOOT_LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 15)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(25, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 15)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(25, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 15)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(25, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 15)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(25, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 15)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(25, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 15)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(25, 35, 100, 150)
                            .build()
            });

            put("SHOOT_RIGHT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 15)
                            .withScale(scale)
                            .withBounds(25, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 15)
                            .withScale(scale)
                            .withBounds(25, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 15)
                            .withScale(scale)
                            .withBounds(25, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 15)
                            .withScale(scale)
                            .withBounds(25, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 15)
                            .withScale(scale)
                            .withBounds(25, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 15)
                            .withScale(scale)
                            .withBounds(25, 35, 100, 150)
                            .build()
            });
        }};
    }

    public enum WaterWizardState {
        SWIM, SHOOT_WAIT, SHOOT, DEATH
    }
}
