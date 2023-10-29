package Engine;

import GameObject.Rectangle;
import GameObject.Sprite;
import Level.LevelState;
import Level.Player;
import Level.PlayerState;
import SpriteFont.SpriteFont;
import Utils.Colors;

import javax.swing.*;

import Enemies.Lava;
import Game.GameState;
import Game.ScreenCoordinator;

import java.awt.*;

/*
 * This is where the game loop process and render back buffer is setup
 */
public class GamePanel extends JPanel {
	// loads Screens on to the JPanel
	// each screen has its own update and draw methods defined to handle a "section" of the game.
	private ScreenManager screenManager;

	// used to draw graphics to the panel
	private GraphicsHandler graphicsHandler;

	private boolean isGamePaused = false;
	private SpriteFont pauseLabel;
	private KeyLocker keyLocker = new KeyLocker();
	private final Key pauseKey = Key.P;
	private Thread gameLoopProcess;

	private Key showFPSKey = Key.G;
	private SpriteFont fpsDisplayLabel;
	private boolean showFPS = false;
	private int currentFPS;

	private Sprite heart1;
	private Sprite heart2;
	private Sprite heart3;
	private GameState currentGameState;


	// The JPanel and various important class instances are setup here
	public GamePanel() {
		super();
		this.setDoubleBuffered(true);

		// attaches Keyboard class's keyListener to this JPanel
		this.addKeyListener(Keyboard.getKeyListener());

		graphicsHandler = new GraphicsHandler();

		screenManager = new ScreenManager();

		pauseLabel = new SpriteFont("PAUSE", 365, 280, "Comic Sans", 24, Color.white);
		pauseLabel.setOutlineColor(Color.black);
		pauseLabel.setOutlineThickness(2.0f);

		fpsDisplayLabel = new SpriteFont("FPS", 4, 3, "Comic Sans", 12, Color.black);

		currentFPS = Config.TARGET_FPS;

		heart1 = new Sprite(ImageLoader.load("Hearts.png").getSubimage(0, 0, 48, 48), 30, 20);
		heart2 = new Sprite(ImageLoader.load("Hearts.png").getSubimage(0, 0, 48, 48), 79, 20);
		heart3 = new Sprite(ImageLoader.load("Hearts.png").getSubimage(0, 0, 48, 48), 128, 20);
		
	
		// this game loop code will run in a separate thread from the rest of the program
		// will continually update the game's logic and repaint the game's graphics
		GameLoop gameLoop = new GameLoop(this);
		gameLoopProcess = new Thread(gameLoop.getGameLoopProcess());
	}

	// this is called later after instantiation, and will initialize screenManager
	// this had to be done outside of the constructor because it needed to know the JPanel's width and height, which aren't available in the constructor
	public void setupGame() {
		setBackground(Colors.CORNFLOWER_BLUE);
		screenManager.initialize(new Rectangle(getX(), getY(), getWidth(), getHeight()));
	}

	// this starts the timer (the game loop is started here
	public void startGame() {
		gameLoopProcess.start();
	}

	public ScreenManager getScreenManager() {
		return screenManager;
	}

	public void setCurrentFPS(int currentFPS) {
		this.currentFPS = currentFPS;
	}

	public void update() {
		updatePauseState();
		updateShowFPSState();
		if(Player.getLives()==3){
			heart3.setImage(ImageLoader.load("Hearts.png").getSubimage(0, 0, 48, 48));
			heart2.setImage(ImageLoader.load("Hearts.png").getSubimage(0, 0, 48, 48));
			heart1.setImage(ImageLoader.load("Hearts.png").getSubimage(0, 0, 48, 48));
			heart3.draw(graphicsHandler);
			heart2.draw(graphicsHandler);
			heart1.draw(graphicsHandler);
		}else if(Player.getLives()==2){
			heart3.setImage(ImageLoader.load("Hearts.png").getSubimage(49, 0, 48, 48));
			heart3.draw(graphicsHandler);
		}else if(Player.getLives()==1){
			heart2.setImage(ImageLoader.load("Hearts.png").getSubimage(49, 0, 48, 48));
			heart2.draw(graphicsHandler);
		}
		
		if(Player.getLevelState()==LevelState.PLAYER_DEAD && Player.getLives()!=3){
			heart1.setImage(ImageLoader.load("Hearts.png").getSubimage(49, 0, 48, 48));
			heart1.draw(graphicsHandler);
			//This is needed because the third heart would still display if hit by lava
			if(Lava.getPlayerTouchedLava()){
				heart3.setImage(ImageLoader.load("Hearts.png").getSubimage(49, 0, 48, 48));
				heart3.draw(graphicsHandler);
			}
		}

		if (!isGamePaused) {
			screenManager.update();
		}
	}

	private void updatePauseState() {
		if (Keyboard.isKeyDown(pauseKey) && !keyLocker.isKeyLocked(pauseKey)) {
			isGamePaused = !isGamePaused;
			keyLocker.lockKey(pauseKey);
		}

		if (Keyboard.isKeyUp(pauseKey)) {
			keyLocker.unlockKey(pauseKey);
		}
	}

	private void updateShowFPSState() {
		if (Keyboard.isKeyDown(showFPSKey) && !keyLocker.isKeyLocked(showFPSKey)) {
			showFPS = !showFPS;
			keyLocker.lockKey(showFPSKey);
		}

		if (Keyboard.isKeyUp(showFPSKey)) {
			keyLocker.unlockKey(showFPSKey);
		}

		fpsDisplayLabel.setText("FPS: " + currentFPS);
	}

	public void draw() {
		screenManager.draw(graphicsHandler);

		// if game is paused, draw pause gfx over Screen gfx
		if (isGamePaused) {
			pauseLabel.draw(graphicsHandler);
			graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), new Color(0, 0, 0, 100));
		}

		if (showFPS) {
			fpsDisplayLabel.draw(graphicsHandler);
		}
		
		if(ScreenCoordinator.getGameState() == GameState.LEVEL1 || ScreenCoordinator.getGameState() == GameState.LEVEL2 || ScreenCoordinator.getGameState() == GameState.LEVEL3 
		|| ScreenCoordinator.getGameState() == GameState.LEVEL4 || ScreenCoordinator.getGameState() == GameState.LEVEL5 || ScreenCoordinator.getGameState() == GameState.LEVEL6 
		&& Player.getLevelState()==LevelState.RUNNING){
		
			heart1.draw(graphicsHandler);
			heart2.draw(graphicsHandler);
			heart3.draw(graphicsHandler);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// every repaint call will schedule this method to be called
		// when called, it will setup the graphics handler and then call this class's draw method
		graphicsHandler.setGraphics((Graphics2D) g);
		draw();
	}
}
