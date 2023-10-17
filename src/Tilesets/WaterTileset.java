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

        // Water1 Tile - (Light Blue)
        Frame Water1Frame = new FrameBuilder(getSubImage(0, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water1Tile = new MapTileBuilder(Water1Frame)
                .withTileType(TileType.WATER);

        mapTiles.add(Water1Tile);

        // Wave1
        Frame Wave1Frame = new FrameBuilder(getSubImage(0, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder Wave1Tile = new MapTileBuilder(Wave1Frame)
                .withTileType(TileType.WATER);
        mapTiles.add(Wave1Tile);

        // Wave2
        Frame Wave2Frame = new FrameBuilder(getSubImage(0, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder Wave2Tile = new MapTileBuilder(Wave2Frame)
                .withTileType(TileType.WATER);
        mapTiles.add(Wave2Tile);
 
        // Wave3
        Frame Wave3Frame = new FrameBuilder(getSubImage(0, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder Wave3Tile = new MapTileBuilder(Wave3Frame)
                .withTileType(TileType.WATER);
        mapTiles.add(Wave3Tile);

        // Wave 4 (Not going to use)
        Frame Wave4Frame = new FrameBuilder(getSubImage(0, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder Wave4Tile = new MapTileBuilder(Wave4Frame)
                .withTileType(TileType.WATER);
        mapTiles.add(Wave4Tile);

        // Water 2 Tile
        Frame Water2Frame = new FrameBuilder(getSubImage(1, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water2Tile = new MapTileBuilder(Water2Frame)
                .withTileType(TileType.WATER);
        mapTiles.add(Water2Tile);

        // TransitionWater1
        Frame TransitionWater1Frame = new FrameBuilder(getSubImage(1, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder TransitionWater1Tile = new MapTileBuilder(TransitionWater1Frame)
                .withTileType(TileType.WATER);
        mapTiles.add(TransitionWater1Tile);

        // TransitionWater2
        Frame TransitionWater2Frame = new FrameBuilder(getSubImage(1, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder TransitionWater2Tile = new MapTileBuilder(TransitionWater2Frame)
                .withTileType(TileType.WATER);
        mapTiles.add(TransitionWater2Tile);

        // TransitionWater3
        Frame TransitionWater3Frame = new FrameBuilder(getSubImage(1, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder TransitionWater3Tile = new MapTileBuilder(TransitionWater3Frame)
                .withTileType(TileType.WATER);
        mapTiles.add(TransitionWater3Tile);
 
        // TransitionWater4
        Frame TransitionWater4Frame = new FrameBuilder(getSubImage(1, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder TransitionWater4Tile = new MapTileBuilder(TransitionWater4Frame)
                .withTileType(TileType.WATER);
        mapTiles.add(TransitionWater4Tile);

        // TransitionWater5 (Light Blue to Blue)
        Frame TransitionWater5Frame = new FrameBuilder(getSubImage(2, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder TransitionWater5Tile = new MapTileBuilder(TransitionWater5Frame)
                .withTileType(TileType.WATER);
        mapTiles.add(TransitionWater5Tile);

        // Ground Spike 1 - (Dark Blue Background)
        Frame GroundSpike1Frame = new FrameBuilder(getSubImage(2, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder GroundSpike1Tile = new MapTileBuilder(GroundSpike1Frame)
                .withTileType(TileType.WATER);

        mapTiles.add(GroundSpike1Tile);

        // Column 1 - (Dark Blue Background)
        Frame Column1Frame = new FrameBuilder(getSubImage(2, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder Column1Tile = new MapTileBuilder(Column1Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Column1Tile);

        // Stone 1 - (dark Blue Background)
        Frame Stone1Frame = new FrameBuilder(getSubImage(2, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder Stone1Tile = new MapTileBuilder(Stone1Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Stone1Tile);

        // Stone 2 - (Blue Background)
        Frame Stone2Frame = new FrameBuilder(getSubImage(2, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder Stone2Tile = new MapTileBuilder(Stone2Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Stone2Tile);

        // Ground Spike 2 - (Blue Background)
        Frame GroundSpike2Frame = new FrameBuilder(getSubImage(3, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder GroundSpike2Tile = new MapTileBuilder(GroundSpike2Frame)
                .withTileType(TileType.WATER);

        mapTiles.add(GroundSpike2Tile);

        // Stone 3 - (Light Blue Background)
        Frame Stone3Frame = new FrameBuilder(getSubImage(3, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder Stone3Tile = new MapTileBuilder(Stone3Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Stone3Tile);

        // TransitionWater6 (Blue to Dark Blue)
        Frame TransitionWater6Frame = new FrameBuilder(getSubImage(3, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder TransitionWater6Tile = new MapTileBuilder(TransitionWater6Frame)
                .withTileType(TileType.WATER);
        mapTiles.add(TransitionWater6Tile);

        // Water 3 Tile - (Dark Blue)
        Frame Water3Frame = new FrameBuilder(getSubImage(3, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder Water3Tile = new MapTileBuilder(Water3Frame)
                .withTileType(TileType.WATER);

        mapTiles.add(Water3Tile);

        // Sky Tile
        Frame SkyFrame = new FrameBuilder(getSubImage(3, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder SkyTile = new MapTileBuilder(SkyFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(SkyTile);

        // Ceiling Spike 1 - (Dark Blue Background)
        Frame CeilingSpike1Frame = new FrameBuilder(getSubImage(4, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder CeilingSpike1Tile = new MapTileBuilder(CeilingSpike1Frame)
                .withTileType(TileType.WATER);

        mapTiles.add(CeilingSpike1Tile);

        // Column 2 - (Blue Background)
        Frame Column2Frame = new FrameBuilder(getSubImage(4, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder Column2Tile = new MapTileBuilder(Column2Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Column2Tile);

        // Ceiling Spike 2 - (Blue Background)
        Frame CeilingSpike2Frame = new FrameBuilder(getSubImage(4, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder CeilingSpike2Tile = new MapTileBuilder(CeilingSpike2Frame)
                .withTileType(TileType.WATER);

        mapTiles.add(CeilingSpike2Tile);

        // Background of some kind? (Not Used)
        Frame BackgroundFrame = new FrameBuilder(getSubImage(4, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder BackgroundTile = new MapTileBuilder(BackgroundFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(BackgroundTile);

        // Ground Spike 3 - (Light Blue Background)
        Frame GroundSpike3Frame = new FrameBuilder(getSubImage(4, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder GroundSpike3Tile = new MapTileBuilder(GroundSpike3Frame)
                .withTileType(TileType.WATER);

        mapTiles.add(GroundSpike3Tile);
    
        return mapTiles;
    }
}