package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import Level.TileType;
import Level.Tileset;
import Utils.SlopeTileLayoutUtils;

import java.util.ArrayList;

//This still needs to be completed in full - Hunter
public class FireTileset extends Tileset {

    public FireTileset() {
        super(ImageLoader.load("FireSetGrid.png"), 16, 16, 3);
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // Lava Platform
        Frame wallTileFrame = new FrameBuilder(getSubImage(1, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder wallTile = new MapTileBuilder(wallTileFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(wallTile);
 
        // Lava1
        Frame lava1Frame = new FrameBuilder(getSubImage(1, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder lava1Tile = new MapTileBuilder(lava1Frame);

        mapTiles.add(lava1Tile);

        return mapTiles;
    }
}