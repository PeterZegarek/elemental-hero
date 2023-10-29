package Enemies;


import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.ElementalAbilityListenerManager;
import Level.Enemy;
import Level.LevelState;
import Level.MapEntity;
import Level.Player;
import Screens.FireLoseScreen;
import Screens.PlayFireLevelScreen;
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;
import java.util.HashMap;


public class Lava extends Enemy {


private FireLoseScreen fireLoseScreen;
private PlayFireLevelScreen playFireLevelScreen;

//flag for if player touched lava
private static boolean playerTouchedLava = false;


public Lava(Point location) {
    super(location.x, location.y, new SpriteSheet(ImageLoader.load("17x17MagentaBackground.png"), 17, 17), "LAVA");
    this.initialize();
}

@Override
public void initialize() {
    super.initialize();
    currentAnimationName = "LAVA";
}


@Override
public void update(Player player) {
    if (intersects(player)) {
        playerTouchedLava = true;
        player.killPlayer();
        super.update(player);
    }
}

public static boolean getPlayerTouchedLava(){
    return playerTouchedLava;
}



@Override
public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
    return new HashMap<String, Frame[]>() {{
        put("LAVA", new Frame[]{
            new FrameBuilder(spriteSheet.getSprite(0, 0))
            .withScale(2)
            .withBounds(1, 1, 50, 17)
            .build()
        });
    }};
}

}
