package Level;

import java.util.ArrayList;
import java.util.List;

import Players.PlayerFireball;

public class ElementalAbilityListenerManager {
    private static List<ElementalAbilityListener> listeners = new ArrayList<>();

    public static void addListener(ElementalAbilityListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public static void removeListener(ElementalAbilityListener listener) {
        listeners.remove(listener);
    }

    public static void fireballSpawned(PlayerFireball fireball) {
        for (ElementalAbilityListener listener : listeners) {
            listener.fireballSpawned(fireball);
        }
    }

    public static void fireballDespawned() {
        for (ElementalAbilityListener listener : listeners) {
            listener.fireballDespawned();
        }
    }
}
