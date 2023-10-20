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


        // default block design used under platforms - - used as background so its transparent
        Frame platformBlockFrame = new FrameBuilder(getSubImage(4,3))
                .withScale(tileScale)
                .build();
        MapTileBuilder platformBlockTile = new MapTileBuilder(platformBlockFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(platformBlockTile);

        // default wall tile design 
        Frame wallFrame = new FrameBuilder(getSubImage(3,3))
                .withScale(tileScale)
                .build();
        MapTileBuilder wallTile = new MapTileBuilder(wallFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(wallTile);

        

        // right 45 degree slope - i mixed them up so the variable names are flipped.
        Frame leftSlopeFrame = new FrameBuilder(getSubImage(4, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder leftSlopeTile = new MapTileBuilder(leftSlopeFrame)
                .withTileType(TileType.SLOPE)
                .withTileLayout(SlopeTileLayoutUtils.createRight45SlopeLayout(spriteWidth, (int) tileScale));

        mapTiles.add(leftSlopeTile);

        // left 45 degree slope - i mixed them up so the variable names are flipped.
        Frame rightSlopeFrame = new FrameBuilder(getSubImage(4, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder rightSlopeTile = new MapTileBuilder(rightSlopeFrame)
                .withTileType(TileType.SLOPE)
                .withTileLayout(SlopeTileLayoutUtils.createLeft45SlopeLayout(spriteWidth, (int) tileScale));

        mapTiles.add(rightSlopeTile);


        // transparent background tile - use it to fill in mistakes
        Frame transparentBackground = new FrameBuilder(getSubImage(4,4))
                .withScale(tileScale)
                .build();
        
        MapTileBuilder transparentFrame = new MapTileBuilder(transparentBackground)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(transparentFrame);

        // middle platform to be jumped through when going up - left side
        Frame middlePlatformFrame = new FrameBuilder(getSubImage(3, 2))
                .withScale(tileScale)
                .withBounds(0, 6, 16, 4)
                .build();

        MapTileBuilder middlePlatformTile = new MapTileBuilder(middlePlatformFrame)
                .withTileType(TileType.JUMP_THROUGH_PLATFORM);

        mapTiles.add(middlePlatformTile);

        // default support beam
        Frame supportBeamFrame = new FrameBuilder(getSubImage(3,1))
                .withScale(tileScale)
                .build();
        MapTileBuilder supportBeamTile = new MapTileBuilder(supportBeamFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(supportBeamTile);

        // half platform that you can jump through - right side
        Frame halfPlatformFrame = new FrameBuilder(getSubImage(2,2))
                .withScale(tileScale)
                .withBounds(0, 6, 16, 4)
                .build();
        MapTileBuilder halfPlatformTile = new MapTileBuilder(halfPlatformFrame)
                .withTileType(TileType.JUMP_THROUGH_PLATFORM);

        mapTiles.add(halfPlatformTile);
        
        // default wall tile design - bottom piece
        Frame wallBottomFrame = new FrameBuilder(getSubImage(3,4))
                .withScale(tileScale)
                .build();
        MapTileBuilder wallBottomTile = new MapTileBuilder(wallBottomFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(wallBottomTile);

        // default wall tile design - top piece
        Frame wallTopFrame = new FrameBuilder(getSubImage(3,4))
                .withScale(tileScale)
                .withImageEffect(ImageEffect.FLIP_VERTICAL)
                .build();
        MapTileBuilder wallTopTile = new MapTileBuilder(wallTopFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(wallTopTile);

        // sky frame without clouds
        Frame skyFrame = new FrameBuilder(getSubImage(3,0))
                .withScale(tileScale)
                .build();
        MapTileBuilder skyTile = new MapTileBuilder(skyFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(skyTile);

        // sky frame with clouds
        Frame sky1Frame = new FrameBuilder(getSubImage(2,0))
                .withScale(tileScale)
                .build();
        MapTileBuilder sky1Tile = new MapTileBuilder(sky1Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(sky1Tile);

        // sky frame with one big cloud
        Frame sky2Frame = new FrameBuilder(getSubImage(1,0))
                .withScale(tileScale)
                .build();
        MapTileBuilder sky2Tile = new MapTileBuilder(sky2Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(sky2Tile);

        // middle platform to be jumped through when going up - middle
        Frame middlePlatform1Frame = new FrameBuilder(getSubImage(2, 1))
                .withScale(tileScale)
                .withBounds(0, 6, 16, 4)
                .build();

        MapTileBuilder middlePlatform1Tile = new MapTileBuilder(middlePlatform1Frame)
                .withTileType(TileType.JUMP_THROUGH_PLATFORM);

        mapTiles.add(middlePlatform1Tile);


        
        return mapTiles;

    }
    
    
}
