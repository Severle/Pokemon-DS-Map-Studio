
package tileset;

import lombok.Getter;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author Trifindo
 */
@Getter
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public class Tile2 {

    //Tileset
    private final Tileset tileset;

    //Tile properties
    public static int maxTileSize = 6;
    private final int width;
    private final int height;
    private final boolean xTileable;
    private final boolean yTileable;
    private final boolean uTileable;
    private final boolean vTileable;
    private final boolean globalTexMapping;
    private final float globalTexScale;
    private final float xOffset;
    private final float yOffset;

    //Path and OBJ file name
    private final String folderPath;
    private final String objFilename;

    //Thumbnail image
    private BufferedImage thumbnail;
    private BufferedImage smallThumbnail;

    //Geometry
    private ArrayList<TileGeometry> tris;
    private ArrayList<TileGeometry> quads;


    public Tile2(Tileset tileset, String folderPath, String objFilename,
                 int width, int height, boolean xTileable, boolean yTileable,
                 boolean uTileable, boolean vTileable,
                 boolean globalTexMapping, float globalTexScale,
                 float xOffset, float yOffset) {

        this.tileset = tileset;

        this.folderPath = folderPath;
        this.objFilename = objFilename;

        this.width = width;
        this.height = height;

        this.xTileable = xTileable;
        this.yTileable = yTileable;
        this.uTileable = uTileable;
        this.vTileable = vTileable;

        this.globalTexMapping = globalTexMapping;
        this.globalTexScale = globalTexScale;
        this.xOffset = xOffset;
        this.yOffset = yOffset;

        //loadFromObj(folderPath, objFilename);
    }
}
