package Level;

import Players.PlayerFireball;

// This listener will be used to broadcast and listen to events relating to the elemental abilities
public interface ElementalAbilityListener {
    void fireballSpawned(PlayerFireball fireball);
    void fireballDespawned();
}
