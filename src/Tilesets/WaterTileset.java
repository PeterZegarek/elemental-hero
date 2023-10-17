package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;
import Utils.SlopeTileLayoutUtils;

import java.util.ArrayList;

public class WaterTileset extends Tileset {

    public WaterTileset() {
        super(ImageLoader.load("WaterTileset.png"), 15, 15, 3);
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // Placeholder for now
        Frame Water1Frame = new FrameBuilder(getSubImage(0, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water1Tile = new MapTileBuilder(Water1Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Water1Tile);

        // Placeholder for now
        Frame Water2Frame = new FrameBuilder(getSubImage(0, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water2Tile = new MapTileBuilder(Water2Frame)
                .withTileType(TileType.NOT_PASSABLE);
        mapTiles.add(Water2Tile);

        // Placeholder for now
        Frame Water3Frame = new FrameBuilder(getSubImage(0, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water3Tile = new MapTileBuilder(Water3Frame);

        mapTiles.add(Water3Tile);
 
        // Placeholder for now
        Frame Water4Frame = new FrameBuilder(getSubImage(0, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water4Tile = new MapTileBuilder(Water4Frame);

        mapTiles.add(Water4Tile);

        // Placeholder for now
        Frame Water5Frame = new FrameBuilder(getSubImage(0, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water5Tile = new MapTileBuilder(Water5Frame);

        mapTiles.add(Water5Tile);

        // Placeholder for now
        Frame Water6Frame = new FrameBuilder(getSubImage(1, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water6Tile = new MapTileBuilder(Water6Frame);

        mapTiles.add(Water6Tile);

        // Placeholder for now
        Frame Water7Frame = new FrameBuilder(getSubImage(1, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water7Tile = new MapTileBuilder(Water7Frame);

        mapTiles.add(Water7Tile);

        // Placeholder for now
        Frame Water8Frame = new FrameBuilder(getSubImage(1, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water8Tile = new MapTileBuilder(Water8Frame);

        mapTiles.add(Water8Tile);

        // Placeholder for now
        Frame Water9Frame = new FrameBuilder(getSubImage(1, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water9Tile = new MapTileBuilder(Water9Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Water9Tile);
 
        // Placeholder for now
        Frame Water10Frame = new FrameBuilder(getSubImage(1, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water10Tile = new MapTileBuilder(Water10Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Water10Tile);

        // Placeholder for now
        Frame Water11Frame = new FrameBuilder(getSubImage(2, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water11Tile = new MapTileBuilder(Water11Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Water11Tile);

        // Placeholder for now
        Frame Water12Frame = new FrameBuilder(getSubImage(2, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water12Tile = new MapTileBuilder(Water12Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Water12Tile);

        // Placeholder for now
        Frame Water13Frame = new FrameBuilder(getSubImage(2, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water13Tile = new MapTileBuilder(Water13Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Water13Tile);

        // Placeholder for now
        Frame Water14Frame = new FrameBuilder(getSubImage(2, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water14Tile = new MapTileBuilder(Water14Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Water14Tile);

        // Placeholder for now
        Frame Water15Frame = new FrameBuilder(getSubImage(2, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water15Tile = new MapTileBuilder(Water15Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Water15Tile);

        // Placeholder for now
        Frame Water16Frame = new FrameBuilder(getSubImage(3, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water16Tile = new MapTileBuilder(Water16Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Water16Tile);

        // Placeholder for now
        Frame Water17Frame = new FrameBuilder(getSubImage(3, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water17Tile = new MapTileBuilder(Water17Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Water17Tile);

        // Placeholder for now
        Frame Water18Frame = new FrameBuilder(getSubImage(3, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water18Tile = new MapTileBuilder(Water18Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Water18Tile);

        // Placeholder for now
        Frame Water19Frame = new FrameBuilder(getSubImage(3, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water19Tile = new MapTileBuilder(Water19Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Water19Tile);

        // Placeholder for now
        Frame Water20Frame = new FrameBuilder(getSubImage(3, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water20Tile = new MapTileBuilder(Water20Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Water20Tile);

        // Placeholder for now
        Frame Water21Frame = new FrameBuilder(getSubImage(4, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water21Tile = new MapTileBuilder(Water21Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Water21Tile);

        // Placeholder for now
        Frame Water22Frame = new FrameBuilder(getSubImage(4, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water22Tile = new MapTileBuilder(Water22Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Water22Tile);

        // Placeholder for now
        Frame Water23Frame = new FrameBuilder(getSubImage(4, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water23Tile = new MapTileBuilder(Water23Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Water23Tile);

        // Placeholder for now
        Frame Water24Frame = new FrameBuilder(getSubImage(4, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water24Tile = new MapTileBuilder(Water24Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Water24Tile);

        // Placeholder for now
        Frame Water25Frame = new FrameBuilder(getSubImage(4, 4))
                .withScale(tileScale)
                .build();

        // Placeholder for now
        MapTileBuilder Water25Tile = new MapTileBuilder(Water25Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Water25Tile);
    
        return mapTiles;
    }
}