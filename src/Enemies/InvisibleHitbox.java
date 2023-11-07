package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.Enemy;
import Level.MapEntityStatus;
import Level.Player;
import Utils.Point;

import java.util.HashMap;

// This class is for the invisible enemy that one shots the player
public class InvisibleHitbox extends Enemy {

    private boolean touched;

    private int moves = 0;

    // create the hitbox
    public InvisibleHitbox(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("InvisibleEnemy.png"), 16, 16), "DEFAULT");
        this.initialize();
    }



    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void update(Player player) {

        // if it touches the player it will flip the boolean touched variable
        if (intersects(player) ){
            touched = true;
        }
        else {
            touched = false;
        }

        if (touched){
            touchedPlayer(player);
        }
        super.update(player);

        if (moves  > 0){
            movePlayer(player);
            moves--;
        }

    }

    public boolean isTouched(){
        return touched;
    }

    // begin to move the player over time
    @Override
    public void touchedPlayer(Player player){
        moves = 13;
    }

    // move the player up
    public void movePlayer(Player player){
        player.moveYHandleCollision(-25);

    }

    public void remove(){
        this.mapEntityStatus = MapEntityStatus.REMOVED;
    }


    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[] {
                    // The whole thing is magenta so the sprite number or animation number doesnt really matter
                    // I just wanted to keep it similar to other classes which is why its 16x16
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 60)
                            .withScale(3)
                            .withBounds(0, 0, 16, 16)
                            .build()
            });

        }};
    }
}


