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
public class Fireboss extends Enemy {

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

        protected int lives = 3;
        protected Player player;

        public Fireboss(Point startLocation, Point endLocation, Direction facingDirection) {
                super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("FireBoss.png"), 41, 49),
                                "STAND_RIGHT");
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
                        currentAnimationName = "STAND_RIGHT";
                } else if (facingDirection == Direction.LEFT) {
                        currentAnimationName = "STAND_RIGHT";
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
                                        bossState = BossState.SHOOT_WAIT;
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
                                if (lives >= 3)  {
                                        this.currentAnimationName = facingDirection == Direction.RIGHT
                                                        ? "STAND_LEFT"
                                                        : "STAND_LEFT";
                                } else if (lives >= 2) {
                                        this.currentAnimationName = facingDirection == Direction.RIGHT
                                                        ? "STAND_LEFT"
                                                        : "STAND_LEFT";
                                } else if (lives >= 1) {
                                        this.currentAnimationName = facingDirection == Direction.RIGHT
                                                        ? "STAND_LEFT"
                                                        : "STAND_LEFT";
                                }
                                
                        
                                shootTimer = 100;
                                // This line of code takes the current direction the WaterWizard is facing and
                                // makes it shoot in that direction
                                // If facingdirection is right, it shoots right, else it shoots left
                                // currentAnimationName = facingDirection == Direction.RIGHT ? "SHOOT_LEFT" :
                                // "SHOOT_LEFT";
                                if (this.getX() > player.getX()) {
                                        if (lives >= 1)  {
                                                currentAnimationName = "STAND_LEFT";
                                        } 
                                } else {
                                        if (lives >= 1)  {
                                                currentAnimationName = "STAND_LEFT";
                                        } 
                                }
                        } else if (shootTimer == 0 && (currentAnimationName != "HURT_LEFT" || currentAnimationName != "HURT_LEFT") && currentAnimationName != "HIDDEN") {
                                bossState = BossState.SHOOT;
                        } else {
                                shootTimer--;
                        }
                }
                else if (bossState == BossState.HURT) {
                        if(lives > 1){
                                if (this.getX() + 250 > player.getX()){
                                        currentAnimationName = "HURT_LEFT";
                                }
                                else{
                                       currentAnimationName = "HURT_LEFT"; 
                                }                                
                        }
                        else {
                                this.currentAnimationName = facingDirection == Direction.RIGHT
                                                        ? "DEATH_LEFT"
                                                        : "DEATH_LEFT";   
                        }
                }
                // this is for actually having the WaterWizard shoot the wave
                else if (bossState == BossState.SHOOT) {
                        this.currentAnimationName = "HIDDEN";
                        // define where WAVE will spawn on map (x location) relative to WaterWizard's
                        // location
                        // and define its movement speed
                        int waveX;
                        if (currentAnimationName == "HIDDEN") {
                                waveX = Math.round(getX()) + getWidth() + 35;
                        } else {
                                waveX = Math.round(getX() - 95);

                        }

                        // define where wave will spawn on the map (y location) relative to
                        // WaterWizard's location
                        int waveY = Math.round(getY() - 16);

                        // create Wave enemies
                        //Wave waveAttackLeft = new Wave(waveX, waveY, Direction.LEFT);
                        //Wave waveAttackRight = new Wave(waveX, waveY, Direction.RIGHT);

                        //create FireBall enemies
                        //KrakenEnemy Kraken = new KrakenEnemy(new Point(waveX - 305, waveY - 100),  150);

                        // add WAVE enemy to the map for it to spawn in the level
                        //map.addEnemy(Kraken);
                        // change Boss back to its Stand state after shooting, reset shootTimer to wait
                        // a certain number of frames before shooting again
                        bossState = BossState.STAND;
                                

                        // reset shoot wait timer so the process can happen again (STAND around, then
                        // waits, then shoots)
                        shootWaitTimer = 150;
                        super.update(player);
                }
                // will need to be changed to electric attack
                if (activeRockAttack != null){
                        if (intersects(activeRockAttack)){
                                bossState = BossState.HURT;
                                isInvincible = true;
                                isInvincibleCounter = 40;
                                shootWaitTimer = 150;
                                enemyAttacked(this);
                                // broadcast so the fireball disappears
                                ElementalAbilityListenerManager.rockAttackKilledEnemy();
                        }
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
                float scale = 1.2f;
                return new HashMap<String, Frame[]>() {
                        {
                                put("STAND_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                                                                .withScale(scale)
                                                                //.withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(21, 24, 9, 12)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                                                                .withScale(scale)
                                                                //.withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(19, 20, 12, 16)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 2), 14)
                                                                .withScale(scale)
                                                                //.withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(17, 17, 15, 23)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 3), 14)
                                                                .withScale(scale)
                                                                //.withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(23, 6, 33, 40)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 4), 14)
                                                                .withScale(scale)
                                                                //.withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(10, 6, 36, 48)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 5), 14)
                                                                .withScale(scale)
                                                                //.withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(10, 4, 34, 46)
                                                                .build(),
                                                
                                });
                                put("STAND_LEFT", new Frame[] {
                                    new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                                    .withScale(scale)
                                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                    .withBounds(21, 24, 9, 12)
                                    .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                                    .withScale(scale)
                                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                    .withBounds(19, 20, 12, 16)
                                    .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 14)
                                    .withScale(scale)
                                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                    .withBounds(17, 17, 15, 23)
                                    .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 14)
                                    .withScale(scale)
                                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                    .withBounds(23, 6, 33, 40)
                                    .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 14)
                                    .withScale(scale)
                                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                    .withBounds(10, 6, 36, 48)
                                    .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), 14)
                                    .withScale(scale)
                                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                    .withBounds(10, 4, 34, 46)
                                    .build(),
                                });

                                put("HURT_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(1, 4), 15)
                                                                .withScale(scale)
                                                                .withBounds(6, 4, 38, 50)
                                                                .build(),
                                });

                                put("HURT_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(1, 4), 15)
                                                                .withScale(scale)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(6, 4,  38, 50)
                                                                .build(),
                                });
                                put("DEATH_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(3, 0), 20)
                                                                .withScale(scale)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 0), 20)
                                                                .withScale(scale)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 2), 20)
                                                                .withScale(scale)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 2), 20)
                                                                .withScale(scale)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 4), 20)
                                                                .withScale(scale)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 4), 20)
                                                                .withScale(scale)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 6), 20)
                                                                .withScale(scale)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 6), 20)
                                                                .withScale(scale)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 8), 20)
                                                                .withScale(scale)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 8), 20)
                                                                .withScale(scale)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 10), 20)
                                                                .withScale(scale)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                });
                                put("DEATH_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(3, 0), 20)
                                                                .withScale(scale)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 0), 20)
                                                                .withScale(scale)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 2), 20)
                                                                .withScale(scale)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 2), 20)
                                                                .withScale(scale)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 4), 20)
                                                                .withScale(scale)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 4), 20)
                                                                .withScale(scale)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 6), 20)
                                                                .withScale(scale)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 6), 20)
                                                                .withScale(scale)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 8), 20)
                                                                .withScale(scale)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 8), 20)
                                                                .withScale(scale)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 10), 20)
                                                                .withScale(scale)
                                                                .withBounds(85, 40, 40, 47)
                                                                .build(),

                                });
                                put("HIDDEN", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(2, 3), 20)
                                                                .withScale(1)
                                                                .withBounds(0, 0, 0, 0)
                                                                .build()

                                });
                                
                        }
                };
        }

        public enum BossState {
                STAND, SHOOT_WAIT, SHOOT, HURT, HIDDEN
        }
}
