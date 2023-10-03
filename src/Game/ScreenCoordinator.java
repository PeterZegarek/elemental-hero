package Game;

import Engine.DefaultScreen;
import Engine.GraphicsHandler;
import Engine.Screen;
import Screens.CreditsScreen;
import Screens.MenuScreen;
import Screens.PlayEarthLevelScreen;
import Screens.PlayFireLevelScreen;
import Screens.PlayWaterLevelScreen;
import Screens.PlayElectricLevelScreen;
import Screens.PlayAirLevelScreen;
import Screens.PlayVoidLevelScreen;

/*
 * Based on the current game state, this class determines which Screen should be shown
 * There can only be one "currentScreen", although screens can have "nested" screens
 */
public class ScreenCoordinator extends Screen {
	// currently shown Screen
	protected Screen currentScreen = new DefaultScreen();

	// keep track of gameState so ScreenCoordinator knows which Screen to show
	protected static GameState gameState;
	protected GameState previousGameState;

	public static GameState getGameState() {
		return gameState;
	}

	// Other Screens can set the gameState of this class to force it to change the currentScreen
	public void setGameState(GameState gameState) {
		ScreenCoordinator.gameState = gameState;
	}

	@Override
	public void initialize() {
		// start game off with Menu Screen
		gameState = GameState.MENU;
	}

	@Override
	public void update() {
		do {
			// if previousGameState does not equal gameState, it means there was a change in gameState
			// this triggers ScreenCoordinator to bring up a new Screen based on what the gameState is			
			if (previousGameState != gameState) {
				switch(gameState) {
					case MENU:
						currentScreen = new MenuScreen(this);
						break;
					case LEVEL1:
						currentScreen = new PlayEarthLevelScreen(this); //MAP TESTING - CHANGE TEST MAP TO LEVEL1 or Replace "EARTH" with whichever ELEMENT NAME
						break;
					case LEVEL2:
						currentScreen = new PlayFireLevelScreen(this);
						break;
					case LEVEL3:
						currentScreen = new PlayWaterLevelScreen(this);
						break;
					case LEVEL4:
						currentScreen = new PlayElectricLevelScreen(this);
						break;
					case LEVEL5:
						currentScreen = new PlayAirLevelScreen(this);
						break;
					case LEVEL6:
						currentScreen = new PlayVoidLevelScreen(this);
						break;
					case CREDITS:
						currentScreen = new CreditsScreen(this);
						break;
				}
				currentScreen.initialize();
			}
			previousGameState = gameState;

			// call the update method for the currentScreen
			currentScreen.update();
		} while (previousGameState != gameState);
	}

	@Override
	public void draw(GraphicsHandler graphicsHandler) {
		// call the draw method for the currentScreen
		currentScreen.draw(graphicsHandler);
	}
}
