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

    // Not the BEST way to control Sprite animation flip... but it works (Added String variable named "animation" and Added Parameter to Constructor)
    String animation; 

    public UnknownTraveler(Point location, String animation) { 
        super(location.x, location.y, new SpriteSheet(ImageLoader.load("UnknownTraveler.png"), 37, 47), animation);
        isInteractable = true;
        talkedToTime = 200;
        if (ScreenCoordinator.getGameState() == GameState.LEVEL1) { // Earth Level
            textbox.setText("BEWARE! Something evil is lurking in the cave!                                                   ");
            textbox.setFontSize(20);
        }
        else if (ScreenCoordinator.getGameState() == GameState.LEVEL2) {// Fire Level
            textbox.setText("Watch out for lava!                      ");
            textbox.setFontSize(20);
        }
        else if (ScreenCoordinator.getGameState() == GameState.LEVEL3) { // Water Level           
            textbox.setText("Something 'BIG' lies deep within these waters...                                                   ");
            textbox.setFontSize(20);
        }
        else if (ScreenCoordinator.getGameState() == GameState.LEVEL4) { // Electric Level
            textbox.setText("Watch out for lightning! Might want to jump on the clouds...                                                              ");
            textbox.setFontSize(20);
            textbox.setX(60);
        }
        else if (ScreenCoordinator.getGameState() == GameState.LEVEL5) { // Air Level
            textbox.setText("It's windy up there! You might get 'popped' out of existence...                                                                      ");
            textbox.setFontSize(20);
        }
        else if (ScreenCoordinator.getGameState() == GameState.LEVEL6) {// Void Level
            textbox.setText("                       This is the ultimate challenge! \nAll your acquired abilities are at your disposal. Use them wisely...                                                                    ");
            textbox.setFontSize(20);
        }
        textboxOffsetX = 50;
        textboxOffsetY = -34;
        if ((ScreenCoordinator.getGameState() == (GameState.LEVEL4)) || (ScreenCoordinator.getGameState() == GameState.LEVEL1)){
            textboxOffsetX = -250;
        }
        if ((ScreenCoordinator.getGameState() == (GameState.LEVEL6))){
            textboxOffsetX = -150;
            textboxOffsetY = -60;
        }
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
            put("STANDING_RIGHT", new Frame[] {
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
                           .build()
        });
           put("STANDING_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                           .withScale(2)                          
                           .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                           .withScale(2)                     
                           .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 2), 14)
                           .withScale(2)
                           .build(),
                    new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                            .withScale(2)
                           .build(),
            });
        }};
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

    public NPC withImageEffect(ImageEffect flipHorizontal) {
        return null;
    }
}
