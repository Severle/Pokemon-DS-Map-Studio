
package editor.tileseteditor;

import editor.handler.MapData;
import editor.handler.MapEditorHandler;
import editor.smartdrawing.SmartGrid;
import lombok.Getter;
import lombok.Setter;
import tileset.Tileset;
import tileset.TilesetMaterial;

import java.awt.*;

/**
 * @author Trifindo
 */
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public class TilesetEditorHandler {

    private final MapEditorHandler handler;

    private final Tileset oldTset;

    @Getter
    @Setter
    private int textureIdIndexSelected = 0;
    @Getter
    @Setter
    private int materialIndexSelected  = 0;

    //private TilesetRenderer tr;

    @Setter
    @Getter
    private Color lastColorUsed = Color.white;

    public TilesetEditorHandler(MapEditorHandler handler) {
        this.handler = handler;

        oldTset = handler.getTileset().clone();

        //tr = new TilesetRenderer(handler.getTileset());
    }

    public int[] getChangeIndices() {
        Tileset tset = handler.getTileset();
        int[] indices = new int[oldTset.size()];
        for (int i = 0; i < oldTset.size(); i++) {
            indices[i] = tset.getIndexOfTile(oldTset.get(i));
        }
        return indices;
    }

    public void fixMapGridIndices(int[] indices) {
        for (MapData mapEntry : handler.getMapMatrix().getMatrix().values()) {
            mapEntry.getGrid().replaceTilesUsingIndices(indices);
        }
        //handler.getGrid().replaceTilesUsingIndices(indices);
    }

    public void fixTilesetGridIndices(int[] indices) {
        for (SmartGrid sgrid : handler.getSmartGridArray()) {
            sgrid.replaceTilesUsingIndices(indices);
        }
    }

    public int getTextureIndexSelected() {
        return handler.getTileSelected().getTextureIDs().get(textureIdIndexSelected);
    }

    public String getTextureSelectedName() {
        return handler.getTileset().getImageName(handler.getTileSelected().getTextureIDs().get(textureIdIndexSelected));
    }

    public String getMaterialSelectedTextureName() {
        return handler.getTileset().getImageName(getMaterialIndexSelected());
    }

    public MapEditorHandler getMapEditorHandler() {
        return handler;
    }

    public TilesetMaterial getMaterialSelected() {
        return handler.getTileset().getMaterial(materialIndexSelected);
    }

}
