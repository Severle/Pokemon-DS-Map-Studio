package formats.collisions.bw;

import editor.handler.MapEditorHandler;
import formats.collisions.Collisions;
import lombok.Getter;
import lombok.Setter;

public class CollisionHandlerBW {

    private final MapEditorHandler         handler;
    private final CollisionsBW3D[]         collisionFiles = new CollisionsBW3D[2];
    @Getter
    private final CollisionsEditorDialogBW dialog;
    @Setter
    private       int                      selectedCollisionFile = 0;

    @Setter
    @Getter
    private float[] currentTile = new float[4];

    public CollisionHandlerBW(MapEditorHandler handler, CollisionsEditorDialogBW dialog){
        this.handler = handler;
        this.dialog = dialog;

        try {
            collisionFiles[0] = new CollisionsBW3D(handler.getCollisions().toByteArray(), handler.getGameIndex());
        }catch(Exception e){
            collisionFiles[0] = null;
        }

        try {
            collisionFiles[1] = new CollisionsBW3D(handler.getCollisions2().toByteArray(), handler.getGameIndex());
        }catch(Exception e){
            collisionFiles[1] = null;
        }
    }

    public void saveToCollision() throws Exception {
        handler.setCollisions(new Collisions(collisionFiles[0].toByteArray()));
        handler.setCollisions2(new Collisions(collisionFiles[1].toByteArray()));
    }

    public void moveCurrentTile(float dz){
        for(int i = 0; i < currentTile.length; i++){
            currentTile[i] += dz;
        }
    }

    public void rotateCurrentTile(){
        float temp = currentTile[0];
        for(int i = 0; i < currentTile.length - 1; i++){
            currentTile[i] = currentTile[i + 1];
        }
        currentTile[currentTile.length - 1] = temp;
    }

    public void flipCurrentTile(){
        float temp = currentTile[0];
        currentTile[0] = currentTile[1];
        currentTile[1] = temp;

        temp = currentTile[2];
        currentTile[2] = currentTile[3];
        currentTile[3] = temp;
    }

    public CollisionsBW3D getCollisionsBW3D() {
        return collisionFiles[selectedCollisionFile];
    }

    public void setCollisionsBW3D(CollisionsBW3D collisionsBW3D) {
        this.collisionFiles[selectedCollisionFile] = collisionsBW3D;
    }
}


