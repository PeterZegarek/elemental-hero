package Enemies;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.Enemy;
import Level.Player;
import Utils.Point;

import java.util.HashMap;

// This class is for the invisible enemy that one shots the player
public class InvisibleEnemy extends Enemy {



    public InvisibleEnemy(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("InvisibleEnemy.png"), 16, 16), "DEFAULT");
        this.initialize();
    }



    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void update(Player player) {

        // if it touches the player it will kill the player
        if (intersects(player) ){
            touchedPlayer(player);
        }
        super.update(player);


    }

    // kill the player
    @Override
    public void touchedPlayer(Player player){
        player.killPlayer();
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

