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

public class FireTileset extends Tileset {

    public FireTileset() {
        super(ImageLoader.load("FireTileset.png"), 16, 16, 3);
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // Add tile name here
        Frame Fire1Frame = new FrameBuilder(getSubImage(0, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire1Tile = new MapTileBuilder(Fire1Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Fire1Tile);
 
        // Add tile name here
        Frame Fire2Frame = new FrameBuilder(getSubImage(0, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire2Tile = new MapTileBuilder(Fire2Frame);

        mapTiles.add(Fire2Tile);

        // Add tile name here
        Frame Fire3Frame = new FrameBuilder(getSubImage(1, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire3Tile = new MapTileBuilder(Fire3Frame);

        mapTiles.add(Fire3Tile);

        // Add tile name here
        Frame Fire4Frame = new FrameBuilder(getSubImage(1, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire4Tile = new MapTileBuilder(Fire4Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(Fire4Tile);
 
        // Add tile name here
        Frame Fire5Frame = new FrameBuilder(getSubImage(3, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire5Tile = new MapTileBuilder(Fire5Frame);

        mapTiles.add(Fire5Tile);

        // Add tile name here
        Frame Fire6Frame = new FrameBuilder(getSubImage(3, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire6Tile = new MapTileBuilder(Fire6Frame);

        mapTiles.add(Fire6Tile);

        // Add tile name here
        Frame Fire7Frame = new FrameBuilder(getSubImage(3, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire7Tile = new MapTileBuilder(Fire7Frame);

        mapTiles.add(Fire7Tile);

        // Add tile name here
        Frame Fire8Frame = new FrameBuilder(getSubImage(4, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire8Tile = new MapTileBuilder(Fire8Frame);

        mapTiles.add(Fire8Tile);

        // Add tile name here
        Frame Fire9Frame = new FrameBuilder(getSubImage(4, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire9Tile = new MapTileBuilder(Fire9Frame);

        mapTiles.add(Fire9Tile);

        // Add tile name here
        Frame Fire10Frame = new FrameBuilder(getSubImage(4, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire10Tile = new MapTileBuilder(Fire10Frame);

        mapTiles.add(Fire10Tile);

        // Add tile name here
        Frame Fire11Frame = new FrameBuilder(getSubImage(4, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire11Tile = new MapTileBuilder(Fire11Frame);

        mapTiles.add(Fire11Tile);

        // Add tile name here
        Frame Fire12Frame = new FrameBuilder(getSubImage(4, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire12Tile = new MapTileBuilder(Fire12Frame);

        mapTiles.add(Fire12Tile);

        return mapTiles;
    }
}