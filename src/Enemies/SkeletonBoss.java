package Enemies;

import Builders.FrameBuilder;
import Enemies.AngelBoss.AngelState;
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

// This class is for the skeleton boss at the end of the earth level
// Every so often he attacks. When attacking, he reflects fireball attacks back at the player
public class SkeletonBoss extends Enemy {

        // start and end location defines the point it spawns on
        protected Point startLocation;

        private Direction startFacingDirection;
        protected Direction facingDirection;

        // timer is used to determine how often he "attacks"
        protected int shootWaitTimer;

        // timer is used to determine when he is to attack
        protected int shootTimer;

        // can be either WALK or SHOOT based on what the enemy is currently set to do
        protected SkeletonState skeletonState;
        protected SkeletonState previousSkeletonState;

        protected int lives = 3;
        protected int hurtTimer;
        // determines when his reflection is active
        protected int reflectionTimer;

        public SkeletonBoss(Point startLocation, Direction facingDirection) {
                super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("SkeletonEnemy.png"), 63, 63),"STAND_LEFT");
                this.startLocation = startLocation;
                this.startFacingDirection = facingDirection;
                this.initialize();
        }

        @Override
        public void initialize() {
                // Add the Boss as an enemy to listen for elemental abilities
                ElementalAbilityListenerManager.addEnemyListener(this);
                super.initialize();
                skeletonState = SkeletonState.WALK;
                previousSkeletonState = skeletonState;
                facingDirection = startFacingDirection;
                if (facingDirection == Direction.RIGHT) {
                        currentAnimationName = "STAND_RIGHT";
                } 
                else if (facingDirection == Direction.LEFT) {
                        currentAnimationName = "STAND_LEFT";
                }
                reflectionTimer = 0;

                // every certain number of frames, he will attack
                shootWaitTimer = 90;
        }

        @Override
        public void update(Player player) {

                // if shoot timer is up and skeleton is not currently shooting, set its state to SHOOT_WAIT
                if (shootWaitTimer == 0 && skeletonState != SkeletonState.SHOOT_WAIT) {
                        skeletonState = SkeletonState.SHOOT_WAIT;
                } 
                else if (this.skeletonState != SkeletonState.HURT && shootWaitTimer > -1) {
                        shootWaitTimer--;
                }

                // if skeleton is waiting to attack, he begins his shooting animation
                // after this waiting period is over, he enter the "shoot state" where he reflects fireballs and has a bigger hitbox
                if (skeletonState == SkeletonState.SHOOT_WAIT) {
                        if (previousSkeletonState == SkeletonState.WALK) {
                                shootTimer = 56;
                                if (player.getX() < this.getX()) {
                                        currentAnimationName = "SHOOT_LEFT";
                                } 
                                else {
                                        currentAnimationName = "SHOOT_RIGHT";
                                }
                                // if timer is at 0 he enters "shoot" where he reflects projectiles
                        } 
                        else if (shootTimer == 0) {
                                skeletonState = SkeletonState.SHOOT;
                                reflectionTimer = 70;
                        } 
                        else {
                                shootTimer--;
                        }
                }

                // this is for having the skeleton shoot projectiles
                if (skeletonState == SkeletonState.SHOOT && reflectionTimer != 0) {
                        reflectionTimer--;

                } 
                else if (skeletonState == SkeletonState.SHOOT && reflectionTimer == 0) {
                        // change skeleton back to its WALK state after shooting, reset shootTimer to wait a certain number of frames before shooting
                        skeletonState = SkeletonState.WALK;
                        shootTimer = 56;
                        if (player.getX() < this.getX()) {
                                this.facingDirection = Direction.LEFT;
                                currentAnimationName = "STAND_LEFT";
                        } 
                        else {
                                this.facingDirection = Direction.RIGHT;
                                currentAnimationName = "STAND_RIGHT";
                        }

                        // reset shoot wait timer so the process can happen again
                        shootWaitTimer = 90;
                }

                // if there is a fireball and it got hit
                if (activeFireball != null) {
                        if (intersects(activeFireball)) {
                                enemyAttacked(this);

                        }
                }

                if (skeletonState == SkeletonState.HURT) {
                        hurtTimer--;
                        // after being hurt, enters walk state and shootTimer is highly reduced to avoid being able to spam the boss
                        if (hurtTimer == 0) {
                                skeletonState = SkeletonState.WALK;
                                if (this.facingDirection == Direction.LEFT) {
                                        currentAnimationName = "STAND_LEFT";
                                } 
                                else {
                                        currentAnimationName = "STAND_RIGHT";
                                }
                                shootWaitTimer = 20;
                        }
                }

                super.update(player);

                previousSkeletonState = skeletonState;
        }

        @Override
        public void enemyAttacked(Enemy enemy) {

                // broadcast to the fireball that it killed something so it should disappear
                ElementalAbilityListenerManager.fireballKilledEnemy();

                // if it is not reflecting it gets hit
                if (this.reflectionTimer == 0) {
                        // When it has 3 or 2 lives it gets put into HURT state
                        if (lives > 1) { 
                                lives--;
                                // Need to make the fireball attacks null after first intersection, otherwise it will take away all lives
                                activeFireball = null; 
                                skeletonState = SkeletonState.HURT;
                                if (facingDirection == Direction.LEFT) {
                                        currentAnimationName = "HURT_LEFT";
                                } 
                                else {
                                        currentAnimationName = "HURT_RIGHT";
                                }
                                hurtTimer = 60;
                        } else if (lives == 0) {
                                isOnMap = false;
                                // This makes the enemy disappear
                                enemy.setMapEntityStatus(MapEntityStatus.REMOVED);
                                
                        }
                }
                else {
                         // define where fireball will spawn on map (x location) relative to dinosaur enemy's location and define its movement speed
                        int fireballX;
                        float movementSpeed;
                        if (facingDirection == Direction.RIGHT) {
                                fireballX = Math.round(getX()) + getWidth();
                                movementSpeed = 3f;
                        } else {
                                fireballX = Math.round(getX() - 21);
                                movementSpeed = -3.5f;
                        }

                        // define where fireball will spawn on the map (y location) relative to dinosaur enemy's location
                        int fireballY = Math.round(getY()) + 75;

                        // create Fireball enemy
                        LargerFireball fireball = new LargerFireball(new Point(fireballX, fireballY), movementSpeed, 70);

                        // add fireball enemy to the map for it to spawn in the level
                        map.addEnemy(fireball);
                }
        }

        @Override
        public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
                return new HashMap<String, Frame[]>() {
                        {
                                put("STAND_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(3, 0), 14)
                                                                .withScale(3)
                                                                .withBounds(22, 16, 18, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 1), 14)
                                                                .withScale(3)
                                                                .withBounds(22, 16, 18, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 2), 14)
                                                                .withScale(3)
                                                                .withBounds(22, 16, 18, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 3), 14)
                                                                .withScale(3)
                                                                .withBounds(22, 16, 18, 30)
                                                                .build()
                                });

                                put("STAND_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(3, 0), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(22, 16, 18, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 1), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(22, 16, 18, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 2), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(22, 16, 18, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 3), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(22, 16, 18, 30)
                                                                .build()
                                });

                                put("SHOOT_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                                                                .withScale(3)
                                                                .withBounds(22, 16, 18, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                                                                .withScale(3)
                                                                .withBounds(22, 16, 18, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 2), 14)
                                                                .withScale(3)
                                                                .withBounds(22, 16, 18, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 3), 14)
                                                                .withScale(3)
                                                                .withBounds(22, 16, 18, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 4), 14)
                                                                .withScale(3)
                                                                .withBounds(22, 16, 39, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 5), 14)
                                                                .withScale(3)
                                                                .withBounds(22, 16, 39, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 6), 14)
                                                                .withScale(3)
                                                                .withBounds(22, 16, 39, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 7), 14)
                                                                .withScale(3)
                                                                .withBounds(22, 16, 39, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 8), 14)
                                                                .withScale(3)
                                                                .withBounds(22, 16, 39, 30)
                                                                .build()
                                });

                                put("SHOOT_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(22, 16, 18, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(22, 16, 18, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 2), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(22, 16, 18, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 3), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(22, 16, 18, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 4), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(1, 16, 39, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 5), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(1, 16, 39, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 6), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(1, 16, 39, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 7), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(1, 16, 39, 30)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 8), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(1, 16, 39, 30)
                                                                .build()
                                });

                                put("HURT_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(1, 0), 30)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(22, 16, 18, 30)
                                                                .build()
                                });

                                put("HURT_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(1, 0), 30)
                                                                .withScale(3)
                                                                .withBounds(22, 16, 18, 30)
                                                                .build()
                                });

                        }
                };
        }

        public enum SkeletonState {
                WALK, SHOOT_WAIT, SHOOT, HURT
        }
}
