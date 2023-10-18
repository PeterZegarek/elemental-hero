package EnhancedMapTiles;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import GameObject.Rectangle;
import Level.EnhancedMapTile;
import Level.Player;
import Level.TileType;
import Utils.Direction;
import Utils.Point;

import java.awt.image.BufferedImage;


//Just a cloud that moves up/down
//Copied from horizontal moving platform class
public class MovingCloud extends EnhancedMapTile{
    private Point startLocation;
    private Point endLocation;
    private float movementSpeed = 1f;
    private Direction startDirection;
    private Direction direction;

    public MovingCloud(BufferedImage image, Point startLocation, Point endLocation, TileType tileType, float scale, Rectangle bounds, Direction startDirection) {
        super(startLocation.x, startLocation.y, new FrameBuilder(image).withBounds(bounds).withScale(scale).build(), tileType);
        isUpdateOffScreen = true; //should be true
        this.startLocation = startLocation;
        this.endLocation = endLocation;
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
        float startBound = startLocation.y;
        float endBound = endLocation.y;

        //This keeps player from glitching when cloud moves down
        if(direction == Direction.DOWN && overlaps(player)){
            player.moveY(movementSpeed);
        }

        // move platform up or down based on its current direction
        int moveAmountY = 0;
        if (direction == Direction.DOWN) {
            moveAmountY += movementSpeed;
        } else if (direction == Direction.UP) {
            moveAmountY -= movementSpeed;
        }

        moveY(moveAmountY);

        // if platform reaches the start or end location, it turns around
        // platform may end up going a bit past the start or end location depending on movement speed
        // this calculates the difference and pushes the platform back a bit so it ends up right on the start or end location
        if (getY1() + getHeight() <= endBound) {
            float difference = endBound - (getY1() + getHeight()); //Can comment out first three lines and it still works
            moveY(-difference);
            moveAmountY -= difference;
            direction = Direction.DOWN;
        } else if (getY1() >= startBound) {
            float difference = startBound - getY1();
            moveY(difference);
            moveAmountY += difference;
            direction = Direction.UP;
        }

        // if tile type is NOT PASSABLE, if the platform is moving and hits into the player (x axis), it will push the player
        if (tileType == TileType.NOT_PASSABLE) {
            if (intersects(player) && moveAmountY >= 0 && player.getBoundsY1() <= getBoundsY2()) {
                player.moveYHandleCollision(getBoundsY2() - player.getBoundsY1());
            } else if (intersects(player) && moveAmountY <= 0 && player.getBoundsY2() >= getBoundsY1()) { 
                player.moveYHandleCollision(getBoundsY1() - player.getBoundsY2());
            }
        }

        super.update(player);
    }

    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

}
