package EnhancedMapTiles;




import java.util.HashMap;


import Builders.FrameBuilder;
import Enemies.Fireball;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.EnhancedMapTile;
import Level.Player;
import Level.TileType;
import Utils.Point;


public class FireCannon extends EnhancedMapTile {


private Point startLocation;
private int fireballCounter = 0;


public FireCannon(Point startLocation) {
super(startLocation.x, startLocation.y+25, new SpriteSheet(ImageLoader.load("FireCannonCopy.png"), 14, 14),TileType.NOT_PASSABLE);
isUpdateOffScreen = true;
this.startLocation = new Point(startLocation.x, startLocation.y + 25);
this.initialize();
}


@Override
public void initialize() {
super.initialize();
}


@Override
public void update(Player player) {
fireballCounter++;


// As of right now, it shoots fireball every 2.2 seconds
if ((fireballCounter % 132 == 0)){
// define where fireball will spawn on map (x location) relative to FireCannon's location
        // and define its movement speed
        int fireballWidth = 7 * 3; // Fireball sprite width (7) multiplied by its scale (3)
        int fireballX = Math.round(this.startLocation.x) - fireballWidth;
        float movementSpeed = -1.5f;

        // define where fireball will spawn on the map (y location) relative to FireCannon's location
        int fireballY = Math.round(this.startLocation.y) + 4;

        // create Fireball enemy
        Fireball fireball = new Fireball(new Point(fireballX, fireballY), movementSpeed, 60);

        // add fireball enemy to the map for it to spawn in the level
map.addEnemy(fireball);
}
super.update(player);
}


@Override
public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
return new HashMap<String, Frame[]>() {{
put("DEFAULT", new Frame[] {
new FrameBuilder(spriteSheet.getSprite(0, 0), 50)
.withScale(3)
.withBounds(1, 1, 11, 7)
.build(),
});
}};
}
}
