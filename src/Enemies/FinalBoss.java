package Enemies;

import Builders.FrameBuilder;
import Enemies.WaterWizardEnemy.WaterWizardState;
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
    protected BossState bossState;
    protected BossState previousBossState;

    protected BossPhase BossPhase;
    protected BossPhase previousBossPhase;

    protected int isInvincibleCounter;
    protected boolean isInvincible;

    protected int lives = 12;
    protected Player player;

    public FinalBoss(Point startLocation, Point endLocation, Direction facingDirection) {
        super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("FinalBoss.png"), 159, 127),"EARTH_STAND_LEFT");
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
        
        BossPhase = BossPhase.Earth;
        previousBossPhase = BossPhase;

        bossState = BossState.STAND;
        previousBossState = bossState;

        facingDirection = startFacingDirection;
        if (facingDirection == Direction.RIGHT) {
            currentAnimationName = "EARTH_STAND_RIGHT"; 
        } else if (facingDirection == Direction.LEFT) {
            currentAnimationName = "EARTH_STAND_LEFT"; 
        }
        airGroundState = AirGroundState.GROUND;

        // every certain number of frames, the Wave will be shot out
        shootWaitTimer = 45;  
    }

    @Override
    public void update(Player player) {

        handleBossPhase();

        if (isInvincibleCounter > 0){           
            isInvincibleCounter--;
            if (isInvincibleCounter == 0){                                             
                isInvincible = false;               
                this.mapEntityStatus = MapEntityStatus.REMOVED;             
            }
        }

        // if shoot timer is up and WaterWizard is not currently shooting, set its state to SHOOT
        if (shootWaitTimer == 0 && bossState != BossState.SHOOT_WAIT) {
            bossState = BossState.SHOOT_WAIT;
        }
        else {
            shootWaitTimer--;
        }
        
        // if BossWizard is waiting to shoot, it does its animations then throws the WAVE
        // after this waiting period is over, the WAVE is thrown
        if (bossState == BossState.SHOOT_WAIT) {
            if (previousBossState == BossState.STAND) {
                if (BossPhase == BossPhase.Earth) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "EARTH_STAND_RIGHT" : "EARTH_STAND_LEFT";
                } 
                else if (BossPhase == BossPhase.Fire) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "FIRE_STAND_RIGHT" : "FIRE_STAND_LEFT";
                } 
                else if (BossPhase == BossPhase.Water) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "WATER_STAND_RIGHT" : "WATER_STAND_LEFT";
                } 
                else if (BossPhase == BossPhase.Electric) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "ELECTRIC_STAND_RIGHT" : "ELECTRIC_STAND_LEFT";
                } 
                else if (BossPhase == BossPhase.Air) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "AIR_STAND_RIGHT" : "AIR_STAND_LEFT";
                }

                // Makes Hurt Animation stay while standing
                if (isInvincible == true) {
                        this.currentAnimationName = facingDirection == Direction.RIGHT ? "BOSS_HURT_RIGHT" : "BOSS_HURT_LEFT";
                }

                shootTimer = 40;
                // This line of code takes the current direction the WaterWizard is facing and makes it shoot in that direction
                // If facingdirection is right, it shoots right, else it shoots left
                // currentAnimationName = facingDirection == Direction.RIGHT ? "SHOOT_RIGHT" : "SHOOT_LEFT";
                if (this.getX() > player.getX()){
                    if (BossPhase == BossPhase.Earth) {
                        currentAnimationName = "EARTH_SHOOT_LEFT";
                        } 
                    else if (BossPhase == BossPhase.Fire) {
                        currentAnimationName = "FIRE_SHOOT_LEFT";
                        }
                    else if (BossPhase == BossPhase.Water) {
                        currentAnimationName = "WATER_SHOOT_LEFT";
                        }
                    else if (BossPhase == BossPhase.Electric) {
                        currentAnimationName = "ELECTRIC_SHOOT_LEFT";
                        }
                    else if (BossPhase == BossPhase.Air) {
                        currentAnimationName = "AIR_SHOOT_LEFT";
                        }
                }
                else{
                    if (BossPhase == BossPhase.Earth) {
                        currentAnimationName = "EARTH_SHOOT_RIGHT";
                        } 
                    else if (BossPhase == BossPhase.Fire) {
                        currentAnimationName = "FIRE_SHOOT_RIGHT";
                        }
                    else if (BossPhase == BossPhase.Water) {
                        currentAnimationName = "WATER_SHOOT_RIGHT";
                        }
                    else if (BossPhase == BossPhase.Electric) {
                        currentAnimationName = "ELECTRIC_SHOOT_RIGHT";
                        }
                    else if (BossPhase == BossPhase.Air) {
                        currentAnimationName = "AIR_SHOOT_RIGHT";
                        }
                    }
            } 
            else if (shootTimer == 0) {
                bossState = BossState.SHOOT;
            }
            else {
                shootTimer--;
            }
        }
        // this is for actually having the WaterWizard shoot the wave
        else if (bossState == BossState.SHOOT) {
            // define where WAVE will spawn on map (x location) relative to WaterWizard's location            
            // and define its movement speed
            int waveX;
            if (currentAnimationName == "EARTH_SHOOT_RIGHT" || currentAnimationName == "FIRE_SHOOT_RIGHT" || currentAnimationName == "WATER_SHOOT_RIGHT" ||
                currentAnimationName == "ELECTRIC_SHOOT_RIGHT" || currentAnimationName == "AIR_SHOOT_RIGHT") {
                waveX = Math.round(getX()) + getWidth() + 35;
            } else {
                waveX = Math.round(getX() - 95);

            }

            // define where wave will spawn on the map (y location) relative to WaterWizard's location
            int waveY = Math.round(getY()) - 16;

            // create Wave enemy
            Wave waveAttackLeft = new Wave(waveX, waveY, Direction.LEFT);

            Wave waveAttackRight = new Wave(waveX, waveY, Direction.RIGHT);

            // add WAVE enemy to the map for it to spawn in the level
            if(previousAnimationName == "EARTH_SHOOT_LEFT" || previousAnimationName == "FIRE_SHOOT_LEFT" || previousAnimationName == "WATER_SHOOT_LEFT" ||
                previousAnimationName == "ELECTRIC_SHOOT_LEFT" || previousAnimationName == "AIR_SHOOT_LEFT"){
                map.addEnemy(waveAttackLeft);          
            }
            else{
                map.addEnemy(waveAttackRight);
            }
            // change Boss back to its Stand state after shooting, reset shootTimer to wait a certain number of frames before shooting again
            bossState = BossState.STAND;

            // reset shoot wait timer so the process can happen again (STAND around, then waits, then shoots)
            shootWaitTimer = 150;
        }
        else if (bossState == BossState.HURT) {
            this.currentAnimationName = facingDirection == Direction.RIGHT ? "BOSS_HURT_RIGHT" : "BOSS_HURT_LEFT";
        }

        else if (bossState == BossState.DEATH) {
                if(facingDirection == Direction.RIGHT){
                        if (BossPhase == BossPhase.Earth) {
                                currentAnimationName = "EARTH_DEATH_RIGHT";
                        } 
                        else if (BossPhase == BossPhase.Fire) {
                                currentAnimationName = "FIRE_DEATH_RIGHT";
                        }
                        else if (BossPhase == BossPhase.Water) {
                                currentAnimationName = "WATER_DEATH_RIGHT";
                        }
                        else if (BossPhase == BossPhase.Electric) {
                                currentAnimationName = "ELECTRIC_DEATH_RIGHT";
                        }
                        else if (BossPhase == BossPhase.Air) {
                                currentAnimationName = "AIR_DEATH_RIGHT";
                        }
                }
                else if(facingDirection == Direction.LEFT){
                        if (BossPhase == BossPhase.Earth) {
                                currentAnimationName = "EARTH_DEATH_LEFT";
                        } 
                        else if (BossPhase == BossPhase.Fire) {
                                currentAnimationName = "FIRE_DEATH_LEFT";
                        }
                        else if (BossPhase == BossPhase.Water) {
                                currentAnimationName = "WATER_DEATH_LEFT";
                        }
                        else if (BossPhase == BossPhase.Electric) {
                                currentAnimationName = "ELECTRIC_DEATH_LEFT";
                        }
                        else if (BossPhase == BossPhase.Air) {
                                currentAnimationName = "AIR_DEATH_LEFT";
                        }
                }

        }
        
        
        
        super.update(player);

        previousBossState = bossState;
    }

    private void handleBossPhase() {

        if (lives > 12) {
            BossPhase = BossPhase.Earth;
        } 
        else if (lives > 9) {
            BossPhase = BossPhase.Fire;
        } 
        else if (lives > 6) {
            BossPhase = BossPhase.Water;
        } 
        else if (lives > 3) {
            BossPhase = BossPhase.Electric;
        } 
        else if (lives > 0) {
            BossPhase = BossPhase.Air;
        } 
        else{
            bossState = BossState.DEATH;
        }
    }

    protected void BossHurt() {
        isInvincible = true;
        bossState = BossState.STAND; // Allows player to move after going into Hurt animation
        isInvincibleCounter = 140; // Keeps Player in a state of Hurt (ADJUST AS SEE FIT)
    }

    protected void BossDeath() {
        isInvincible = true;
        isInvincibleCounter = 40;
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        float scale = 4f;
        return new HashMap<String, Frame[]>() {
            {
                put("FIRE_STAND_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),                      
                });

                put("FIRE_STAND_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)                            
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build()             
                });
                put("FIRE_DEATH_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(6, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(6, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(6, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(6, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(6, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(6, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(6, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(6, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(6, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build()
                });
                put("FIRE_DEATH_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(6, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(6, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(6, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(6, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(6, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(6, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(6, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(6, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(6, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build()
                });
                put("FIRE_SHOOT_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(2, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 9), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 10), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 11), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 12), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build()
                });
                put("FIRE_SHOOT_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(2, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 9), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 10), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 11), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 12), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build()
                });
                put("WATER_STAND_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(7, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(7, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(7, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(7, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(7, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(7, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(7, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(7, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),                  
                });

                put("WATER_STAND_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(7, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)                            
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(7, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(7, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(7, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(7, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(7, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(7, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(7, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build()             
                });
                put("WATER_DEATH_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(13, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(13, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(13, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(13, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(13, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(13, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(13, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(13, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(13, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build()
                });
                put("WATER_DEATH_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(13, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(13, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(13, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(13, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(13, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(13, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(13, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(13, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(13, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build()
                });
                put("WATER_SHOOT_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(9, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 9), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 10), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 11), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 12), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build()
                });
                put("WATER_SHOOT_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(9, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 9), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 10), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 11), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(9, 12), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build()
                });
                put("ELECTRIC_STAND_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(14, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(14, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(14, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(14, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(14, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(14, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(14, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(14, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),         
                });

                put("ELECTRIC_STAND_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(14, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)                            
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(14, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(14, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(14, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(14, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(14, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(14, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(14, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build()             
                });
                put("ELECTRIC_DEATH_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(21, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build()
                });
                put("ELECTRIC_DEATH_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(21, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build()
                });
                put("ELECTRIC_SHOOT_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(16, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 9), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 10), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 11), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 12), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build()
                });
                put("ELECTRIC_SHOOT_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(16, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 9), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 10), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 11), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(16, 12), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build()
                });               
                put("AIR_STAND_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(21, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),                     
                });
                put("AIR_STAND_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(21, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)                            
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(21, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build()             
                });
        
                put("AIR_DEATH_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(27, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(27, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(27, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(27, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(27, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(27, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(27, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(27, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(27, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build()
                });
                put("AIR_DEATH_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(27, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(27, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(27, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(27, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(27, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(27, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(27, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(27, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(27, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build()
                });
                put("AIR_SHOOT_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(23, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 9), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 10), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 11), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 12), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build()
                });
                put("AIR_SHOOT_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(23, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 9), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 10), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 11), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(23, 12), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build()
                });               
                put("EARTH_STAND_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(28, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(28, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(28, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(28, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(28, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(28, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(28, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(28, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),                    
                });
                put("EARTH_STAND_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(28, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)                            
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(28, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(28, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(28, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(28, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(28, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(28, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(28, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),            
                });
                put("EARTH_DEATH_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(34, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(34, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(34, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(34, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(34, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(34, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(34, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(34, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(34, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build()
                });
                put("EARTH_DEATH_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(34, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(34, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(34, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(34, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(34, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(34, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(34, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(34, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(34, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build()
                });               
                put("EARTH_SHOOT_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(30, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 9), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 10), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 11), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 12), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build()
                });
                put("EARTH_SHOOT_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(30, 0), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 2), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 3), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 4), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 5), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 6), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 7), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 8), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 9), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 10), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 11), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(30, 12), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build()
                });
                put("BOSS_HURT_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(5, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)

                                .build()              
                });
                put("BOSS_HURT_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(5, 1), 14)
                                .withScale(scale)
                                .withBounds(55, 65, 45, 50)
                                .build()        
                });               
            }
        };
    }

    public enum BossState {
        STAND, DEATH, SHOOT_WAIT, SHOOT, HURT
    }

    public enum BossPhase {
        Fire, Water, Earth, Electric, Air
    }
}
