
package editor.buildingeditor2.buildfile;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Trifindo
 */
@Setter
public class Build {

    public static final int dataSize = 48;

    private int   modelID;
    @Getter
    private float x, y, z;
    @Getter
    private float scaleX, scaleY, scaleZ;

    public Build() {
        this.modelID = 0;
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.scaleZ = 1.0f;
    }

    public Build(int modelID, float x, float y, float z, float scaleX, float scaleY, float scaleZ) {
        this.modelID = modelID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    public int getModeID() {
        return modelID;
    }
}
