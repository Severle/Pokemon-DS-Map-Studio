
package editor.handler;

import editor.MainFrame;
import editor.bordermap.BorderMapsGrid;
import editor.buildingeditor2.buildfile.BuildFile;
import editor.game.Game;
import editor.grid.MapGrid;
import editor.mapmatrix.MapMatrix;
import editor.smartdrawing.SmartGrid;
import editor.state.MapLayerState;
import editor.state.StateHandler;
import formats.backsound.BackSound;
import formats.bdhc.Bdhc;
import formats.bdhcam.Bdhcam;
import formats.collisions.Collisions;
import lombok.Getter;
import lombok.Setter;
import tileset.Tile;
import tileset.Tileset;
import utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

/**
 * @author Trifindo
 */
@SuppressWarnings({"SpellCheckingInspection", "unused", "ExtractMethodRecommender"})
public class MapEditorHandler {

    //Version name
    public static final String versionName = "Pokemon DS Map Studio 2.2";

    //Main frame
    @Getter
    private final MainFrame mainFrame;

    //Game
    @Getter
    private final Game game;

    //Working directory
    @Setter
    @Getter
    private String lastTilesetDirectoryUsed = null;
    @Setter
    @Getter
    private String lastMapDirectoryUsed        = null;
    @Setter
    @Getter
    private String lastBdhcDirectoryUsed       = null;
    @Setter
    @Getter
    private String lastCollisionsDirectoryUsed = null;
    @Setter
    @Getter
    private String lastNsbtxDirectoryUsed       = null;
    @Setter
    @Getter
    private String lastTileObjDirectoryUsed     = null;
    private String lastBuildEditorDirectoryUsed = null;

    //Tileset
    private Tileset tset;

    //Tile selector
    @Setter
    private int indexTileSelected = 0;

    //Height map
    public static final int minHeight = -15;
    public static final int maxHeight = 15;
    public static final int     numHeights   = maxHeight - minHeight + 1;
    @Getter
    private final       int[]   heights      = new int[numHeights];
    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    private final Color[]         heightColors        = new Color[numHeights];
    @Getter
    private       int             heightIndexSelected = 15;
    private       BufferedImage[] heightImages        = new BufferedImage[numHeights];

    //Smart grid
    @Getter
    @Setter
    private int smartGridIndexSelected = 0;

    //Layers
    public boolean[] renderLayers = new boolean[MapGrid.numLayers];

    //Border Maps
    private Tileset        borderMapTileset;
    @Setter
    @Getter
    private BorderMapsGrid borderMapsGrid = new BorderMapsGrid();

    //Map Data
    @Getter
    private MapMatrix mapMatrix;
    @Getter
    private Point     mapSelected = new Point(0, 0);
    private int     activeLayer     = 0;
    @Getter
    private int[][] tileLayerCopy   = null;
    @Getter
    private int[][] heightLayerCopy = null;

    //Map State Hanlder
    @Setter
    @Getter
    private StateHandler mapStateHandler = new StateHandler();
    @Setter
    @Getter
    private boolean      layerChanged    = false;

    @Setter
    private boolean realTimePostProcessing = true;

    public MapEditorHandler(MainFrame frame) {
        initHeights();

        this.mainFrame = frame;

        game = new Game(Game.DIAMOND);
        game.loadGameIcons();

        mapMatrix = new MapMatrix(this);

        //Active layer
        Arrays.fill(renderLayers, true);

    }

    private void initHeights() {
        heightImages = Utils.loadVerticalImageArrayAsResource("/imgs/heights_increased.png", 31);
        for (int i = 0; i < numHeights; i++) {
            heights[i] = minHeight + i;
        }

    }

    public MapData getCurrentMap() {
        return mapMatrix.getMapAndCreate(mapSelected);
    }

    public void incrementTileSelected(int delta) {
        int newTileIndex = indexTileSelected + delta;
        if ((newTileIndex >= 0) && (newTileIndex < tset.size())) {
            indexTileSelected = newTileIndex;
        }
    }

    public void incrementHeightSelected(int delta) {
        int newHeightIndex = heightIndexSelected + delta;
        if ((newHeightIndex >= 0) && (newHeightIndex < numHeights)) {
            heightIndexSelected = newHeightIndex;
        }
    }

    public BufferedImage getHeightImage(int index) {
        return heightImages[index];
    }

    public BufferedImage getHeightImageByValue(int value) {
        return heightImages[maxHeight - value];
    }

    public int getNumHeights() {
        return numHeights;
    }

    public int getHeight(int index) {
        return heights[index];
    }

    public int getHeightSelected() {
        return heights[heightIndexSelected];
    }

    public void setHeightSelected(int value) {
        setHeightIndexSelected(value - minHeight);
    }

    public void setHeightIndexSelected(int index) {
        if (index >= 0 && index < heights.length) {
            this.heightIndexSelected = index;
        }
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public Color getHeightColor(int heightValue) {
        return heightColors[heightValue - minHeight];
    }

    public Color getHeightColorByIndex(int index) {
        return heightColors[index];
    }

    public int getTileIndexSelected() {
        return indexTileSelected;
    }

    public void setTileset(Tileset tileset) {
        this.tset = tileset;
    }

    public Tile getTileSelected() {
        return tset.get(indexTileSelected);
    }

    public Tileset getTileset() {
        return tset;
    }

    public MapGrid getGrid() {
        return getCurrentMap().getGrid();
    }

    public void setGrid(MapGrid grid) {
        getCurrentMap().setGrid(grid);
    }

    public ArrayList<SmartGrid> getSmartGridArray() {
        return tset.getSmartGridArray();
    }


    public SmartGrid getSmartGrid(int index) {
        return tset.getSmartGridArray().get(index);
    }

    public String getLastBuildDirectoryUsed() {
        return lastBuildEditorDirectoryUsed;
    }

    public void setLastBuildDirectoryUsed(String lastDirectoryUsed) {
        this.lastBuildEditorDirectoryUsed = lastDirectoryUsed;
    }

    public int[][] getActiveTileLayer() {
        return getCurrentMap().getGrid().tileLayers[activeLayer];
    }

    public int[][] getActiveHeightLayer() {
        return getCurrentMap().getGrid().heightLayers[activeLayer];
    }

    public void setActiveTileLayer(int index) {
        MapGrid grid = getCurrentMap().getGrid();
        if (index >= 0 && index < MapGrid.numLayers) {
            activeLayer = index;
        }
    }

    public void setOnlyActiveTileLayer(int index) {
        MapGrid grid = getCurrentMap().getGrid();
        if (index >= 0 && index < MapGrid.numLayers) {
            Arrays.fill(renderLayers, false);
            renderLayers[index] = true;
            activeLayer = index;
            mainFrame.repaintMapDisplay();
        }
    }

    public boolean isLayerTheOnlyActive(int index) {
        MapGrid grid = getCurrentMap().getGrid();
        for(int i = 0; i < MapGrid.numLayers; i++){
            if(renderLayers[i] && i != index){
                return false;
            }
        }
        return true;
    }

    public void setLayersEnabled(boolean enabled) {
        MapGrid grid = getCurrentMap().getGrid();
        Arrays.fill(renderLayers, enabled);
    }

    public int[][] getTileLayer(int index) {
        return getCurrentMap().getGrid().tileLayers[index];
    }

    public int[][] getHeightLayer(int index) {
        return getCurrentMap().getGrid().heightLayers[index];
    }

    public int getActiveLayerIndex() {
        return activeLayer;
    }

    public SmartGrid getSmartGridSelected() {
        return tset.getSmartGridArray().get(smartGridIndexSelected);
    }

    public void invertLayerState(int index) {
        renderLayers[index] = !renderLayers[index];
        //mainFrame.repaintMapDisplay();
    }

    public void setLayerState(int index, boolean enabled) {
        renderLayers[index] = enabled;
    }

    public void updateLayerThumbnail(int index) {
        mainFrame.getThumbnailLayerSelector().drawLayerThumbnail(index);
    }

    public void repaintThumbnailLayerSelector() {
        mainFrame.getThumbnailLayerSelector().repaint();
    }

    public Bdhc getBdhc() {
        return getCurrentMap().getBdhc();
    }

    public Bdhcam getBdhcam(){
        return getCurrentMap().getBdhcam();
    }

    public void setBdhcam(Bdhcam bdhcam){
        getCurrentMap().setBdhcam(bdhcam);
    }

    public void setBdhc(Bdhc bdhc) {
        getCurrentMap().setBdhc(bdhc);
    }

    public BackSound getBacksound() {
        return getCurrentMap().getBacksound();
    }

    public void setBacksound(BackSound backsound) {
        getCurrentMap().setBacksound(backsound);
    }

    public Collisions getCollisions() {
        return getCurrentMap().getCollisions();
    }

    public void setCollisions(Collisions collisions) {
        getCurrentMap().setCollisions(collisions);
    }

    public Collisions getCollisions2() {
        return getCurrentMap().getCollisions2();
    }

    public void setCollisions2(Collisions collisions) {
        getCurrentMap().setCollisions2(collisions);
    }

    public void setGameIndex(int game) {
        this.game.gameSelected = game;
    }

    public int getGameIndex() {
        return game.gameSelected;
    }

    public void addMapState(MapLayerState state) {
        mapStateHandler.addState(state);
        mainFrame.getUndoButton().setEnabled(true);
        mainFrame.getRedoButton().setEnabled(false);
    }

    public void resetMapStateHandler() {
        mapStateHandler = new StateHandler();
    }

    public void moveSelectedSmartGridUp() {
        if (smartGridIndexSelected > 0) {
            Collections.swap(tset.getSmartGridArray(), smartGridIndexSelected, smartGridIndexSelected - 1);
            smartGridIndexSelected--;
        }
    }

    public void moveSelectedSmartGridDown() {
        if (smartGridIndexSelected < tset.getSmartGridArray().size() - 1) {
            Collections.swap(tset.getSmartGridArray(), smartGridIndexSelected, smartGridIndexSelected + 1);
            smartGridIndexSelected++;
        }
    }

    public boolean mapHasVertexColors() {
        //Get indices of tiles used in map
        MapGrid grid = getGrid();
        TreeSet<Integer> tileIndicesInGrid = new TreeSet<>();
        for (int i = 0; i < grid.tileLayers.length; i++) {
            for (int j = 0; j < grid.tileLayers[i].length; j++) {
                for (int k = 0; k < grid.tileLayers[i][j].length; k++) {
                    int tileIndex = grid.tileLayers[i][j][k];
                    if (tileIndex != -1) {
                        tileIndicesInGrid.add(tileIndex);
                    }
                }
            }
        }

        //Get indices of materials used in tiles
        TreeSet<Integer> materialIndicesInGrid = new TreeSet<>();
        for (Integer tileIndex : tileIndicesInGrid) {
            Tile tile = tset.get(tileIndex);
            materialIndicesInGrid.addAll(tile.getTextureIDs());
        }

        //Check if materials in map use vertex colors
        for (Integer materialIndex : materialIndicesInGrid) {
            if (tset.getMaterial(materialIndex).vertexColorsEnabled()) {
                return true;
            }
        }
        return false;
    }

    public void updateAllMapThumbnails() {
        for (HashMap.Entry<Point, MapData> map : mapMatrix.getMatrix().entrySet()) {
            map.getValue().updateMapThumbnail();
        }
    }

    public void updateMapThumbnails(Set<Point> mapsCoords) {
        for (Point mapCoords : mapsCoords) {
            MapData data = mapMatrix.getMap(mapCoords);
            if (data != null) {
                data.updateMapThumbnail();
            }
        }
    }

    public void setBorderMapsTileset(Tileset borderMapTileset) {
        this.borderMapTileset = borderMapTileset;
    }

    public Tileset getBorderMapsTileset() {
        return borderMapTileset;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getMapName() {
        return Utils.removeExtensionFromPath(new File(getMapMatrix().filePath).getName());
    }

    public BuildFile getBuildings() {
        return getCurrentMap().getBuildings();
    }

    public void setBuildings(BuildFile buildings) {
        this.getCurrentMap().setBuildings(buildings);
    }

    public void setMapSelected(Point mapCoords, boolean updateScrollbars) {
        this.mapSelected = mapCoords;

        mainFrame.getThumbnailLayerSelector().drawAllLayerThumbnails();
        mainFrame.getThumbnailLayerSelector().repaint();

        mainFrame.getMapMatrixDisplay().repaint();
        if (updateScrollbars) {
            mainFrame.updateMapMatrixDisplayScrollBars();
        }

        mainFrame.updateViewMapInfo();
    }

    public void setMapSelected(Point mapCoords) {
        setMapSelected(mapCoords, true);
    }


    public void setDefaultMapSelected() {
        if (mapMatrix.getMatrix().isEmpty()) { //|| mapMatrix.getMatrix().get(new Point(0, 0)) == null) {
            mapSelected = new Point(0, 0);
        } else {
            mapSelected = mapMatrix.getMatrix().keySet().iterator().next();
        }
    }

    public boolean mapExists(Point mapCoords) {
        return mapMatrix.getMatrix().containsKey(mapCoords);
    }

    public boolean mapSelectedNotExists() {
        return !mapExists(mapSelected);
    }

    public MapData getMapData() {
        return mapMatrix.getMapAndCreate(mapSelected);
    }

    public void setMapMatrix(MapMatrix mapMatrix) {
        this.mapMatrix = mapMatrix;
        mapSelected = new Point(0, 0);
    }

    public boolean useRealTimePostProcessing() {
        return realTimePostProcessing;
    }

    public void clearLayer(int index) {
        addMapState(new MapLayerState("Clear Layer", this));
        getGrid().clearLayer(index);
        mainFrame.getThumbnailLayerSelector().drawLayerThumbnail(index);
        mainFrame.getThumbnailLayerSelector().repaint();
        mainFrame.getMapDisplay().updateMapLayerGL(index);
        mainFrame.getMapDisplay().repaint();
    }

    public void pasteLayer(int index) {
        if (getTileset().size() > 0) {
            if (tileLayerCopy != null && heightLayerCopy != null) {
                addMapState(new MapLayerState("Paste Tile and Height Layer", this));
                pasteTileLayer(index);
                pasteHeightLayer(index);
                mainFrame.getMapDisplay().updateMapLayerGL(index);
                mainFrame.getMapDisplay().repaint();
                updateLayerThumbnail(index);
                mainFrame.getThumbnailLayerSelector().repaint();
            }
        }
    }

    public void pasteLayerTiles(int index) {
        if (getTileset().size() > 0) {
            if (tileLayerCopy != null) {
                addMapState(new MapLayerState("Paste Tile Layer", this));
                pasteTileLayer(index);
                mainFrame.getMapDisplay().updateMapLayerGL(index);
                mainFrame.getMapDisplay().repaint();
                updateLayerThumbnail(index);
                mainFrame.getThumbnailLayerSelector().repaint();
            }
        }
    }

    public void pasteLayerHeights(int index) {
        if (getTileset().size() > 0) {
            if (heightLayerCopy != null) {
                addMapState(new MapLayerState("Paste Height Layer", this));
                pasteHeightLayer(index);
                mainFrame.getMapDisplay().updateMapLayerGL(index);
                mainFrame.getMapDisplay().repaint();
                updateLayerThumbnail(index);
                mainFrame.getThumbnailLayerSelector().repaint();
            }
        }
    }


    public void copySelectedLayer() {
        copyLayer(getActiveLayerIndex());
    }

    public void pasteTileLayer() {
        pasteTileLayer(getActiveLayerIndex());
    }

    public void pasteHeightLayer() {
        pasteHeightLayer(getActiveLayerIndex());
    }

    public void copyLayer(int index) {
        tileLayerCopy = getGrid().cloneTileLayer(index);
        heightLayerCopy = getGrid().cloneHeightLayer(index);
    }

    public void pasteTileLayer(int index) {
        if (tileLayerCopy != null) {
            getGrid().tileLayers[index] = getGrid().cloneLayer(tileLayerCopy);
        }
    }

    public void pasteHeightLayer(int index) {
        if (heightLayerCopy != null) {
            getGrid().heightLayers[index] = getGrid().cloneLayer(heightLayerCopy);
        }
    }

    public void clearCopyLayer() {
        tileLayerCopy = null;
        heightLayerCopy = null;
    }

    public void indexOfTileVisualData() {

    }
}
