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

// This class is for horizontal electricity
// the electrity will shoot out to the side until it hits a wall
// if the player is hit by it they will be paralyzed
public class HorizontalElectricity extends Enemy {
    // private Point startLocation;
    private int frameCounter = 0;
    private boolean landed = false;
    private int landedFrameCounter = 0;

    public HorizontalElectricity(Point startLocation) {
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
            this.mapEntityStatus = MapEntityStatus.REMOVED;
            landedFrameCounter++;
            if (landedFrameCounter == 5){
                // this.mapEntityStatus = MapEntityStatus.REMOVED;
            }
        }
        else {
            // move lightning right every few frames. If you lower the number within the frame counter it moves faster
            if (frameCounter % 7 == 0) {
                // move right one tile
                this.moveXHandleCollision(26);

                
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




    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // If it collides with anything, notify that the lightning has landed
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
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(4)
                            .withBounds(0, 4, 16, 10)
                            .build(),
            });

        }};
    }

}
