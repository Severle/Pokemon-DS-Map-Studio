
package editor.buildingeditor2;

import editor.buildingeditor2.animations.BuildAnimations;
import editor.buildingeditor2.animations.BuildAnimeListHGSS;
import editor.buildingeditor2.animations.MapAnimations;
import editor.buildingeditor2.areabuild.AreaBuild;
import editor.buildingeditor2.areabuild.AreaBuildList;
import editor.buildingeditor2.areadata.AreaDataListHGSS;
import editor.buildingeditor2.buildmodel.BuildModelList;
import editor.buildingeditor2.buildmodel.BuildModelMatshp;
import editor.buildingeditor2.tileset.BuildTileset;
import editor.buildingeditor2.tileset.BuildTilesetList;
import editor.game.GameFileSystemHGSS;
import formats.narc2.Narc;
import formats.narc2.NarcIO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import nitroreader.nsbmd.NSBMD;
import nitroreader.nsbmd.sbccommands.MAT;
import nitroreader.nsbmd.sbccommands.SBCCommand;
import nitroreader.nsbmd.sbccommands.SHP;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Trifindo
 */
@Log4j2
@SuppressWarnings({"SpellCheckingInspection", "unused", "DuplicatedCode"})
public class BuildHandlerHGSS {

    //private MapEditorHandler handler;
    @Getter
    @Setter
    private       String             gameFolderPath;
    private final GameFileSystemHGSS gameFileSystem;

    @Setter
    private       int buildBlockIndexSelected = 0;
    private final int numBuildBlocks          = 2;

    private final BuildModelList[]     buildModelList      = new BuildModelList[numBuildBlocks]; //2
    private final BuildModelMatshp[]   buildModelMatshp    = new BuildModelMatshp[numBuildBlocks]; //2
    private final BuildAnimeListHGSS[] buildModelAnimeList = new BuildAnimeListHGSS[numBuildBlocks]; //2
    @Getter
    private BuildAnimations  buildModelAnims;
    @Getter
    private AreaDataListHGSS areaDataList;
    @Getter
    private BuildTilesetList buildTilesetList;
    @Getter
    private AreaBuildList areaBuildList;
    @Getter
    private MapAnimations mapAnimations;


    public BuildHandlerHGSS(String gameFolderPath) {
        this.gameFolderPath = gameFolderPath;
        this.gameFileSystem = new GameFileSystemHGSS();
    }

    public boolean areAllFilesLoaded() {
        return buildModelList[0] != null
                && buildModelList[1] != null
                && buildModelMatshp[0] != null
                && buildModelMatshp[1] != null
                && buildModelAnimeList[0] != null
                && buildModelAnimeList[1] != null
                && buildModelAnims != null
                && areaDataList != null
                && buildTilesetList != null
                && areaBuildList != null
                && mapAnimations != null;
    }

    public boolean areAllFilesAvailable() {
        //TODO change this
        return (isGameFileAvailable(gameFileSystem.getBuildModelPath())
                && isGameFileAvailable(gameFileSystem.getBuildModelRoomPath())
                && isGameFileAvailable(gameFileSystem.getBuildModelMatshpPath())
                && isGameFileAvailable(gameFileSystem.getBuildModelRoomMatshpPath())
                && isGameFileAvailable(gameFileSystem.getBuildModelAnimeListPath())
                && isGameFileAvailable(gameFileSystem.getBuildModelRoomAnimeListPath())
                && isGameFileAvailable(gameFileSystem.getBuildModelAnimePath())
                && isGameFileAvailable(gameFileSystem.getAreaDataPath())
                && isGameFileAvailable(gameFileSystem.getAreaBuildTilesetPath())
                && isGameFileAvailable(gameFileSystem.getAreaBuildModelPath())
                && isGameFileAvailable(gameFileSystem.getMapAnimationsPath()));
    }

    public void loadAllFiles() throws Exception {
        try {
            Narc buildModelNarc = NarcIO.loadNarc(getGameFilePath(gameFileSystem.getBuildModelPath()));
            buildModelList[0] = new BuildModelList(buildModelNarc);
            Narc buildModelRoomNarc = NarcIO.loadNarc(getGameFilePath(gameFileSystem.getBuildModelRoomPath()));
            buildModelList[1] = new BuildModelList(buildModelRoomNarc);

            buildModelMatshp[0] = new BuildModelMatshp(getGameFilePath(gameFileSystem.getBuildModelMatshpPath()));
            buildModelMatshp[1] = new BuildModelMatshp(getGameFilePath(gameFileSystem.getBuildModelRoomMatshpPath()));

            Narc buildModelAnimeListNarc = NarcIO.loadNarc(getGameFilePath(gameFileSystem.getBuildModelAnimeListPath()));
            buildModelAnimeList[0] = new BuildAnimeListHGSS(buildModelAnimeListNarc);
            Narc buildModelRoomAnimeListNarc = NarcIO.loadNarc(getGameFilePath(gameFileSystem.getBuildModelRoomAnimeListPath()));
            buildModelAnimeList[1] = new BuildAnimeListHGSS(buildModelRoomAnimeListNarc);

            Narc buildModelAnimsNarc = NarcIO.loadNarc(getGameFilePath(gameFileSystem.getBuildModelAnimePath()));
            buildModelAnims = new BuildAnimations(buildModelAnimsNarc);

            Narc areaDataListNarc = NarcIO.loadNarc(getGameFilePath(gameFileSystem.getAreaDataPath()));
            areaDataList = new AreaDataListHGSS(areaDataListNarc);

            Narc buildTilesetListNarc = NarcIO.loadNarc(getGameFilePath(gameFileSystem.getAreaBuildTilesetPath()));
            buildTilesetList = new BuildTilesetList(buildTilesetListNarc);

            Narc areaBuildListNarc = NarcIO.loadNarc(getGameFilePath(gameFileSystem.getAreaBuildModelPath()));
            areaBuildList = new AreaBuildList(areaBuildListNarc);

            Narc mapAnimsNarc = NarcIO.loadNarc(getGameFilePath(gameFileSystem.getMapAnimationsPath()));
            mapAnimations = new MapAnimations(mapAnimsNarc);


        } catch (Exception ex) {
            buildModelList[0] = null;
            buildModelList[1] = null;
            buildModelMatshp[0] = null;
            buildModelMatshp[1] = null;
            buildModelAnimeList[0] = null;
            buildModelAnimeList[1] = null;
            buildModelAnims = null;
            areaDataList = null;
            buildTilesetList = null;
            areaBuildList = null;
            mapAnimations = null;
            throw ex;
        }
    }

    public void saveAllFiles() throws Exception {
        try {
            NarcIO.writeNarc(buildModelList[0].toNarc(), getGameFilePath(gameFileSystem.getBuildModelPath()));
            NarcIO.writeNarc(buildModelList[1].toNarc(), getGameFilePath(gameFileSystem.getBuildModelRoomPath()));
            buildModelMatshp[0].saveToFile(getGameFilePath(gameFileSystem.getBuildModelMatshpPath()));
            buildModelMatshp[1].saveToFile(getGameFilePath(gameFileSystem.getBuildModelRoomMatshpPath()));
            NarcIO.writeNarc(buildModelAnimeList[0].toNarc(), getGameFilePath(gameFileSystem.getBuildModelAnimeListPath()));
            NarcIO.writeNarc(buildModelAnimeList[1].toNarc(), getGameFilePath(gameFileSystem.getBuildModelRoomAnimeListPath()));
            NarcIO.writeNarc(buildModelAnims.toNarc(), getGameFilePath(gameFileSystem.getBuildModelAnimePath()));
            NarcIO.writeNarc(areaDataList.toNarc(), getGameFilePath(gameFileSystem.getAreaDataPath()));
            NarcIO.writeNarc(areaBuildList.toNarc(), getGameFilePath(gameFileSystem.getAreaBuildModelPath()));
            NarcIO.writeNarc(buildTilesetList.toNarc(), getGameFilePath(gameFileSystem.getAreaBuildTilesetPath()));
            NarcIO.writeNarc(mapAnimations.toNarc(), getGameFilePath(gameFileSystem.getMapAnimationsPath()));
        } catch (Exception ex) {
            log.error(ex);
            throw ex;
        }
    }

    private void generateMissingFiles() {

    }

    public void addBuilding(String path) throws IOException {
        try {
            getBuildModelList().addBuildingModel(path);

            try {
                byte[] data = getBuildModelList().getModelsData().get(getBuildModelList().getSize() - 1);
                ArrayList<Integer> materials = BuildHandlerHGSS.getMaterialOrder(data);
                getBuildModelMatshp().addBuildingMaterials(materials);
            } catch (Exception ex) {
                getBuildModelMatshp().addBuildingMaterials(new ArrayList<>());
            }

            getBuildModelAnimeList().addBuildingAnimation(new ArrayList<>(), (byte) -1);

        } catch (Exception ex) {
            throw new IOException();
        }
    }

    public void replaceBuilding(int index, String path) throws IOException {
        try {
            getBuildModelList().replaceBuildingModel(index, path);
            getBuildModelMatshp().replaceBuildingMaterials(index, new ArrayList<>());
            getBuildModelAnimeList().replaceBuildingAnimationIDs(index, new ArrayList<>());
        } catch (Exception ex) {
            throw new IOException();
        }
    }

    public void saveBuilding(int index, String path) throws IOException {
        getBuildModelList().saveBuildingModel(index, path);
    }

    public void saveBuildingTileset(int index, String path) throws IOException {
        buildTilesetList.saveTileset(index, path);
    }

    public void removeBuilding(int index) {
        getBuildModelList().removeBuildingModel(index);
        getBuildModelMatshp().removeBuildingMaterials(index);
        getBuildModelAnimeList().removeBuildingAnimation(index);
        areaBuildList.shiftBuildingIDsFrom(index);
    }

    public void addBuildingTileset(byte[] data) {
        buildTilesetList.getTilesets().add(new BuildTileset(data));
        areaBuildList.getAreaBuilds().add(new AreaBuild(new ArrayList<>()));
    }

    public void replaceBuildingTileset(int index, byte[] data) {
        buildTilesetList.getTilesets().set(index, new BuildTileset(data));
    }

    public void addBuildingMaterial(int buildIndex) {
        getBuildModelMatshp().addBuildingMaterial(buildIndex);
    }

    public void removeBuildingMaterial(int buildIndex, int materialIndex) {
        getBuildModelMatshp().removeBuildingMaterial(buildIndex, materialIndex);
    }

    public void removeAllBuildingMaterials(int buildIndex) {
        getBuildModelMatshp().replaceBuildingMaterials(buildIndex, new ArrayList<>());
    }

    public void moveBuildingMaterialUp(int buildIndex, int materialIndex) {
        getBuildModelMatshp().moveMaterialUp(buildIndex, materialIndex);
    }

    public void moveBuildingMaterialDown(int buildIndex, int materialIndex) {
        getBuildModelMatshp().moveMaterialDown(buildIndex, materialIndex);
    }

    public void addBuildingAnimation(int buildIndex, int animationIndex) {
        getBuildModelAnimeList().addBuildingAnimation(buildIndex, animationIndex);
    }

    public void removeBuildingAnimation(int buildIndex, int animationIndex) {
        getBuildModelAnimeList().removeBuildingAnimation(buildIndex, animationIndex);
    }

    public void replaceBuildingAnimation(int buildIndex, int animationIndex, int oldAnimationIndex) {
        getBuildModelAnimeList().replaceBuildingAnimation(buildIndex, animationIndex, oldAnimationIndex);
    }

    public void addBuildToAreaBuild(int areaBuildIndex, int buildIndex) {
        areaBuildList.addBuilding(areaBuildIndex, buildIndex);
    }

    public void replaceBuildToAreaBuild(int areaBuildIndex, int buildIndex, int oldAnimationIndex) {
        areaBuildList.replaceBuilding(areaBuildIndex, buildIndex, oldAnimationIndex);
    }

    public void removeBuildToAreaBuild(int areaBuildIndex, int buildIndex) {
        areaBuildList.removeBuilding(areaBuildIndex, buildIndex);
    }

    public void addAnimationFile(String path) throws IOException {
        buildModelAnims.addAnimation(path);
    }

    public void replaceAnimationFile(int index, String path) throws IOException {
        buildModelAnims.replaceAnimation(index, path);
    }

    public void saveAnimationFile(int index, String path) throws IOException {
        buildModelAnims.saveAnimation(index, path);
    }

    public void addMapAnimationFile(String path) throws IOException {
        mapAnimations.addAnimation(path);
    }

    public void replaceMapAnimationFile(int index, String path) throws IOException {
        mapAnimations.replaceAnimation(index, path);
    }

    public void saveMapAnimationFile(int index, String path) throws IOException {
        mapAnimations.saveAnimation(index, path);
    }

    private String getGameFilePath(String relativePath) {
        return gameFolderPath + File.separator + relativePath;
    }

    private boolean isGameFileAvailable(String path) {
        return isFileAvailable(gameFolderPath + File.separator + path);
    }

    private boolean isFileAvailable(String path) {
        return new File(path).exists();
    }

    public BuildModelList getBuildModelList() {
        return buildModelList[buildBlockIndexSelected];
    }

    public BuildModelMatshp getBuildModelMatshp() {
        return buildModelMatshp[buildBlockIndexSelected];
    }

    public BuildAnimeListHGSS getBuildModelAnimeList() {
        return buildModelAnimeList[buildBlockIndexSelected];
    }

    public static ArrayList<Integer> getMaterialOrder(byte[] nsbmdData) {
        NSBMD nsbmd = new NSBMD(nsbmdData);

        List<SBCCommand> sbc = nsbmd.getMLD0().getModels().getFirst().getSBC();
        List<Integer> materialIDs = new ArrayList<>();
        List<Integer> shapeIDs = new ArrayList<>();

        for (SBCCommand cmd : sbc) {
            if (cmd instanceof MAT mat) {
                materialIDs.add(mat.getMatID());
            } else if (cmd instanceof SHP shp) {
                shapeIDs.add(shp.getShpID());
            }
        }

        int[] polyLookup = new int[materialIDs.size()];
        for (int i = 0; i < materialIDs.size(); i++) {
            polyLookup[materialIDs.get(i)] = shapeIDs.get(i);
        }

        ArrayList<Integer> materials = new ArrayList<>(polyLookup.length);
        for (int j : polyLookup) {
            materials.add(j);
        }

        if (Utils.hasDuplicates(materials)) {
            return new ArrayList<>();
        }

        return materials;
    }
}
