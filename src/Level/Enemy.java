package Level;

import GameObject.Frame;
import GameObject.SpriteSheet;
import Players.PlayerFireball;

import java.util.HashMap;


// This class is a base class for all enemies in the game -- all enemies should extend from it
public class Enemy extends MapEntity implements ElementalAbilityListener {

    //Variable to figure out if there currently is a fireball on the map or not
    protected PlayerFireball activeFireball = null;

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
        if (activeFireball != null){
            if (intersects(activeFireball)){
                enemyAttacked(this);
            }
        }
    }


    // A subclass can override this method to specify what it does when it touches the player
    public void touchedPlayer(Player player) {
        player.hurtPlayer(this);
    }

    // All the elemental abilities are being treated as "enemies"
    // When the ability attacks an enemy this function will be called
    // Override as necessary; The enemy parameter is the enemy being hit
    public void enemyAttacked(Enemy enemy){
    
        // This makes the enemy dissapear
        enemy.mapEntityStatus = MapEntityStatus.REMOVED;
    }
}
