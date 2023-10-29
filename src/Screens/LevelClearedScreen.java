package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import Engine.ScreenManager;
import SpriteFont.SpriteFont;
import java.awt.*;

// This class is for the level cleared screen
public class LevelClearedScreen extends Screen {
    protected SpriteFont winMessage, earthLevelMessage, fireLevelMessage, waterLevelMessage, electricLevelMessage, airLevelMessage, voidLevelMessage1, voidLevelMessage2;
    protected String map;
    

    public LevelClearedScreen(String map) {
        this.map = map;
        initialize();
    }

    @Override
    public void initialize() {
        winMessage = new SpriteFont("Level Cleared!", 300, 215, "Bold Comic Sans", 50, Color.white);
        earthLevelMessage = new SpriteFont("Now Entering: Fire Level...", 320, 330, "Comic Sans", 25, Color.white);
        fireLevelMessage = new SpriteFont("Now Entering: Water Level...", 303, 330, "Comic Sans", 25, Color.white);
        waterLevelMessage = new SpriteFont("Now Entering: Electric Level...", 300, 330, "Comic Sans", 25, Color.white);
        electricLevelMessage = new SpriteFont("Now Entering: Air Level...", 325, 330, "Comic Sans", 25, Color.white);
        airLevelMessage = new SpriteFont("Now Entering: Void Level!?!?!?", 290, 330, "Comic Sans", 25, Color.white);
        voidLevelMessage1 = new SpriteFont("Congratulations!", 300, 215, "Comic Sans", 50, Color.white);
        voidLevelMessage2 = new SpriteFont("You have completed Elemnental Hero!", 285, 330, "Comic Sans", 25, Color.white);
    }

    @Override
    public void update() {

    }

    public void draw(GraphicsHandler graphicsHandler) {
        // paint entire screen black and dislpay level cleared text
        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), Color.black);
        if(map == "Earth"){
            winMessage.draw(graphicsHandler);
            earthLevelMessage.draw(graphicsHandler);
        }
        else if(map == "Fire"){
            winMessage.draw(graphicsHandler);
            fireLevelMessage.draw(graphicsHandler);
        }
        else if(map == "Water"){
            winMessage.draw(graphicsHandler);
            waterLevelMessage.draw(graphicsHandler);
        }
        else if(map == "Electric"){
            winMessage.draw(graphicsHandler);
            electricLevelMessage.draw(graphicsHandler);
        }
        else if(map == "Air"){
            winMessage.draw(graphicsHandler);
            airLevelMessage.draw(graphicsHandler);
        }
        else if(map == "Void"){
            voidLevelMessage1.draw(graphicsHandler);
            voidLevelMessage2.draw(graphicsHandler);
        }
    }
}
