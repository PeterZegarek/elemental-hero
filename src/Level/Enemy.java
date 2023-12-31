package Level;

import GameObject.Frame;
import GameObject.SpriteSheet;
import Players.PlayerFireball;
import Players.RockAttack;
import Players.Wave;

import java.util.HashMap;

import Enemies.WaveEnemy;


// This class is a base class for all enemies in the game -- all enemies should extend from it
public class Enemy extends MapEntity implements ElementalAbilityListener {

    //Variable to figure out if there currently is a fireball on the map or not
    protected PlayerFireball activeFireball = null;
    protected Wave activeWave = null;
    protected WaveEnemy activeWaveEnemy = null;
    protected RockAttack activeRockAttack = null;

    protected boolean isOnMap = true;

    @Override
    public void rockAttackSpawned(RockAttack rock) {
        this.activeRockAttack = rock;
    }
    @Override
    public void rockAttackDespawned() {
       activeRockAttack = null;
    }
    @Override
    public void rockAttackKilledEnemy() {
    }


    // These come from the listener and let the enemy know whether or not there is a fireball active
    @Override
    public void fireballSpawned(PlayerFireball fireball){
        activeFireball = fireball;
    }
    @Override
    public void fireballDespawned(){
        activeFireball = null;
    }
    
    // This is irrelevant for enemies, only relevant for the player fireball
    @Override
    public void fireballKilledEnemy(){}

    @Override
    public void waveSpawned(Wave wave){
        activeWave = wave;
    }
    
    @Override
    public void waveDespawned(){
        activeWave = null;
    }

    @Override
    public void waveKilledEnemy(){}

    @Override
    public void waveEnemySpawned(WaveEnemy waveEnemy){
        activeWaveEnemy = waveEnemy;
    }
    
    @Override
    public void waveEnemyDespawned(){
        activeWaveEnemy = null;
    }

    @Override
    public void waveEnemyKilledEnemy(){}

    // Enemy constructors
    public Enemy(float x, float y, SpriteSheet spriteSheet, String startingAnimation) {
        super(x, y, spriteSheet, startingAnimation);
    }

    public Enemy(float x, float y, HashMap<String, Frame[]> animations, String startingAnimation) {
        super(x, y, animations, startingAnimation);
    }

    public Enemy(float x, float y, Frame[] frames) {
        super(x, y, frames);
    }

    public Enemy(float x, float y, Frame frame) {
        super(x, y, frame);
    }

    public Enemy(float x, float y) {
        super(x, y);
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    // This is called every frame
    public void update(Player player) {
        super.update();
        if (intersects(player)) {
            touchedPlayer(player);
        }
        // If there is a fireball active on the screen it checks if the enemy is touching it
        // This is commented out for the enemies within the final boss to work
        // if (activeFireball != null){
        //     if (intersects(activeFireball)){
        //         enemyAttacked(this);
        //     }
        // }
        // if(activeWave!=null){
        //     if(intersects(activeWave)){
        //         enemyAttacked(this);
        //     }
        // }
        // if(activeRockAttack!=null){
        //     if(intersects(activeRockAttack)){
        //         enemyAttacked(this);
        //     }
        // }
    }


    // A subclass can override this method to specify what it does when it touches the player
    public void touchedPlayer(Player player) {
        player.hurtPlayer(this);
    }

    // All the elemental abilities are being treated as "enemies"
    // When the ability attacks an enemy this function will be called
    // Override as necessary; The enemy parameter is the enemy being hit
    public void enemyAttacked(Enemy enemy){
    
        isOnMap = false;
        // This makes the enemy dissapear
        enemy.mapEntityStatus = MapEntityStatus.REMOVED;
    }
   
     
}
