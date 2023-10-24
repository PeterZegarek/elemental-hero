package Players;


import java.util.HashMap;


import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.ElementalAbilityListenerManager;
import Level.Enemy;
import Level.MapEntity;
import Level.MapEntityStatus;
import Level.Player;
import Utils.Direction;




public class RockAttack extends Enemy {


private float movementSpeed;
// change this to change how quickly it's going to fall
private float gravity = .2f;
// to calculate thrown height
private float terminalVelocity = 6f;
private float momentumY;


private int existenceFrames;


public RockAttack(float xPos, float yPos, float movementSpeed, int existenceFrames) {
super(xPos, yPos, new SpriteSheet(ImageLoader.load("38x48Rock.png"), 38,48 ), "DEFAULT");
// How fast it moves
this.movementSpeed = movementSpeed;


// how long the fireball will exist for before disappearing
this.existenceFrames = existenceFrames;
this.movementSpeed = movementSpeed;
// Initial upward velocity
this.momentumY = -5; // Adjust this value to make the rock shoot higher or lower
this.momentumY = momentumY;
initialize();


}








@Override
public void initialize() {
// This lets other classes know that a fireball has been spawned
ElementalAbilityListenerManager.rockAttackSpawned(this);
// This is so that it can react when killing something (by disappearing)
ElementalAbilityListenerManager.addElementListener(this);
super.initialize();
}






@Override
public void update(Player player) {
// If timer is up, set map entity status to REMOVED
if (existenceFrames == 0) {
ElementalAbilityListenerManager.rockAttackDespawned();
this.mapEntityStatus = MapEntityStatus.REMOVED;
} else {
// Move rock forward
moveXHandleCollision(movementSpeed);


// Apply gravity
momentumY = Math.min(momentumY + gravity, terminalVelocity);
moveYHandleCollision(momentumY);


super.update(player);
}
existenceFrames--;
}


@Override
public void onEndCollisionCheckY(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
// if fireball collides with anything solid on the x axis, it is removed
if (hasCollided) {
this.mapEntityStatus = MapEntityStatus.REMOVED;
}
}


// If it collides with a map tile it will dissapear
@Override
public void onEndCollisionCheckX(boolean hasCollided, Direction direction, MapEntity entityCollidedWith) {
// if fireball collides with anything solid on the x axis, it is removed
if (hasCollided) {
ElementalAbilityListenerManager.rockAttackDespawned();
this.mapEntityStatus = MapEntityStatus.REMOVED;
}
}


// Touched player should not do anything
@Override
public void touchedPlayer(Player player){}




// As it stands, enemyAttacked is not necessary as the enemies themselves will die if they get hit
@Override
public void enemyAttacked(Enemy enemy) {}


// If the fireball has killed an enemy, it will disappear
public void rockKilledEnemy(){
ElementalAbilityListenerManager.rockAttackDespawned();
this.mapEntityStatus = MapEntityStatus.REMOVED;
}


@Override
public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
return new HashMap<String, Frame[]>() {{
put("DEFAULT", new Frame[]{
new FrameBuilder(spriteSheet.getSprite(0, 0))
.withScale(2) // This scales the size of the sprite, good to keep in mind
.withBounds(10,14 , 15, 20)
.build()
});
}};
}
}
