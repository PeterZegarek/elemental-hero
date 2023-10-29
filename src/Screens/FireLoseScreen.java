package Screens;

import Engine.*;
import Game.ScreenCoordinator;
import SpriteFont.SpriteFont;

import java.awt.*;

// This is the class for the Fire level lose screen
public class FireLoseScreen extends Screen {
    protected SpriteFont loseMessage;
    protected SpriteFont instructions;
    protected KeyLocker keyLocker = new KeyLocker();
    protected PlayFireLevelScreen playFireLevelScreen;
    protected ScreenCoordinator level = new ScreenCoordinator();

    public FireLoseScreen(PlayFireLevelScreen playFireLevelScreen) {
        this.playFireLevelScreen = playFireLevelScreen;
        initialize();
    }

    @Override
    public void initialize() {
        loseMessage = new SpriteFont("You lose!", 395, 239, "Comic Sans", 30, Color.white);
        instructions = new SpriteFont("Press Space to try again or Escape to go back to the main menu", 195, 279,"Comic Sans", 20, Color.white);
        keyLocker.lockKey(Key.SPACE);
        keyLocker.lockKey(Key.ESC);
    }

    @Override
    public void update() {
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (Keyboard.isKeyUp(Key.ESC)) {
            keyLocker.unlockKey(Key.ESC);
        }

        // if space is pressed, reset level. if escape is pressed, go back to main menu
        if (Keyboard.isKeyDown(Key.SPACE) && !keyLocker.isKeyLocked(Key.SPACE)) {
            playFireLevelScreen.resetLevel();
        }else if (Keyboard.isKeyDown(Key.ESC) && !keyLocker.isKeyLocked(Key.ESC)) {
            playFireLevelScreen.goBackToMenu();
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), Color.black);
        loseMessage.draw(graphicsHandler);
        instructions.draw(graphicsHandler);
    }
}
