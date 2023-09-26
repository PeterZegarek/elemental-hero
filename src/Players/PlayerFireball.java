package Players;

import java.util.HashMap;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.ElementalAbilityListenerManager;
import Level.Enemy;
import Level.MapEntity;
import Level.MapEntityStatus;
import Level.Player;
import Utils.Direction;


// This is the class for the fireball that the player can shoot out
// Future updates may require this ability to be hidden and then later on unlockable.
// This might be possible with the locked key mechanic but it's more likely that we just add a global enum / variable 
// This global variable can be used for determining whether or not the ability button press does anything.
public class PlayerFireball extends Enemy {
    private float movementSpeed;
    private int existenceFrames;

    public PlayerFireball(float xPos, float yPos, float movementSpeed, int existenceFrames) {
        // This fireball is going to be larger than the enemy fireball
        super(xPos, yPos, new SpriteSheet(ImageLoader.load("Fireball.png"), 7, 7), "DEFAULT");
       
        // How fast it moves
        this.movementSpeed = movementSpeed;

        // how long the fireball will exist for before disappearing
        this.existenceFrames = existenceFrames;

        initialize();
    }

    @Override
    public void initialize() {
        // This lets other classes know that a fireball has been spawned
        ElementalAbilityListenerManager.fireballSpawned(this);
        super.initialize();
        
    }

    @Override
    public void update(Player player) {
        // if timer is up, set map entity status to REMOVED
        // the camera class will see this next frame and remove it permanently from the map
        if (existenceFrames == 0) {
            ElementalAbilityListenerManager.fireballDespawned();
            this.mapEntityStatus = MapEntityStatus.REMOVED;
        } else {
            // move fireball forward
            moveXHandleCollision(movementSpeed);
            super.update(player);
        }
        existenceFrames--;
    }

    // If it collides with a map tile it will dissapear
    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // if fireball collides with anything solid on the x axis, it is removed
        if (hasCollided) {
            ElementalAbilityListenerManager.fireballDespawned();
            this.mapEntityStatus = MapEntityStatus.REMOVED;
        }
    }

    // Touched player should not do anything
    @Override
    public void touchedPlayer(Player player){}


    // As it stands, enemyAttacked is not necessary as the enemies themselves will die if they get hit
    @Override
    public void enemyAttacked(Enemy enemy) {}

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(5) // This scales the size of the sprite, good to keep in mind
                            .withBounds(1, 1, 5, 5)
                            .build()
            });
        }};
    }
}
