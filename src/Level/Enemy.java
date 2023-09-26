package Level;

import GameObject.Frame;
import GameObject.SpriteSheet;
import Players.PlayerFireball;

import java.util.ArrayList;
import java.util.HashMap;


// This class is a base class for all enemies in the game -- all enemies should extend from it
public class Enemy extends MapEntity implements ElementalAbilityListener {

    //Variable to figure out if there currently is a fireball on the map or not
    protected PlayerFireball activeFireball = null;

    // classes that listen to elemental events can be added to this list
    protected ArrayList<ElementalAbilityListener> listeners = new ArrayList<>();

    int counter = 0;
    public void addListener(ElementalAbilityListener listener) {
        listeners.add(listener);
        counter++;
        System.out.println(counter);
    }

    @Override
    public void fireballSpawned(PlayerFireball fireball){
        activeFireball = fireball;
    }

    @Override
    public void fireballDespawned(){
        activeFireball = null;
    }
    

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

    
    public void update(Player player) {
        super.update();
        if (intersects(player)) {
            touchedPlayer(player);
        }
        if (activeFireball != null){
            if (intersects(activeFireball)){
                enemyAttacked(this);
            }
        }
    }

    // add params for each elemental ability to interact with enemies (however this needs to happen)
    public void updateForElementalAbility(PlayerFireball fireball){
        if (intersects(fireball)){
            // Add whatever code the elemental ability needs to do to the enemy for it to be attacked.
            // Fireball just makes it dissapear.
            // Maybe the earth ability will knock it up in the air then die.
            enemyAttacked(this);
        }
    }

    // A subclass can override this method to specify what it does when it touches the player
    public void touchedPlayer(Player player) {
        player.hurtPlayer(this);
    }

    // -Peter 9/21
    // I'm thinking that we should treat all the elemental abilities as "enemies"
    // When the ability attacks an enemy this function will be called
    // Override as necessary
    // The enemy parameter is the enemy being hit
    public void enemyAttacked(Enemy enemy){
        System.out.println("Enemy attacked");
        // This should make the enemy dissapear
        enemy.mapEntityStatus = MapEntityStatus.REMOVED;
    }
}
