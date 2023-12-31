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
import Players.Wave;
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;

import java.util.HashMap;

// This class is for the WaterWizard that Shoots WAVEs
// It SWIMs back and forth between two set points (startLocation and endLocation)
// Every so often (based on shootTimer) it will throw a WAVE enemy
public class WaterWizardEnemy extends Enemy {

    // start and end location defines the two points that it SWIMs between
    // is only made to SWIM along the x axis and has no air ground state logic, so make sure both points have the same Y value
    protected Point startLocation;
    protected Point endLocation;

    protected float movementSpeed = 1f;
    private Direction startFacingDirection;
    protected Direction facingDirection;
    protected AirGroundState airGroundState;

    // timer is used to determine how long WaterWizard freezes in place before shooting wave
    protected int shootWaitTimer;

    // timer is used to determine when a wave is to be shot out
    protected int shootTimer;

    // can be either SWIM or SHOOT based on what the enemy is currently set to do
    protected WaterWizardState waterWizardState;
    protected WaterWizardState previousWaterWizardState;

    protected int isInvincibleCounter;
    protected boolean isInvincible;
    
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
        waterWizardState = WaterWizardState.SWIM;
        previousWaterWizardState = waterWizardState;
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
        
        if (isInvincibleCounter > 0){           
            isInvincibleCounter--;
            if (isInvincibleCounter == 0){                                             
                isInvincible = false;               
                this.mapEntityStatus = MapEntityStatus.REMOVED;             
            }
        }
        
        float startBound = startLocation.x;
        float endBound = endLocation.x;

        // if shoot timer is up and WaterWizard is not currently shooting, set its state to SHOOT
        if (shootWaitTimer == 0 && waterWizardState != WaterWizardState.SHOOT_WAIT) {
            waterWizardState = WaterWizardState.SHOOT_WAIT;
        }
        else {
            shootWaitTimer--;
        }

        // if WaterWizard is SWIMMING, determine which direction to SWIM in based on facing direction
        if (waterWizardState == WaterWizardState.SWIM) {
            if (facingDirection == Direction.RIGHT) {
                currentAnimationName = "SWIM_RIGHT";
                moveXHandleCollision(movementSpeed);
            } else {
                currentAnimationName = "SWIM_LEFT";
                moveXHandleCollision(-movementSpeed);
            }

            // if WaterWizard reaches the start or end location, it turns around
            // WaterWizard may end up going a bit past the start or end location depending on movement speed
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

        // if WaterWizard is waiting to shoot, it does its animations then throws the WAVE
        // after this waiting period is over, the WAVE is thrown
        if (waterWizardState == WaterWizardState.SHOOT_WAIT) {
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
                waterWizardState = WaterWizardState.SHOOT;
            }
            else {
                shootTimer--;
            }
        }
        if (waterWizardState == WaterWizardState.DEATH){
            currentAnimationName = "DEATH";
        }
        // this is for actually having the WaterWizard shoot the wave
        if (waterWizardState == WaterWizardState.SHOOT) {
            // define where WAVE will spawn on map (x location) relative to WaterWizard's location            
            // and define its movement speed
            int waveX;
            if (currentAnimationName == "SHOOT_RIGHT") {
                waveX = Math.round(getX()) + getWidth() + 35;
            } else {
                waveX = Math.round(getX() - 95);

            }

            // define where wave will spawn on the map (y location) relative to WaterWizard's location
            int waveY = Math.round(getY()) - 16;

            // create Wave enemy
            WaveEnemy waveAttackLeft = new WaveEnemy(waveX, waveY, Direction.LEFT);

            WaveEnemy waveAttackRight = new WaveEnemy(waveX, waveY, Direction.RIGHT);

            // add WAVE enemy to the map for it to spawn in the level
            if(previousAnimationName == "SHOOT_LEFT"){
                map.addEnemy(waveAttackLeft);
                
            }
            else{
                map.addEnemy(waveAttackRight);
            }
            // change WaterWizard back to its SWIM state after shooting, reset shootTimer to wait a certain number of frames before shooting again
            waterWizardState = WaterWizardState.SWIM;

            // reset shoot wait timer so the process can happen again (WaterWizard SWIMs around, then waits, then shoots)
            shootWaitTimer = 150;
        }
        if (Player.getIsElectricOn() == true){
            if (intersects(player)){
                enemyAttacked(this);
            }
        }

        super.update(player);

        previousWaterWizardState = waterWizardState;

    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // if WaterWizard enemy collides with something on the x axis, it turns around and SWIMs the other way
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

    @Override
        public void enemyAttacked(Enemy enemy) {
                waterWizardState = WaterWizardState.DEATH;
                isInvincible = true;
                isInvincibleCounter = 40;
                shootWaitTimer = 150;
        }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        float scale = .5f;
        return new HashMap<String, Frame[]>() {{
            put("SWIM_LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(3, 0), 14)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(20, 100, 200, 10)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 1), 14)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(20, 100, 200, 100)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 2), 14)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(20, 100, 200, 100)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 3), 14)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(20, 100, 200, 100)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 4), 14)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(20, 100, 200, 100)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 5), 14)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(20, 100, 200, 100)
                            .build(),                
            });

            put("SWIM_RIGHT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(3, 0), 14)
                            .withScale(scale)
                            .withBounds(20, 100, 200, 100)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 1), 14)
                            .withScale(scale)
                            .withBounds(20, 100, 200, 100)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 2), 14)
                            .withScale(scale)
                            .withBounds(20, 100, 200, 100)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 3), 14)
                            .withScale(scale)
                            .withBounds(20, 100, 200, 100)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 4), 14)
                            .withScale(scale)
                            .withBounds(20, 100, 200, 100)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 5), 14)
                            .withScale(scale)
                            .withBounds(20, 100, 200, 100)
                            .build(),
            });

            put("SHOOT_LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 15)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(100, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 15)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(100, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 15)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(100, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 15)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(100, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 15)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(100, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 15)
                            .withScale(scale)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(100, 35, 100, 150)
                            .build()
            });

            put("SHOOT_RIGHT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 15)
                            .withScale(scale)
                            .withBounds(15, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 15)
                            .withScale(scale)
                            .withBounds(15, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 15)
                            .withScale(scale)
                            .withBounds(15, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 15)
                            .withScale(scale)
                            .withBounds(15, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 15)
                            .withScale(scale)
                            .withBounds(15, 35, 100, 150)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 15)
                            .withScale(scale)
                            .withBounds(15, 35, 100, 150)
                            .build()
            });
            put("DEATH", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(1, 0), 20)
                            .withScale(scale)
                            .withBounds(0,0,0,0)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 1), 20)
                            .withScale(scale)
                            .withBounds(0,0,0,0)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 2), 20)
                            .withScale(scale)
                            .withBounds(0,0,0,0)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 3), 20)
                            .withScale(scale)
                            .withBounds(0,0,0,0)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 4), 20)
                            .withScale(scale)
                            .withBounds(0,0,0,0)
                            .build(),
            });
        }};
    }

    public enum WaterWizardState {
        SWIM, SHOOT_WAIT, SHOOT, DEATH
    }
}
