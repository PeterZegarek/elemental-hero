package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;

import java.util.ArrayList;

public class MenuTileset extends Tileset {

    public MenuTileset() {
        super(ImageLoader.load("Menu & Credit Screen Tileset.png"), 16, 16, 3);
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // grass1 Tile 
        Frame grass1Frame = new FrameBuilder(getSubImage(0, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder grass1Tile = new MapTileBuilder(grass1Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(grass1Tile);

        // Sky
        Frame SkyFrame = new FrameBuilder(getSubImage(0, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder SkyTile = new MapTileBuilder(SkyFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(SkyTile);

        // Dirt
        Frame DirtFrame = new FrameBuilder(getSubImage(0, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder DirtTile = new MapTileBuilder(DirtFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(DirtTile);
 
        // PurpleFlower
        Frame PurpleFlowerFrame = new FrameBuilder(getSubImage(0, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder PurpleFlowerTile = new MapTileBuilder(PurpleFlowerFrame)
                .withTileType(TileType.PASSABLE);
        mapTiles.add(PurpleFlowerTile);
        

        // TreeBase Tile
        Frame TreeBaseFrame = new FrameBuilder(getSubImage(1, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder TreeBaseTile = new MapTileBuilder(TreeBaseFrame)
                .withTileType(TileType.NOT_PASSABLE);
        mapTiles.add(TreeBaseTile);

        // grass2
        Frame grass2Frame = new FrameBuilder(getSubImage(1, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder grass2Tile = new MapTileBuilder(grass2Frame)
                .withTileType(TileType.NOT_PASSABLE);
        mapTiles.add(grass2Tile);

        // YellowFlower
        Frame YellowFlowerFrame = new FrameBuilder(getSubImage(0, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder YellowFlowerTile = new MapTileBuilder(YellowFlowerFrame)
                .withTileType(TileType.PASSABLE);
        mapTiles.add(YellowFlowerTile);

        // EndBranch 
        Frame EndBranchFrame = new FrameBuilder(getSubImage(1, 5))
                .withScale(tileScale)
                .build();

        MapTileBuilder EndBranchTile = new MapTileBuilder(EndBranchFrame)
                .withTileType(TileType.WATER);
        mapTiles.add(EndBranchTile);

        // Sun
        Frame SunFrame = new FrameBuilder(getSubImage(2, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder SunTile = new MapTileBuilder(SunFrame)
                .withTileType(TileType.WATER);
        mapTiles.add(SunTile);

        // TreeHole
        Frame TreeHoleFrame = new FrameBuilder(getSubImage(2, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder TreeHoleTile = new MapTileBuilder(TreeHoleFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(TreeHoleTile);

        // Branch 
        Frame BranchFrame = new FrameBuilder(getSubImage(2, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder BranchTile = new MapTileBuilder(BranchFrame)
                .withTileType(TileType.PASSABLE);
        mapTiles.add(BranchTile);

        // VertLightnight1 
        Frame VertLightnight1Frame = new FrameBuilder(getSubImage(3, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder VertLightnight1Tile = new MapTileBuilder(VertLightnight1Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(VertLightnight1Tile);

        // VertLightnight2 
        Frame VertLightnight2Frame = new FrameBuilder(getSubImage(3, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder VertLightnight2Tile = new MapTileBuilder(VertLightnight2Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(VertLightnight2Tile);

        // Stone
        Frame StoneFrame = new FrameBuilder(getSubImage(3, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder StoneTile = new MapTileBuilder(StoneFrame)
                .withTileType(TileType.PASSABLE);
        mapTiles.add(StoneTile);

        // Grass3 (45degree)
        Frame Grass3Frame = new FrameBuilder(getSubImage(3, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder Grass3Tile = new MapTileBuilder(Grass3Frame)
                .withTileType(TileType.WATER);

        mapTiles.add(Grass3Tile);

        // iDontKnow
        Frame iDontKnowFrame = new FrameBuilder(getSubImage(3, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder iDontKnowTile = new MapTileBuilder(iDontKnowFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(iDontKnowTile);

        // FireRock1 (45 degree)
        Frame FireRock1Frame = new FrameBuilder(getSubImage(3, 5))
                .withScale(tileScale)
                .build();

        MapTileBuilder FireRock1Tile = new MapTileBuilder(FireRock1Frame)
                .withTileType(TileType.NOT_PASSABLE);
        mapTiles.add(FireRock1Tile);

        // FireRock2 (45 degree)
        Frame FireRock2Frame = new FrameBuilder(getSubImage(4, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder FireRock2Tile = new MapTileBuilder(FireRock2Frame)
                .withTileType(TileType.NOT_PASSABLE);
        mapTiles.add(FireRock2Tile);

        // Chain1
        Frame Chain1Frame = new FrameBuilder(getSubImage(4, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder Chain1Tile = new MapTileBuilder(Chain1Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(Chain1Tile);

        // Chain2
        Frame Chain2Frame = new FrameBuilder(getSubImage(4, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder Chain2Tile = new MapTileBuilder(Chain2Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(Chain2Tile);

        // Chain3
        Frame Chain3Frame = new FrameBuilder(getSubImage(4, 5))
                .withScale(tileScale)
                .build();

        MapTileBuilder Chain3Tile = new MapTileBuilder(Chain3Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(Chain3Tile);

        // Lava
        Frame LavaFrame = new FrameBuilder(getSubImage(5, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder LavaTile = new MapTileBuilder(LavaFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(LavaTile);

        // Brick
        Frame BrickFrame = new FrameBuilder(getSubImage(5, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder BrickTile = new MapTileBuilder(BrickFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(BrickTile);

        // BlackBackground
        Frame BlackBackgroundFrame = new FrameBuilder(getSubImage(5, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder BlackBackgroundTile = new MapTileBuilder(BlackBackgroundFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(BlackBackgroundTile);

        // TransitionWater1
        Frame TransitionWater1Frame = new FrameBuilder(getSubImage(5, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder TransitionWater1Tile = new MapTileBuilder(TransitionWater1Frame)
                .withTileType(TileType.WATER);

        mapTiles.add(TransitionWater1Tile);

        // Ground Spike 1 - (Dark Blue Background)
        Frame GroundSpike1Frame = new FrameBuilder(getSubImage(5, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder GroundSpike1Tile = new MapTileBuilder(GroundSpike1Frame)
                .withTileType(TileType.WATER);

        mapTiles.add(GroundSpike1Tile);

        // LighBlueBackground
        Frame LighBlueBackgroundFrame = new FrameBuilder(getSubImage(5, 5))
                .withScale(tileScale)
                .build();

        MapTileBuilder LighBlueBackgroundTile = new MapTileBuilder(LighBlueBackgroundFrame)
                .withTileType(TileType.WATER);
        mapTiles.add(LighBlueBackgroundTile);

        // WaterStone1 
        Frame WaterStone1Frame = new FrameBuilder(getSubImage(6, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder WaterStone1Tile = new MapTileBuilder(WaterStone1Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(WaterStone1Tile);

        // WaterStone2 
        Frame WaterStone2Frame = new FrameBuilder(getSubImage(6, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder WaterStone2Tile = new MapTileBuilder(WaterStone2Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(WaterStone2Tile);

        // TransitionWater2
        Frame TransitionWater2Frame = new FrameBuilder(getSubImage(6, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder TransitionWater2Tile = new MapTileBuilder(TransitionWater2Frame)
                .withTileType(TileType.WATER);

        mapTiles.add(TransitionWater2Tile);

        // RoofSpikes1
        Frame RoofSpikes1Frame = new FrameBuilder(getSubImage(6, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder RoofSpikes1Tile = new MapTileBuilder(RoofSpikes1Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(RoofSpikes1Tile);

        // Ground Spike 2 - (Light Blue Background)
        Frame GroundSpike2Frame = new FrameBuilder(getSubImage(6, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder GroundSpike2Tile = new MapTileBuilder(GroundSpike2Frame)
                .withTileType(TileType.WATER);

        mapTiles.add(GroundSpike2Tile);

        // BlueBackground
        Frame BlueBackgroundFrame = new FrameBuilder(getSubImage(6, 5))
                .withScale(tileScale)
                .build();

        MapTileBuilder BlueBackgroundTile = new MapTileBuilder(BlueBackgroundFrame)
                .withTileType(TileType.WATER);
        mapTiles.add(BlueBackgroundTile);

        // Cloud1
        Frame Cloud1Frame = new FrameBuilder(getSubImage(7, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder Cloud1Tile = new MapTileBuilder(Cloud1Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Cloud1Tile);

        // Cloud2
        Frame Cloud2Frame = new FrameBuilder(getSubImage(7, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder Cloud2Tile = new MapTileBuilder(Cloud2Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Cloud2Tile);

         // Cloud3
        Frame Cloud3Frame = new FrameBuilder(getSubImage(7, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder Cloud3Tile = new MapTileBuilder(Cloud3Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Cloud3Tile);

        // RoofSpikes2
        Frame RoofSpikes2Frame = new FrameBuilder(getSubImage(7, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder RoofSpikes2Tile = new MapTileBuilder(RoofSpikes2Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(RoofSpikes2Tile);

        // Ground Spike 3 - (Blue Background)
        Frame GroundSpike3Frame = new FrameBuilder(getSubImage(7, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder GroundSpike3Tile = new MapTileBuilder(GroundSpike3Frame)
                .withTileType(TileType.WATER);

        mapTiles.add(GroundSpike3Tile);

        // DarkBlueBackground
        Frame DarkBlueBackgroundFrame = new FrameBuilder(getSubImage(7, 5))
                .withScale(tileScale)
                .build();

        MapTileBuilder DarkBlueBackgroundTile = new MapTileBuilder(DarkBlueBackgroundFrame)
                .withTileType(TileType.WATER);

        mapTiles.add(DarkBlueBackgroundTile);

        // Cloud4
        Frame Cloud4Frame = new FrameBuilder(getSubImage(8, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder Cloud4Tile = new MapTileBuilder(Cloud4Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Cloud4Tile);

        // Cloud5
        Frame Cloud5Frame = new FrameBuilder(getSubImage(8, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder Cloud5Tile = new MapTileBuilder(Cloud5Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Cloud5Tile);

        // Column 1 - (Blue Background)
        Frame Column1Frame = new FrameBuilder(getSubImage(8, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder Column1Tile = new MapTileBuilder(Column1Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Column1Tile);

        // Column 2 - (Dark Blue Background)
        Frame Column2Frame = new FrameBuilder(getSubImage(8, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder Column2Tile = new MapTileBuilder(Column2Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Column2Tile);

        // Wave 
        Frame WaveFrame = new FrameBuilder(getSubImage(8, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder WaveTile = new MapTileBuilder(WaveFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(WaveTile);

        // Cloud6
        Frame Cloud6Frame = new FrameBuilder(getSubImage(9, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder Cloud6Tile = new MapTileBuilder(Cloud6Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(Cloud6Tile);

        // ElectricBackground1 
        Frame ElectricBackground1Frame = new FrameBuilder(getSubImage(9, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder ElectricBackground1Tile = new MapTileBuilder(ElectricBackground1Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(ElectricBackground1Tile);

        // ElectricPlatform1
        Frame ElectricPlatform1Frame = new FrameBuilder(getSubImage(9, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder ElectricPlatform1Tile = new MapTileBuilder(ElectricPlatform1Frame)
                .withTileType(TileType.PASSABLE);
        mapTiles.add(ElectricPlatform1Tile);

        // ElectricPlatform2
        Frame ElectricPlatform2Frame = new FrameBuilder(getSubImage(9, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder ElectricPlatform2Tile = new MapTileBuilder(ElectricPlatform2Frame)
                .withTileType(TileType.PASSABLE);
        mapTiles.add(ElectricPlatform2Tile);

        // ElectricPlatform3
        Frame ElectricPlatform3Frame = new FrameBuilder(getSubImage(9, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder ElectricPlatform3Tile = new MapTileBuilder(ElectricPlatform3Frame)
                .withTileType(TileType.PASSABLE);
        mapTiles.add(ElectricPlatform3Tile);

        // Lightning1
        Frame Lightning1Frame = new FrameBuilder(getSubImage(9, 5))
                .withScale(tileScale)
                .build();

        MapTileBuilder Lightning1Tile = new MapTileBuilder(Lightning1Frame)
                .withTileType(TileType.PASSABLE);
        mapTiles.add(Lightning1Tile);

        // ElectricPlatform4
        Frame ElectricPlatform4Frame = new FrameBuilder(getSubImage(10, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder ElectricPlatform4Tile = new MapTileBuilder(ElectricPlatform4Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(ElectricPlatform4Tile);

        // ElectricPlatform5
        Frame ElectricPlatform5Frame = new FrameBuilder(getSubImage(10, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder ElectricPlatform5Tile = new MapTileBuilder(ElectricPlatform5Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(ElectricPlatform5Tile);

        // ElectricPlatform6
        Frame ElectricPlatform6Frame = new FrameBuilder(getSubImage(10, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder ElectricPlatform6Tile = new MapTileBuilder(ElectricPlatform6Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(ElectricPlatform6Tile);

        // ElectricBackground2
        Frame ElectricBackground2Frame = new FrameBuilder(getSubImage(10, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder ElectricBackground2Tile = new MapTileBuilder(ElectricBackground2Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(ElectricBackground2Tile);

        // ElectricBackground3
        Frame ElectricBackground3Frame = new FrameBuilder(getSubImage(10, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder ElectricBackground3Tile = new MapTileBuilder(ElectricBackground3Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(ElectricBackground3Tile);

        // Lightning1
        Frame Lightning2Frame = new FrameBuilder(getSubImage(10, 5))
                .withScale(tileScale)
                .build();

        MapTileBuilder Lightning2Tile = new MapTileBuilder(Lightning2Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(Lightning2Tile);

        // ElectricBackground4
        Frame ElectricBackground4Frame = new FrameBuilder(getSubImage(11, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder ElectricBackground4Tile = new MapTileBuilder(ElectricBackground4Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(ElectricBackground4Tile);

        // ElectricBackground5
        Frame ElectricBackground5Frame = new FrameBuilder(getSubImage(11, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder ElectricBackground5Tile = new MapTileBuilder(ElectricBackground5Frame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(ElectricBackground5Tile);

        // ElectricPlatform7
        Frame ElectricPlatform7Frame = new FrameBuilder(getSubImage(11, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder ElectricPlatform7Tile = new MapTileBuilder(ElectricPlatform7Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(ElectricPlatform7Tile);

        // Laserthing Tile
        Frame LaserthingFrame = new FrameBuilder(getSubImage(11, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder LaserthingTile = new MapTileBuilder(LaserthingFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(LaserthingTile);

        // UpArrow
        Frame UpArrowFrame = new FrameBuilder(getSubImage(11, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder UpArrowTile = new MapTileBuilder(UpArrowFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(UpArrowTile);

        // LeftArrow 
        Frame LeftArrowFrame = new FrameBuilder(getSubImage(11, 5))
                .withScale(tileScale)
                .build();

        MapTileBuilder LeftArrowTile = new MapTileBuilder(LeftArrowFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(LeftArrowTile);

        // StormCloud1
        Frame StormCloud1Frame = new FrameBuilder(getSubImage(12, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder StormCloud1Tile = new MapTileBuilder(StormCloud1Frame)
                .withTileType(TileType.WATER);

        mapTiles.add(StormCloud1Tile);

        // StormCloud2
        Frame StormCloud2Frame = new FrameBuilder(getSubImage(12, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder StormCloud2Tile = new MapTileBuilder(StormCloud2Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(StormCloud2Tile);

        // StormCloud3
        Frame StormCloud3Frame = new FrameBuilder(getSubImage(12, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder StormCloud3Tile = new MapTileBuilder(StormCloud3Frame)
                .withTileType(TileType.WATER);

        mapTiles.add(StormCloud3Tile);

        // StormCloud4
        Frame StormCloud4Frame = new FrameBuilder(getSubImage(12, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder StormCloud4Tile = new MapTileBuilder(StormCloud4Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(StormCloud4Tile);

        // StormCloud5
        Frame StormCloud5Frame = new FrameBuilder(getSubImage(12, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder StormCloud5Tile = new MapTileBuilder(StormCloud5Frame)
                .withTileType(TileType.WATER);

        mapTiles.add(StormCloud5Tile);

        // StormCloud6
        Frame StormCloud6Frame = new FrameBuilder(getSubImage(12, 5))
                .withScale(tileScale)
                .build();

        MapTileBuilder StormCloud6Tile = new MapTileBuilder(StormCloud6Frame)
                .withTileType(TileType.WATER);

        mapTiles.add(StormCloud6Tile);

        // StormCloud7
        Frame StormCloud7Frame = new FrameBuilder(getSubImage(13, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder StormCloud7Tile = new MapTileBuilder(StormCloud7Frame)
                .withTileType(TileType.WATER);

        mapTiles.add(StormCloud7Tile);

        // StormCloud8
        Frame StormCloud8Frame = new FrameBuilder(getSubImage(13, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder StormCloud8Tile = new MapTileBuilder(StormCloud8Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(StormCloud8Tile);
    
        return mapTiles;
    }
}