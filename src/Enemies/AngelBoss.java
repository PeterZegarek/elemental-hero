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
import java.util.Random;

public class AngelBoss extends Enemy {
    // start and end location defines the two points it flies between
    protected Point startLocation; //left
    protected Point endLocation; //right

    protected float movementSpeed = 2f;
    private Direction startFacingDirection;
    protected Direction facingDirection;
    protected AirGroundState airGroundState;

    // timer is used to determine how long angel freezes before shooting arrow
    protected int shootWaitTimer;

    // timer is used to determine when an arrow is to be shot
    protected int shootTimer;

    //determines how long angel stays hurt
    protected int hurtTimer;

    //determines length of death animation
    protected int deathTimer;

    protected int lives;

    protected AngelState angelState;
    protected AngelState previousAngelState;

    public AngelBoss(Point startLocation, Point endLocation, Direction facingDirection) {
        super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("AngelBoss.png"), 63, 89), "FLY_RIGHT");
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startFacingDirection = facingDirection;
        this.initialize();
    }

    @Override
    public void initialize() {
        // Add the angel as an enemy to listen for elemental abilities
        ElementalAbilityListenerManager.addEnemyListener(this);
        super.initialize();
        angelState = AngelState.FLY;
        previousAngelState = angelState;
        facingDirection = startFacingDirection;
        if (facingDirection == Direction.RIGHT) {
            currentAnimationName = "FLY_RIGHT";
        } else if (facingDirection == Direction.LEFT) {
            currentAnimationName = "FLY_LEFT";
        }
        airGroundState = AirGroundState.GROUND;

        lives = 3;

        // every certain number of frames, the arrow will be shot out
        shootWaitTimer = 45; //65
    }

    @Override
    public void update(Player player) {
        float startBoundX = startLocation.x;
        float endBoundX = endLocation.x;

        // if shoot timer is up and angel is not currently shooting, set its state to SHOOT
        if (shootWaitTimer == 0 && angelState != AngelState.SHOOT_WAIT) {
            angelState = AngelState.SHOOT_WAIT;
        }
        else if(angelState!=AngelState.HURT && angelState!=AngelState.DEAD){ //If angel is not hurt or dead, count down the shootWait timer
            shootWaitTimer--;
        }

        // if angel is flying, determine which direction to fly in based on facing direction
        if (angelState == AngelState.FLY) {
            if (facingDirection == Direction.RIGHT) {
                currentAnimationName = "FLY_RIGHT";
                moveXHandleCollision(movementSpeed);
            } else {
                currentAnimationName = "FLY_LEFT";
                moveXHandleCollision(-movementSpeed);
            }

            // if angel reaches the start or end location, it turns around
            // angel may end up going a bit past the start or end location depending on movement speed
            // this calculates the difference and pushes the boss back a bit so it ends up right on the start or end location
            if (getX1() + getWidth() >= endBoundX) {
                float difference = endBoundX - (getX2());
                moveXHandleCollision(-difference);
                facingDirection = Direction.LEFT;
            } else if (getX1() <= startBoundX) {
                float difference = startBoundX - getX1();
                moveXHandleCollision(difference);
                facingDirection = Direction.RIGHT;
            }
        }

        // if angel is waiting to shoot, it first holds a bow for a set number of frames
        // after this waiting period is over, the arrow is actually shot out
        if (angelState == AngelState.SHOOT_WAIT) {
            if (previousAngelState == AngelState.FLY) {
                shootTimer = 45; //65
                currentAnimationName = facingDirection == Direction.RIGHT ? "SHOOT_RIGHT" : "SHOOT_LEFT";
            } else if (shootTimer == 0) {
                angelState = AngelState.SHOOT;
            }
            else {
                shootTimer--;
            }
        }

        // this is for actually having the angel shoot the arrow
        if (angelState == AngelState.SHOOT) {
            // define where arrow will spawn on map (x location) relative to angel's location
            int arrowX;
            if (facingDirection == Direction.RIGHT) {
                arrowX = Math.round(getX()) + getWidth();
            } else {
                arrowX = Math.round(getX() - 21);
            }

            // define where arrow will spawn on the map (y location) relative to angel's location
            int arrowY = Math.round(getY()) + 4;

            Arrow arrow1, arrow2, arrow3, arrow4, arrow5, arrow6;
            Random rand = new Random();
            float momentumY = 120f;

            // create 6 Arrow enemies, each with randomized speeds but same momentumY
            arrow1 = new Arrow(new Point(arrowX, arrowY), 2*rand.nextFloat(2)+2, momentumY, "RIGHT");
            arrow2 = new Arrow(new Point(arrowX, arrowY), rand.nextFloat(2)+1, momentumY, "RIGHT");
            arrow3 = new Arrow(new Point(arrowX, arrowY), rand.nextFloat(2), momentumY, "RIGHT");
            arrow4 = new Arrow(new Point(arrowX, arrowY), -rand.nextFloat(2), momentumY, "LEFT");
            arrow5 = new Arrow(new Point(arrowX, arrowY), -rand.nextFloat(2)-1, momentumY, "LEFT");
            arrow6 = new Arrow(new Point(arrowX, arrowY), -2*rand.nextFloat(2)-2, momentumY, "LEFT");

            // add arrow enemy to the map for it to spawn in the level
            map.addEnemy(arrow1);
            map.addEnemy(arrow2);
            map.addEnemy(arrow3);
            map.addEnemy(arrow4);
            map.addEnemy(arrow5);
            map.addEnemy(arrow6);

            // change angel back to its FLY state after shooting, reset shootTimer to wait a certain number of frames before shooting again
            angelState = AngelState.FLY;

            // reset shoot wait timer so the process can happen again (angel flies around, then waits, then shoots)
            shootWaitTimer = 90; //130
        }

        //If angel is hurt, start the animation for whether the rock was thrown from the left or right
        //Then decrement the hurt timer until it hits 0, set its state back to FLY, and reset shootWaitTimer
        if(angelState == AngelState.HURT){
            if(player.getFacingDirection() == Direction.RIGHT && currentAnimationName!="HURT_RIGHT") currentAnimationName = "HURT_LEFT";
            else if(currentAnimationName!="HURT_LEFT") currentAnimationName = "HURT_RIGHT";
            hurtTimer--;
            if(hurtTimer==0) {
                angelState = AngelState.FLY;
                shootWaitTimer = 45;
            }
        }

        //If angel dies, start the animation
        //When it changes to second part of the animation it will start moving down til it hits the ground
        //Then when the timer finishes it will disappear
        if(angelState == AngelState.DEAD){
            currentAnimationName = "DEATH";
            deathTimer--;
            if(deathTimer<=180) moveYHandleCollision(1.5f);
            if(deathTimer<=0){
                enemyAttacked(this);
                ElementalAbilityListenerManager.rockAttackKilledEnemy();
            }
        }

        super.update(player);

        previousAngelState = angelState;


        // if there is a rock and it got hit
        if (activeRockAttack != null){
            if (intersects(activeRockAttack)){
                if(lives>1){ //When it has 3 or 2 lives it gets put into HURT state
                    lives--;
                    activeRockAttack = null; //Need to make the rock attacks null after first
                    angelState = AngelState.HURT; //intersection otherwise it will take away all lives
                    hurtTimer = 60;
                }else { //When it has 1 life it gets put into DEAD state
                    lives--;
                    activeRockAttack = null;
                    angelState = AngelState.DEAD;
                    deathTimer = 240;
                }
            }
        }
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // if angel enemy collides with something on the x axis, it turns around and walks the other way
        if (hasCollided) {
            if (direction == Direction.RIGHT) {
                facingDirection = Direction.LEFT;
                currentAnimationName = "FLY_LEFT";
                shootWaitTimer = 90;
            } else {
                facingDirection = Direction.RIGHT;
                currentAnimationName = "FLY_RIGHT";
            }
        }
    }

    @Override
    public void enemyAttacked(Enemy enemy){
        if(lives==0){
            isOnMap = false;
            // This makes the enemy dissapear
            enemy.setMapEntityStatus(MapEntityStatus.REMOVED);
        }
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("FLY_LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 14)
                            .withScale(1)
                            .withBounds(8, 28,41, 48)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 4), 14)
                            .withScale(1)
                            .withBounds(8, 28,41, 48)
                            .build()
            });

            put("FLY_RIGHT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 14)
                            .withScale(1)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 28,41, 48)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 4), 14)
                            .withScale(1)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 28,41, 48)
                            .build()
            });

            put("SHOOT_LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(1, 0))
                            .withScale(1)
                            .withBounds(8, 28,41, 48)
                            .build(),
            });

            put("SHOOT_RIGHT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(1, 0))
                            .withScale(1)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 28,41, 48)
                            .build(),
            });

            put("HURT_LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(1, 3))
                            .withScale(1)
                            .withBounds(8, 28,41, 48)
                            .build(),
            });

            put("HURT_RIGHT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(1, 3))
                            .withScale(1)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8, 28,41, 48)
                            .build(),
            });

            put("DEATH", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 6), 60)
                            .withScale(1)
                            .withBounds(8, 28,41, 48)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 180)
                            .withScale(1)
                            .withBounds(8, 28,41, 48)
                            .build()
            });
        }};
    }

    public enum AngelState {
        FLY, SHOOT_WAIT, SHOOT, HURT, DEAD
    }
}
