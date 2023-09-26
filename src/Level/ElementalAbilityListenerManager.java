package Level;

import java.util.ArrayList;
import java.util.List;

import Players.PlayerFireball;

public class ElementalAbilityListenerManager {
    // List of (probably) enemies that need to have a reaction to getting hit by an elemental ability.
    // Simple terms - if they should be reacting (taking damage, etc) to elemental abilities, you need to add them to this list. 
    // If you're confused, check BugEnemy's initialize block. It's the second line (first after the comment)
    private static List<ElementalAbilityListener> enemyListeners = new ArrayList<>();

    // List of elemental abilities that need to react to something from an enemy
    // As of right now (9/26) this is just so the fireball despawns after killing something
    private static List<ElementalAbilityListener> elementalListeners = new ArrayList<>();

    // Add a listener to the arraylist of enemies 
    public static void addEnemyListener(ElementalAbilityListener listener) {
        if (!enemyListeners.contains(listener)) {
            enemyListeners.add(listener);
        }
    }

    // Add a listener to the arraylist of elemental abilities
    public static void addElementListener(ElementalAbilityListener listener){
        if (!elementalListeners.contains(listener)){
            elementalListeners.add(listener);
        }
    }

    // Remove a listener from the arraylist of enemies.
    // not relevant right now i believe
    public static void removeListener(ElementalAbilityListener listener) {
        enemyListeners.remove(listener);
    }

    // broadcasts the fireball spawning to all listeners
    public static void fireballSpawned(PlayerFireball fireball) {
        for (ElementalAbilityListener listener : enemyListeners) {
            listener.fireballSpawned(fireball);
        }
    }

    // broadcasts the fireball despawning to all listeners
    public static void fireballDespawned() {
        for (ElementalAbilityListener listener : enemyListeners) {
            listener.fireballDespawned();
        }
    }

    // broadcasts the fact that the fireball killed an enemy to the fireball so that it disappears 
    public static void fireballKilledEnemy(){
        for (ElementalAbilityListener listener : elementalListeners){
            listener.fireballKilledEnemy();
        }
    }
}
