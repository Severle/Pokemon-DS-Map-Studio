
package editor.grid;

import editor.handler.MapEditorHandler;
import formats.obj.ObjWriter;
import tileset.Tile;
import tileset.Tileset;
import utils.Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

/**
 * @author Trifindo
 */
@SuppressWarnings({"DuplicatedCode", "SpellCheckingInspection", "unused"})
public class MapGrid {

    private final MapEditorHandler handler;

    public static final int cols = 32;
    public static final int rows = 32;
    public static final int tileSize = 16;
    public static final int width = cols * tileSize;
    public static final int height = rows * tileSize;
    public static final float gridTileSize = 1.0f;

    public static final int numLayers = 8;
    public int[][][] tileLayers = new int[numLayers][cols][rows];
    public int[][][] heightLayers = new int[numLayers][cols][rows];

    public MapLayerGL[] mapLayersGL = new MapLayerGL[numLayers];

    public MapGrid(MapEditorHandler handler) {
        this.handler = handler;

        for (int k = 0; k < numLayers; k++) {
            //int[][] tileGrid = new int[cols][rows];
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    tileLayers[k][i][j] = -1;
                }
            }
        }

        //updateAllMapLayers(handler.useRealTimePostProcessing());
    }

    public void saveMapToOBJ(Tileset tset, String path, boolean saveTextures,
                             boolean saveVertexColors, float tileUpscale) throws FileNotFoundException {
        ObjWriter writer = new ObjWriter(tset, this, path, handler.getGameIndex(),
                saveTextures, saveVertexColors, tileUpscale);
        writer.writeMapObj();
    }

    public static void loadMatrixFromFile(BufferedReader br, int[][] matrix) throws IOException {
        for (int i = 0; i < rows; i++) {
            String line = br.readLine();
            String[] lineSplitted = line.split(" ");
            for (int j = 0; j < cols; j++) {
                matrix[j][i] = Integer.parseInt(lineSplitted[j]);
            }
        }
    }

    private Integer[][] loadLayerFromFile(BufferedReader br) throws IOException {
        Integer[][] layer = new Integer[cols][rows];
        for (int i = 0; i < rows; i++) {
            String line = br.readLine();
            String[] lineSplitted = line.split(" ");
            for (int j = 0; j < cols; j++) {
                layer[j][i] = Integer.parseInt(lineSplitted[j]);
            }
        }
        return layer;
    }

    public void replaceTilesUsingIndices(int[] indices) {
        int[][][] oldTileLayers = cloneTileLayers();
        for (int i = 0; i < numLayers; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < rows; k++) {
                    int index = oldTileLayers[i][j][k];
                    try {
                        if (index == -1) {
                            tileLayers[i][j][k] = -1;
                        } else {
                            tileLayers[i][j][k] = indices[index];
                        }
                    } catch (Exception ex) {
                        if (tileLayers != null) {
                            if (tileLayers[i] != null) {
                                if (tileLayers[i][j] != null) {
                                    tileLayers[i][j][k] = -1;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void replaceTileWithOldLayer(int[][][] oldTileLayers, int oldIndex, int newIndex) {
        for (int i = 0; i < numLayers; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < rows; k++) {
                    if (oldTileLayers[i][j][k] == oldIndex) {
                        tileLayers[i][j][k] = newIndex;
                    }
                }
            }
        }
    }

    public int[][][] cloneTileLayers() {
        int[][][] newTileLayers = new int[numLayers][cols][rows];
        for (int i = 0; i < numLayers; i++) {
            for (int j = 0; j < cols; j++) {
                System.arraycopy(tileLayers[i][j], 0, newTileLayers[i][j], 0, rows);
            }
        }
        return newTileLayers;
    }

    public void removeTileFromMap(int tileIndex) {
        for (int i = 0; i < numLayers; i++) {
            removeTileFromLayer(i, tileIndex);
        }
    }

    public void removeTileFromLayer(int layerIndex, int tileIndex) {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (tileLayers[layerIndex][i][j] == tileIndex) {
                    tileLayers[layerIndex][i][j] = -1;
                }
            }
        }
    }

    public void clearLayer(int layerIndex) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tileLayers[layerIndex][i][j] = -1;
                heightLayers[layerIndex][i][j] = 0;
            }
        }
    }

    public void clearAllLayers() {
        for (int i = 0; i < numLayers; i++) {
            clearLayer(i);
        }
    }

    public void moveTilesUp(int layerIndex) {
        for (int i = 0; i < (cols); i++) {
            for (int j = (rows - 1); j > 0; j--) {
                tileLayers[layerIndex][i][j] = tileLayers[layerIndex][i][j - 1];
                heightLayers[layerIndex][i][j] = heightLayers[layerIndex][i][j - 1];
            }
            tileLayers[layerIndex][i][0] = -1;
            heightLayers[layerIndex][i][0] = 0;
        }
    }

    public void moveTilesDown(int layerIndex) {
        for (int i = 0; i < (cols); i++) {
            for (int j = 0; j < (rows - 1); j++) {
                tileLayers[layerIndex][i][j] = tileLayers[layerIndex][i][j + 1];
                heightLayers[layerIndex][i][j] = heightLayers[layerIndex][i][j + 1];
            }
            tileLayers[layerIndex][i][rows - 1] = -1;
            heightLayers[layerIndex][i][rows - 1] = 0;
        }
    }

    public void moveTilesRight(int layerIndex) {
        for (int i = 0; i < (rows); i++) {
            for (int j = (cols - 1); j > 0; j--) {
                tileLayers[layerIndex][j][i] = tileLayers[layerIndex][j - 1][i];
                heightLayers[layerIndex][j][i] = heightLayers[layerIndex][j - 1][i];
            }
            tileLayers[layerIndex][0][i] = -1;
            heightLayers[layerIndex][0][i] = 0;
        }
    }

    public void moveTilesLeft(int layerIndex) {
        for (int i = 0; i < (rows); i++) {
            for (int j = 0; j < (cols - 1); j++) {
                tileLayers[layerIndex][j][i] = tileLayers[layerIndex][j + 1][i];
                heightLayers[layerIndex][j][i] = heightLayers[layerIndex][j + 1][i];
            }
            tileLayers[layerIndex][rows - 1][i] = -1;
            heightLayers[layerIndex][rows - 1][i] = 0;
        }
    }

    public void moveTilesUpZ(int layerIndex) {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (heightLayers[layerIndex][i][j] < MapEditorHandler.maxHeight) {
                    heightLayers[layerIndex][i][j]++;
                }
            }
        }
    }

    public void moveTilesDownZ(int layerIndex) {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (heightLayers[layerIndex][i][j] > MapEditorHandler.minHeight) {
                    heightLayers[layerIndex][i][j]--;
                }
            }
        }
    }

    public static void printMatrixInFile(PrintWriter out, int[][] matrix) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                out.print(matrix[j][i] + " ");
            }
            out.println();
        }
    }

    public void floodFillTileGrid(int x, int y, int value, int tileWidth, int tileHeight) {
        final int prevC = handler.getActiveTileLayer()[x][y];

        //Generate mask
        boolean[][] mask = new boolean[MapGrid.cols][MapGrid.rows];
        for (int i = 0; i < MapGrid.cols; i++) {
            for (int j = 0; j < MapGrid.rows; j++) {
                mask[i][j] = true;
            }
        }

        for (int i = 0; i < MapGrid.cols; i++) {
            for (int j = 0; j < MapGrid.rows; j++) {
                final int tileIndex = handler.getActiveTileLayer()[i][j];
                if (tileIndex != -1 && tileIndex != prevC) {
                    try {
                        Tile tile = handler.getTileset().get(handler.getActiveTileLayer()[i][j]);
                        int xSize = tile.getWidth() - Math.max(0, i + tile.getWidth() - MapGrid.cols);
                        int ySize = tile.getHeight() - Math.max(0, j + tile.getHeight() - MapGrid.rows);
                        for (int m = 0; m < xSize; m++) {
                            for (int n = 0; n < ySize; n++) {
                                mask[i + m][j + n] = false;
                            }
                        }
                    } catch (Exception ex) {
                        mask[i][j] = false;
                    }
                }
            }
        }

        Utils.floodFillMatrix(tileLayers[handler.getActiveLayerIndex()], mask, x, y, value, tileWidth, tileHeight);
    }

    public void floodFillHeightGrid(int x, int y, int value) {
        Utils.floodFillMatrix(heightLayers[handler.getActiveLayerIndex()], x, y, value);
    }

    public int[][] cloneLayer(int[][] layer) {
        int[][] copy = new int[cols][rows];
        for (int i = 0; i < layer.length; i++) {
            System.arraycopy(layer[i], 0, copy[i], 0, layer[i].length);
        }
        return copy;
    }

    public int[][] cloneTileLayer(int index) {
        return cloneLayer(tileLayers[index]);
    }

    public int[][] cloneHeightLayer(int index) {
        return cloneLayer(heightLayers[index]);
    }

    public void setTileLayer(int index, int[][] tileLayer) {
        this.tileLayers[index] = tileLayer;
    }

    public void setHeightLayer(int index, int[][] heightLayer) {
        this.heightLayers[index] = heightLayer;
    }

    public boolean isEmpty() {
        for (int[][] tileLayer : tileLayers) {
            for (int[] ints : tileLayer) {
                for (int anInt : ints) {
                    if (anInt != -1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void updateAllMapLayers(boolean realTimePostProcessing) {
        for (int i = 0; i < mapLayersGL.length; i++) {
            updateMapLayerGL(i, realTimePostProcessing);
        }
    }

    public void updateMapLayerGL(int layerIndex, boolean realTimePostProcessing) {
        this.mapLayersGL[layerIndex] = new MapLayerGL(
                tileLayers[layerIndex], heightLayers[layerIndex],
                handler.getTileset(),
                realTimePostProcessing, handler.getGame().getMaxTileableSize());
    }

    public void addTileIndicesUsed(HashSet<Integer> indices) {
        for (int[][] tileLayer : tileLayers) {
            for (int[] ints : tileLayer) {
                for (int anInt : ints) {
                    if (anInt != -1) {
                        indices.add(anInt);
                    }
                }
            }
        }
    }

    public HashSet<Integer> getTileIndicesUsed() {
        HashSet<Integer> indices = new HashSet<>();
        addTileIndicesUsed(indices);
        return indices;
    }

    public int getNumTriangles() {
        int numTriangles = 0;
        for (MapLayerGL mapLayerGL : mapLayersGL) {
            if (mapLayerGL != null) {
                numTriangles += mapLayerGL.getNumTriangles();
            }
        }
        return numTriangles;
    }

    public int getNumQuads() {
        int numQuads = 0;
        for (MapLayerGL mapLayerGL : mapLayersGL) {
            if (mapLayerGL != null) {
                numQuads += mapLayerGL.getNumQuads();
            }
        }
        return numQuads;
    }

    public int getNumPolygons() {
        return getNumTriangles() + getNumQuads();
    }

    public int getNumMaterials() {
        int numMaterials = 0;
        for (MapLayerGL mapLayerGL : mapLayersGL) {
            if (mapLayerGL != null) {
                numMaterials += mapLayerGL.getNumGeometryGL();
            }
        }
        return numMaterials;
    }

    public void applyLookupTable(int[] tileIndices) {
        for (int i = 0; i < tileLayers.length; i++) {
            for (int j = 0; j < tileLayers[i].length; j++) {
                for (int k = 0; k < tileLayers[i][j].length; k++) {
                    try {
                        tileLayers[i][j][k] = tileIndices[tileLayers[i][j][k]];
                    } catch (Exception ex) {
                        tileLayers[i][j][k] = -1;
                    }
                }
            }
        }
    }

}
