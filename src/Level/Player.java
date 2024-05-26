package Level;

import Engine.GamePanel;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Engine.Sound;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Players.PlayerFireball;
import Players.Wave;
import Utils.AirGroundState;
import Utils.Direction;
import Players.RockAttack;

import java.util.ArrayList;

import Enemies.AngelBoss;
import Enemies.CloudEnemy;
import Enemies.AngelBoss.AngelState;

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
    protected static LevelState levelState;
    protected GameState gameState;
    protected static int lives = 3;
    protected static int prevLives = 3; //Used to check whether lives went down by more than 1 in the same frame

    //keeps track of glide power and whether it is on or not
    protected static boolean isGlideOn = false;

    //keeps track of electric Ability Animation and if it is on or off
    protected static boolean isElectricOn = false;

    // classes that listen to player events can be added to this list
    protected ArrayList<PlayerListener> listeners = new ArrayList<>();

    // define keys
    protected KeyLocker keyLocker = new KeyLocker();
    protected Key JUMP_KEY = Key.UP;
    protected Key MOVE_LEFT_KEY = Key.LEFT;
    protected Key MOVE_RIGHT_KEY = Key.RIGHT;
    protected Key CROUCH_KEY = Key.DOWN;
    /* LEVEL SKIP
        //adding key to skip level
        protected Key LEVEL_KEY = Key.L;
    */
    // adding the fireball key F
    protected Key FIREBALL_KEY = Key.F;
    // adding wave key F
    protected Key WAVE_KEY = Key.F;
    //adding glide key F
    protected Key GLIDE_KEY = Key.F;
    //adding earth ability key F
    protected Key EARTH_ATTACK_KEY = Key.F;
    //adding eelectric ability key F
    protected Key ELECTRIC_ATTACK_KEY = Key.F;

    // flags
    protected boolean isInvincible = false; // if true, player cannot be hurt by enemies (good for testing)
    protected static boolean fireballOnCooldown = false; // Whether fireball is on cooldown
    protected static boolean waveOnCooldown = false; // Whether wave is on cooldown
    protected static boolean rockOnCooldown = false;
    protected static boolean electricOnCooldown = false;
    protected static boolean electricButtonOff = false; // Checks if the lightning symbol (Electric Ability) is highlighted or not
    

    protected static int cooldownCounter; // Time for the fireball/wave to be on cooldown
    protected static int fireballCooldownCounter;
    protected static int waveCooldownCounter;
    protected static int rockCooldownCounter;
    protected static int isElectricOnCounter;
    protected static int electricButtonTimer; // Time for the Lightning Button (Electric Ability) to not be highlighted 

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
        
        if (lives > 3){
            lives = 3;
        }
        moveAmountX = 0;
        moveAmountY = 0;

        // cooldown counter decreases everytime by 1
        if(fireballCooldownCounter>0){
            fireballCooldownCounter--;
            if(fireballCooldownCounter==0) fireballOnCooldown = false;
        }

        if(waveCooldownCounter>0){
            waveCooldownCounter--;
            if(waveCooldownCounter==0) waveOnCooldown = false;
        }

        if(rockCooldownCounter>0){
            rockCooldownCounter--;
            if(rockCooldownCounter==0) rockOnCooldown = false;
        }

        if(electricButtonTimer > 0){
            electricButtonTimer--;
            if(electricButtonTimer == 0){
                electricButtonOff = false;
            }
        }

        if(isElectricOnCounter > 0){
            isElectricOnCounter--;
            if(isElectricOnCounter==0) {
                isElectricOn = false;     
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

        if(prevLives-lives>1) { //If the # of lives in previous frame was greater than 
            lives++; //the current lives by more than 1, then a life is added back
        }
        prevLives=lives; //Make prevLives equal to the lives of this frame to update it for the next frame
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
            if (isInvincible == true && electricButtonOff == false){
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_STAND_RIGHT" : "HURT_STAND_LEFT";    
                }
            else if (electricButtonOff == true && isInvincible == true){
                    this.currentAnimationName = "ELECTRIC_ATTACK_NORMAL";
                }
            }
        
        else if (playerState == PlayerState.HURT) { 
            this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_STAND_RIGHT" : "HURT_STAND_LEFT";                                                
            }         
        else if (playerState == PlayerState.WALKING) {
            // sets animation to a WALK animation based on which way player is facing
            this.currentAnimationName = facingDirection == Direction.RIGHT ? "WALK_RIGHT" : "WALK_LEFT";
            //Makes Hurt Animation stay while walking
            if (isInvincible == true && electricButtonOff == false){
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_WALK_RIGHT" : "HURT_WALK_LEFT";    
                }  
            else if (electricButtonOff == true && isInvincible == true ){
                    this.currentAnimationName = "ELECTRIC_ATTACK_NORMAL";
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
            if (isInvincible == true && electricButtonOff == false){
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_STAND_RIGHT" : "HURT_STAND_LEFT";    
                } 
            else if (electricButtonOff == true && isInvincible == true ){
                    this.currentAnimationName = "ELECTRIC_ATTACK_NORMAL";
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
            if (lastAmountMovedY <= 0 && gameState != GameState.LEVEL3) {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "JUMP_RIGHT" : "JUMP_LEFT";
                //Makes Hurt Animation stay while jumping
                if (isInvincible == true && electricButtonOff == false){
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_JUMP_RIGHT" : "HURT_JUMP_LEFT";    
                }
                else if (electricButtonOff == true && isInvincible == true ){
                    this.currentAnimationName = "ELECTRIC_ATTACK_NORMAL";
                }
            else {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "FALL_RIGHT" : "FALL_LEFT";
                //Makes Hurt Animation stay while falling
                if (isInvincible == true && electricButtonOff == false){
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_FALL_RIGHT" : "HURT_FALL_LEFT";                         
                }
                else if (electricButtonOff == true && isInvincible == true ){
                    this.currentAnimationName = "ELECTRIC_ATTACK_NORMAL";
                }
            }
        }
            if (lastAmountMovedY <= 0 && gameState == GameState.LEVEL3){
                // checks if the center of the player is currently touching a water tile     
                int centerX = Math.round(getBounds().getX1()) + Math.round(getBounds().getWidth() / 2f);
                int centerY = Math.round(getBounds().getY1()) + Math.round(getBounds().getHeight() / 2f);
                MapTile currentMapTile = map.getTileByPosition(centerX, centerY);
                if (currentMapTile != null && currentMapTile.getTileType() == TileType.WATER&& Keyboard.isKeyUp(MOVE_LEFT_KEY) && Keyboard.isKeyUp(MOVE_RIGHT_KEY)) {
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "SWIM_STAND" : "SWIM_STAND";
                    
                    if (isInvincible == true && electricButtonOff == false){
                        this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_SWIM_STAND" : "HURT_SWIM_STAND";    
                    }
                    else if (electricButtonOff == true && isInvincible == true){
                    this.currentAnimationName = "ELECTRIC_ATTACK_WATER";
                    }
                }       
                else{
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "SWIM_STAND" : "SWIM_STAND";

                    if (isInvincible == true && electricButtonOff == false){
                        this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_SWIM_STAND" : "HURT_SWIM_STAND";    
                    }
                    else if (electricButtonOff == true && isInvincible == true){
                    this.currentAnimationName = "ELECTRIC_ATTACK_WATER";
                    }
                }     
            }           
            else {
                this.currentAnimationName = facingDirection == Direction.RIGHT ? "FALL_RIGHT" : "FALL_LEFT";
                //Makes Hurt Animation stay while falling
                if (isInvincible == true && electricButtonOff == false){
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_FALL_RIGHT" : "HURT_FALL_LEFT";                         
                }
                else if (electricButtonOff == true && isInvincible == true ){
                    this.currentAnimationName = "ELECTRIC_ATTACK_NORMAL";
                }
                // handles putting goggles on when standing in water
                // checks if the center of the player is currently touching a water tile
                int centerX = Math.round(getBounds().getX1()) + Math.round(getBounds().getWidth() / 2f);
                int centerY = Math.round(getBounds().getY1()) + Math.round(getBounds().getHeight() / 2f);
                MapTile currentMapTile = map.getTileByPosition(centerX, centerY);
                if (currentMapTile != null && currentMapTile.getTileType() == TileType.WATER) {
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "SWIM_RIGHT" : "SWIM_LEFT";

                if (isInvincible == true && electricButtonOff == false){
                        this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_SWIM_RIGHT" : "HURT_SWIM_LEFT";    
                    }
                else if (electricButtonOff == true && isInvincible == true){
                    this.currentAnimationName = "ELECTRIC_ATTACK_WATER";
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
                if (isInvincible == true && electricButtonOff == false){
                    this.currentAnimationName = facingDirection == Direction.RIGHT ? "HURT_SWIM_STAND" : "HURT_SWIM_STAND";
                }
                else if (electricButtonOff == true && isInvincible == true){
                    this.currentAnimationName = "ELECTRIC_ATTACK_WATER";
                }
            }
         
        // if the fireball key is being pressed spit one out as long as the cooldown is good.
        // If we plan to make the ability unlockable, all we need is another condition in this statement
        if ((Keyboard.isKeyDown(FIREBALL_KEY)) && (fireballOnCooldown == false) && (isInvincible == false) && (GamePanel.getCurrentAbility()==0)){
            fireballSpit(getX(), getY(), getFacingDirection());
            }
        
        if((Keyboard.isKeyDown(WAVE_KEY)) && (waveOnCooldown==false) && (isInvincible == false) && (GamePanel.getCurrentAbility()==1)){
            waveAttack(getX(), getY(), getFacingDirection());
        }

        if ((Keyboard.isKeyDown(EARTH_ATTACK_KEY)) && (rockOnCooldown == false) && (isInvincible == false) && (GamePanel.getCurrentAbility()==4)){
            RockAttack(getX(), getY(), getFacingDirection());
            }
            

        //turns on glide ability if player is in the air
        isGlideOn=false;
        setTerminalVelocityY(6f);
        // either air or void level
        if((Keyboard.isKeyDown(GLIDE_KEY)) && (GamePanel.getCurrentAbility()==3)){
            if(airGroundState == AirGroundState.AIR){
                isGlideOn=true;
                setTerminalVelocityY(1f);
            }
        }

        if(Keyboard.isKeyDown(ELECTRIC_ATTACK_KEY) && isElectricOn == false && isInvincible == false && !keyLocker.isKeyLocked(ELECTRIC_ATTACK_KEY) && GamePanel.getCurrentAbility()==2){

            isInvincibleCounter = 75;
            isInvincible = true;

            electricButtonTimer = 75;
            electricButtonOff = true;

            isElectricOnCounter = 150;
            isElectricOn = true;     
            Sound.startSFX(3);
        }
        /* LEVEL SKIP
            if((Keyboard.isKeyDown(LEVEL_KEY))){
                //This just completes the level, taken from method updateLevelCompleted()
                for (PlayerListener listener : listeners) {
                    listener.onLevelCompleted();
                }
                lives = 3;
            }
         */
        
    }



    public void RockAttack(float x, float y, Direction direction){
        // This method should be similar to fireballSpit() or waveAttack()
        // but create a PlayerEarthAttack instead
        float movementSpeed;
        float spawnX =x;
        float spawnY = y-15;
        int existenceFrames = 50;
        // sound effect
        Sound.startSFX(5);
        // end sound effect
        if (direction == Direction.RIGHT){
        spawnX+= 30;
        movementSpeed = 4;
        }
        else {
        spawnX += 10;
        movementSpeed = -4;
        }
        if (playerState == PlayerState.CROUCHING)
        {
        spawnY += 5;
        }
        RockAttack rock = new RockAttack(spawnX, spawnY, movementSpeed, existenceFrames);
        map.addEnemy(rock);
        
        
        rockCooldownCounter = 150;
        rockOnCooldown = true;
         
        }

    // x and y are the player's positions, directions is the direction they are facing
    public void fireballSpit(float x, float y, Direction direction){
        float movementSpeed;
        float spawnX =x;
        float spawnY = y;
        int existenceFrames = 50;
        // sound effect
        Sound.startSFX(1);
        // end sound effect
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
        fireballCooldownCounter = 150;
        fireballOnCooldown = true;
    }

    public void waveAttack(float x, float y, Direction direction){
        float spawnX = x;
        float spawnY = y-40; 

        // sound effect
        Sound.startSFX(2);
        // end sound effect
        if(direction==Direction.RIGHT){
            spawnX+=70;
        }
        else {
            spawnX-=50;
        }
        if (playerState == PlayerState.CROUCHING){
            spawnY+=5;
        }

        Wave wave = new Wave(spawnX, spawnY, direction);
        map.addEnemy(wave);

        waveCooldownCounter=100;
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
            //Need 2nd part of if statement so player doesn't get hurt by dead angel when it's falling
            if (mapEntity instanceof Enemy &&(ScreenCoordinator.getGameState()!=GameState.LEVEL5 || AngelBoss.getAngelState()!=AngelState.DEAD || mapEntity instanceof CloudEnemy)){
                if(lives != 1){              
                    Sound.startSFX(0);      
                    playerState = PlayerState.HURT; 
                    lives--;                                                  
                }
                else{
                    lives--; //Need to make lives 0 for this to work
                    if(prevLives-lives<=1) levelState = LevelState.PLAYER_DEAD; //If the difference is less than or equal to 1 player dies
                    else lives++; //If the difference is more than one, a life is added back
                }
            }    
        }
    }

    // this is called by the invisible enemy to instakill the player
    public void killPlayer(){
        levelState = LevelState.PLAYER_DEAD;
    }

    // other entities can call this to tell the player they beat a level
    public void completeLevel() {
        levelState = LevelState.LEVEL_COMPLETED;
    }

    // if player has beaten level, this will be the update cycle
    public void updateLevelCompleted() {      
            // tell all player listeners that the player has finished the level
            for (PlayerListener listener : listeners) {
                listener.onLevelCompleted();
            }
            lives = 3;
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
                lives = 3;
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

    public static LevelState getLevelState(){
        return Player.levelState;
    }

    public void setLevelState(LevelState levelState) {
        Player.levelState = levelState;
    }

    public void addListener(PlayerListener listener) {
        listeners.add(listener);
    }

    public int getCooldownCounter(){
        return cooldownCounter;
    }

    public static boolean getFireballOnCooldown(){
        return Player.fireballOnCooldown;
    }

    public static boolean getWaveOnCooldown(){
        return Player.waveOnCooldown;
    }

    public static boolean getRockOnCooldown(){
        return Player.rockOnCooldown;
    }

    public static boolean getIsGlideOn(){
        return Player.isGlideOn;
    }

    public static boolean getIsElectricOn(){
        return Player.isElectricOn;
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

    public static int getLives(){
        return Player.lives;
    }

    public static void setLives(int lives){
        Player.lives = lives;
    }
}
