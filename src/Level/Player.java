package Level;

import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Players.PlayerFireball;
import Players.Wave;
import Utils.AirGroundState;
import Utils.Direction;

import java.util.ArrayList;

public abstract class Player extends GameObject{
    // values that affect player movement
    // these should be set in a subclass
    protected float walkSpeed = 0;
    protected float gravity = 0;
    protected float jumpHeight = 0;
    protected float jumpDegrade = 0;
    protected float terminalVelocityY = 0;
    protected float momentumYIncrease = 0;

    // values used to handle player movement
    protected float jumpForce = 0;
    protected float momentumY = 0;
    protected float moveAmountX, moveAmountY;
    protected float lastAmountMovedX, lastAmountMovedY;

    // values used to keep track of player's current state
    protected PlayerState playerState;
    protected PlayerState previousPlayerState;
    protected Direction facingDirection;
    protected AirGroundState airGroundState;
    protected AirGroundState previousAirGroundState;
    protected LevelState levelState;
    protected GameState gameState;
    protected int lives = 3;

    //keeps track of glide power and whether it is on or not
    protected boolean isGlideOn = false;

    // classes that listen to player events can be added to this list
    protected ArrayList<PlayerListener> listeners = new ArrayList<>();

    // define keys
    protected KeyLocker keyLocker = new KeyLocker();
    protected Key JUMP_KEY = Key.UP;
    protected Key MOVE_LEFT_KEY = Key.LEFT;
    protected Key MOVE_RIGHT_KEY = Key.RIGHT;
    protected Key CROUCH_KEY = Key.DOWN;
    //adding key to skip level
    protected Key LEVEL_KEY = Key.L;
    // adding the fireball key F
    protected Key FIREBALL_KEY = Key.F;
    // adding wave key W
    protected Key WAVE_KEY = Key.W;
    //adding glide key SHIFT
    protected Key GLIDE_KEY = Key.SHIFT;

    // flags
    protected boolean isInvincible = false; // if true, player cannot be hurt by enemies (good for testing)
    protected boolean fireballOnCooldown = false; // Whether fireball is on cooldown
    protected boolean waveOnCooldown = false; // Whether wave is on cooldown
    protected static int cooldownCounter; // Time for the fireball/wave to be on cooldown
    protected int isInvincibleCounter; // Invincible for a couple seconds after being hit

    public Player(SpriteSheet spriteSheet, float x, float y, String startingAnimationName) {
        super(spriteSheet, x, y, startingAnimationName);
        facingDirection = Direction.RIGHT;
        airGroundState = AirGroundState.AIR;
        previousAirGroundState = airGroundState;
        playerState = PlayerState.STANDING;
        previousPlayerState = playerState;
        levelState = LevelState.RUNNING;
    }

    public void update() {
        System.out.println(playerState);
        moveAmountX = 0;
        moveAmountY = 0;

        // cooldown counter decreases everytime by 1
       if (cooldownCounter > 0){
            cooldownCounter--;
            if (cooldownCounter == 0){
                fireballOnCooldown = false;
                waveOnCooldown = false;                         
            }
        }
        //cooldown counter for invincibility
        if (isInvincibleCounter > 0){           
            isInvincibleCounter--;
            if (isInvincibleCounter == 0){                                             
                isInvincible = false;             
            }
        }
        // if player is currently playing through level (has not won or lost)
        if (levelState == LevelState.RUNNING) {
            int centerX = Math.round(getBounds().getX1()) + Math.round(getBounds().getWidth() / 2f);
            int centerY = Math.round(getBounds().getY1()) + Math.round(getBounds().getHeight() / 2f);
            MapTile currentMapTile = map.getTileByPosition(centerX, centerY);
            if (currentMapTile != null && currentMapTile.getTileType() == TileType.WATER && playerState != PlayerState.HURT) {
                playerState = PlayerState.SWIMMING;  
                
            }
            applyGravity();

            // update player's state and current actions, which includes things like determining how much it should move each frame and if its walking or jumping
            do {
                previousPlayerState = playerState;
                handlePlayerState();
            } while (previousPlayerState != playerState);

            previousAirGroundState = airGroundState;

            // move player with respect to map collisions based on how much player needs to move this frame
            lastAmountMovedX = super.moveXHandleCollision(moveAmountX);
            lastAmountMovedY = super.moveYHandleCollision(moveAmountY);

            handlePlayerAnimation();

            updateLockedKeys();
            
            // update player's animation
            super.update();
        }

        // if player has beaten level
        else if (levelState == LevelState.LEVEL_COMPLETED) {
            updateLevelCompleted();
        }
        // if player has lost level
        else if (levelState == LevelState.PLAYER_DEAD) {
            updatePlayerDead();
        }
    }

    // add gravity to player, which is a downward force
    protected void applyGravity() {
        moveAmountY += gravity + momentumY;
    }

    // based on player's current state, call appropriate player state handling method
    protected void handlePlayerState() {
        switch (playerState) {
            case HURT:                
                playerHurt();              
                break;
            case STANDING:
                playerStanding();
                break;
            case WALKING:
                playerWalking();
                break;
            case CROUCHING:
                playerCrouching();
                break;
            case JUMPING:
                playerJumping();
                break;
            case SWIMMING:
                playerSwimming();
                break;
        }
    }

    protected void playerSwimming() {

        if (Keyboard.isKeyDown(MOVE_LEFT_KEY)) {
            moveAmountX -= walkSpeed;
            facingDirection = Direction.LEFT;
            playerState = PlayerState.SWIMMING;
        }

        // if walk right key is pressed, move player to the right
        else if (Keyboard.isKeyDown(MOVE_RIGHT_KEY)) {
            moveAmountX += walkSpeed;
            facingDirection = Direction.RIGHT;
            playerState = PlayerState.SWIMMING;
        }   
        else if (Keyboard.isKeyUp(MOVE_LEFT_KEY) && Keyboard.isKeyUp(MOVE_RIGHT_KEY)) {
            playerState = PlayerState.SWIMMING;
        }

        // if jump key is pressed, player enters JUMPING state
        if (Keyboard.isKeyDown(JUMP_KEY)) {
            moveAmountY -= walkSpeed;
            playerState = PlayerState.SWIMMING;
        }

        // if crouch key is pressed,
        else if (Keyboard.isKeyDown(CROUCH_KEY)) {
            moveAmountY += walkSpeed;
            playerState = PlayerState.SWIMMING;
        }
    }
        
    
    
    // player STANDING state logic
    protected void playerStanding() {
        // if walk left or walk right key is pressed, player enters WALKING state
        if (Keyboard.isKeyDown(MOVE_LEFT_KEY) || Keyboard.isKeyDown(MOVE_RIGHT_KEY)) {
            playerState = PlayerState.WALKING;
        }

        // if jump key is pressed, player enters JUMPING state
        else if (Keyboard.isKeyDown(JUMP_KEY) && !keyLocker.isKeyLocked(JUMP_KEY)) {
            keyLocker.lockKey(JUMP_KEY);
            playerState = PlayerState.JUMPING;
        }

        // if crouch key is pressed, player enters CROUCHING state
        else if (Keyboard.isKeyDown(CROUCH_KEY)) {
            playerState = PlayerState.CROUCHING;
        }
    }

    // player WALKING state logic
    protected void playerWalking() {
        // if walk left key is pressed, move player to the left
        if (Keyboard.isKeyDown(MOVE_LEFT_KEY)) {
            moveAmountX -= walkSpeed;
            facingDirection = Direction.LEFT;
        }

        // if walk right key is pressed, move player to the right
        else if (Keyboard.isKeyDown(MOVE_RIGHT_KEY)) {
            moveAmountX += walkSpeed;
            facingDirection = Direction.RIGHT;
        } else if (Keyboard.isKeyUp(MOVE_LEFT_KEY) && Keyboard.isKeyUp(MOVE_RIGHT_KEY)) {
            playerState = PlayerState.STANDING;
        }

        // if jump key is pressed, player enters JUMPING state
        if (Keyboard.isKeyDown(JUMP_KEY) && !keyLocker.isKeyLocked(JUMP_KEY)) {
            keyLocker.lockKey(JUMP_KEY);
            playerState = PlayerState.JUMPING;
        }

        // if crouch key is pressed,
        else if (Keyboard.isKeyDown(CROUCH_KEY)) {
            playerState = PlayerState.CROUCHING;
        }
    }

    // player CROUCHING state logic
    protected void playerCrouching() {
        // if crouch key is released, player enters STANDING state
        if (Keyboard.isKeyUp(CROUCH_KEY)) {
            playerState = PlayerState.STANDING;
        }

        // if jump key is pressed, player enters JUMPING state
        if (Keyboard.isKeyDown(JUMP_KEY) && !keyLocker.isKeyLocked(JUMP_KEY)) {
            keyLocker.lockKey(JUMP_KEY);
            playerState = PlayerState.JUMPING;
        }
    }
    
    // player JUMPING state logic
    protected void playerJumping() {
        // if last frame player was on ground and this frame player is still on ground, the jump needs to be setup
        if (previousAirGroundState == AirGroundState.GROUND && airGroundState == AirGroundState.GROUND) {

            // sets animation to a JUMP animation based on which way player is facing
            currentAnimationName = facingDirection == Direction.RIGHT ? "JUMP_RIGHT" : "JUMP_LEFT";

            // player is set to be in air and then player is sent into the air
            airGroundState = AirGroundState.AIR;
            jumpForce = jumpHeight;
            if (jumpForce > 0) {
                moveAmountY -= jumpForce;
                jumpForce -= jumpDegrade;
                if (jumpForce < 0) {
                    jumpForce = 0;
                }
            }
        }

        // if player is in air (currently in a jump) and has more jumpForce, continue sending player upwards
        else if (airGroundState == AirGroundState.AIR) {
            if (jumpForce > 0) {
                moveAmountY -= jumpForce;
                jumpForce -= jumpDegrade;
                if (jumpForce < 0) {
                    jumpForce = 0;
                }
            }

            // allows you to move left and right while in the air
            if (Keyboard.isKeyDown(MOVE_LEFT_KEY)) {               
                moveAmountX -= walkSpeed;
            } else if (Keyboard.isKeyDown(MOVE_RIGHT_KEY)) {
                moveAmountX += walkSpeed;
            }

            // if player is falling, increases momentum as player falls so it falls faster over time
            if (moveAmountY > 0) {
                increaseMomentum();
            }
        }

        // if player last frame was in air and this frame is now on ground, player enters STANDING state
        // if player hits ground then glide ability shuts off
        else if (previousAirGroundState == AirGroundState.AIR && airGroundState == AirGroundState.GROUND) {
            playerState = PlayerState.STANDING;
            if(isGlideOn){
                isGlideOn = false;
                setTerminalVelocityY(6f);               
            }
        }
    }

    protected void playerHurt() {
        isInvincible = true; 
        playerState = PlayerState.STANDING; //Allows player to move after going into Hurt animation
        isInvincibleCounter = 120; // Keeps Player in a state of Hurt (ADJUST AS SEE FIT) 
        handlePlayerAnimation();
        }

    // while player is in air, this is called, and will increase momentumY by a set amount until player reaches terminal velocity
    protected void increaseMomentum() {
        momentumY += momentumYIncrease;
        if (momentumY > terminalVelocityY) {
            momentumY = terminalVelocityY;
        }
    }

    protected void updateLockedKeys() {
        if (Keyboard.isKeyUp(JUMP_KEY)) {
            keyLocker.unlockKey(JUMP_KEY);
        }
    }

    // anything extra the player should do based on interactions can be handled here
    protected void handlePlayerAnimation() {
        if (playerState == PlayerState.STANDING) {
            // sets animation to a STAND animation based on which way player is facing
            this.currentAnimationName = facingDirection == Direction.RIGHT ? "STAND_RIGHT" : "STAND_LEFT";
            //Makes Hurt Animation stay while standing
            if (isInvincible == true){
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_STAND_RIGHT" : "HURT_STAND_LEFT";    
                }

            // handles putting goggles on when standing in water
            // checks if the center of the player is currently touching a water tile
            int centerX = Math.round(getBounds().getX1()) + Math.round(getBounds().getWidth() / 2f);
            int centerY = Math.round(getBounds().getY1()) + Math.round(getBounds().getHeight() / 2f);
            MapTile currentMapTile = map.getTileByPosition(centerX, centerY);
            if (currentMapTile != null && currentMapTile.getTileType() == TileType.WATER) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "SWIM_STAND" : "SWIM_STAND";
            }
                if (isInvincible == true){
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_SWIM_STAND" : "HURT_SWIM_STAND";   //Change to Hurt_SWIM_STAND_... LEFT or RIGHT 
                }
            }
        
        else if (playerState == PlayerState.HURT) { 
            this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_STAND_RIGHT" : "HURT_STAND_LEFT";                                                
            }         
        else if (playerState == PlayerState.WALKING) {
            // sets animation to a WALK animation based on which way player is facing
            this.currentAnimationName = facingDirection == Direction.RIGHT ? "WALK_RIGHT" : "WALK_LEFT";
            //Makes Hurt Animation stay while walking
            if (isInvincible == true){
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_WALK_RIGHT" : "HURT_WALK_LEFT";    
                }  
            // handles putting goggles on when standing in water
            // checks if the center of the player is currently touching a water tile
            int centerX = Math.round(getBounds().getX1()) + Math.round(getBounds().getWidth() / 2f);
            int centerY = Math.round(getBounds().getY1()) + Math.round(getBounds().getHeight() / 2f);
            MapTile currentMapTile = map.getTileByPosition(centerX, centerY);
            if (currentMapTile != null && currentMapTile.getTileType() == TileType.WATER) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "SWIM_RIGHT" : "SWIM_LEFT";
            }              
        }
        else if (playerState == PlayerState.CROUCHING) {
            // sets animation to a CROUCH animation based on which way player is facing
            this.currentAnimationName = facingDirection == Direction.RIGHT ? "CROUCH_RIGHT" : "CROUCH_LEFT";
            if (isInvincible == true){
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_STAND_RIGHT" : "HURT_STAND_LEFT";    
                } 
            // handles putting goggles on when standing in water
            // checks if the center of the player is currently touching a water tile
            int centerX = Math.round(getBounds().getX1()) + Math.round(getBounds().getWidth() / 2f);
            int centerY = Math.round(getBounds().getY1()) + Math.round(getBounds().getHeight() / 2f);
            MapTile currentMapTile = map.getTileByPosition(centerX, centerY);
            if (currentMapTile != null && currentMapTile.getTileType() == TileType.WATER) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "SWIM_STAND" : "SWIM_STAND";
            }
        }
        else if (playerState == PlayerState.JUMPING) {
            // if player is moving upwards, set player's animation to jump. if player moving downwards, set player's animation to fall
            if (lastAmountMovedY <= 0) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "JUMP_RIGHT" : "JUMP_LEFT";
                //Makes Hurt Animation stay while jumping
                if (isInvincible == true){
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_JUMP_RIGHT" : "HURT_JUMP_LEFT";    
                }
                // checks if the center of the player is currently touching a water tile     
                int centerX = Math.round(getBounds().getX1()) + Math.round(getBounds().getWidth() / 2f);
                int centerY = Math.round(getBounds().getY1()) + Math.round(getBounds().getHeight() / 2f);
                MapTile currentMapTile = map.getTileByPosition(centerX, centerY);
                if (currentMapTile != null && currentMapTile.getTileType() == TileType.WATER&& Keyboard.isKeyUp(MOVE_LEFT_KEY) && Keyboard.isKeyUp(MOVE_RIGHT_KEY)) {
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "SWIM_STAND" : "SWIM_STAND";
                    
                    if (isInvincible == true){
                        this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_SWIM_STAND" : "HURT_SWIM_STAND";    
                   }
                }
                else{
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "SWIM_RIGHT" : "SWIM_LEFT";

                    if (isInvincible == true){
                        this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_SWIM_RIGHT" : "HURT_SWIM_LEFT";    
                   }
                }     
            }
            else {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "FALL_RIGHT" : "FALL_LEFT";
                //Makes Hurt Animation stay while falling
                if (isInvincible == true){
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_FALL_RIGHT" : "HURT_FALL_LEFT";                         
                }
                // handles putting goggles on when standing in water
                // checks if the center of the player is currently touching a water tile
                int centerX = Math.round(getBounds().getX1()) + Math.round(getBounds().getWidth() / 2f);
                int centerY = Math.round(getBounds().getY1()) + Math.round(getBounds().getHeight() / 2f);
                MapTile currentMapTile = map.getTileByPosition(centerX, centerY);
                if (currentMapTile != null && currentMapTile.getTileType() == TileType.WATER) {
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "SWIM_STAND" : "SWIM_STAND";

                    if (isInvincible == true){
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_SWIM_STAND" : "HURT_SWIM_STAND";    
                    }
                }
            }           
        }
        else if (playerState == PlayerState.SWIMMING){
            // handles putting goggles on when standing in water
            // checks if the center of the player is currently touching a water tile
            int centerX = Math.round(getBounds().getX1()) + Math.round(getBounds().getWidth() / 2f);
            int centerY = Math.round(getBounds().getY1()) + Math.round(getBounds().getHeight() / 2f);
            MapTile currentMapTile = map.getTileByPosition(centerX, centerY);
            if (currentMapTile != null && currentMapTile.getTileType() == TileType.WATER) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "SWIM_STAND" : "SWIM_STAND";
                }

                //Makes Hurt Animation stay while swimming
                if (isInvincible == true){
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_SWIM_STAND" : "HURT_SWIM_STAND";
                }
        }
        
    
        // if the fireball key is being pressed spit one out as long as the cooldown is good.
        // If we plan to make the ability unlockable, all we need is another condition in this statement
        if ((Keyboard.isKeyDown(FIREBALL_KEY)) && (fireballOnCooldown == false) && (isInvincible == false) && (ScreenCoordinator.getGameState() == GameState.LEVEL1)){
            fireballSpit(getX(), getY(), getFacingDirection());
        }
        
        if((Keyboard.isKeyDown(WAVE_KEY)) && (waveOnCooldown==false) && (isInvincible == false) && (ScreenCoordinator.getGameState() == GameState.LEVEL2)){
            waveAttack(getX(), getY(), getFacingDirection());
        }

        //turns on glide ability if player is in the air
        isGlideOn=false;
        setTerminalVelocityY(6f);
        if(Keyboard.isKeyDown(GLIDE_KEY)){
            if(airGroundState == AirGroundState.AIR){
                isGlideOn=true;
                setTerminalVelocityY(1f);
            }
        }

        if((Keyboard.isKeyDown(LEVEL_KEY))){
            //This just completes the level, taken from method updateLevelCompleted()
            for (PlayerListener listener : listeners) {
                listener.onLevelCompleted();
            }
        }
    }


    // x and y are the player's positions, directions is the direction they are facing
    public void fireballSpit(float x, float y, Direction direction){
        float movementSpeed;
        float spawnX =x;
        float spawnY = y;
        int existenceFrames = 50;
        if (direction == Direction.RIGHT){
            spawnX+= 30;
            movementSpeed = 4;
        }
        else {
            spawnX += 10;
            movementSpeed = -4;
        }
        if (playerState == PlayerState.CROUCHING){
            spawnY += 17;
        }
        // Create a fireball and add it to the map
        PlayerFireball fireball = new PlayerFireball(spawnX, spawnY, movementSpeed, existenceFrames);
        map.addEnemy(fireball);
        // Peter

        // Set the cooldown here (cooldown is in frames, remember 60 fps so 60 = 1 second)
        cooldownCounter = 200;
        fireballOnCooldown = true;
    }

    public void waveAttack(float x, float y, Direction direction){
        float spawnX = x;
        float spawnY = y-20; //can make it y-25 if you want to make it so you have to crouch to kill bug

        if(direction==Direction.RIGHT){
            spawnX+=50;
        }
        else {
            spawnX-=30;
        }
        if (playerState == PlayerState.CROUCHING){
            spawnY+=5;
        }

        Wave wave = new Wave(spawnX, spawnY, direction);
        map.addEnemy(wave);

        cooldownCounter=150;
        waveOnCooldown = true;
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) { }

    @Override
    public void onEndCollisionCheckY(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
        // if player collides with a map tile below it, it is now on the ground
        // if player does not collide with a map tile below, it is in air
        if (direction == Direction.DOWN) {
            if (hasCollided) {
                momentumY = 0;
                airGroundState = AirGroundState.GROUND;
            } else {
                playerState = PlayerState.JUMPING;
                airGroundState = AirGroundState.AIR;
            }
        }

        // if player collides with map tile upwards, it means it was jumping and then hit into a ceiling -- immediately stop upwards jump velocity
        else if (direction == Direction.UP) {
            if (hasCollided) {
                jumpForce = 0;
            }
        }
    }

    // other entities can call this method to hurt the player
    public void hurtPlayer(MapEntity mapEntity) {
        if (!isInvincible) {
            // if map entity is an enemy, Check lives, Hurt player if lives > 1, Kill player if collision with 1 life
            if (mapEntity instanceof Enemy){
                if(lives != 1){                    
                    playerState = PlayerState.HURT; 
                    lives--;                                                  
                }
                else{
                    levelState = LevelState.PLAYER_DEAD;
                }
            }    
        }
    }

    // other entities can call this to tell the player they beat a level
    public void completeLevel() {
        levelState = LevelState.LEVEL_COMPLETED;
    }

    // if player has beaten level, this will be the update cycle
    public void updateLevelCompleted() {
        // if player is not on ground, player should fall until it touches the ground
        if (airGroundState != AirGroundState.GROUND && map.getCamera().containsDraw(this)) {
            currentAnimationName = "FALL_RIGHT";
            applyGravity();
            increaseMomentum();
            super.update();
            moveYHandleCollision(moveAmountY);
        }
        // move player to the right until it walks off screen
        else if (map.getCamera().containsDraw(this)){
            if(ScreenCoordinator.getGameState()== GameState.LEVEL2 || ScreenCoordinator.getGameState() == GameState.LEVEL5){
                currentAnimationName = "WALK_LEFT";
                super.update();
                moveXHandleCollision(-walkSpeed);
            }
            else{
                currentAnimationName = "WALK_RIGHT";
                super.update();
                moveXHandleCollision(walkSpeed);
            }
        } 
        else {
            // tell all player listeners that the player has finished the level
            for (PlayerListener listener : listeners) {
                listener.onLevelCompleted();
            }
        }
    }

    // if player has died, this will be the update cycle
    public void updatePlayerDead() {
        // change player animation to DEATH
        if (!currentAnimationName.startsWith("DEATH")) {
            if (facingDirection == Direction.RIGHT) {
                currentAnimationName = "DEATH_RIGHT";
            } else {
                currentAnimationName = "DEATH_LEFT";
            }
            super.update();
        }
        // if death animation not on last frame yet, continue to play out death animation
        else if (currentFrameIndex != getCurrentAnimation().length - 1) {
          super.update();
        }
        // if death animation on last frame (it is set up not to loop back to start), player should continually fall until it goes off screen
        else if (currentFrameIndex == getCurrentAnimation().length - 1) {
            if (map.getCamera().containsDraw(this)) {
                moveY(3);
            } else {
                // tell all player listeners that the player has died in the level
                for (PlayerListener listener : listeners) {
                    listener.onDeath();
                }
            }
        }
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public AirGroundState getAirGroundState() {
        return airGroundState;
    }

    public Direction getFacingDirection() {
        return facingDirection;
    }

    public void setFacingDirection(Direction facingDirection) {
        this.facingDirection = facingDirection;
    }

    public void setLevelState(LevelState levelState) {
        this.levelState = levelState;
    }

    public void addListener(PlayerListener listener) {
        listeners.add(listener);
    }

    public int getCooldownCounter(){
        return cooldownCounter;
    }

    public static void setCooldownCounter(int cooldownCounter){
        Player.cooldownCounter = cooldownCounter;
    }

    public float getTerminalVelocityY(){
        return terminalVelocityY;
    }

    public void setTerminalVelocityY(float terminalVelocityY){
        this.terminalVelocityY = terminalVelocityY;
    }
}
