package Players;

import java.util.HashMap;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.ElementalAbilityListenerManager;
import Level.Enemy;
import Level.MapEntity;
import Level.MapEntityStatus;
import Level.Player;
import Utils.Direction;

public class Wave extends Enemy {
    private final float movementSpeed = 0;
    private int existenceFrames = 32;
    private Direction startDirection;
    private Direction facingDirection;

    public Wave(float xPos, float yPos, Direction direction) {
        super(xPos, yPos, new SpriteSheet(ImageLoader.load("Wave.png"), 32, 67), "RIGHT");
        //direction the wave is facing
        this.startDirection = direction;

        initialize();
    }

    @Override
    public void initialize() {
        // This lets other classes know that a wave has been spawned
        ElementalAbilityListenerManager.waveSpawned(this);
        // This is so that it can react when killing something (by disappearing)
        ElementalAbilityListenerManager.addElementListener(this);
        super.initialize();
        facingDirection = startDirection;
        if (facingDirection == Direction.RIGHT) {
            currentAnimationName = "RIGHT";
        } else if (facingDirection == Direction.LEFT) {
            currentAnimationName = "LEFT";
        }
    }

    @Override
    public void update(Player player) {
        // if timer is up, set map entity status to REMOVED
        // the camera class will see this next frame and remove it permanently from the map
        if (existenceFrames == 0) {
            ElementalAbilityListenerManager.waveDespawned();
            this.mapEntityStatus = MapEntityStatus.REMOVED;
        } else {
            // move wave forward
            moveXHandleCollision(movementSpeed);
            super.update(player);
        }
        existenceFrames--;
    }

    // If it collides with a map tile it will dissapear
    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // if wave collides with anything solid on the x axis, it is removed
       // if (hasCollided) {
       //     ElementalAbilityListenerManager.waveDespawned();
       //     this.mapEntityStatus = MapEntityStatus.REMOVED;
       // }
    }

    // Touched player should not do anything
    @Override
    public void touchedPlayer(Player player){}


    // As it stands, enemyAttacked is not necessary as the enemies themselves will die if they get hit
    @Override
    public void enemyAttacked(Enemy enemy) {}

    // If the wave has killed an enemy, it will disappear
    @Override
    public void waveKilledEnemy(){
        //ElementalAbilityListenerManager.waveDespawned();
        //this.mapEntityStatus = MapEntityStatus.REMOVED;
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            
                put("RIGHT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                            .withScale(2) 
                            .withBounds(1, 1, 30, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                            .withScale(2) 
                            .withBounds(1, 1, 30, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(2) 
                            .withBounds(1, 1,30, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(2) 
                            .withBounds(1, 1, 30, 30)
                            .build()
                }); 
            
                put("LEFT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL) 
                            .withBounds(1, 1, 30, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                            .withScale(2) 
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(1, 1, 30, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(2) 
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(1, 1, 30, 30)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(2) 
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(1, 1, 30, 30)
                            .build()
                });
           
        }};
    }
}
