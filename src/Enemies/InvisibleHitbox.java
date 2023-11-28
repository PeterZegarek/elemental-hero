package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.Enemy;
import Level.MapEntityStatus;
import Level.Player;
import Utils.Direction;
import Utils.Point;

import java.util.HashMap;

// This class is for the invisible enemy that one shots the player
public class InvisibleHitbox extends Enemy {

    private boolean touched;

    // variable to keep track of launching the player up
    private int moves = 0;

    private float movementSpeed = 0;
    private boolean isMoving = false;

    // start and end location defines the two points that it flies between
    protected Point startLocation;
    protected Point endLocation;

    protected Direction facingDirection = null;

    // create the hitbox
    public InvisibleHitbox(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("InvisibleEnemy.png"), 16, 16), "DEFAULT");
        this.initialize();
    }

    // this is called by the electric mini boss
    // if ever used again it would need to be changed to suit something else
    public InvisibleHitbox(Point location, float movementSpeed, Point startLocation, Point endLocation) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("InvisibleEnemy.png"), 16, 16), "ELECTRIC");
        this.movementSpeed = movementSpeed;
        isMoving = true;
        this.startLocation = startLocation;
        this.endLocation = endLocation.subtractX(45);
        // hardcoding this for now, should only be applicable in this one scenario
        // although this wouldn't be hard to fix
        facingDirection = Direction.RIGHT;
        this.initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void update(Player player) {


        // if it touches the player it will flip the boolean touched variable
        if (intersects(player)) {
            touched = true;
        } else {
            touched = false;
        }

        if (touched) {
            touchedPlayer(player);
        }
        super.update(player);

        if (moves > 0) {
            movePlayer(player);
            moves--;
        }

        // if this is a moving hitbox, it will move
        if (isMoving) {
            float startBound = startLocation.x;
            float endBound = endLocation.x;
            // Need it to actually move
            // When changing direction it changes the way he is facing
            if (facingDirection == Direction.RIGHT) {
                moveXHandleCollision(movementSpeed);
            } else {
                moveXHandleCollision(-movementSpeed);
            }
            // this makes sure it doesn't go far over the end bound or start bound
            if (getX1() + getWidth() >= endBound) {
                float difference = endBound - (getX2());
                moveXHandleCollision(-difference);
                facingDirection = Direction.LEFT;
            } else if (getX1() <= startBound) {
                float difference = startBound - getX1();
                moveXHandleCollision(difference);
                facingDirection = Direction.RIGHT;
            }
        }

    }

    public boolean isTouched() {
        return touched;
    }

    // begin to move the player over time
    @Override
    public void touchedPlayer(Player player) {
        moves = 13;
    }

    // move the player up
    public void movePlayer(Player player) {
        player.moveYHandleCollision(-25);

    }

    public void remove() {
        this.mapEntityStatus = MapEntityStatus.REMOVED;
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {
            {
                put("DEFAULT", new Frame[] {
                        // The whole thing is magenta so the sprite number or animation number doesnt
                        // really matter
                        // I just wanted to keep it similar to other classes which is why its 16x16
                        new FrameBuilder(spriteSheet.getSprite(0, 1), 60)
                                .withScale(3)
                                .withBounds(0, 0, 16, 16)
                                .build()
                });
                put("ELECTRIC", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(0, 1), 60)
                                .withScale(3)
                                .withBounds(0, 0, 31, 10)
                                .build()
                });

            }
        };
    }
}
