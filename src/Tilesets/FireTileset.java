package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
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

        //Main walking block
        Frame Fire1Frame = new FrameBuilder(getSubImage(0, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire1Tile = new MapTileBuilder(Fire1Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Fire1Tile);
 
        // Lava pool in wall(Not being used)
        Frame Fire2Frame = new FrameBuilder(getSubImage(0, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire2Tile = new MapTileBuilder(Fire2Frame)
                .withTileType(TileType.NOT_PASSABLE);
        mapTiles.add(Fire2Tile);

        // Pattern block(Not being used)
        Frame Fire3Frame = new FrameBuilder(getSubImage(1, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire3Tile = new MapTileBuilder(Fire3Frame);

        mapTiles.add(Fire3Tile);

        //Jumping Part background brick
        Frame Fire4Frame = new FrameBuilder(getSubImage(1, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire4Tile = new MapTileBuilder(Fire4Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(Fire4Tile);
 
        //Type of lava(Not being used)
        Frame Fire5Frame = new FrameBuilder(getSubImage(3, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire5Tile = new MapTileBuilder(Fire5Frame);

        mapTiles.add(Fire5Tile);

        //Main lava
        Frame Fire6Frame = new FrameBuilder(getSubImage(3, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire6Tile = new MapTileBuilder(Fire6Frame);

        mapTiles.add(Fire6Tile);

        //Black background (USE THIS ROW AND COLUMN BECAUSE IT IS THE CORRECT SHADE)
        Frame Fire7Frame = new FrameBuilder(getSubImage(6, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire7Tile = new MapTileBuilder(Fire7Frame);

        mapTiles.add(Fire7Tile);

        // Orange shade(Not being used)
        Frame Fire8Frame = new FrameBuilder(getSubImage(4, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire8Tile = new MapTileBuilder(Fire8Frame);

        mapTiles.add(Fire8Tile);

        // Spikes(Not being used)
        Frame Fire9Frame = new FrameBuilder(getSubImage(4, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire9Tile = new MapTileBuilder(Fire9Frame);

        mapTiles.add(Fire9Tile);

        // Spikes 2(Not being used)
        Frame Fire10Frame = new FrameBuilder(getSubImage(4, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire10Tile = new MapTileBuilder(Fire10Frame);

        mapTiles.add(Fire10Tile);

        // Chains
        Frame Fire11Frame = new FrameBuilder(getSubImage(5, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire11Tile = new MapTileBuilder(Fire11Frame);

        mapTiles.add(Fire11Tile);

        // Lava Stream(Not being used)
        Frame Fire12Frame = new FrameBuilder(getSubImage(4, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire12Tile = new MapTileBuilder(Fire12Frame);

        mapTiles.add(Fire12Tile);

        // left 45 degree slope
        Frame leftSlopeFrame = new FrameBuilder(getSubImage(5, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder leftSlopeTile = new MapTileBuilder(leftSlopeFrame)
                .withTileType(TileType.SLOPE)
                .withTileLayout(SlopeTileLayoutUtils.createLeft45SlopeLayout(spriteWidth, (int) tileScale));

        mapTiles.add(leftSlopeTile);
        

        // right 45 degree slope
        Frame rightSlopeFrame = new FrameBuilder(getSubImage(5, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder rightSlopeTile = new MapTileBuilder(rightSlopeFrame)
                .withTileType(TileType.SLOPE)
                .withTileLayout(SlopeTileLayoutUtils.createRight45SlopeLayout(spriteWidth, (int) tileScale));

        mapTiles.add(rightSlopeTile);

        //Chain Platform 
        Frame Fire15Frame = new FrameBuilder(getSubImage(5, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder Fire15Tile = new MapTileBuilder(Fire15Frame)
                .withTileType(TileType.JUMP_THROUGH_PLATFORM);
        mapTiles.add(Fire15Tile);

        return mapTiles;
    }
}