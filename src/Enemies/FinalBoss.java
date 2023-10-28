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

// This class is for the Angry Boss that throws WAVEs
// It WALKs back and forth between two set points (startLocation and endLocation)
// Every so often (based on shootTimer) it will throw a WAVE enemy
public class FinalBoss extends Enemy {

    // start and end location defines the two points that it WALKs between
    // is only made to WALK along the x axis and has no air ground state logic, so
    // make sure both points have the same Y value
    protected Point startLocation;
    protected Point endLocation;

    protected float movementSpeed = 1f;
    private Direction startFacingDirection;
    protected Direction facingDirection;
    protected AirGroundState airGroundState;

    // timer is used to determine how long Boss freezes in place before shooting
    // branch
    protected int shootWaitTimer;

    // timer is used to determine when a branch is to be shot out
    protected int shootTimer;

    // can be either WALK or SHOOT based on what the enemy is currently set to do
    protected BossState BossState;
    protected BossState previousBossState;

    protected BossPhase BossPhase;
    protected BossPhase previousBossPhase;

    protected int isInvincibleCounter;
    protected boolean isInvincible;

    protected int lives = 15;
    protected Player player;

    public FinalBoss(Point startLocation, Point endLocation, Direction facingDirection) {
        super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("BossEnemy.png"), 215, 226),
                "BOSS_STAND_LEFT");
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startFacingDirection = facingDirection;
        this.initialize();
    }

    @Override
    public void initialize() {
        // Add the Boss enemy as an enemy to listen for elemental abilities
        ElementalAbilityListenerManager.addEnemyListener(this);
        super.initialize();
        BossState = BossState.WALK;
        BossPhase = BossPhase.Fire;
        previousBossState = BossState;

        facingDirection = startFacingDirection;
        if (facingDirection == Direction.RIGHT) {
            currentAnimationName = "BOSS_FIRE_WALK_RIGHT"; // Fire Walk Here
        } else if (facingDirection == Direction.LEFT) {
            currentAnimationName = "BOSS_FIRE_WALK_RIGHT"; // Fire Walk Here
        }
        airGroundState = AirGroundState.GROUND;

        // every certain number of frames, the Wave will be shot out
        shootWaitTimer = 65;
    }

    @Override
    public void update() {

        // cooldown counter for invincibility
        if (isInvincibleCounter > 0) {
            isInvincibleCounter--;
            if (isInvincibleCounter == 0) {
                isInvincible = false;
            }
        }

        handleBossPhase();

        // if shoot timer is up and Boss is not currently shooting, set its state to
        // SHOOT
        if (shootWaitTimer == 0 && BossState != BossState.SHOOT_WAIT) {
            BossState = BossState.SHOOT;
        } else {
            shootWaitTimer--;
        }
        handleBossAnimation();

        super.update(player);

        previousBossState = BossState;

    }

    private void handleBossPhase() {

        if (lives > 12) {
            BossPhase = BossPhase.Earth;
        } else if (lives > 9) {
            BossPhase = BossPhase.Fire;
        } else if (lives > 6) {
            BossPhase = BossPhase.Water;
        } else if (lives > 3) {
            BossPhase = BossPhase.Electric;
        } else if (lives > 0) {
            BossPhase = BossPhase.Air;
        } else if (lives == 0) {
            BossState = BossState.DEATH;
        }
    }

    // anything extra the Boss should do based on interactions can be handled here
    protected void handleBossAnimation() {

        if (BossState == BossState.SHOOT_WAIT) {

            if (BossPhase == BossPhase.Earth) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "BOSS_EARTH_STAND_RIGHT" : "BOSS_EARTH_STAND_LEFT";
            } else if (BossPhase == BossPhase.Fire) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "BOSS_FIRE_STAND_RIGHT" : "BOSS_FIRE_STAND_LEFT";
            } else if (BossPhase == BossPhase.Water) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "BOSS_WATER_STAND_RIGHT" : "BOSS_WATER_STAND_LEFT";
            } else if (BossPhase == BossPhase.Electric) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "BOSS_ELECTRIC_STAND_RIGHT" : "BOSS_ELECTRIC_STAND_LEFT";
            } else if (BossPhase == BossPhase.Air) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "BOSS_AIR_STAND_RIGHT" : "BOSS_AIR_STAND_LEFT";
            }

            // Makes Hurt Animation stay while standing
            if (isInvincible == true) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "BOSS_HURT_RIGHT" : "BOSS_HURT_LEFT";
            }
        }

        else if (BossState == BossState.HURT) {
            this.currentAnimationName = facingDirection == Direction.RIGHT ? "BOSS_HURT_RIGHT" : "BOSS_HURT_LEFT";
        }

        else if (BossState == BossState.WALK) {

            if (BossPhase == BossPhase.Earth) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "BOSS_EARTH_WALK_RIGHT" : "BOSS_EARTH_WALK_LEFT";
            } else if (BossPhase == BossPhase.Fire) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "BOSS_FIRE_WALK_RIGHT" : "BOSS_FIRE_WALK_LEFT";
            } else if (BossPhase == BossPhase.Water) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "BOSS_WATER_WALK_RIGHT" : "BOSS_WATER_WALK_LEFT";
            } else if (BossPhase == BossPhase.Electric) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "BOSS_ELECTRIC_WALK_RIGHT" : "BOSS_ELECTRIC_WALK_LEFT";
            } else if (BossPhase == BossPhase.Air) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "BOSS_AIR_WALK_RIGHT" : "BOSS_AIR_WALK_LEFT";
            }
            // Makes Hurt Animation stay while walking
            if (isInvincible == true) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "BOSS_HURT_RIGHT" : "BOSS_HURT_LEFT";
            }
        }

        else if (BossState == BossState.SHOOT) {

            if (BossPhase == BossPhase.Earth) {
                if (this.getX() > player.getX()) {
                    currentAnimationName = "BOSS_EARTH_SHOOT_RIGHT";
                } else {
                    currentAnimationName = "BOSS_EARTH_SHOOT_LEFT";
                }
            } else if (BossPhase == BossPhase.Fire) {
                if (this.getX() > player.getX()) {
                    currentAnimationName = "BOSS_FIRE_SHOOT_RIGHT";
                } else {
                    currentAnimationName = "BOSS_FIRE_SHOOT_LEFT";
                }
            } else if (BossPhase == BossPhase.Water) {
                if (this.getX() > player.getX()) {
                    currentAnimationName = "BOSS_WATER_SHOOT_RIGHT";
                } else {
                    currentAnimationName = "BOSS_WATER_SHOOT_LEFT";
                }
            } else if (BossPhase == BossPhase.Electric) {
                if (this.getX() > player.getX()) {
                    currentAnimationName = "BOSS_ELECTRIC_SHOOT_RIGHT";
                } else {
                    currentAnimationName = "BOSS_ELECTRIC_SHOOT_LEFT";
                }
            } else if (BossPhase == BossPhase.Air) {
                if (this.getX() > player.getX()) {
                    currentAnimationName = "BOSS_AIR_SHOOT_RIGHT";
                } else {
                    currentAnimationName = "BOSS_AIR_SHOOT_LEFT";
                }
            }
        }

        else if (BossState == BossState.DEATH) {
            this.currentAnimationName = facingDirection == Direction.RIGHT ? "BOSS_DEATH" : "BOSS_DEATH";
        }
    }

    protected void handleBossState() {
        switch (BossState) {
            case HURT:
                BossHurt();
                break;
            case WALK:
                BossWalking();
                break;
            case SHOOT:
                BossShoot();
                break;
            case SHOOT_WAIT:
                BossShootWait(player);
                break;
            case DEATH:
                BossDeath();
                break;
        }
    }

    protected void BossShootWait(Player player) {
        // if Boss is waiting to shoot, it does its animations then throws the WAVE
        // after this waiting period is over, the WAVE is thrown
        if (previousBossState == BossState.WALK) {
            shootTimer = 40;
            // This line of code takes the current direction the Boss is facing and makes it
            // shoot in that direction
            // If facingdirection is right, it shoots right, else it shoots left
            // currentAnimationName = facingDirection == Direction.RIGHT ? "SHOOT_RIGHT" :
            // "SHOOT_LEFT";
        } else if (shootTimer == 0) {
            BossState = BossState.SHOOT;
        } else {
            shootTimer--;
        }
    }

    protected void BossWalking() {

        float startBound = startLocation.x;
        float endBound = endLocation.x;

        // if Boss is WALKING, determine which direction to WALK in based on facing
        // direction
        if (BossState == BossState.WALK) {
            if (facingDirection == Direction.RIGHT) {
                moveXHandleCollision(movementSpeed);
            } else {
                moveXHandleCollision(-movementSpeed);
            }

            // if Boss reaches the start or end location, it turns around
            // dinosaur may end up going a bit past the start or end location depending on
            // movement speed
            // this calculates the difference and pushes the enemy back a bit so it ends up
            // right on the start or end location
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

    }

    protected void BossHurt() {
        isInvincible = true;
        BossState = BossState.WALK; // Allows player to move after going into Hurt animation
        isInvincibleCounter = 120; // Keeps Player in a state of Hurt (ADJUST AS SEE FIT)
        handleBossAnimation();
    }

    protected void BossShoot() {

        // define where WAVE will spawn on map (x location) relative to Boss's location
        // and define its movement speed
        int attackX;
        if (facingDirection == facingDirection.RIGHT) {
            attackX = Math.round(getX()) + getWidth() + 35;
        } else {
            attackX = Math.round(getX() - 95);

        }

        // define where wave will spawn on the map (y location) relative to Boss's
        // location
        int attackY = Math.round(getY()) - 16;

        /* 
        // create Things to shoot out HERE!
        Wave waveAttackLeft = new Wave(attackX, attackY, Direction.LEFT);

        Wave waveAttackRight = new Wave(attackX, attackY, Direction.RIGHT);

        // add WAVE enemy to the map for it to spawn in the level
        if (previousAnimationName == "BOSS_EARTH_SHOOT_LEFT") {
            map.addEnemy(waveAttackLeft);
        } 
        else {
            map.addEnemy(waveAttackRight);
        }

        if(previousAnimationName == "BOSS_FIRE_SHOOT_LEFT"){
            map.addEnemy(waveAttackLeft);
        } 
        else {
            map.addEnemy(waveAttackRight);
        }

        if(previousAnimationName == "BOSS_WATER_SHOOT_LEFT"){
            map.addEnemy(waveAttackLeft);
        } 
        else {
            map.addEnemy(waveAttackRight);
        }

        if(previousAnimationName == "BOSS_ELECTRIC_SHOOT_LEFT"){
            map.addEnemy(waveAttackLeft);
        } 
        else {
            map.addEnemy(waveAttackRight);
        }

        if(previousAnimationName == "BOSS_AIR_SHOOT_LEFT"){
            map.addEnemy(waveAttackLeft);
        } 
        else {
            map.addEnemy(waveAttackRight);
        }
        */

        // change Boss back to its WALK state after shooting, reset shootTimer to wait a
        // certain number of frames before shooting again
        BossState = BossState.WALK;

        // reset shoot wait timer so the process can happen again (Boss WALKs around,
        // then waits, then shoots)
        shootWaitTimer = 150;
    }

    protected void BossDeath() {
        isInvincible = true;
        isInvincibleCounter = 20;
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // if dinosaur enemy collides with something on the x axis, it turns around and
        // WALKs the other way
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

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        float scale = .5f;
        return new HashMap<String, Frame[]>() {
            {
                put("BOSS_EARTH_WALK_LEFT", new Frame[] {
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

                put("BOSS_EARTH_WALK_RIGHT", new Frame[] {
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

                put("BOSS_EARTH_SHOOT_LEFT", new Frame[] {
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

                put("BOSS_EARTH_SHOOT_RIGHT", new Frame[] {
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
                put("BOSS_DEATH", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(1, 0), 20)
                                .withScale(scale)
                                .withBounds(20, 100, 200, 100)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 1), 20)
                                .withScale(scale)
                                .withBounds(20, 100, 200, 100)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 2), 20)
                                .withScale(scale)
                                .withBounds(20, 100, 200, 100)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 3), 20)
                                .withScale(scale)
                                .withBounds(20, 100, 200, 100)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 4), 20)
                                .withScale(scale)
                                .withBounds(20, 100, 200, 100)
                                .build(),
                });
            }
        };
    }

    public enum BossState {
        WALK, DEATH, SHOOT_WAIT, SHOOT, HURT
    }

    public enum BossPhase {
        Fire, Water, Earth, Electric, Air
    }
}
