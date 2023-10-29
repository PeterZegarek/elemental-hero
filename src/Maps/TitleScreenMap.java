package Maps;

import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Sprite;
import Level.Map;
import Tilesets.MenuTileset;
import Utils.Colors;
import Utils.Point;

// Represents the map that is used as a background for the main menu and credits menu screen
public class TitleScreenMap extends Map {

    private Sprite Hero_Earth, Hero_Fire, Hero_Water, Hero_Electric, Hero_Air;

    public TitleScreenMap() {
        super("title_screen_map.txt", new MenuTileset());

        Point heroLocationEarth = getMapTile(2, 6).getLocation().subtractX(15).subtractY(18);
        Hero_Earth = new Sprite(ImageLoader.loadSubImage("HeroSheet.png", Colors.MAGENTA, 0, 27, 26, 25));
        Hero_Earth.setScale(3);
        Hero_Earth.setLocation(heroLocationEarth.x, heroLocationEarth.y);

        Point heroLocationFire = getMapTile(6, 2).getLocation().subtractX(15).subtractY(18);
        Hero_Fire = new Sprite(ImageLoader.loadSubImage("HeroSheet.png", Colors.MAGENTA, 0, 54, 26, 25));
        Hero_Fire.setScale(3);
        Hero_Fire.setLocation(heroLocationFire.x, heroLocationFire.y);

        Point heroLocationWater = getMapTile(10, 9).getLocation().subtractX(24).subtractY(18);
        Hero_Water = new Sprite(ImageLoader.loadSubImage("HeroSheet.png", Colors.MAGENTA, 27, 81, 26, 25));
        Hero_Water.setScale(3);
        Hero_Water.setLocation(heroLocationWater.x, heroLocationWater.y);

        Point heroLocationElectric = getMapTile(13, 6).getLocation().subtractX(13).subtractY(18);
        Hero_Electric = new Sprite(ImageLoader.loadSubImage("HeroSheet.png", Colors.MAGENTA, 81, 27, 26, 25));
        Hero_Electric.setScale(3);
        Hero_Electric.setLocation(heroLocationElectric.x, heroLocationElectric.y);

        Point heroLocationAir = getMapTile(18, 6).getLocation().subtractX(24).subtractY(18);
        Hero_Air = new Sprite(ImageLoader.loadSubImage("HeroSheet.png", Colors.MAGENTA, 54, 27, 26, 25));
        Hero_Air.setScale(3);
        Hero_Air.setLocation(heroLocationAir.x, heroLocationAir.y);
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
        Hero_Earth.draw(graphicsHandler);
        Hero_Fire.draw(graphicsHandler);
        Hero_Water.draw(graphicsHandler);
        Hero_Electric.draw(graphicsHandler);
        Hero_Air.draw(graphicsHandler);
    }

}
