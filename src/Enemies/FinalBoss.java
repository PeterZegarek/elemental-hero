package Enemies;

import Builders.FrameBuilder;
import Enemies.SlimeEnemy.SlimeState;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.ElementalAbilityListenerManager;
import Level.Enemy;
import Level.LevelState;
import Level.MapEntityStatus;
import Level.Player;
import Level.PlayerState;
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

        protected int isInvincibleCounter;
        protected boolean isInvincible = false;

        protected int lives = 15;
        protected Player player;

        public FinalBoss(Point startLocation, Point endLocation, Direction facingDirection) {
                super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("FinalBoss.png"), 159, 127),
                                "EARTH_STAND_LEFT");
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
                if (isInvincibleCounter > 0) {
                        isInvincibleCounter--;
                        if (isInvincibleCounter == 0) {
                                isInvincible = false;
                                lives--;
                                if(lives > 0){
                                        bossState = BossState.STAND;
                                } 
                                else{
                                        this.mapEntityStatus = MapEntityStatus.REMOVED; 
                                }
                        }
                        super.update(player);
                }

                // if shoot timer is up and WaterWizard is not currently shooting, set its state
                // to SHOOT
                if (shootWaitTimer == 0 && bossState != BossState.SHOOT_WAIT) {
                        bossState = BossState.SHOOT_WAIT;
                } else {
                        shootWaitTimer--;
                }

                // if BossWizard is waiting to shoot, it does its animations then throws the
                // WAVE
                // after this waiting period is over, the WAVE is thrown
                if (bossState == BossState.SHOOT_WAIT) {
                        if (previousBossState == BossState.STAND) {
                                if (lives >= 13)  {
                                        this.currentAnimationName = facingDirection == Direction.RIGHT
                                                        ? "EARTH_STAND_RIGHT"
                                                        : "EARTH_STAND_LEFT";
                                } else if (lives >= 10) {
                                        this.currentAnimationName = facingDirection == Direction.RIGHT
                                                        ? "FIRE_STAND_RIGHT"
                                                        : "FIRE_STAND_LEFT";
                                } else if (lives >= 7) {
                                        this.currentAnimationName = facingDirection == Direction.RIGHT
                                                        ? "WATER_STAND_RIGHT"
                                                        : "WATER_STAND_LEFT";
                                } else if (lives >= 4) {
                                        this.currentAnimationName = facingDirection == Direction.RIGHT
                                                        ? "ELECTRIC_STAND_RIGHT"
                                                        : "ELECTRIC_STAND_LEFT";
                                } else if (lives >= 1) {
                                        this.currentAnimationName = facingDirection == Direction.RIGHT
                                                        ? "AIR_STAND_RIGHT"
                                                        : "AIR_STAND_LEFT";
                                }
                                
                        
                                shootTimer = 40;
                                // This line of code takes the current direction the WaterWizard is facing and
                                // makes it shoot in that direction
                                // If facingdirection is right, it shoots right, else it shoots left
                                // currentAnimationName = facingDirection == Direction.RIGHT ? "SHOOT_RIGHT" :
                                // "SHOOT_LEFT";
                                if (this.getX() + 250 > player.getX()) {
                                        if (lives >= 13)  {
                                                currentAnimationName = "EARTH_SHOOT_LEFT";
                                        } else if (lives >= 10) {
                                                currentAnimationName = "FIRE_SHOOT_LEFT";
                                        } else if (lives >= 7) {
                                                currentAnimationName = "WATER_SHOOT_LEFT";
                                        } else if (lives >= 4) {
                                                currentAnimationName = "ELECTRIC_SHOOT_LEFT";
                                        } else if (lives >= 1) {
                                                currentAnimationName = "AIR_SHOOT_LEFT";
                                        }
                                } else {
                                        if (lives >= 13)  {
                                                currentAnimationName = "EARTH_SHOOT_RIGHT";
                                        } else if (lives >= 10) {
                                                currentAnimationName = "FIRE_SHOOT_RIGHT";
                                        } else if (lives >= 7) {
                                                currentAnimationName = "WATER_SHOOT_RIGHT";
                                        } else if (lives >= 4) {
                                                currentAnimationName = "ELECTRIC_SHOOT_RIGHT";
                                        } else if (lives >= 1) {
                                                currentAnimationName = "AIR_SHOOT_RIGHT";
                                        }
                                }
                        } else if (shootTimer == 0 && currentAnimationName != "BOSS_HURT_RIGHT" || currentAnimationName != "BOSS_HURT_LEFT") {
                                bossState = BossState.SHOOT;
                        } else {
                                shootTimer--;
                        }
                }
                else if (bossState == BossState.HURT) {
                        if(lives == 15 || lives == 14 || lives == 12 || lives == 11 || lives == 9 || 
                           lives == 8 || lives == 6 || lives == 5 ||lives == 3 || lives == 2){
                                if (this.getX() + 250 > player.getX()){
                                        currentAnimationName = "BOSS_HURT_LEFT";
                                }
                                else{
                                       currentAnimationName = "BOSS_HURT_RIGHT"; 
                                }                                
                        }
                        else if (lives == 13){
                                this.currentAnimationName = facingDirection == Direction.RIGHT
                                                        ? "EARTH_DEATH_RIGHT"
                                                        : "EARTH_DEATH_LEFT";   
                        }
                        else if (lives == 10){
                                this.currentAnimationName = facingDirection == Direction.RIGHT
                                                        ? "FIRE_DEATH_RIGHT"
                                                        : "FIRE_DEATH_LEFT";   
                        }
                        else if (lives == 7){
                                this.currentAnimationName = facingDirection == Direction.RIGHT
                                                        ? "WATER_DEATH_RIGHT"
                                                        : "WATER_DEATH_LEFT";   
                        }
                        else if (lives == 4){
                                this.currentAnimationName = facingDirection == Direction.RIGHT
                                                        ? "ELECTRIC_DEATH_RIGHT"
                                                        : "ELECTRIC_DEATH_LEFT";   
                        }
                        else if (lives == 1){
                                this.currentAnimationName = facingDirection == Direction.RIGHT
                                                        ? "AIR_DEATH_RIGHT"
                                                        : "AIR_DEATH_LEFT";   
                        }
                }
                // this is for actually having the WaterWizard shoot the wave
                else if (bossState == BossState.SHOOT) {
                        // define where WAVE will spawn on map (x location) relative to WaterWizard's
                        // location
                        // and define its movement speed
                        int waveX;
                        if (currentAnimationName == "EARTH_SHOOT_RIGHT" || currentAnimationName == "FIRE_SHOOT_RIGHT"
                                        || currentAnimationName == "WATER_SHOOT_RIGHT" ||
                                        currentAnimationName == "ELECTRIC_SHOOT_RIGHT"
                                        || currentAnimationName == "AIR_SHOOT_RIGHT") {
                                waveX = Math.round(getX()) + getWidth() + 35;
                        } else {
                                waveX = Math.round(getX() - 95);

                        }

                        // define where wave will spawn on the map (y location) relative to
                        // WaterWizard's location
                        int waveY = Math.round(getY() - 16);

                        // create Wave enemies
                        Wave waveAttackLeft = new Wave(waveX, waveY, Direction.LEFT);
                        Wave waveAttackRight = new Wave(waveX, waveY, Direction.RIGHT);

                        //create FireBall enemies
                        Fireball fireball1 = new Fireball(new Point(waveX, waveY), movementSpeed, 60);

                        // add WAVE enemy to the map for it to spawn in the level
                        if (previousAnimationName == "EARTH_SHOOT_LEFT" || previousAnimationName == "FIRE_SHOOT_LEFT" || previousAnimationName == "WATER_SHOOT_LEFT" || 
                            previousAnimationName == "ELECTRIC_SHOOT_LEFT" || previousAnimationName == "AIR_SHOOT_LEFT") {
                                if((bossState != BossState.HURT && (currentAnimationName != "BOSS_HURT_LEFT" || currentAnimationName != "BOSS_HURT_RIGHT")) || 
                                        (bossState != BossState.SHOOT_WAIT && (currentAnimationName != "BOSS_HURT_LEFT" || currentAnimationName != "BOSS_HURT_RIGHT"))){
                                        map.addEnemy(fireball1);
                                }
                        } else {
                                if((bossState != BossState.HURT && (currentAnimationName != "BOSS_HURT_LEFT" || currentAnimationName != "BOSS_HURT_RIGHT")) ||
                                    (bossState != BossState.SHOOT_WAIT && (currentAnimationName != "BOSS_HURT_LEFT" || currentAnimationName != "BOSS_HURT_RIGHT"))){
                                        map.addEnemy(fireball1);
                                }
                        }
                        // change Boss back to its Stand state after shooting, reset shootTimer to wait
                        // a certain number of frames before shooting again
                        bossState = BossState.STAND;

                        // reset shoot wait timer so the process can happen again (STAND around, then
                        // waits, then shoots)
                        shootWaitTimer = 150;
                        super.update(player);
                }

                super.update(player);

                previousBossState = bossState;
        }

        @Override
        public void enemyAttacked(Enemy enemy) {
                bossState = BossState.HURT;
                isInvincible = true;
                isInvincibleCounter = 40;
                shootWaitTimer = 150;
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
                                                new FrameBuilder(spriteSheet.getSprite(6, 0), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 1), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 2), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 3), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 4), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 5), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 6), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 7), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 8), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build()
                                });
                                put("FIRE_DEATH_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(6, 0), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 1), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 2), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 3), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 4), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 5), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 6), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 7), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 8), 40)
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
                                                new FrameBuilder(spriteSheet.getSprite(13, 0), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(13, 1), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(13, 2), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(13, 3), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(13, 4), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(13, 5), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(13, 6), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(13, 7), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(13, 8), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build()
                                });
                                put("WATER_DEATH_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(13, 0), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(13, 1), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(13, 2), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(13, 3), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(13, 4), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(13, 5), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(13, 6), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(13, 7), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(13, 8), 40)
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
                                                new FrameBuilder(spriteSheet.getSprite(20, 0), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(20, 1), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(20, 2), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(20, 3), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(20, 4), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(20, 5), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(20, 6), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(20, 7), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(20, 8), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build()
                                });
                                put("ELECTRIC_DEATH_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(20, 0), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(20, 1), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(20, 2), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(20, 3), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(20, 4), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(20, 5), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(20, 6), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(20, 7), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(20, 8), 40)
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
                                                new FrameBuilder(spriteSheet.getSprite(27, 0), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(27, 1), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(27, 2), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(27, 3), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(27, 4), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(27, 5), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(27, 6), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(27, 7), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(27, 8), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build()
                                });
                                put("AIR_DEATH_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(27, 0), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(27, 1), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(27, 2), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(27, 3), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(27, 4), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(27, 5), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(27, 6), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(27, 7), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(27, 8), 40)
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
                                                new FrameBuilder(spriteSheet.getSprite(34, 0), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(34, 1), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(34, 2), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(34, 3), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(34, 4), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(34, 5), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(34, 6), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(34, 7), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(34, 8), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build()
                                });
                                put("EARTH_DEATH_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(34, 0), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(34, 1), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(34, 2), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(34, 3), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(34, 4), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(34, 5), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(34, 6), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(34, 7), 40)
                                                                .withScale(scale)
                                                                .withBounds(55, 65, 45, 50)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(34, 8), 40)
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
                                                new FrameBuilder(spriteSheet.getSprite(5, 1), 40)
                                                                .withScale(scale)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(0,0,0,0)
                                                                .build()
                                });
                                put("BOSS_HURT_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(5, 1), 40)
                                                                .withScale(scale)
                                                                .withBounds(0,0,0,0)
                                                                .build()
                                });
                        }
                };
        }

        public enum BossState {
                STAND, SHOOT_WAIT, SHOOT, HURT
        }
}
