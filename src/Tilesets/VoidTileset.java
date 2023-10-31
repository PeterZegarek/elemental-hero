package Tilesets;

import Engine.ImageLoader;
import Level.Tileset;
import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import GameObject.Frame;
import GameObject.ImageEffect;
import Level.TileType;
import Utils.SlopeTileLayoutUtils;

import java.util.ArrayList;


public class VoidTileset extends Tileset {

    public VoidTileset(){
         super(ImageLoader.load("VoidTileset.png"), 16, 16, 3);
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // default background wall tile
        Frame wallFrame = new FrameBuilder(getSubImage(0,0))
                .withScale(tileScale)
                .build();
        MapTileBuilder wallTile = new MapTileBuilder(wallFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(wallTile);


        // default background wall tile with cracks in it
        Frame wall2Frame = new FrameBuilder(getSubImage(0,1))
                .withScale(tileScale)
                .build();
        MapTileBuilder wall2Tile = new MapTileBuilder(wall2Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(wall2Tile);

        // default wall tile with lantern
        Frame wallLanternFrame = new FrameBuilder(getSubImage(0,2))
                .withScale(tileScale)
                .build();
        MapTileBuilder wallLanternTile = new MapTileBuilder(wallLanternFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(wallLanternTile);

        

        // banner top piece
        Frame bannerBackground = new FrameBuilder(getSubImage(0,3))
                .withScale(tileScale)
                .build();
        
        MapTileBuilder bannerFrame = new MapTileBuilder(bannerBackground)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(bannerFrame);

        // banner bottom piece
        Frame banner2Background = new FrameBuilder(getSubImage(1,3))
                .withScale(tileScale)
                .build();
        
        MapTileBuilder banner2Frame = new MapTileBuilder(banner2Background)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(banner2Frame);





        // Frame leftSlopeFrame = new FrameBuilder(getSubImage(0, 3))
        //         .withScale(tileScale)
        //         .build();

        // MapTileBuilder leftSlopeTile = new MapTileBuilder(leftSlopeFrame)
        //         .withTileType(TileType.SLOPE)
        //         .withTileLayout(SlopeTileLayoutUtils.createRight45SlopeLayout(spriteWidth, (int) tileScale));

        // mapTiles.add(leftSlopeTile);

        // // left 45 degree slope - i mixed them up so the variable names are flipped.
        // Frame rightSlopeFrame = new FrameBuilder(getSubImage(4, 0))
        //         .withScale(tileScale)
        //         .build();

        // MapTileBuilder rightSlopeTile = new MapTileBuilder(rightSlopeFrame)
        //         .withTileType(TileType.SLOPE)
        //         .withTileLayout(SlopeTileLayoutUtils.createLeft45SlopeLayout(spriteWidth, (int) tileScale));

        // mapTiles.add(rightSlopeTile);


        

        // platform to be jumped through when going up - left side
        Frame leftPlatformFrame = new FrameBuilder(getSubImage(0, 5))
                .withScale(tileScale)
                .withBounds(0, 6, 16, 10)
                .build();

        MapTileBuilder leftPlatformTile = new MapTileBuilder(leftPlatformFrame)
                .withTileType(TileType.JUMP_THROUGH_PLATFORM);

        mapTiles.add(leftPlatformTile);

        // platform to be jumped through when going up - middle
        Frame middlePlatformFrame = new FrameBuilder(getSubImage(0, 6))
                .withScale(tileScale)
                .withBounds(0, 6, 16, 10)
                .build();

        MapTileBuilder middlePlatformTile = new MapTileBuilder(middlePlatformFrame)
                .withTileType(TileType.JUMP_THROUGH_PLATFORM);

        mapTiles.add(middlePlatformTile);

        // platform to be jumped through when going up - right side
        Frame rightPlatformFrame = new FrameBuilder(getSubImage(0, 7))
                .withScale(tileScale)
                .withBounds(0, 6, 16, 10)
                .build();

        MapTileBuilder rightPlatformTile = new MapTileBuilder(rightPlatformFrame)
                .withTileType(TileType.JUMP_THROUGH_PLATFORM);

        mapTiles.add(rightPlatformTile);

        

        // default floor tile
        Frame floorFrame = new FrameBuilder(getSubImage(1,5))
                .withScale(tileScale)
                .build();
        MapTileBuilder floorTile = new MapTileBuilder(floorFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(floorTile);

        

        
        return mapTiles;

    }
    
    
}
