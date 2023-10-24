package Players;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.Player;

import java.util.HashMap;

//This is essentially the same as the Cat class but for our own hero character
public class Hero extends Player {
    public Hero(float x, float y) {
        super(new SpriteSheet(ImageLoader.load("HeroSheet.png"), 26, 26), x, y, "STAND_RIGHT");
        gravity = .5f;
        terminalVelocityY = 6f;
        jumpHeight = 14.5f;
        jumpDegrade = .5f;
        walkSpeed = 2.9f;
        momentumYIncrease = .5f;
    }

    public void update() {
        super.update();
    }

    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
        // drawBounds(graphicsHandler, new Color(255, 0, 0, 170));
    }

    // Peter changed 10/20. 
    // Originally the withBounds for the hero was .withBounds(8,10,8,10)

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(2, 0))
                            .withScale(3)
                            .withBounds(8,6,8,14)
                            .build()
            });

            put("STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(2, 0))
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8,6,8,14)
                            .build()
            });

            put("WALK_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(1, 0), 14)
                            .withScale(3)
                            .withBounds(8,6,8,14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                            .withScale(3)
                            .withBounds(8,6,8,14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 2), 14)
                            .withScale(3)
                            .withBounds(8,6,8,14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 3), 14)
                            .withScale(3)
                            .withBounds(8,6,8,14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 4), 14)
                            .withScale(3)
                            .withBounds(8,6,8,14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 5), 14)
                            .withScale(3)
                            .withBounds(8,6,8,14)
                            .build()
            });

            put("WALK_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(1, 0), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8,6,8,14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 1), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8,6,8,14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 2), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8,6,8,14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 3), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8,6,8,14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 4), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8,6,8,14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(1, 5), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8,6,8,14)
                            .build()
            });

            put("JUMP_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(1, 2))
                            .withScale(3)
                            .withBounds(8,6,8,14)
                            .build()
            });

            put("JUMP_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(1, 2))
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8,6,8,14)
                            .build()
            });

            put("FALL_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(1, 3))
                            .withScale(3)
                            .withBounds(8,6,8,14)
                            .build()
            });

            put("FALL_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(1, 3)) 
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8,6,8,14)
                            .build()
            });

            put("CROUCH_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(1, 4))
                            .withScale(3)
                            .withBounds(8,11,8,9)
                            .build()
            });

            put("CROUCH_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(1, 4))
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8,11,8,9)
                            .build()
            });

            put("HURT_STAND_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(2,1))
                            .withScale(3)
                            .withBounds(8,6,8,14)
                            .build()
            });

            put("HURT_STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(2, 1))
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8,6,8,14)
                            .build()
            }); 

            put("HURT_WALK_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(2, 2), 14)
                            .withScale(3)
                            .withBounds(8,6,8,14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 3), 14)
                            .withScale(3)
                            .withBounds(8,6,8,14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 4), 14)
                            .withScale(3)
                            .withBounds(8,6,8,14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 5), 14)
                            .withScale(3)
                            .withBounds(8,6,8,14)
                            .build(),
            });

            put("HURT_WALK_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(2, 2), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8,6,8,14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 3), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8,6,8,14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 4), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8,6,8,14)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(2, 5), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8,6,8,14)
                            .build(),
            });

            put("HURT_JUMP_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(2,3))
                            .withScale(3)
                            .withBounds(8,6,8,14)
                            .build()
            });

            put("HURT_JUMP_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(2, 3))
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8,6,8,14)
                            .build()
            });

            put("HURT_FALL_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(2, 4))
                            .withScale(3)
                            .withBounds(8,6,8,14)
                            .build()
            });

            put("HURT_FALL_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(2, 4))
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(8,6,8,14)
                            .build()
            });

            put("DEATH_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                            .withScale(3)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                            .withScale(3)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(3)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(3)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(3)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), -1)
                            .withScale(3)
                            .build()
            });

            put("DEATH_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 5), -1)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .build()
            });

            put("SWIM_STAND", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(3, 3), 8)
                            .withScale(3)
                            .withBounds(6,5,15,18)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 4), 8)
                            .withScale(3)
                            .withBounds(6,5,15,18)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 5), 8)
                            .withScale(3)
                            .withBounds(6,5,15,18)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 4), 8)
                            .withScale(3)
                            .withBounds(6,5,15,18)
                            .build(),
            });

            put("SWIM_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(3, 0), 8)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(6,5,15,18)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 1), 8)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(6,5,15,18)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 2), 8)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(6,5,15,18)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 1), 8)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(6,5,15,18)
                            .build(),
            });
            put("SWIM_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(3, 0), 8)
                            .withScale(3)
                            .withBounds(6,5,15,18)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 1), 8)
                            .withScale(3)
                            .withBounds(6,5,15,18)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 2), 8)
                            .withScale(3)
                            .withBounds(6,5,15,18)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(3, 1), 8)
                            .withScale(3)
                            .withBounds(6,5,15,18)
                            .build(),
            });
            put("HURT_SWIM_STAND", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(4, 3), 8)
                            .withScale(3)
                            .withBounds(6,5,15,18)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(4, 4), 8)
                            .withScale(3)
                            .withBounds(6,5,15,18)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(4, 5), 8)
                            .withScale(3)
                            .withBounds(6,5,15,18)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(4, 4), 8)
                            .withScale(3)
                            .withBounds(6,5,15,18)
                            .build(),
            });

            put("HURT_SWIM_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(4, 0), 8)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(6,5,15,18)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(4, 1), 8)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(6,5,15,18)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(4, 2), 8)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(6,5,15,18)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(4, 1), 8)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(6,5,15,18)
                            .build(),
            });
            put("HURT_SWIM_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(4, 0), 8)
                            .withScale(3)
                            .withBounds(6,5,15,18)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(4, 1), 8)
                            .withScale(3)
                            .withBounds(6,5,15,18)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(4, 2), 8)
                            .withScale(3)
                            .withBounds(6,5,15,18)
                            .build(),
                    new FrameBuilder(spriteSheet.getSprite(4, 1), 8)
                            .withScale(3)
                            .withBounds(6,5,15,18)
                            .build(),
            });
        }};
    }
}