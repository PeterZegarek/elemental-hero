package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import EnhancedMapTiles.EndLevelBox;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.ElementalAbilityListenerManager;
import Level.Enemy;
import Level.MapEntityStatus;
import Level.Player;
import Players.PlayerFireball;
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;

import java.util.HashMap;

// This class is for the Angry Boss that throws fireballs
// It WALKs back and forth between two set points (startLocation and endLocation)
// Every so often (based on shootTimer) it will throw a fireball enemy
public class Fireboss extends Enemy {

        // start and end location defines the two points that it WALKs between
        // is only made to WALK along the x axis and has no air ground state logic, so
        // make sure both points have the same Y value
        protected Point startLocation;
        protected Point endLocation;

        // protected float movementSpeed = 1f;
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
                super(startLocation.x, startLocation.y,
                                new SpriteSheet(ImageLoader.load("FireBossUpdated.png"), 81, 81), "STAND_LEFT");
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
                        currentAnimationName = "STAND_LEFT";
                }
                airGroundState = AirGroundState.GROUND;

                // every certain number of frames, the fireball will be shot out
                shootWaitTimer = 45;
        }

        @Override
        public void update(Player player) {

                if (isInvincibleCounter > 0) {
                        isInvincibleCounter--;
                        if (isInvincibleCounter == 0) {
                                isInvincible = false;
                                lives--;
                                if (lives > 0) {
                                        bossState = BossState.STAND;
                                } else {
                                        this.mapEntityStatus = MapEntityStatus.REMOVED;
                                }
                        }
                        super.update(player);
                }
 
                if (bossState == BossState.STAND) {
                        if (this.getX() + 100 > player.getX()) {
                                currentAnimationName = "STAND_LEFT";
                        } else{
                                currentAnimationName = "STAND_RIGHT";
                        }
                }

                // if shoot timer is up and Boss is not currently shooting, set its state
                // to SHOOT
                if (shootWaitTimer == 0 && bossState != BossState.SHOOT_WAIT) {
                        bossState = BossState.SHOOT_WAIT;
                } else {
                        shootWaitTimer--;
                }

                // if Boss is waiting to shoot, it does its animations then throws the
                // fireball
                // after this waiting period is over, the fireball is thrown
                if (bossState == BossState.SHOOT_WAIT) {
                        if (previousBossState == BossState.STAND) {
                                if (lives >= 3) {
                                        this.currentAnimationName = facingDirection == Direction.RIGHT ? "STAND_RIGHT" : "STAND_LEFT";
                                } else if (lives >= 2) {
                                        this.currentAnimationName = facingDirection == Direction.RIGHT ? "STAND_RIGHT" : "STAND_LEFT";
                                } else if (lives >= 1) {
                                        this.currentAnimationName = facingDirection == Direction.RIGHT ? "STAND_RIGHT" : "STAND_LEFT";
                                }

                                shootTimer = 40;
                                // This line of code takes the current direction the Boss is facing and
                                // makes it shoot in that direction
                                // If facingdirection is right, it shoots right, else it shoots left
                                // currentAnimationName = facingDirection == Direction.RIGHT ? "SHOOT_LEFT" :
                                // "SHOOT_LEFT";
                                if (this.getX() + 100 > player.getX()) {
                                        if (lives >= 1) {
                                                currentAnimationName = "SHOOT_LEFT";
                                        }
                                } else {
                                        if (lives >= 1) {
                                                currentAnimationName = "SHOOT_RIGHT";
                                        }
                                }
                        } else if (shootTimer == 0 && (currentAnimationName != "HURT_LEFT" || currentAnimationName != "HURT_RIGHT")) {
                                bossState = BossState.SHOOT;
                        } else {
                                shootTimer--;
                        }
                } else if (bossState == BossState.HURT) {
                        if (lives > 1) {
                                if (this.getX() + 100 > player.getX()) {
                                        currentAnimationName = "HURT_LEFT";
                                } else {
                                        currentAnimationName = "HURT_RIGHT";
                                }
                        } else {
                                this.currentAnimationName = facingDirection == Direction.RIGHT ? "DEATH_RIGHT" : "DEATH_LEFT";
                                spawnEndLevelBox();
                        }
                }
                // this is for actually having the FireBoss shoot the fireball
                else if (bossState == BossState.SHOOT) {
                        // create FireBall enemies
                        float movementSpeed;
                        int fireballX;
                        if (currentAnimationName == "SHOOT_RIGHT") {
                                fireballX = Math.round(getX() + getWidth() + 30);
                                movementSpeed = 1.5f;
                        } else {
                                fireballX = Math.round(getX() + 150);
                                movementSpeed = -1.5f;
                        }
                        int fireballY = Math.round(getY()) + 4;

                        PlayerFireball fireball1 = new PlayerFireball(fireballX - 115, fireballY + 150, movementSpeed, 150);
                        PlayerFireball fireball2 = new PlayerFireball(fireballX - 115, fireballY + 100, movementSpeed, 150);
                        PlayerFireball fireball3 = new PlayerFireball(fireballX - 115, fireballY + 50, movementSpeed, 150);
                        // add fireball enemy to the map for it to spawn in the level
                        map.addEnemy(fireball1);
                        map.addEnemy(fireball2);
                        map.addEnemy(fireball3);
                        // change Boss back to its Stand state after shooting, reset shootTimer to wait
                        // a certain number of frames before shooting again
                        bossState = BossState.STAND;

                        // reset shoot wait timer so the process can happen again (STAND around, then
                        // waits, then shoots)
                        shootWaitTimer = 45;
                        super.update(player);
                }
                // wave attack kills fire boss
                if (activeWave != null) {
                        if (intersects(activeWave)) {
                                bossState = BossState.HURT;
                                isInvincible = true;
                                isInvincibleCounter = 40;
                                shootWaitTimer = 45;
                                enemyAttacked(this);
                                // broadcast so the fireball disappears
                                ElementalAbilityListenerManager.waveKilledEnemy();
                                if (lives <= 0) {
                                        this.mapEntityStatus = MapEntityStatus.REMOVED;
                                }
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

        private void spawnEndLevelBox() {             
                EndLevelBox endLevelBox4 = new EndLevelBox(map.getMapTile(18, 11).getLocation());
                map.addEnhancedMapTile(endLevelBox4);
        }

        @Override
        public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
                return new HashMap<String, Frame[]>() {
                        {
                                put("STAND_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                                                                .withScale(3)
                                                                .withBounds(20, 20, 34, 40)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                                                                .withScale(3)
                                                                .withBounds(20, 20, 34, 40)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 2), 14)
                                                                .withScale(3)
                                                                .withBounds(20, 20, 34, 40)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 3), 14)
                                                                .withScale(3)
                                                                .withBounds(20, 20, 34, 40)
                                                                .build(),
                                });
                                put("STAND_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(27, 20, 34, 40)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(27, 20, 34, 40)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 2), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(27, 20, 34, 40)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 3), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(27, 20, 34, 40)
                                                                .build(),
                                });

                                put("SHOOT_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(1, 0), 14)
                                                                .withScale(3)
                                                                .withBounds(20, 20, 34, 40)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                                                                .withScale(3)
                                                                .withBounds(20, 20, 34, 40)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 2), 14)
                                                                .withScale(3)
                                                                .withBounds(20, 20, 34, 40)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 3), 14)
                                                                .withScale(3)
                                                                .withBounds(20, 20, 34, 40)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 4), 14)
                                                                .withScale(3)
                                                                .withBounds(20, 20, 34, 40)
                                                                .build(),
                                });
                                put("SHOOT_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(1, 0), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(27, 20, 34, 40)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(27, 20, 34, 40)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 2), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(27, 20, 34, 40)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 3), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(27, 20, 34, 40)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 4), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(27, 20, 34, 40)
                                                                .build(),
                                });

                                put("HURT_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(0, 4), 15)
                                                                .withScale(3)
                                                                .withBounds(0,0,0,0)
                                                                .build(),
                                });

                                put("HURT_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(0, 4), 15)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(0,0,0,0)
                                                                .build(),

                                });
                                put("DEATH_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(2, 0), 20)
                                                                .withScale(3)
                                                                .withBounds(0,0,0,0)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 1), 20)
                                                                .withScale(3)
                                                                .withBounds(0,0,0,0)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 2), 20)
                                                                .withScale(3)
                                                                .withBounds(0,0,0,0)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 3), 20)
                                                                .withScale(3)
                                                                .withBounds(0,0,0,0)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 4), 20)
                                                                .withScale(3)
                                                                .withBounds(0,0,0,0)
                                                                .build(),
                                });
                                put("DEATH_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(2, 0), 20)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(0,0,0,0)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 1), 20)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(0,0,0,0)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 2), 20)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(0,0,0,0)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 3), 20)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(0,0,0,0)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 4), 20)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(0,0,0,0)
                                                                .build(),
                                });
                        }
                };
        }

        public enum BossState {
                STAND, SHOOT_WAIT, SHOOT, HURT
        }
}
