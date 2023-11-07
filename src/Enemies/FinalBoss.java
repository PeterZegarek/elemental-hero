package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import EnhancedMapTiles.EndLevelBox;
import EnhancedMapTiles.LightningCloud;
import EnhancedMapTiles.Tornado;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.ElementalAbilityListenerManager;
import Level.Enemy;
import Level.MapEntityStatus;
import Level.Player;
import Players.RockAttack;
import Players.Wave;
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;
import Level.BossLivesListener;
import java.util.Random;
import java.util.ArrayList;
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

        // used to determine if clouds/tornadoes have been spawned
        protected boolean cloudsSpawned = false;
        protected boolean tornadoesSpawned = false;

        // cooldown for spawning the tree enemies
        protected int treeCooldown = 660;
        protected int treesSpawned;
        protected int treesAlive;

        // cooldown for spawning the fireWisp enemies
        protected int fireWispCooldown = 660;
        protected int fireWispsSpawned;
        protected int fireWispsAlive;

        // cooldown for spawning the Slime enemies
        protected int slimeEnemyCooldown = 660;
        protected int slimeEnemiesSpawned;
        protected int slimeEnemiesAlive;

        // cooldown for spawning the Cloud enemies
        protected int cloudEnemyCooldown = 660;
        protected int cloudEnemiesSpawned;
        protected int cloudEnemiesAlive;




        private ArrayList<TreeEnemy> trees = new ArrayList<TreeEnemy>(10);
        private ArrayList<Firewisp> fireWisps = new ArrayList<Firewisp>(10);
        private ArrayList<SlimeEnemy> slimeEnemies = new ArrayList<SlimeEnemy>(10);
        private ArrayList<CloudEnemy> cloudEnemies = new ArrayList<CloudEnemy>(10);
        private ArrayList<BossLivesListener> listeners;

        public FinalBoss(Point startLocation, Point endLocation, Direction facingDirection) {
                super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("FinalBoss.png"), 159, 127),
                                "EARTH_STAND_LEFT");
                this.startLocation = startLocation;
                this.endLocation = endLocation;
                this.startFacingDirection = facingDirection;
                listeners = new ArrayList<BossLivesListener>(10);
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
                // sendBossLives();
        }

        @Override
        public void update(Player player) {

                treeCooldown--;
                fireWispCooldown--;
                slimeEnemyCooldown--;
                cloudEnemyCooldown--;
                if (lives < 16 && lives > 13){
                        if(treesSpawned < 3){
                        spawnTrees(true);
                        }
                }
                else if (lives < 13 && lives > 9){
                        if(fireWispsSpawned < 6 ){
                        spawnFireWisps(true);
                        }
                }         
                else if (lives < 10 && lives > 6){
                        if (slimeEnemiesSpawned < 8){
                        spawnSlimeEnemies(true);
                        }
                }
                else if (lives < 4 && lives > 1){
                        if (cloudEnemiesSpawned < 3){
                        spawnCloudEnemies(true);
                        }
                }

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
                                if (lives >= 13) {
                                        // check if trees were killed
                                        checkTrees();
                                        this.currentAnimationName = facingDirection == Direction.RIGHT
                                                        ? "EARTH_STAND_RIGHT"
                                                        : "EARTH_STAND_LEFT";
                                } else if (lives >= 10) {
                                        // check if firewisps were killed
                                        checkFireWisps();
                                        this.currentAnimationName = facingDirection == Direction.RIGHT
                                                        ? "FIRE_STAND_RIGHT"
                                                        : "FIRE_STAND_LEFT";
                                } else if (lives >= 7) {
                                        // check if SlimeEnemies were killed
                                        checkSlimeEnemies();
                                        this.currentAnimationName = facingDirection == Direction.RIGHT
                                                        ? "WATER_STAND_RIGHT"
                                                        : "WATER_STAND_LEFT";
                                } else if (lives >= 4) {
                                        this.currentAnimationName = facingDirection == Direction.RIGHT
                                                        ? "ELECTRIC_STAND_RIGHT"
                                                        : "ELECTRIC_STAND_LEFT";
                                } else if (lives >= 1) {
                                        // check if CloudEnemies were killed
                                        checkCloudEnemies();
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
                                        if (lives >= 13) {
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
                                        if (lives >= 13) {
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
                        } else if (shootTimer == 0 && currentAnimationName != "BOSS_HURT_RIGHT"
                                        || currentAnimationName != "BOSS_HURT_LEFT") {
                                bossState = BossState.SHOOT;
                        } else {
                                shootTimer--;
                        }
                } else if (bossState == BossState.HURT) {
                        if (lives == 15 || lives == 14 || lives == 12 || lives == 11 || lives == 9 ||
                                        lives == 8 || lives == 6 || lives == 5 || lives == 3 || lives == 2) {
                                if (this.getX() + 250 > player.getX()) {
                                        currentAnimationName = "BOSS_HURT_LEFT";
                                } else {
                                        currentAnimationName = "BOSS_HURT_RIGHT";
                                }
                        } else if (lives == 13) {
                                for (BossLivesListener listener : listeners) {
                                        listener.getBossLives(lives);
                                }
                                this.currentAnimationName = facingDirection == Direction.RIGHT
                                                ? "EARTH_DEATH_RIGHT"
                                                : "EARTH_DEATH_LEFT";
                        } else if (lives == 10) {
                                for (BossLivesListener listener : listeners) {
                                        listener.getBossLives(lives);
                                }
                                this.currentAnimationName = facingDirection == Direction.RIGHT
                                                ? "FIRE_DEATH_RIGHT"
                                                : "FIRE_DEATH_LEFT";
                        } else if (lives == 7) {
                                spawnClouds();
                                for (BossLivesListener listener : listeners) {
                                        listener.getBossLives(lives);
                                }
                                this.currentAnimationName = facingDirection == Direction.RIGHT
                                                ? "WATER_DEATH_RIGHT"
                                                : "WATER_DEATH_LEFT";
                        } else if (lives == 4) {
                                spawnTornadoes();
                                for (BossLivesListener listener : listeners) {
                                        listener.getBossLives(lives);
                                }
                                this.currentAnimationName = facingDirection == Direction.RIGHT
                                                ? "ELECTRIC_DEATH_RIGHT"
                                                : "ELECTRIC_DEATH_LEFT";
                        } else if (lives == 1) {
                                for (BossLivesListener listener : listeners) {
                                        listener.getBossLives(lives);
                                }
                                this.currentAnimationName = facingDirection == Direction.RIGHT
                                                ? "AIR_DEATH_RIGHT"
                                                : "AIR_DEATH_LEFT";
                                spawnEndLevelBox();
                        }
                }
                // this is for actually having the Boss shoot the attacks
                else if (bossState == BossState.SHOOT) {

                        // this will be for spawning the tree enemies


              
                        // FIRE PHASE
                        Fireball fireball1 = new Fireball(map.getMapTile(20, 11).getLocation().addY(10), -movementSpeed, 120);
                        fireball1.setScale(5);
                        Fireball fireball2 = new Fireball(map.getMapTile(16, 9).getLocation().addY(10), -movementSpeed, 150);
                        fireball2.setScale(5);
                        Fireball fireball3 = new Fireball(map.getMapTile(19,6).getLocation().addY(10), -movementSpeed, 120);
                        fireball3.setScale(5);
                        Fireball fireball4 = new Fireball(map.getMapTile(29,11).getLocation().addY(10), movementSpeed, 120);
                        fireball4.setScale(5);
                        Fireball fireball5 = new Fireball(map.getMapTile(30,6).getLocation().addY(10), movementSpeed, 150);
                        fireball5.setScale(5);
                        Fireball fireball6 = new Fireball(map.getMapTile(33,9).getLocation().addY(10), movementSpeed, 120);
                        fireball6.setScale(5);
                        //Fireball fireball7 = new Fireball(map.getMapTile(30,6).getLocation().addY(10), movementSpeed, 150);
                        //fireball7.setScale(5);
                        //Fireball fireball8 = new Fireball(map.getMapTile(33,9).getLocation().addY(10), movementSpeed, 120);
                        //fireball8.setScale(5);

                        //AIR PHASE
                        float momentumY = 30f;
                        Arrow arrow1 = new Arrow(map.getMapTile(10, 1).getLocation(), 2, momentumY, "RIGHT");
                        Arrow arrow2 = new Arrow(map.getMapTile(12,1).getLocation(), 2, momentumY, "RIGHT");
                        Arrow arrow3 = new Arrow(map.getMapTile(14, 1).getLocation(), 2, momentumY, "RIGHT");
                        Arrow arrow4 = new Arrow(map.getMapTile(39,1).getLocation(), -2, momentumY, "LEFT");
                        Arrow arrow5 = new Arrow(map.getMapTile(37,1).getLocation(), -2, momentumY, "LEFT");
                        Arrow arrow6 = new Arrow(map.getMapTile(35,1).getLocation(), -2, momentumY, "LEFT");

                       
                        

                        

                        // add enemies to the map for it to spawn in the level
                        if (previousAnimationName == "EARTH_SHOOT_LEFT" || previousAnimationName == "FIRE_SHOOT_LEFT"
                                        || previousAnimationName == "WATER_SHOOT_LEFT" ||
                                        previousAnimationName == "ELECTRIC_SHOOT_LEFT"
                                        || previousAnimationName == "AIR_SHOOT_LEFT") {
                                if ((bossState != BossState.HURT && (currentAnimationName != "BOSS_HURT_LEFT"
                                                || currentAnimationName != "BOSS_HURT_RIGHT")) ||
                                                (bossState != BossState.SHOOT_WAIT
                                                                && (currentAnimationName != "BOSS_HURT_LEFT"
                                                                                || currentAnimationName != "BOSS_HURT_RIGHT"))) {
                                        if (lives >= 13){
                                                //map.addEnemy(rock1);
                                                //map.addEnemy(rock2);
                                                //map.addEnemy(rock3);
                                                //map.addEnemy(rock4);
                                                //map.addEnemy(rock5);
                                                //map.addEnemy(rock6);
                                        }                
                                        else if(lives >= 10){
                                                map.addEnemy(fireball1);
                                                map.addEnemy(fireball2);
                                                map.addEnemy(fireball3);
                                                map.addEnemy(fireball4);
                                                map.addEnemy(fireball5);
                                                map.addEnemy(fireball6);
                                                //map.addEnemy(fireball7);
                                                //map.addEnemy(fireball8);
                                        }
                                        else if (lives >= 1){
                                                map.addEnemy(arrow1);
                                                map.addEnemy(arrow2);
                                                map.addEnemy(arrow3);   
                                                map.addEnemy(arrow4);
                                                map.addEnemy(arrow5);
                                                map.addEnemy(arrow6);                                 
                                        }
                                }
                        } else {
                                if ((bossState != BossState.HURT && (currentAnimationName != "BOSS_HURT_LEFT"
                                                || currentAnimationName != "BOSS_HURT_RIGHT")) ||
                                                (bossState != BossState.SHOOT_WAIT
                                                                && (currentAnimationName != "BOSS_HURT_LEFT"
                                                                                || currentAnimationName != "BOSS_HURT_RIGHT"))) {
                                        if (lives >= 13){
                                                //map.addEnemy(rock1);
                                                //map.addEnemy(rock2);
                                                //map.addEnemy(rock3);
                                                //map.addEnemy(rock4);
                                                //map.addEnemy(rock5);
                                                //map.addEnemy(rock6);
                                        }                
                                        else if(lives >= 10){
                                                map.addEnemy(fireball1);
                                                map.addEnemy(fireball2);
                                                map.addEnemy(fireball3);
                                                map.addEnemy(fireball4);
                                                map.addEnemy(fireball5);
                                                map.addEnemy(fireball6);
                                        }
                                        else if (lives >= 1){
                                                map.addEnemy(arrow1);
                                                map.addEnemy(arrow2);
                                                map.addEnemy(arrow3);  
                                                map.addEnemy(arrow4);
                                                map.addEnemy(arrow5);
                                                map.addEnemy(arrow6);                                            
                                        }
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
                if(lives >= 13){ 
                        // if there are no trees left then he can take damage
                        if (activeFireball != null && treesAlive == 0){
                                if (intersects(activeFireball)){
                                        bossState = BossState.HURT;
                                        isInvincible = true;
                                        isInvincibleCounter = 40;
                                        shootWaitTimer = 150;
                                        sendBossLives();
                                    // broadcast to the fireball that it killed something so it should disappear
                                    ElementalAbilityListenerManager.fireballKilledEnemy();
                                    // set trees spawned to 0 so that he respawns trees after getting hit
                                    treesSpawned = 0;
                                }             
                        }
                }
                else if (lives >= 10){
                        // if there are no fireWisps left then he can take damage
                        if (activeWave != null && fireWispsAlive == 0){
                                if (intersects(activeWave)){
                                        bossState = BossState.HURT;
                                        isInvincible = true;
                                        isInvincibleCounter = 40;
                                        shootWaitTimer = 150;
                                        sendBossLives();
                                        // broadcast to the fireball that it killed something so it should disappear
                                        ElementalAbilityListenerManager.waveKilledEnemy();
                                        // set firewisp spawned to 0 so that he respawns firewisps after getting hit
                                        fireWispsSpawned = 0;
                                        } 
                                }
                        }
                //ELectric Ability
              /*else if (lives >= 7){ 
                        if (activeElectric != null && SlimeEnemiesAlive == 0){
                                if (intersects(activeElectric)){ 
                                        bossState = BossState.HURT;
                                        isInvincible = true;
                                        isInvincibleCounter = 40;
                                        shootWaitTimer = 150;
                                        sendBossLives();
                                        // broadcast to the fireball that it killed something so it should disappear
                                        ElementalAbilityListenerManager.waveKilledEnemy();
                                        } 
                                }
                                
                        }
                
                else if (lives >= 4){
                        Key GLIDE_KEY = Key.SHIFT;
                        if (Keyboard.isKeyDown(GLIDE_KEY)){
                                if(intersects(player)){
                                        bossState = BossState.HURT;
                                        isInvincible = true;
                                        isInvincibleCounter = 40;
                                        shootWaitTimer = 150;
                                        sendBossLives();
                                } 
                        }
                }
                */
                else if (lives < 4 && lives >= 1){
                        // if there are no CloudEnemies left then he can take damage
                        if (activeRockAttack != null && cloudEnemiesAlive == 0){
                                if (intersects(activeRockAttack)){
                                        bossState = BossState.HURT;
                                        isInvincible = true;
                                        isInvincibleCounter = 40;
                                        shootWaitTimer = 150;
                                        sendBossLives();
                                        // broadcast to the rockattack that it killed something so it should disappear
                                        ElementalAbilityListenerManager.rockAttackKilledEnemy();
                                        // set firewisp spawned to 0 so that he respawns cloudEnemies after getting hit
                                        cloudEnemiesSpawned = 0;
                                } 
                        }
                }
                
        }

        // to spawn tree enemies - params are whether it's initial spawn or secondary spawn
        /*
         * Locations to spawn trees:
         * 21,11 to 18, 11
         * 29, 11 to 31, 11
         * 17, 9 to 13, 9
         * 33, 9 to 36, 9
         * 22, 4 to 27, 4
         */
        // not verified to work yet
        public void spawnTrees(boolean initialSpawn){
                // if this is the initial spawn, it spawns 3 immediately
                if (initialSpawn){
                        TreeEnemy tree1 = new TreeEnemy(map.getMapTile(18,11).getLocation().addY(12), map.getMapTile(21,11).getLocation().addY(12) , Direction.RIGHT);
                        trees.add(tree1);
                        map.addEnemy(tree1);
                        treesSpawned++;
                        treesAlive++;
                        TreeEnemy tree2 = new TreeEnemy(map.getMapTile(13,9).getLocation().addY(12), map.getMapTile(17,9).getLocation().addY(12) , Direction.RIGHT);
                        trees.add(tree2);
                        map.addEnemy(tree2);
                        treesSpawned++;
                        treesAlive++;
                        TreeEnemy tree3 = new TreeEnemy(map.getMapTile(22,4).getLocation(), map.getMapTile(27,4).getLocation() , Direction.RIGHT);
                        trees.add(tree3);
                        map.addEnemy(tree3);
                        treesSpawned++;
                        treesAlive++;
                }
                else{
                        // spawn 1 tree at a time
                        // for now i'm leaving this out. may make the difficulty too hard
                }
        }

        // check to see if trees have been killed
        // this is called every time the boss goes into shoot wait phase
        public void checkTrees(){
                for (int counter = 0; counter < trees.size(); counter++){
                        if (trees.get(counter).getMapEntityStatus() == MapEntityStatus.REMOVED){
                                trees.remove(counter);
                                treesAlive--;
                        }
                }
        }

        public void spawnFireWisps(boolean initialSpawn){
                // if this is the initial spawn, it spawns 3 immediately
                if (initialSpawn){
                        Firewisp firewisp1 = new Firewisp(map.getMapTile(10, 12).getLocation().addY(5), map.getMapTile(17,12).getLocation().addY(5), Direction.RIGHT);
                        fireWisps.add(firewisp1);
                        map.addEnemy(firewisp1);
                        fireWispsSpawned++;
                        fireWispsAlive++;

                        Firewisp firewisp2 = new Firewisp(map.getMapTile(10, 5).getLocation().addY(5), map.getMapTile(16,5).getLocation().addY(5), Direction.LEFT);
                        fireWisps.add(firewisp2);
                        map.addEnemy(firewisp2);
                        fireWispsSpawned++;
                        fireWispsAlive++;

                        Firewisp firewisp3 = new Firewisp(map.getMapTile(22, 3).getLocation().addY(5), map.getMapTile(27,3).getLocation().addY(5), Direction.LEFT);
                        fireWisps.add(firewisp3);
                        map.addEnemy(firewisp3);
                        fireWispsSpawned++;
                        fireWispsAlive++;

                        Firewisp firewisp4 = new Firewisp(map.getMapTile(32, 12).getLocation().addY(5), map.getMapTile(38,12).getLocation().addY(5), Direction.LEFT);
                        fireWisps.add(firewisp4);
                        map.addEnemy(firewisp4);
                        fireWispsSpawned++;
                        fireWispsAlive++;

                        Firewisp firewisp5 = new Firewisp(map.getMapTile(34, 5).getLocation().addY(5), map.getMapTile(37,5).getLocation().addY(5), Direction.LEFT);
                        fireWisps.add(firewisp5);
                        map.addEnemy(firewisp5);
                        fireWispsSpawned++;
                        fireWispsAlive++;
   
                }
                else{
                        // spawn 1 firewisp at a time
                }
        }
        public void checkFireWisps(){
                for (int counter = 0; counter < fireWisps.size(); counter++){
                        if (fireWisps.get(counter).getMapEntityStatus() == MapEntityStatus.REMOVED){
                                fireWisps.remove(counter);
                                fireWispsAlive--;
                        }
                }
        }


        // to add things to the list of listeners listening to the amount of lives the boss has
        public void addToArrayList(BossLivesListener listener) {
                this.listeners.add(listener);
        }

        // to send the amount of lives to the listeners
        public void sendBossLives() {
                for (BossLivesListener listener : listeners) {
                        listener.getBossLives(lives);
                }
        }

        public void spawnSlimeEnemies(boolean initialSpawn){
                // if this is the initial spawn, it spawns 3 immediately
                if (initialSpawn){
                        SlimeEnemy SlimeEnemy1 = new SlimeEnemy(map.getMapTile(11, 6).getLocation(), map.getMapTile(13, 6).getLocation(), Direction.RIGHT);
                        slimeEnemies.add(SlimeEnemy1);
                        map.addEnemy(SlimeEnemy1);
                        slimeEnemiesSpawned++;
                        slimeEnemiesAlive++;
                
                        SlimeEnemy SlimeEnemy2 = new SlimeEnemy(map.getMapTile(13, 9).getLocation(), map.getMapTile(16, 9).getLocation(), Direction.RIGHT);
                        slimeEnemies.add(SlimeEnemy2);
                        map.addEnemy(SlimeEnemy2);
                        slimeEnemiesSpawned++;
                        slimeEnemiesAlive++;
                
                        SlimeEnemy SlimeEnemy3 = new SlimeEnemy(map.getMapTile(18, 11).getLocation(), map.getMapTile(20, 11).getLocation(), Direction.RIGHT);
                        slimeEnemies.add(SlimeEnemy3);
                        map.addEnemy(SlimeEnemy3);
                        slimeEnemiesSpawned++;
                        slimeEnemiesAlive++;
                
                        SlimeEnemy SlimeEnemy4 = new SlimeEnemy(map.getMapTile(38, 6).getLocation(), map.getMapTile(36, 6).getLocation(), Direction.LEFT);
                        slimeEnemies.add(SlimeEnemy4);
                        map.addEnemy(SlimeEnemy4);
                        slimeEnemiesSpawned++;
                        slimeEnemiesAlive++;
                
                        SlimeEnemy SlimeEnemy5 = new SlimeEnemy(map.getMapTile(36, 9).getLocation(), map.getMapTile(33, 9).getLocation(), Direction.LEFT);
                        slimeEnemies.add(SlimeEnemy5);
                        map.addEnemy(SlimeEnemy5);
                        slimeEnemiesSpawned++;
                        slimeEnemiesAlive++;
                
                        SlimeEnemy SlimeEnemy6 = new SlimeEnemy(map.getMapTile(31, 11).getLocation(), map.getMapTile(29, 11).getLocation(), Direction.LEFT);
                        slimeEnemies.add(SlimeEnemy6);
                        map.addEnemy(SlimeEnemy6);
                        slimeEnemiesSpawned++;
                        slimeEnemiesAlive++;

                        SlimeEnemy SlimeEnemy7 = new SlimeEnemy(map.getMapTile(22, 6).getLocation(), map.getMapTile(27, 6).getLocation(), Direction.LEFT, "FLIP");
                        slimeEnemies.add(SlimeEnemy7);
                        map.addEnemy(SlimeEnemy7);
                        slimeEnemiesSpawned++;
                        slimeEnemiesAlive++;                      
                }
                else{
                        // spawn 1 slimeEnemy at a time
                }
        }

        public void checkSlimeEnemies(){
                for (int counter = 0; counter < slimeEnemies.size(); counter++){
                        if (slimeEnemies.get(counter).getMapEntityStatus() == MapEntityStatus.REMOVED){
                                slimeEnemies.remove(counter);
                                slimeEnemiesAlive--;
                        }
                }
        }

        public void spawnCloudEnemies(boolean initialSpawn){
                // if this is the initial spawn, it spawns 3 immediately
                if (initialSpawn){
                        CloudEnemy cloud1 = new CloudEnemy(map.getMapTile(10, 11).getLocation().addY(5), map.getMapTile(17,11).getLocation().addY(5), Direction.RIGHT, 1.5f);
                        cloudEnemies.add(cloud1);
                        map.addEnemy(cloud1);
                        cloudEnemiesSpawned++;
                        cloudEnemiesAlive++;

                        CloudEnemy cloud2 = new CloudEnemy(map.getMapTile(10, 4).getLocation().addY(25), map.getMapTile(16,4).getLocation().addY(25), Direction.RIGHT, 1.5f);
                        cloudEnemies.add(cloud2);
                        map.addEnemy(cloud2);
                        cloudEnemiesSpawned++;
                        cloudEnemiesAlive++;

                        CloudEnemy cloud3 = new CloudEnemy(map.getMapTile(22, 2).getLocation().addY(25), map.getMapTile(27,2).getLocation().addY(25), Direction.RIGHT, 1.5f);
                        cloudEnemies.add(cloud3);
                        map.addEnemy(cloud3);
                        cloudEnemiesSpawned++;
                        cloudEnemiesAlive++;

                        CloudEnemy cloud4 = new CloudEnemy(map.getMapTile(32, 11).getLocation().addY(10), map.getMapTile(38,11).getLocation().addY(10), Direction.RIGHT, 1.5f);
                        cloudEnemies.add(cloud4);
                        map.addEnemy(cloud4);
                        cloudEnemiesSpawned++;
                        cloudEnemiesAlive++;

                        CloudEnemy cloud5 = new CloudEnemy(map.getMapTile(34, 4).getLocation().addY(25), map.getMapTile(37,4).getLocation().addY(25), Direction.RIGHT, 1.5f);
                        cloudEnemies.add(cloud5);
                        map.addEnemy(cloud5);
                        cloudEnemiesSpawned++;
                        cloudEnemiesAlive++;     
                }
                else{
                        // spawn 1 CLoudEnemy at a time
                }
        }

        // check to see if CLoudEnemies have been killed
        // this is called every time the boss goes into shoot wait phase
        public void checkCloudEnemies(){
                for (int counter = 0; counter < cloudEnemies.size(); counter++){
                        if (cloudEnemies.get(counter).getMapEntityStatus() == MapEntityStatus.REMOVED){
                                cloudEnemies.remove(counter);
                                cloudEnemiesAlive--;
                        }
                }
        }

        // this spawns in all the clouds when the boss enters the electric phase
        public void spawnClouds() {
                if (!cloudsSpawned) {
                        LightningCloud cloud1 = new LightningCloud(map.getMapTile(17, 1).getLocation());
                        map.addEnhancedMapTile(cloud1);
                        listeners.add(cloud1);
                        LightningCloud cloud2 = new LightningCloud(map.getMapTile(20, 1).getLocation());
                        map.addEnhancedMapTile(cloud2);
                        listeners.add(cloud2);
                        // this one works, but for balancing we may need to get rid of it
                        // LightningCloud cloud3 = new LightningCloud(map.getMapTile(24, 1).getLocation());
                        // map.addEnhancedMapTile(cloud3);
                        // listeners.add(cloud3);
                        LightningCloud cloud4 = new LightningCloud(map.getMapTile(28, 1).getLocation());
                        map.addEnhancedMapTile(cloud4);
                        listeners.add(cloud4);
                        LightningCloud cloud5 = new LightningCloud(map.getMapTile(31, 1).getLocation());
                        map.addEnhancedMapTile(cloud5);
                        listeners.add(cloud5);
                        cloudsSpawned = true;
                }

        }

        public void spawnTornadoes() {
                
                if (!tornadoesSpawned) {
                        Random random = new Random();

                        Tornado tornado1 = new Tornado(map.getMapTile(18, 11).getLocation().subtractY(20).subtractY(20), map.getMapTile(20, 11).getLocation(), random.nextFloat()*2+3, Direction.RIGHT);
                        map.addEnhancedMapTile(tornado1);
                        listeners.add(tornado1);

                        Tornado tornado2 = new Tornado(map.getMapTile(13, 9).getLocation().subtractY(20), map.getMapTile(16,9).getLocation().subtractY(20), random.nextFloat()*2+3, Direction.RIGHT);
                        map.addEnhancedMapTile(tornado2);
                        listeners.add(tornado2);

                        Tornado tornado3= new Tornado(map.getMapTile(17,6).getLocation().subtractY(20), map.getMapTile(19,6).getLocation().subtractY(20), random.nextFloat()*2+3, Direction.RIGHT);
                        map.addEnhancedMapTile(tornado3);
                        listeners.add(tornado3);

                        Tornado tornado4 = new Tornado(map.getMapTile(29,11).getLocation().subtractY(20), map.getMapTile(31,11 ).getLocation().subtractY(20), random.nextFloat()*2+3, Direction.RIGHT);
                        map.addEnhancedMapTile(tornado4);
                        listeners.add(tornado4);

                        Tornado tornado5 = new Tornado(map.getMapTile(30,6).getLocation().subtractY(20), map.getMapTile(32,6).getLocation().subtractY(20), random.nextFloat()*2+3, Direction.RIGHT);
                        map.addEnhancedMapTile(tornado5);
                        listeners.add(tornado5);

                        Tornado tornado6 = new Tornado(map.getMapTile(33,9).getLocation().subtractY(20), map.getMapTile(36,9).getLocation().subtractY(20), random.nextFloat()*2+3, Direction.RIGHT);
                        map.addEnhancedMapTile(tornado6);
                        listeners.add(tornado6);

                        tornadoesSpawned = true;
                }
        }

        private void spawnEndLevelBox() {
                EndLevelBox endLevelBox1 = new EndLevelBox(map.getMapTile(24, 9).getLocation());
                map.addEnhancedMapTile(endLevelBox1);   
                
                EndLevelBox endLevelBox2 = new EndLevelBox(map.getMapTile(25, 9).getLocation());
                map.addEnhancedMapTile(endLevelBox2);    
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
                                                                .withBounds(0, 0, 0, 0)
                                                                .build()
                                });
                                put("BOSS_HURT_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(5, 1), 40)
                                                                .withScale(scale)
                                                                .withBounds(0, 0, 0, 0)
                                                                .build()
                                });
                        }
                };
        }

        public enum BossState {
                STAND, SHOOT_WAIT, SHOOT, HURT
        }
}
