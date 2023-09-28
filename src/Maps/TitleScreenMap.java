package Maps;

import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Sprite;
import Level.Map;
import Tilesets.EarthTileset;
import Utils.Colors;
import Utils.Point;

// Represents the map that is used as a background for the main menu and credits menu screen
public class TitleScreenMap extends Map {

    private Sprite hero;

    public TitleScreenMap() {
        super("title_screen_map.txt", new EarthTileset());
        Point heroLocation = getMapTile(6, 8).getLocation().subtractX(24).subtractY(20);
        hero = new Sprite(ImageLoader.loadSubImage("Hero.png", Colors.MAGENTA, 0, 54, 26, 26));
        hero.setScale(3);
        hero.setLocation(heroLocation.x, heroLocation.y);
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
        hero.draw(graphicsHandler);
    }

}
