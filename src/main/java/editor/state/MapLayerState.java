
package editor.state;

import editor.handler.MapData;
import editor.handler.MapEditorHandler;
import lombok.Getter;

import java.awt.Point;
import java.util.HashMap;
import java.util.Set;

/**
 * @author Trifindo
 */
@SuppressWarnings("SpellCheckingInspection")
public class MapLayerState extends State {

    private final MapEditorHandler handler;
    @Getter
    private final int              layerIndex;

    private final HashMap<Point, int[][]> mapTileLayers;
    private final HashMap<Point, int[][]> mapHeightLayers;

    public MapLayerState(String name, MapEditorHandler handler) {
        this(name, handler, true);
    }

    public MapLayerState(String name, MapEditorHandler handler, boolean fullState) {
        super(name);

        this.handler = handler;
        this.layerIndex = handler.getActiveLayerIndex();

        mapTileLayers = new HashMap<>();
        mapHeightLayers = new HashMap<>();
        if (fullState) {
            for (HashMap.Entry<Point, MapData> mapEntry : handler.getMapMatrix().getMatrix().entrySet()) {
                mapTileLayers.put(mapEntry.getKey(), mapEntry.getValue().getGrid().cloneTileLayer(layerIndex));
                mapHeightLayers.put(mapEntry.getKey(), mapEntry.getValue().getGrid().cloneHeightLayer(layerIndex));
            }
        } else {
            mapTileLayers.put(handler.getMapSelected(), handler.getGrid().cloneTileLayer(layerIndex));
            mapHeightLayers.put(handler.getMapSelected(), handler.getGrid().cloneHeightLayer(layerIndex));
        }

    }

    @Override
    public void revertState() {
        //TODO: This function doesnt recover extra file data like PER or BDHC files

        for (HashMap.Entry<Point, int[][]> mapEntry : mapTileLayers.entrySet()) {
            handler.getMapMatrix().getMapAndCreate(mapEntry.getKey()).getGrid().setTileLayer(layerIndex, mapEntry.getValue());
        }
        for (HashMap.Entry<Point, int[][]> mapEntry : mapHeightLayers.entrySet()) {
            handler.getMapMatrix().getMapAndCreate(mapEntry.getKey()).getGrid().setHeightLayer(layerIndex, mapEntry.getValue());
        }

        //Remove maps that were not used
        handler.getMapMatrix().getMatrix().entrySet().removeIf(entry -> !mapTileLayers.containsKey(entry.getKey()));

    }

    public void updateState() {
        if (!mapTileLayers.containsKey(handler.getMapSelected())) {
            System.out.println("Updating state: " + handler.getMapSelected().x + " " + handler.getMapSelected().y);
            mapTileLayers.put(handler.getMapSelected(), handler.getGrid().cloneTileLayer(layerIndex));
        }
        if (!mapHeightLayers.containsKey(handler.getMapSelected())) {
            mapHeightLayers.put(handler.getMapSelected(), handler.getGrid().cloneHeightLayer(layerIndex));
        }

    }

    public Set<Point> getKeySet() {
        return mapTileLayers.keySet();
    }

}
