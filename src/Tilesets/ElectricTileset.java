package Tilesets;

import Engine.ImageLoader;
import Level.Tileset;
import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import GameObject.Frame;
import GameObject.ImageEffect;
import Level.Map;
import Level.MapTile;
import Level.TileType;
import Utils.SlopeTileLayoutUtils;

import java.util.ArrayList;


public class ElectricTileset extends Tileset {

    public ElectricTileset(){
         super(ImageLoader.load("InProgressElectricTileset.png"), 16, 16, 3);
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // default platform - orange and purple
        Frame platformFrame = new FrameBuilder(getSubImage(4,2))
                .withScale(tileScale)
                .build();
        MapTileBuilder platformTile = new MapTileBuilder(platformFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(platformTile);


        
        return mapTiles;

    }
    
    
}
