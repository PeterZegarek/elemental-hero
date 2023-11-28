package Level;

import Enemies.WaveEnemy;
import Players.PlayerFireball;
import Players.RockAttack;
import Players.Wave;

// This listener will be used to broadcast and listen to events relating to the elemental abilities
public interface ElementalAbilityListener {
    void fireballSpawned(PlayerFireball fireball);
    void fireballDespawned();
    // wasn't sure what to name this - this should be broadcasted by an enemy if they get killed by the fireball
    // this will trigger the fireball to delete itself
    void fireballKilledEnemy();

    void rockAttackSpawned(RockAttack rock);
    void rockAttackDespawned();
    void rockAttackKilledEnemy();

    void waveSpawned(Wave wave);
    void waveDespawned();
    void waveKilledEnemy();

    void waveEnemySpawned(WaveEnemy WaveEnemy);
    void waveEnemyDespawned();
    void waveEnemyKilledEnemy();
}
