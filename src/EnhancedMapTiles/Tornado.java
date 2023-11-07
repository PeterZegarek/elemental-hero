package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.BossLivesListener;
import Level.EnhancedMapTile;
import Level.MapEntityStatus;
import Level.Player;
import Level.TileType;
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;

import java.util.HashMap;

public class Tornado extends EnhancedMapTile implements BossLivesListener{
    private Point startLocation;
    private Point endLocation;
    private float movementSpeed;
    private Direction startDirection;
    private Direction direction;

    public Tornado(Point startLocation, Point endLocation, float movementSpeed, Direction startDirection) {
        super(startLocation.x, startLocation.y, new SpriteSheet(ImageLoader.load("Tornado.png"), 31, 47), TileType.PASSABLE);
        isUpdateOffScreen = true;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.movementSpeed = movementSpeed;
        this.startDirection = startDirection;
        this.initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        direction = startDirection;
    }

    @Override
    public void update(Player player) {
        float startBound = startLocation.x;
        float endBound = endLocation.x;

        // move platform left or right based on its current direction
        int moveAmountX = 0;
        if (direction == Direction.RIGHT) {
            moveAmountX += movementSpeed;
        } else if (direction == Direction.LEFT) {
            moveAmountX -= movementSpeed;
        }

        moveX(moveAmountX);

        // if platform reaches the start or end location, it turns around
        // platform may end up going a bit past the start or end location depending on movement speed
        // this calculates the difference and pushes the platform back a bit so it ends up right on the start or end location
        if (getX1() + getWidth() >= endBound) {
            float difference = endBound - (getX1() + getWidth());
            moveX(-difference);
            moveAmountX -= difference;
            direction = Direction.LEFT;
        } else if (getX1() <= startBound) {
            float difference = startBound - getX1();
            moveX(difference);
            moveAmountX += difference;
            direction = Direction.RIGHT;
        }

        // makes tornado push the player
        if (tileType == TileType.PASSABLE) { 
            if (intersects(player) && moveAmountX >= 0 && player.getBoundsX1() <= getBoundsX2()) {
                for(int i=0;i<30;i++){
                    if(i%3==0){
                        player.moveXHandleCollision(1);
                        //player.moveYHandleCollision(-1); //to move player up, super glitchy
                    }
                }
            } else if (intersects(player) && moveAmountX <= 0 && player.getBoundsX2() >= getBoundsX1()) {
                for(int i=0;i<30;i++){
                    if(i%3==0){
                        player.moveXHandleCollision(-1);
                        //player.moveYHandleCollision(-1);
                    }
                }
            }
        }

        // if player is on standing on top of platform, move player by the amount the platform is moving
        // this will cause the player to "ride" with the moving platform
        // without this code, the platform would slide right out from under the player
        if (overlaps(player) && (player.getBoundsY2() + 1) == getBoundsY1() && player.getAirGroundState() == AirGroundState.GROUND) {
            player.moveXHandleCollision(moveAmountX);
        }

        super.update(player);
    }

    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

    @Override
    public void getBossLives(int bossLives){
        if (bossLives < 1){
            this.mapEntityStatus = MapEntityStatus.REMOVED;
        }
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                        .withScale(2)
                        .withBounds(5, 17, 20, 40)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                        .withScale(2)
                        .withBounds(5, 17, 20, 40)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                        .withScale(2)
                        .withBounds(5, 17, 20, 40)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                        .withScale(2)
                        .withBounds(5, 17, 20, 40)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                        .withScale(2)
                        .withBounds(5, 17, 20, 40)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 5), 8)
                        .withScale(2)
                        .withBounds(5, 17, 20, 40)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 6), 8)
                        .withScale(2)
                        .withBounds(5, 17, 20, 40)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 7), 8)
                        .withScale(2)
                        .withBounds(5, 17, 20, 40)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 8), 8)
                        .withScale(2)
                        .withBounds(5, 17, 20, 40)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 9), 8)
                        .withScale(2)
                        .withBounds(5, 17, 20, 40)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 10), 8)
                        .withScale(2)
                        .withBounds(5, 17, 20, 40)
                        .build()
            });
        }};
    }
}
