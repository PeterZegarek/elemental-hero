package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.NPC;
import Level.Player;
import Utils.Point;

import java.util.HashMap;

// This class is for the UnknownTraveler NPC
public class UnknownTraveler extends NPC {

    public UnknownTraveler(Point location) {
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("NPCPOSSS.png"), 37, 47), "STANDING");
        isInteractable = true;
        talkedToTime = 200;
        if (ScreenCoordinator.getGameState() == GameState.LEVEL1) // Earth Level
            textbox.setText("Press the 'F' key to use Fireball. Oh, and beware... something 'BIG' lives in the cave!");
        else if (ScreenCoordinator.getGameState() == GameState.LEVEL2) // Fire Level
            textbox.setText("Press the 'W' key to use WaterBlast...Oh, and Watch out for Lava!");
        else if (ScreenCoordinator.getGameState() == GameState.LEVEL3) // Water Level
            textbox.setText("");
        else if (ScreenCoordinator.getGameState() == GameState.LEVEL4) // Electric Level
            textbox.setText("Yep, you can stand on clouds. Watch out for lightning... the SHIFT key might help you glide around in the air.");
        else if (ScreenCoordinator.getGameState() == GameState.LEVEL5) // Air Level
            textbox.setText("");
        else if (ScreenCoordinator.getGameState() == GameState.LEVEL6) // Void Level
            textbox.setText("");
           
        textboxOffsetX = -4;
        textboxOffsetY = -34;
    }

    public void update(Player player) {
/* 
        if (talkedTo) {
            currentAnimationName = "STANDING";
        } else {
            currentAnimationName = "STANDING";
        }
*/
        super.update(player);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STANDING", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                           .withScale(2)
                           .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                           .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                           .withScale(2)
                           .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                           .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 14)
                           .withScale(2)
                           .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                           .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                            .withScale(2)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                           .build(),
           });
        }};
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
