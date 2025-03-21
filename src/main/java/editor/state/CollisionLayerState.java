
package editor.state;

import formats.collisions.CollisionHandler;
import lombok.Getter;

/**
 * @author Trifindo
 */
public class CollisionLayerState extends State {

    private final CollisionHandler collisionHandler;
    @Getter
    private final int              layerIndex;
    private final byte[][]         layer;

    public CollisionLayerState(String name, CollisionHandler collisionHandler) {
        super(name);
        this.collisionHandler = collisionHandler;

        layerIndex = collisionHandler.getIndexLayerSelected();
        layer = collisionHandler.cloneLayer(layerIndex);
    }

    @Override
    public void revertState() {
        collisionHandler.setLayer(layerIndex, layer);
    }


}
