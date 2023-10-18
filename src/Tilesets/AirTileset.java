package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;

import java.util.ArrayList;

//This represents the tileset specific to the air map in AirTileset.png
public class AirTileset extends Tileset {

    public AirTileset(){
        super(ImageLoader.load("AirTileset.png"), 15, 15, 3);
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();


        //Eraser
        Frame eraserFrame = new FrameBuilder(getSubImage(0, 0))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder eraserTile = new MapTileBuilder(eraserFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(eraserTile);
        

        //Column 1 of cloud

        Frame cloud1Frame = new FrameBuilder(getSubImage(4, 0))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud1Tile = new MapTileBuilder(cloud1Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud1Tile);


        Frame cloud2Frame = new FrameBuilder(getSubImage(5, 0))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud2Tile = new MapTileBuilder(cloud2Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud2Tile);


        Frame cloud3Frame = new FrameBuilder(getSubImage(6, 0))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud3Tile = new MapTileBuilder(cloud3Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud3Tile);


        Frame cloud4Frame = new FrameBuilder(getSubImage(7, 0))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud4Tile = new MapTileBuilder(cloud4Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud4Tile);



        //Column 2 of cloud

        Frame cloud5Frame = new FrameBuilder(getSubImage(4, 1))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud5Tile = new MapTileBuilder(cloud5Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud5Tile);


        Frame cloud6Frame = new FrameBuilder(getSubImage(5, 1))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud6Tile = new MapTileBuilder(cloud6Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud6Tile);


        Frame cloud7Frame = new FrameBuilder(getSubImage(6, 1))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud7Tile = new MapTileBuilder(cloud7Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud7Tile);


        Frame cloud8Frame = new FrameBuilder(getSubImage(7, 1))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud8Tile = new MapTileBuilder(cloud8Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud8Tile);



        //Column 3 of cloud

        Frame cloud9Frame = new FrameBuilder(getSubImage(4, 2))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud9Tile = new MapTileBuilder(cloud9Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud9Tile);


        Frame cloud10Frame = new FrameBuilder(getSubImage(5, 2))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud10Tile = new MapTileBuilder(cloud10Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud10Tile);


        Frame cloud11Frame = new FrameBuilder(getSubImage(6, 2))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud11Tile = new MapTileBuilder(cloud11Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud11Tile);


        Frame cloud12Frame = new FrameBuilder(getSubImage(7, 2))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud12Tile = new MapTileBuilder(cloud12Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud12Tile);



        //Column 4 of cloud

        Frame cloud13Frame = new FrameBuilder(getSubImage(4, 3))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud13Tile = new MapTileBuilder(cloud13Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud13Tile);


        Frame cloud14Frame = new FrameBuilder(getSubImage(5, 3))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud14Tile = new MapTileBuilder(cloud14Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud14Tile);


        Frame cloud15Frame = new FrameBuilder(getSubImage(6, 3))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud15Tile = new MapTileBuilder(cloud15Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud15Tile);


        Frame cloud16Frame = new FrameBuilder(getSubImage(7, 3))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud16Tile = new MapTileBuilder(cloud16Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud16Tile);


        
        //Smaller clouds at bottom of tileset

        Frame cloud17Frame = new FrameBuilder(getSubImage(8, 0))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud17Tile = new MapTileBuilder(cloud17Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud17Tile);


        Frame cloud18Frame = new FrameBuilder(getSubImage(8, 1))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud18Tile = new MapTileBuilder(cloud18Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud18Tile);


        Frame cloud19Frame = new FrameBuilder(getSubImage(8, 2))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud19Tile = new MapTileBuilder(cloud19Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud19Tile);


        Frame cloud20Frame = new FrameBuilder(getSubImage(8, 3))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud20Tile = new MapTileBuilder(cloud20Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud20Tile);

        Frame cloud21Frame = new FrameBuilder(getSubImage(8, 4))
                       .withScale(tileScale)
                       .build();
        
        MapTileBuilder cloud21Tile = new MapTileBuilder(cloud21Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(cloud21Tile);



        //Background cloud 1
        Frame backCloud1Frame = new FrameBuilder(getSubImage(0, 2))
                          .withScale(tileScale)
                          .build();

        MapTileBuilder backCloud1Tile = new MapTileBuilder(backCloud1Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(backCloud1Tile);

        Frame backCloud2Frame = new FrameBuilder(getSubImage(0, 3))
                          .withScale(tileScale)
                          .build();

        MapTileBuilder backCloud2Tile = new MapTileBuilder(backCloud2Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(backCloud2Tile);

        Frame backCloud3Frame = new FrameBuilder(getSubImage(0, 4))
                          .withScale(tileScale)
                          .build();

        MapTileBuilder backCloud3Tile = new MapTileBuilder(backCloud3Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(backCloud3Tile);


        //Background cloud 2
        Frame backCloud4Frame = new FrameBuilder(getSubImage(1, 0))
                          .withScale(tileScale)
                          .build();

        MapTileBuilder backCloud4Tile = new MapTileBuilder(backCloud4Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(backCloud4Tile);

        Frame backCloud5Frame = new FrameBuilder(getSubImage(1, 1))
                          .withScale(tileScale)
                          .build();

        MapTileBuilder backCloud5Tile = new MapTileBuilder(backCloud5Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(backCloud5Tile);

        Frame backCloud6Frame = new FrameBuilder(getSubImage(1, 2))
                          .withScale(tileScale)
                          .build();

        MapTileBuilder backCloud6Tile = new MapTileBuilder(backCloud6Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(backCloud6Tile);
        

        //Background cloud 3
        Frame backCloud7Frame = new FrameBuilder(getSubImage(2, 1))
                          .withScale(tileScale)
                          .build();

        MapTileBuilder backCloud7Tile = new MapTileBuilder(backCloud7Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(backCloud7Tile);

        Frame backCloud8Frame = new FrameBuilder(getSubImage(2, 2))
                          .withScale(tileScale)
                          .build();

        MapTileBuilder backCloud8Tile = new MapTileBuilder(backCloud8Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(backCloud8Tile);

        Frame backCloud9Frame = new FrameBuilder(getSubImage(2, 3))
                          .withScale(tileScale)
                          .build();

        MapTileBuilder backCloud9Tile = new MapTileBuilder(backCloud9Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(backCloud9Tile);

        Frame backCloud10Frame = new FrameBuilder(getSubImage(2, 4))
                          .withScale(tileScale)
                          .build();

        MapTileBuilder backCloud10Tile = new MapTileBuilder(backCloud10Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(backCloud10Tile);


        //Background cloud 4
        Frame backCloud11Frame = new FrameBuilder(getSubImage(3, 0))
                          .withScale(tileScale)
                          .build();

        MapTileBuilder backCloud11Tile = new MapTileBuilder(backCloud11Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(backCloud11Tile);

        Frame backCloud12Frame = new FrameBuilder(getSubImage(3, 1))
                          .withScale(tileScale)
                          .build();

        MapTileBuilder backCloud12Tile = new MapTileBuilder(backCloud12Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(backCloud12Tile);

        Frame backCloud13Frame = new FrameBuilder(getSubImage(3, 2))
                          .withScale(tileScale)
                          .build();

        MapTileBuilder backCloud13Tile = new MapTileBuilder(backCloud13Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(backCloud13Tile);

        return mapTiles;
    }
    
}
