package Level;

import java.util.ArrayList;
import java.util.List;

import Players.PlayerFireball;
import Players.RockAttack;
import Players.Wave;

public class ElementalAbilityListenerManager {
    // List of (probably) enemies that need to have a reaction to getting hit by an elemental ability.
    // Simple terms - if they should be reacting (taking damage, etc) to elemental abilities, you need to add them to this list. 
    // If you're confused, check BugEnemy's initialize block. It's the second line (first after the comment)
    private static List<ElementalAbilityListener> enemyListeners = new ArrayList<>();

    // List of elemental abilities that need to react to something from an enemy; currently water and fire ability
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
    public static void fireballSpawned(PlayerFireball playerFireball) {
        for (ElementalAbilityListener listener : enemyListeners) {
            listener.fireballSpawned(playerFireball);
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


    // broadcasts the wave spawning to all listeners
    public static void waveSpawned(Wave wave) {
        for (ElementalAbilityListener listener : enemyListeners) {
            listener.waveSpawned(wave);
        }
    }

    // broadcasts the wave despawning to all listeners
    public static void waveDespawned() {
        for (ElementalAbilityListener listener : enemyListeners) {
            listener.waveDespawned();
        }
    }

    // broadcasts the fact that the wave killed an enemy to the wave so that it disappears 
    public static void waveKilledEnemy(){
        for (ElementalAbilityListener listener : elementalListeners){
            listener.waveKilledEnemy();
        }
    }

    public static void rockAttackKilledEnemy(){
        for (ElementalAbilityListener listener : elementalListeners){
            listener.rockAttackKilledEnemy();
    }
}
    
    
    public static void rockAttackDespawned() {
        for (ElementalAbilityListener listener : elementalListeners){
            listener.rockAttackDespawned();
    }
}
    
    
    public static void rockAttackSpawned(RockAttack rockAttack) {
        for (ElementalAbilityListener listener : enemyListeners) {
            listener.rockAttackSpawned(rockAttack);
    }
    }
}
    
