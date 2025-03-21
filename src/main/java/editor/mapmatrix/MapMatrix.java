
package editor.mapmatrix;

import com.jogamp.common.nio.Buffers;
import editor.buildingeditor2.buildfile.BuildFile;
import editor.game.Game;
import editor.grid.MapGrid;
import editor.handler.MapData;
import editor.handler.MapEditorHandler;
import formats.backsound.BackSound;
import formats.bdhc.*;
import formats.bdhcam.Bdhcam;
import formats.bdhcam.BdhcamLoader;
import formats.bdhcam.BdhcamWriter;
import formats.collisions.Collisions;
import formats.obj.ObjWriter;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import tileset.TextureNotFoundException;
import tileset.Tile;
import tileset.Tileset;
import tileset.TilesetIO;
import utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Trifindo
 */
@Log4j2
@SuppressWarnings({"SpellCheckingInspection", "unused", "DuplicatedCode"})
public class MapMatrix {

    private static final int expectedMaxNumMaps = 25;

    private static final String mapTag = "mapstart";
    private static final String areaIndexTag = "areaindex";
    private static final String mapEndTag = "mapend";
    private static final String gameIndexTag = "gameindex";
    private static final String tileGridTag = "tilegrid";
    private static final String heightGridTag = "heightgrid";
    private static final String tilesetTag = "tileset";
    private static final String bdhcTag = "bdhc";

    private final MapEditorHandler        handler;
    @Getter
    private       HashMap<Point, MapData> matrix; //Key is map coords

    //Border maps
    @Getter
    private HashSet<Point> borderMaps;

    //Contours
    @Getter
    private HashMap<Integer, ArrayList<Point>> contourPoints;
    private HashMap<Integer, FloatBuffer> contourPointsBuffer;
    @Getter
    private HashMap<Integer, Color>       areaColors;

    public String filePath;
    public String tilesetFilePath;
    public static final String fileExtension = "pdsmap";

    public MapMatrix(MapEditorHandler handler) {
        this.handler = handler;

        handler.clearCopyLayer();

        filePath = "";
        tilesetFilePath = "";

        matrix = new HashMap<>(expectedMaxNumMaps);
        matrix.put(new Point(0, 0), new MapData(handler));

        borderMaps = new HashSet<>();
        contourPoints = new HashMap<>();
        contourPointsBuffer = new HashMap<>();
        areaColors = new HashMap<>();

        updateBordersData();
    }

    public void updateBordersData() {
        updateBorderMaps();

        final HashSet<Integer> areaIndices = getAreaIndices();
        updateContourPoints(areaIndices);

        updateAreaColors(areaIndices);
    }

    public void saveGridsToFile(String path) throws FileNotFoundException {
        if (!path.endsWith("." + fileExtension)) {
            path = path.concat("." + fileExtension);
        }


        removeUnusedMaps();
        removeAllUnusedMapFiles();

        PrintWriter out = new PrintWriter(path);

        out.println(gameIndexTag);
        out.println(handler.getGameIndex());

        out.println(tilesetTag);
        String filename = Utils.removeExtensionFromPath(new File(path).getName());
        out.println(filename + "." + Tileset.fileExtension);

        Point minCoords = getMinCoords();
        for (HashMap.Entry<Point, MapData> map : matrix.entrySet()) {
            out.println(mapTag);
            out.println((map.getKey().x - minCoords.x) + " " + (map.getKey().y - minCoords.y));

            out.println(areaIndexTag);
            out.println(map.getValue().getAreaIndex());

            for (int[][] tLayer : map.getValue().getGrid().tileLayers) {
                out.println(tileGridTag);
                MapGrid.printMatrixInFile(out, tLayer); //Todo change this
            }

            for (int[][] hLayer : map.getValue().getGrid().heightLayers) {
                out.println(heightGridTag);
                MapGrid.printMatrixInFile(out, hLayer); //Todo change this
            }
            out.println(mapEndTag);
        }

        out.close();
    }

    public void loadGridsFromFile(String path) throws Exception {
        filePath = "";
        tilesetFilePath = "";

        matrix = new HashMap<>(expectedMaxNumMaps);

        borderMaps = new HashSet<>();
        updateBordersData();

        InputStream input = new FileInputStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(input));

        int numMapsRead = 0;
        int numTileLayersRead = 0;
        int numHeightLayersRead = 0;

        Point currentMapCoords = new Point(0, 0);
        MapGrid currentGrid = new MapGrid(handler);
        int currentAreaIndex = 0;
        String line;
        while ((line = br.readLine()) != null) {
            if (line.startsWith(gameIndexTag)) {
                handler.setGameIndex(Integer.parseInt(br.readLine()));
            } else if (line.startsWith(tilesetTag)) {
                String folderPath = new File(path).getParent();
                tilesetFilePath = folderPath + File.separator + br.readLine();
                System.out.println("Tileset path: " + tilesetFilePath);
            } else if (line.startsWith(mapTag)) {
                String[] splittedLine = br.readLine().split(" ");
                int x = Integer.parseInt(splittedLine[0]);
                int y = Integer.parseInt(splittedLine[1]);
                currentMapCoords = new Point(x, y);
                currentGrid = new MapGrid(handler);
            } else if (line.startsWith(areaIndexTag)) {
                currentAreaIndex = Integer.parseInt(br.readLine());
            } else if (line.startsWith(tileGridTag)) {
                MapGrid.loadMatrixFromFile(br, currentGrid.tileLayers[numTileLayersRead]);
                numTileLayersRead++;
            } else if (line.startsWith(heightGridTag)) {
                MapGrid.loadMatrixFromFile(br, currentGrid.heightLayers[numHeightLayersRead]);
                numHeightLayersRead++;
            } else if (line.startsWith(mapEndTag)) {
                MapData mapData = new MapData(handler);
                mapData.setGrid(currentGrid);
                mapData.setAreaIndex(currentAreaIndex);
                matrix.put(currentMapCoords, mapData);
                numMapsRead++;
                numTileLayersRead = 0;
                numHeightLayersRead = 0;
            }
        }
        if (numMapsRead == 0) {
            MapData mapData = new MapData(handler);
            mapData.setGrid(currentGrid);
            matrix.put(new Point(0, 0), mapData);
        }

        br.close();
        input.close();
    }

    public static HashMap<Point, MapData> getGridsFromFile(String path, MapEditorHandler handler) throws Exception {
        HashMap<Point, MapData> matrix = new HashMap<>(expectedMaxNumMaps);

        InputStream input = new FileInputStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(input));

        String tilesetFilePath;

        int numMapsRead = 0;
        int numTileLayersRead = 0;
        int numHeightLayersRead = 0;

        Point currentMapCoords = new Point(0, 0);
        MapGrid currentGrid = new MapGrid(handler);
        int currentAreaIndex = 0;
        String line;
        while ((line = br.readLine()) != null) {
            //noinspection StatementWithEmptyBody
            if (line.startsWith(gameIndexTag)) {

            } else if (line.startsWith(tilesetTag)) {
                String folderPath = new File(path).getParent();
                br.readLine();

            } else if (line.startsWith(mapTag)) {
                String[] splittedLine = br.readLine().split(" ");
                int x = Integer.parseInt(splittedLine[0]);
                int y = Integer.parseInt(splittedLine[1]);
                currentMapCoords = new Point(x, y);
                currentGrid = new MapGrid(handler);
            } else if (line.startsWith(areaIndexTag)) {
                currentAreaIndex = Integer.parseInt(br.readLine());
            } else if (line.startsWith(tileGridTag)) {
                MapGrid.loadMatrixFromFile(br, currentGrid.tileLayers[numTileLayersRead]);
                numTileLayersRead++;
            } else if (line.startsWith(heightGridTag)) {
                MapGrid.loadMatrixFromFile(br, currentGrid.heightLayers[numHeightLayersRead]);
                numHeightLayersRead++;
            } else if (line.startsWith(mapEndTag)) {
                MapData mapData = new MapData(handler);
                mapData.setGrid(currentGrid);
                mapData.setAreaIndex(currentAreaIndex);
                matrix.put(currentMapCoords, mapData);
                numMapsRead++;
                numTileLayersRead = 0;
                numHeightLayersRead = 0;
            }
        }
        if (numMapsRead == 0) {
            MapData mapData = new MapData(handler);
            mapData.setGrid(currentGrid);
            matrix.put(new Point(0, 0), mapData);
        }

        br.close();
        input.close();

        return matrix;
    }

    public void addMapsFromFile(HashMap<Point, MapData> newMaps, Point offset, String folderPath, String fileName) throws IOException, NullPointerException, TextureNotFoundException {
        fileName = Utils.removeExtensionFromPath(fileName);
        Tileset tileset = TilesetIO.readTilesetFromFile(folderPath + File.separator + fileName + "." + Tileset.fileExtension);

        ArrayList<Tile> newTiles = new ArrayList<>();

        int[] tileLookupTable = new int[tileset.size()];
        for (int i = 0; i < tileset.size(); i++) {
            Tile tile = tileset.get(i);

            int index = handler.getTileset().indexOfTileVisualData(tile);
            if (index == -1) {
                //System.out.println("Tile not found: " + i);
                tileLookupTable[i] = handler.getTileset().size() + newTiles.size();
                newTiles.add(tile);
            } else {
                tileLookupTable[i] = index;
            }
        }

        for (MapData mapData : newMaps.values()) {
            mapData.getGrid().applyLookupTable(tileLookupTable);
        }

        handler.getTileset().importTiles(newTiles);
        //handler.getTileset().removeUnusedTextures();


        loadBDHCsFromFile(newMaps, folderPath, fileName, handler.getGameIndex());
        loadBdhcamsFromFile(newMaps, folderPath, fileName, handler.getGameIndex());
        loadCollisionsFromFile(newMaps, folderPath, fileName, handler.getGameIndex());
        loadBacksoundsFromFile(newMaps, folderPath, fileName, handler.getGameIndex());
        loadBuildingsFromFile(newMaps, folderPath, fileName);


        for (HashMap.Entry<Point, MapData> entry : newMaps.entrySet()) {
            Point coords = new Point(entry.getKey().x + offset.x, entry.getKey().y + offset.y);
            matrix.put(coords, entry.getValue());
        }

    }

    public void saveMapsAsObj(String path, boolean saveTextures, boolean includeVertexColors, float tileUpscale) throws FileNotFoundException {
        removeUnusedMaps();

        String folderPath = new File(path).getParent();
        String fileName = Utils.removeExtensionFromPath(new File(path).getName());

        for (HashMap.Entry<Point, MapData> mapEntry : matrix.entrySet()) {
            String objFilePath = getFilePathWithCoords(matrix, folderPath, fileName, mapEntry.getKey(), "obj");
            mapEntry.getValue().getGrid().saveMapToOBJ(handler.getTileset(), objFilePath, saveTextures, includeVertexColors, tileUpscale);
        }
        //TODO: This method saves textures for each map. Make that textures are exported only once
    }

    public void saveMapsAsObjJoined(String path, boolean saveTextures, boolean includeVertexColors, float tileUpscale) throws FileNotFoundException {
        removeUnusedMaps();

        String folderPath = new File(path).getParent();
        String fileName = Utils.removeExtensionFromPath(new File(path).getName());

        String objFilePath = folderPath + File.separator + fileName + ".obj";

        ObjWriter writer = new ObjWriter(handler.getTileset(), generateGridHashMap(), objFilePath, handler.getGameIndex(),
                saveTextures, includeVertexColors, tileUpscale);
        writer.writeMapObj();

    }

    public void saveBDHCs() throws IOException {
        int game = handler.getGameIndex();
        for (HashMap.Entry<Point, MapData> mapEntry : matrix.entrySet()) {
            String path = getFilePathWithCoords(matrix, new File(filePath).getParent(),
                    new File(filePath).getName(), mapEntry.getKey(), Bdhc.fileExtension);
            if (game == Game.DIAMOND || game == Game.PEARL) {
                BdhcWriterDP.writeBdhc(mapEntry.getValue().getBdhc(), path);
            } else {
                BdhcWriterHGSS.writeBdhc(mapEntry.getValue().getBdhc(), path);
            }
        }
    }

    public void saveBacksounds() throws IOException {
        int game = handler.getGameIndex();
        for (HashMap.Entry<Point, MapData> mapEntry : matrix.entrySet()) {
            String path = getFilePathWithCoords(matrix, new File(filePath).getParent(),
                    new File(filePath).getName(), mapEntry.getKey(), BackSound.fileExtension);
            if (game == Game.HEART_GOLD || game == Game.SOUL_SILVER) {
                mapEntry.getValue().getBacksound().writeToFile(path);
            }
        }
    }

    public void saveBdhcams() throws IOException {
        int game = handler.getGameIndex();
        for (HashMap.Entry<Point, MapData> mapEntry : matrix.entrySet()) {
            String path = getFilePathWithCoords(matrix, new File(filePath).getParent(),
                    new File(filePath).getName(), mapEntry.getKey(), Bdhcam.fileExtension);
            if (game == Game.PLATINUM || game == Game.HEART_GOLD || game == Game.SOUL_SILVER) {
                BdhcamWriter.writeBdhcamToFile(path, mapEntry.getValue().getBdhcam(), mapEntry.getValue().getBdhc(), game);
            }
        }
    }

    public void saveCollisions() throws IOException {
        for (HashMap.Entry<Point, MapData> mapEntry : matrix.entrySet()) {
            String path = getFilePathWithCoords(matrix, new File(filePath).getParent(),
                    new File(filePath).getName(), mapEntry.getKey(), Collisions.fileExtension);
            mapEntry.getValue().getCollisions().saveToFile(path);

            if (Game.isGenV(handler.getGameIndex())) {
                if (mapEntry.getValue().getCollisions2() != null) {
                    String path2 = getFilePathWithCoords(matrix, new File(filePath).getParent(),
                            new File(filePath).getName(), "2", mapEntry.getKey(), Collisions.fileExtension);
                    mapEntry.getValue().getCollisions2().saveToFile(path2);
                }
            }
        }
    }

    public void saveBuildings() throws IOException {
        for (HashMap.Entry<Point, MapData> mapEntry : matrix.entrySet()) {
            String path = getFilePathWithCoords(matrix, new File(filePath).getParent(),
                    new File(filePath).getName(), mapEntry.getKey(), BuildFile.fileExtension);
            mapEntry.getValue().getBuildings().saveToFile(path);
        }
    }


    public void loadBDHCsFromFile(HashMap<Point, MapData> matrix, String folderPath, String mapFileName, int game) {
        if (matrix.size() == 1) {//OLD MAP TYPE
            HashMap.Entry<Point, MapData> mapEntry = matrix.entrySet().iterator().next();
            try {
                String bdhcPath = getFilePathWithCoords(matrix, folderPath, mapFileName, mapEntry.getKey(), Bdhc.fileExtension);
                loadBDHC(bdhcPath, mapEntry.getValue(), game);
            } catch (Exception ex) {
                try {
                    String bdhcPath = getFilePath(folderPath, mapFileName, Bdhc.fileExtension);
                    loadBDHC(bdhcPath, mapEntry.getValue(), game);
                } catch (Exception ex2) {
                    mapEntry.getValue().setBdhc(new Bdhc());
                }
            }
        } else {
            for (HashMap.Entry<Point, MapData> mapEntry : matrix.entrySet()) {
                try {
                    String bdhcPath = getFilePathWithCoords(matrix, folderPath, mapFileName, mapEntry.getKey(), Bdhc.fileExtension);
                    loadBDHC(bdhcPath, mapEntry.getValue(), game);
                } catch (Exception ex) {
                    mapEntry.getValue().setBdhc(new Bdhc());
                }
            }
        }
    }

    public void loadBDHCsFromFile(String folderPath, String mapFileName) {
        loadBDHCsFromFile(matrix, folderPath, mapFileName, handler.getGameIndex());
    }

    public void loadBDHC(String path, MapData mapData, int game) throws IOException {
        if (game == Game.DIAMOND || game == Game.PEARL) {
            mapData.setBdhc(new BdhcLoaderDP().loadBdhcFromFile(path));
        } else {
            mapData.setBdhc(new BdhcLoaderHGSS().loadBdhcFromFile(path));
        }
    }

    public static void loadBdhcamsFromFile(HashMap<Point, MapData> matrix, String folderPath, String mapFileName, int game) {
        for (HashMap.Entry<Point, MapData> mapEntry : matrix.entrySet()) {
            try {
                String bdhcamPath = getFilePathWithCoords(matrix, folderPath, mapFileName, mapEntry.getKey(), Bdhcam.fileExtension);
                loadBdhcam(bdhcamPath, mapEntry.getValue(), game);
            } catch (Exception ex) {
                mapEntry.getValue().setBdhcam(new Bdhcam());

            }
        }
    }

    public static void loadBdhcam(String path, MapData mapData, int game) throws Exception {
        if (game == Game.PLATINUM || game == Game.HEART_GOLD || game == Game.SOUL_SILVER) {
            mapData.setBdhcam(BdhcamLoader.loadBdhcam(path));
        } else {
            mapData.setBdhcam(new Bdhcam());
        }
    }

    public void loadBdhcamsFromFile(String folderPath, String mapFileName) {
        loadBdhcamsFromFile(matrix, folderPath, mapFileName, handler.getGameIndex());
    }

    public static void loadBacksoundsFromFile(HashMap<Point, MapData> matrix, String folderPath, String mapFileName, int game) {
        if (matrix.size() == 1) {//OLD MAP TYPE
            HashMap.Entry<Point, MapData> mapEntry = matrix.entrySet().iterator().next();
            try {
                String backsoundPath = getFilePathWithCoords(matrix, folderPath, mapFileName, mapEntry.getKey(), BackSound.fileExtension);
                loadBacksound(backsoundPath, mapEntry.getValue(), game);
            } catch (Exception ex) {
                try {
                    String backsoundPath = getFilePath(folderPath, mapFileName, BackSound.fileExtension);
                    loadBacksound(backsoundPath, mapEntry.getValue(), game);
                } catch (Exception ex2) {
                    mapEntry.getValue().setBacksound(new BackSound());
                }
            }
        } else {
            for (HashMap.Entry<Point, MapData> mapEntry : matrix.entrySet()) {
                try {
                    String backsoundPath = getFilePathWithCoords(matrix, folderPath, mapFileName, mapEntry.getKey(), BackSound.fileExtension);
                    loadBacksound(backsoundPath, mapEntry.getValue(), game);
                } catch (Exception ex) {
                    mapEntry.getValue().setBacksound(new BackSound());

                }
            }
        }
    }

    public void loadBacksoundsFromFile(String folderPath, String mapFileName) {
        loadBacksoundsFromFile(matrix, folderPath, mapFileName, handler.getGameIndex());
    }

    private static void loadBacksound(String path, MapData mapData, int game) throws Exception {
        if (game == Game.HEART_GOLD || game == Game.SOUL_SILVER) {
            mapData.setBacksound(new BackSound(path));
        } else {
            mapData.setBacksound(new BackSound());
        }
    }

    public static void loadCollisionsFromFile(HashMap<Point, MapData> matrix, String folderPath, String mapFileName, int gameIndex) {
        if (matrix.size() == 1) {//OLD MAP TYPE
            HashMap.Entry<Point, MapData> mapEntry = matrix.entrySet().iterator().next();
            try {
                String collisionsPath = getFilePathWithCoords(matrix, folderPath, mapFileName, mapEntry.getKey(), Collisions.fileExtension);
                mapEntry.getValue().setCollisions(new Collisions(collisionsPath));
            } catch (Exception ex) {
                try {
                    String collisionsPath = getFilePath(folderPath, mapFileName, Collisions.fileExtension);
                    mapEntry.getValue().setCollisions(new Collisions(collisionsPath));
                } catch (Exception ex2) {
                    mapEntry.getValue().setCollisions(new Collisions(gameIndex));
                }
            }
            if (Game.isGenV(gameIndex)) {
                try {
                    String collisionsPath = getFilePathWithCoords(matrix, folderPath, mapFileName, "2",
                            mapEntry.getKey(), Collisions.fileExtension);
                    mapEntry.getValue().setCollisions2(new Collisions(collisionsPath));
                } catch (Exception ex) {
                    mapEntry.getValue().setCollisions2(new Collisions(gameIndex));
                }
            }
        } else {
            for (HashMap.Entry<Point, MapData> mapEntry : matrix.entrySet()) {
                try {
                    String collisionsPath = getFilePathWithCoords(matrix, folderPath, mapFileName, mapEntry.getKey(), Collisions.fileExtension);
                    mapEntry.getValue().setCollisions(new Collisions(collisionsPath));
                } catch (Exception ex) {
                    mapEntry.getValue().setCollisions(new Collisions(gameIndex));
                }
                if (Game.isGenV(gameIndex)) {
                    try {
                        String collisionsPath = getFilePathWithCoords(matrix, folderPath, mapFileName, "2",
                                mapEntry.getKey(), Collisions.fileExtension);
                        mapEntry.getValue().setCollisions2(new Collisions(collisionsPath));
                    } catch (Exception ex) {
                        mapEntry.getValue().setCollisions2(new Collisions(gameIndex));
                    }
                }
            }
        }
    }

    public void loadCollisionsFromFile(String folderPath, String mapFileName) {
        loadCollisionsFromFile(matrix, folderPath, mapFileName, handler.getGameIndex());
    }

    public static void loadBuildingsFromFile(HashMap<Point, MapData> matrix, String folderPath, String mapFileName) {
        if (matrix.size() == 1) {//OLD MAP TYPE
            HashMap.Entry<Point, MapData> mapEntry = matrix.entrySet().iterator().next();
            try {
                String buildingsPath = getFilePathWithCoords(matrix, folderPath, mapFileName, mapEntry.getKey(), BuildFile.fileExtension);
                mapEntry.getValue().setBuildings(new BuildFile(buildingsPath));
                //handler.setBuildings(new BuildFile(buildingsPath));
            } catch (Exception ex) {
                try {
                    String buildingsPath = getFilePath(folderPath, mapFileName, BuildFile.fileExtension);
                    mapEntry.getValue().setBuildings(new BuildFile(buildingsPath));
                    //handler.setBuildings(new BuildFile(buildingsPath));
                } catch (Exception ex2) {
                    mapEntry.getValue().setBuildings(new BuildFile());
                    //handler.setBuildings(new BuildFile());
                }
            }
        } else {
            for (HashMap.Entry<Point, MapData> mapEntry : matrix.entrySet()) {
                try {
                    String buildingsPath = getFilePathWithCoords(matrix, folderPath, mapFileName, mapEntry.getKey(), BuildFile.fileExtension);
                    mapEntry.getValue().setBuildings(new BuildFile(buildingsPath));
                    //handler.setBuildings(new BuildFile(buildingsPath));
                } catch (Exception ex) {
                    mapEntry.getValue().setBuildings(new BuildFile());
                    //handler.setBuildings(new BuildFile());
                }
            }
        }
    }

    public void loadBuildingsFromFile(String folderPath, String mapFileName) {
        loadBuildingsFromFile(matrix, folderPath, mapFileName);
    }

    public static String getFilePathWithCoords(HashMap<Point, MapData> matrix, String folderPath, String mapFileName, Point mapCoords, String extensionName) {
        return getFilePathWithCoords(matrix, folderPath, mapFileName, "", mapCoords, extensionName);
    }

    public static String getFilePathWithCoords(HashMap<Point, MapData> matrix, String folderPath, String mapFileName, String nameEnd, Point mapCoords, String extensionName) {
        String filename = getMapName(matrix, mapFileName, nameEnd, mapCoords);
        return folderPath + File.separator + filename + "." + extensionName;
    }

    public static String getMapName(HashMap<Point, MapData> matrix, String mapFileName, String nameEnd, Point mapCoords){
        String mapName = Utils.removeExtensionFromPath(mapFileName) + nameEnd;
        Point minCoords = getMinCoords(matrix);
        mapName += "_" + String.format("%02d", mapCoords.x - minCoords.x) + "_" + String.format("%02d", mapCoords.y - minCoords.y);
        return mapName;
    }

    public String getMapName(Point mapCoords){
        return getMapName(this.matrix, new File(filePath).getName(),"",mapCoords);
    }

    private static String getFilePath(String folderPath, String mapFileName, String extensionName) {
        String filename = Utils.removeExtensionFromPath(mapFileName);
        return folderPath + File.separator + filename + "." + extensionName;
    }

    public MapData getMapAndCreate(Point mapCoords) {
        MapData mapData = matrix.get(mapCoords);
        if (mapData == null) {
            System.out.println("NEW MAP at " + mapCoords.x + " " + mapCoords.y);
            mapData = new MapData(handler);
            matrix.put(mapCoords, mapData);

            updateBordersData();
        }
        return mapData;
    }

    public MapData getMap(Point mapCoords) {
        return matrix.get(mapCoords);
    }

    public void updateBorderMaps() {
        Set<Point> mapCoords = matrix.keySet();
        this.borderMaps = new HashSet<>(mapCoords.size() * 8); //Approximation

        //Set borders
        for (Point map : mapCoords) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    borderMaps.add(new Point(map.x + i, map.y + j));
                }
            }
        }

        //Clear interior
        for (Point map : mapCoords) {
            borderMaps.remove(map);
        }

        //System.out.println("Border maps updated! " + borderMaps.size());
    }

    public void updateContourPoints(HashSet<Integer> areaIndices) {
        contourPoints = generateAllContourPoints(new Point(0, 0), areaIndices);
        contourPointsBuffer = generateAllContourPointsGL(contourPoints);
    }

    public HashMap<Integer, Color> generateAreaColors(HashSet<Integer> areaIndices) {
        HashMap<Integer, Color> colors = new HashMap<>(areaIndices.size());
        for (Integer areaIndex : areaIndices) {
            colors.put(areaIndex, new Color(Color.HSBtoRGB((areaIndex * 45.0f + 155f) / 255f, 0.8f, 1.0f)));
        }
        return colors;
    }

    public void updateAreaColors(HashSet<Integer> areaIndices) {
        areaColors = generateAreaColors(areaIndices);
    }

    public MapData getMapAndCreate(int x, int y) {
        return getMapAndCreate(new Point(x, y));
    }

    public Point getMinCoords() {
        return getMinCoords(matrix);
    }

    public static Point getMinCoords(HashMap<Point, MapData> matrix) {
        if (!matrix.isEmpty()) {
            Point min = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
            Set<Point> coords = matrix.keySet();
            for (Point p : coords) {
                if (p.x < min.x) {
                    min.x = p.x;
                }
                if (p.y < min.y) {
                    min.y = p.y;
                }
            }
            return min;
        } else {
            return new Point(0, 0);
        }
    }

    public Point getMaxCoords() {
        return getMaxCoords(matrix);
    }

    public static Point getMaxCoords(HashMap<Point, MapData> matrix) {
        if (!matrix.isEmpty()) {
            Point max = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
            Set<Point> coords = matrix.keySet();
            for (Point p : coords) {
                if (p.x > max.x) {
                    max.x = p.x;
                }
                if (p.y > max.y) {
                    max.y = p.y;
                }
            }
            return max;
        } else {
            return new Point(0, 0);
        }
    }

    public Dimension getMatrixSize() {
        return getMatrixSize(matrix);
    }

    public static Dimension getMatrixSize(HashMap<Point, MapData> matrix) {
        Point min = getMinCoords(matrix);
        Point max = getMaxCoords(matrix);
        return new Dimension(max.x - min.x + 1, max.y - min.y + 1);
    }

    public void removeUnusedMaps() {
        matrix.entrySet().removeIf(e -> e.getValue().isUnused() && matrix.size() > 1);
        updateBordersData();
    }

    public void updateAllLayersGL() {
        for (MapData mapData : matrix.values()) {
            mapData.getGrid().updateAllMapLayers(handler.useRealTimePostProcessing());
        }
    }

    public void removeAllUnusedMapFiles() {
        try {
            String folderPath = new File(filePath).getParent();

            removeUnusedMapFiles(folderPath, "obj");
            removeUnusedMapFiles(folderPath, "mtl");
            removeUnusedMapFiles(folderPath, "imd");
            removeUnusedMapFiles(folderPath, "nsbmd");
            removeUnusedMapFiles(folderPath, Bdhc.fileExtension);
            removeUnusedMapFiles(folderPath, Collisions.fileExtension);
            removeUnusedMapFiles(folderPath, BuildFile.fileExtension);
            removeUnusedMapFiles(folderPath, BackSound.fileExtension);
        } catch (Exception ex) {
            log.warn(ex);
        }
    }

    private void removeUnusedMapFiles(String folderPath, String fileExtension) {
        Point minCoords = getMinCoords();

        if (folderPath != null) {
            File folder = new File(folderPath);
            File[] filesToRemove = folder.listFiles((dir, name) -> canRemoveMapFile(name, fileExtension, minCoords));

            if (filesToRemove != null) {
                for (File file : filesToRemove) {
                    try {
                        Files.deleteIfExists(file.toPath());
                    } catch (Exception ex) {
                        log.warn(ex);
                    }
                }
            }
        }
    }

    public void clearAllCollisions() {
        for (MapData mapData : matrix.values()) {
            mapData.setCollisions(new Collisions(handler.getGameIndex()));
            mapData.setCollisions2(new Collisions(handler.getGameIndex()));
        }
    }

    public ArrayList<Point> generateContourPoints(Point min) {
        ArrayList<Point> contourPoints = new ArrayList<>(matrix.size() * 3);//Approximation
        for (Point p : matrix.keySet()) {
            if (!matrix.containsKey(new Point(p.x - 1, p.y))) {
                contourPoints.add(new Point(p.x - min.x, p.y - min.y));
                contourPoints.add(new Point(p.x - min.x, p.y + 1 - min.y));
            }
            if (!matrix.containsKey(new Point(p.x + 1, p.y))) {
                contourPoints.add(new Point(p.x + 1 - min.x, p.y - min.y));
                contourPoints.add(new Point(p.x + 1 - min.x, p.y + 1 - min.y));
            }
            if (!matrix.containsKey(new Point(p.x, p.y - 1))) {
                contourPoints.add(new Point(p.x - min.x, p.y - min.y));
                contourPoints.add(new Point(p.x + 1 - min.x, p.y - min.y));
            }
            if (!matrix.containsKey(new Point(p.x, p.y + 1))) {
                contourPoints.add(new Point(p.x - min.x, p.y + 1 - min.y));
                contourPoints.add(new Point(p.x + 1 - min.x, p.y + 1 - min.y));
            }
        }
        return contourPoints;
    }

    public HashSet<Integer> getAreaIndices() {
        HashSet<Integer> areaIndices = new HashSet<>();
        for (MapData map : matrix.values()) {
            areaIndices.add(map.getAreaIndex());
        }
        return areaIndices;
    }

    public int getNumAreas() {
        return getAreaIndices().size();
    }

    public HashMap<Integer, ArrayList<Point>> generateAllContourPoints(Point min, HashSet<Integer> areaIndices) {

        HashMap<Integer, ArrayList<Point>> allContours = new HashMap<>(areaIndices.size());
        for (Integer areaIndex : areaIndices) {
            allContours.put(areaIndex, new ArrayList<>());
        }

        for (HashMap.Entry<Point, MapData> mapEntry : matrix.entrySet()) {
            Point p = mapEntry.getKey();
            MapData map = mapEntry.getValue();

            ArrayList<Point> contourPoints = allContours.get(map.getAreaIndex());
            if (canAddContour(p, new Point(p.x - 1, p.y))) {
                contourPoints.add(new Point(p.x - min.x, p.y - min.y));
                contourPoints.add(new Point(p.x - min.x, p.y + 1 - min.y));
            }
            if (canAddContour(p, new Point(p.x + 1, p.y))) {
                contourPoints.add(new Point(p.x + 1 - min.x, p.y - min.y));
                contourPoints.add(new Point(p.x + 1 - min.x, p.y + 1 - min.y));
            }
            if (canAddContour(p, new Point(p.x, p.y - 1))) {
                contourPoints.add(new Point(p.x - min.x, p.y - min.y));
                contourPoints.add(new Point(p.x + 1 - min.x, p.y - min.y));
            }
            if (canAddContour(p, new Point(p.x, p.y + 1))) {
                contourPoints.add(new Point(p.x - min.x, p.y + 1 - min.y));
                contourPoints.add(new Point(p.x + 1 - min.x, p.y + 1 - min.y));
            }

        }
        return allContours;
    }

    private boolean canAddContour(Point p, Point nearP) {
        if (!matrix.containsKey(nearP)) {
            return true;
        } else return matrix.get(p).getAreaIndex() != matrix.get(nearP).getAreaIndex();
    }

    public float[] generateContourPointsGL(ArrayList<Point> points) {
        float[] contourPointsGL = new float[points.size() * 3];
        int i = 0;
        for (Point p : points) {
            contourPointsGL[i] = p.x;
            contourPointsGL[i + 1] = p.y;
            contourPointsGL[i + 2] = 0.0f;
            i += 3;
        }
        return contourPointsGL;
    }

    public HashMap<Integer, FloatBuffer> generateAllContourPointsGL(HashMap<Integer, ArrayList<Point>> contourPoints) {
        HashMap<Integer, FloatBuffer> allContourPoints = new HashMap<>(contourPoints.size());
        for (HashMap.Entry<Integer, ArrayList<Point>> entry : contourPoints.entrySet()) {
            ArrayList<Point> points = entry.getValue();
            float[] contourPointsGL = new float[points.size() * 3];
            int i = 0;
            for (Point p : points) {
                contourPointsGL[i] = p.x;
                contourPointsGL[i + 1] = p.y;
                contourPointsGL[i + 2] = 0.0f;
                i += 3;
            }
            allContourPoints.put(entry.getKey(), Buffers.newDirectFloatBuffer(contourPointsGL));
        }

        return allContourPoints;
    }

    private boolean canRemoveMapFile(String fileName, String fileExtension, Point minCoords) {
        try {
            //System.out.println("MAP FILE: " + fileName);
            return fileName.endsWith("." + fileExtension)
                    && nameHasMapCoords(fileName)
                    && !isMapFileUsed(fileName, minCoords);
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean isMapFileUsed(String fileName, Point minCoords) {
        Point mapCoords = geMapCoordsFromName(fileName);

        mapCoords.x += minCoords.x;
        mapCoords.y += minCoords.y;

        //System.out.println(mapCoords.x + " " + mapCoords.y + " " + fileName + " USED: " + matrix.keySet().contains(mapCoords));
        return matrix.containsKey(mapCoords);
    }

    private Point geMapCoordsFromName(String fileName) {
        String name = Utils.removeExtensionFromPath(fileName);
        String[] splittedName = name.split("_");
        return new Point(Integer.parseInt(splittedName[splittedName.length - 2]),
                Integer.parseInt(splittedName[splittedName.length - 1]));
    }

    private boolean nameHasMapCoords(String fileName) {
        //System.out.println(fileName);
        String name = Utils.removeExtensionFromPath(fileName);
        try {
            String[] splittedName = name.split("_");
            return canParseInteger(splittedName[splittedName.length - 1])
                    && canParseInteger(splittedName[splittedName.length - 2]);
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean canParseInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public HashMap<Integer, FloatBuffer> getContourPointsGL() {
        return contourPointsBuffer;
    }

    public void moveMap(Point src, Point dst) {
        if (matrix.containsKey(dst)) {
            MapData copy = matrix.get(dst);
            matrix.put(dst, matrix.get(src));
            matrix.put(src, copy);
        } else {
            matrix.put(dst, matrix.get(src));
            matrix.remove(src);
        }
    }

    public HashMap<Point, MapGrid> generateGridHashMap() {
        HashMap<Point, MapGrid> map = new HashMap<>(matrix.size());
        for (HashMap.Entry<Point, MapData> mapEntry : matrix.entrySet()) {
            map.put(mapEntry.getKey(), mapEntry.getValue().getGrid());
        }
        return map;
    }

    public BufferedImage getMapMatrixImage() {

        try {
            Point min = handler.getMapMatrix().getMinCoords();
            Dimension size = handler.getMapMatrix().getMatrixSize();

            BufferedImage mapsImg = new BufferedImage(
                    size.width * MapData.mapThumbnailSize,
                    size.height * MapData.mapThumbnailSize,
                    BufferedImage.TYPE_INT_ARGB);

            Graphics g = mapsImg.getGraphics();

            for (HashMap.Entry<Point, MapData> map : handler.getMapMatrix().getMatrix().entrySet()) {
                Point p = map.getKey();
                int x = (p.x - min.x) * MapData.mapThumbnailSize;
                int y = (p.y - min.y) * MapData.mapThumbnailSize;
                g.drawImage(map.getValue().getMapThumbnail(), x, y, null);

                try {
                    Color c = handler.getMapMatrix().getAreaColors().get(map.getValue().getAreaIndex());
                    g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 50));
                    g.fillRect(x, y, MapData.mapThumbnailSize - 1, MapData.mapThumbnailSize - 1);
                } catch (Exception ex) {
                    log.warn(ex);
                }

                g.setColor(Color.white);
                g.drawRect(x, y, MapData.mapThumbnailSize - 1, MapData.mapThumbnailSize - 1);
            }

            //ArrayList<Point> contourPoints = handler.getMapMatrix().generateContourPoints(handler.getMapMatrix().getMinCoords());
            HashMap<Integer, ArrayList<Point>> allContourPoints = handler.getMapMatrix().getContourPoints();
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(8));
            for (HashMap.Entry<Integer, ArrayList<Point>> entry : allContourPoints.entrySet()) {
                ArrayList<Point> contourPoints = entry.getValue();
                for (int i = 0; i < contourPoints.size(); i += 2) {
                    try {
                        g.setColor(handler.getMapMatrix().getAreaColors().get(entry.getKey()));
                    } catch (Exception ex) {
                        g.setColor(Color.blue);
                    }
                    Point p1 = contourPoints.get(i);
                    Point p2 = contourPoints.get(i + 1);
                    g.drawLine(
                            (p1.x - min.x) * MapData.mapThumbnailSize,
                            (p1.y - min.y) * MapData.mapThumbnailSize,
                            (p2.x - min.x) * MapData.mapThumbnailSize,
                            (p2.y - min.y) * MapData.mapThumbnailSize);
                }
            }
            return mapsImg;
        } catch (Exception ex) {
            return null;
        }

    }

}
