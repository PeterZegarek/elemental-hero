package Enemies;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.MapEntity;
import Level.MapEntityStatus;
import Level.Player;
import Utils.Direction;
import Utils.Point;
import Level.Enemy;
import java.util.HashMap;

// This class is for vertical electricity
// the electrity will strike down a specific amount of tiles
// if the player is hit by it they will be paralyzed
public class VerticalElectricity extends Enemy {
    // private Point startLocation;
    private int frameCounter = 0;
    // private boolean childHitGround = false;
    // private boolean hasSpawnedChild = false;
    private boolean landed = false;
    private int landedFrameCounter = 0;

    public VerticalElectricity(Point startLocation) {
        // Change the image to electricity image
        super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("VerticalLightning.png"), 16, 16),
                "AIR");
        // this.startLocation = startLocation;
        this.initialize();
        currentAnimationName = "AIR";
    }

    @Override
    public void initialize() {
        super.initialize();
    }


    @Override
    public void update(Player player) {

        frameCounter++;

        if (landed){
            currentAnimationName = "STRIKE";
            landedFrameCounter++;
            if (landedFrameCounter == 15){
                this.mapEntityStatus = MapEntityStatus.REMOVED;
            }
        }

        if (currentAnimationName == "AIR") {
            // move lightning down every few frames. If you lower the number it moves faster
            if (frameCounter % 7 == 0) {
                // move down one tile
                this.moveYHandleCollision(26);

                // alternatively create a new lightning below it
                // if (!hasSpawnedChild){
                // spawnNewLightning();
                // }
                // if (childHitGround){
                //     airGroundState = AirGroundState.GROUND;
                // }
                
            }

            // if lightning touches the player it will damage the player and trigger the fact that it landed
            if (intersects(player)) {
                // In the future, paralyze the player
                // Update landed variable to show that lightning has found it's target
                landed = true;
                

            }
        }

        super.update(player);
    }



    // New lightning spawner - irrelevant unless we change how lightning works
    // public boolean spawnNewLightning(){
    //     boolean hasHitGround = false;
    //     Point spawnLocation = new Point(startLocation.x, startLocation.y + 16);

    //     VerticalElectricity verticalElectricity = new VerticalElectricity(spawnLocation);
    //     map.addEnemy(verticalElectricity);
    //     if (verticalElectricity.airGroundState == AirGroundState.GROUND){
    //         hasHitGround = true;
    //     }

    //     hasSpawnedChild = true;
    //     childHitGround = hasHitGround;
    //     return hasHitGround;
    // }


    @Override
    public void onEndCollisionCheckY(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // If it collides with the ground, notify that the lightning has landed
        if (hasCollided){
            landed = true;
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }


    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("AIR", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                            .withScale(4)
                            .withBounds(4, 0, 10, 15)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                            .withScale(4)
                            //.withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(4, 0, 10, 15)
                            .build(),
            });

            put("STRIKE", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(1, 0), 8)
                            .withScale(4)
                            .withBounds(4, 0, 10, 15)
                            .build(),
            });
        }};
    }

}
