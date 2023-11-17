package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.InstructionsScreenMap;
import SpriteFont.SpriteFont;

import java.awt.*;

// This class is for the credits screen
public class InstructionsScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected Map background;
    protected KeyLocker keyLocker = new KeyLocker();
    protected SpriteFont instructionsLabel1, instructionsLabel2, instructionsLabel3, instructionsLabel4, instructionsLabel5;
    protected SpriteFont createdByLabel1, createdByLabel2, createdByLabel3, createdByLabel4, createdByLabel5, createdByLabel6, 
                         createdByLabel7, createdByLabel8, createdByLabel9, createdByLabel10, createdByLabel11, createdByLabel12;
    protected SpriteFont returnInstructionsLabel1, returnInstructionsLabel2;

    public InstructionsScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // setup graphics on screen (background map, spritefont text)
        background = new InstructionsScreenMap();
        background.setAdjustCamera(false);

        //Left Panel - Instructions Page
        instructionsLabel1 = new SpriteFont("Instructions", 255, 175, "Times New Roman", 35, Color.white);
        createdByLabel1 = new SpriteFont("- Explore each level", 190, 250, "Times New Roman", 20, Color.white);
        createdByLabel2 = new SpriteFont("- Locate the 'Unknown Traveler' NPC", 190, 300, "Times New Roman", 20, Color.white);
        createdByLabel3 = new SpriteFont("- Take down enemies along your path", 190, 350, "Times New Roman", 20, Color.white);
        createdByLabel4 = new SpriteFont("- Defeat end-level boss to progress", 190, 400, "Times New Roman", 20, Color.white);

        //Right Panel - Keybinds
        //Movement Binds
        instructionsLabel2 = new SpriteFont("Movement:", 555, 175, "Times New Roman", 20, Color.white);
        createdByLabel5 = new SpriteFont("Jump           ->   Up Arrow", 555, 205, "Times New Roman", 14, Color.white);
        createdByLabel6 = new SpriteFont("Crouch        ->    Down Arrow", 555, 225, "Times New Roman", 14, Color.white);
        createdByLabel7 = new SpriteFont("Walk Left     ->   Left Arrow  ", 555, 245, "Times New Roman", 14, Color.white);
        createdByLabel8 = new SpriteFont("Walk Right   ->    Right Arrow ", 555, 265, "Times New Roman", 14, Color.white);
        //Attack Binds
        instructionsLabel3 = new SpriteFont("Attacking:", 555, 300, "Times New Roman", 20, Color.white);
        createdByLabel9 = new SpriteFont("Use Ability        ->   F Key", 555, 330, "Times New Roman", 14, Color.white);
        createdByLabel10 = new SpriteFont("Cycle Abilities   ->   R Key", 555, 350, "Times New Roman", 14, Color.white);
        //Misc Binds
        instructionsLabel4 = new SpriteFont("Misc:", 555, 385, "Times New Roman", 20, Color.white);
        createdByLabel11 = new SpriteFont("Talk to NPC   ->   Space Bar", 555, 415, "Times New Roman", 14, Color.white);
        createdByLabel12 = new SpriteFont("Pause Game   ->   P Key    ", 555, 435, "Times New Roman", 14, Color.white);

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
        instructionsLabel1.draw(graphicsHandler);
        instructionsLabel2.draw(graphicsHandler);
        instructionsLabel3.draw(graphicsHandler);
        instructionsLabel4.draw(graphicsHandler);
        createdByLabel1.draw(graphicsHandler);
        createdByLabel2.draw(graphicsHandler);
        createdByLabel3.draw(graphicsHandler);
        createdByLabel4.draw(graphicsHandler);
        createdByLabel5.draw(graphicsHandler);
        createdByLabel6.draw(graphicsHandler);
        createdByLabel7.draw(graphicsHandler);
        createdByLabel8.draw(graphicsHandler);
        createdByLabel9.draw(graphicsHandler);
        createdByLabel10.draw(graphicsHandler);
        createdByLabel11.draw(graphicsHandler);
        createdByLabel12.draw(graphicsHandler);
        returnInstructionsLabel1.draw(graphicsHandler);
        returnInstructionsLabel2.draw(graphicsHandler);
    }
}
