package Maps;

import Engine.GraphicsHandler;
import Level.Map;
import Tilesets.MenuTileset;


// Represents the map that is used as a background for the main menu and credits menu screen
public class InstructionsScreenMap extends Map {


    public InstructionsScreenMap() {
        super("InstructionsScreenMap.txt", new MenuTileset()); 
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

}
