package Enemies;

import java.util.HashMap;

import Builders.FrameBuilder;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Utils.Point;

// only difference between this class and the traditional fireball is just that this one is bigger
// has a scale of 5 rather than 3
public class LargerFireball extends Fireball{

    public LargerFireball(Point location, float movementSpeed, int existenceFrames) {
        super(location, movementSpeed, existenceFrames);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[]{
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(5)
                            .withBounds(1, 1, 5, 5)
                            .build()
            });
        }};
    }

}