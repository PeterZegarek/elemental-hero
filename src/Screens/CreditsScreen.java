package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMap;
import SpriteFont.SpriteFont;

import java.awt.*;

// This class is for the credits screen
public class CreditsScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected Map background;
    protected KeyLocker keyLocker = new KeyLocker();
    protected SpriteFont creditsLabel;
    protected SpriteFont createdByLabel1, createdByLabel2, createdByLabel3, createdByLabel4, createdByLabel5, createdByLabel6, createdByLabel7, createdByLabel8;
    protected SpriteFont returnInstructionsLabel1, returnInstructionsLabel2;

    public CreditsScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // setup graphics on screen (background map, spritefont text)
        background = new TitleScreenMap();
        background.setAdjustCamera(false);
        creditsLabel = new SpriteFont("Credits", 195, 135, "Times New Roman", 30, Color.white);
        createdByLabel1 = new SpriteFont("Created by:", 195, 185, "Times New Roman", 20, Color.white);
        createdByLabel2 = new SpriteFont("Lukas Martino", 290, 190, "Times New Roman", 16, Color.white);
        createdByLabel3 = new SpriteFont("Peter Zegarek", 290, 205, "Times New Roman", 16, Color.white);
        createdByLabel4 = new SpriteFont("Hunter Yocum", 290, 220, "Times New Roman", 16, Color.white);
        createdByLabel5 = new SpriteFont("Philip Nora ", 290, 235, "Times New Roman", 16, Color.white);
        createdByLabel6 = new SpriteFont("Alex Thimineur", 275, 293, "Times New Roman", 16, Color.white);
        createdByLabel7 = new SpriteFont("Special thanks to...", 195, 270, "Times New Roman", 20, Color.white);
        createdByLabel8 = new SpriteFont("Kevin Rodriguez", 275, 310, "Times New Roman", 16, Color.white);

        returnInstructionsLabel1 = new SpriteFont("(Press Space)", 25, 532, "BOLD Times New Roman", 20, Color.BLACK);
        returnInstructionsLabel2 = new SpriteFont("Return to the Menu", 3, 500, "Bold Times New Roman", 22, Color.BLACK);
        keyLocker.lockKey(Key.SPACE);
    }

    public void update() {
        background.update(null);

        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }

        // if space is pressed, go back to main menu
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            screenCoordinator.setGameState(GameState.MENU);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        creditsLabel.draw(graphicsHandler);
        createdByLabel1.draw(graphicsHandler);
        createdByLabel2.draw(graphicsHandler);
        createdByLabel3.draw(graphicsHandler);
        createdByLabel4.draw(graphicsHandler);
        createdByLabel5.draw(graphicsHandler);
        createdByLabel6.draw(graphicsHandler);
        createdByLabel7.draw(graphicsHandler);
        createdByLabel8.draw(graphicsHandler);
        returnInstructionsLabel1.draw(graphicsHandler);
        returnInstructionsLabel2.draw(graphicsHandler);
    }
}
