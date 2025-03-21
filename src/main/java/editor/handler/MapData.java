
package editor.handler;

import formats.backsound.BackSound;
import formats.bdhc.Bdhc;
import editor.buildingeditor2.buildfile.BuildFile;
import formats.bdhcam.Bdhcam;
import formats.collisions.Collisions;
import editor.grid.MapGrid;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * @author Trifindo
 */
@Log4j2
@SuppressWarnings("SpellCheckingInspection")
public class MapData {

    //Map Editor Handler
    private final MapEditorHandler handler;

    //Map grid
    @Setter
    @Getter
    private MapGrid grid;

    //Bdhc
    @Setter
    @Getter
    private Bdhc bdhc;

    //Bdhcam
    @Setter
    @Getter
    private Bdhcam bdhcam;

    //Backsound
    @Setter
    @Getter
    private BackSound backsound;

    //Collisions
    @Setter
    @Getter
    private Collisions collisions;
    @Setter
    @Getter
    private Collisions collisions2;

    //Building file
    @Setter
    @Getter
    private BuildFile buildings;

    //Map thumbnail
    public static final  int           mapThumbnailSize = 64;
    private static final int           smallTileSize    = 2;
    @Getter
    private              BufferedImage mapThumbnail;

    //Area index
    @Getter
    @Setter
    private int areaIndex;

    public MapData(MapEditorHandler handler) {
        this.handler = handler;
        grid = new MapGrid(handler);

        bdhc = new Bdhc();
        backsound = new BackSound();
        collisions = new Collisions(handler.getGameIndex());
        collisions2 = new Collisions(handler.getGameIndex());
        buildings = new BuildFile();
        bdhcam = new Bdhcam();

        areaIndex = 0;
    }

    public void updateMapThumbnail() {
        int[][][] tiles   = grid.tileLayers;
        int[][][] heights = grid.heightLayers;

        mapThumbnail = new BufferedImage(mapThumbnailSize, mapThumbnailSize, BufferedImage.TYPE_INT_ARGB);
        Graphics g = mapThumbnail.getGraphics();

        g.setColor(new Color(0.0f, 0.5f, 0.5f, 1.0f));
        g.fillRect(0, 0, mapThumbnail.getWidth(), mapThumbnail.getHeight());

        for (int i = 0; i < MapGrid.cols; i++) {
            for (int j = 0; j < MapGrid.rows; j++) {
                try {
                    @SuppressWarnings("NumericOverflow")
                    int maxHeight = -Integer.MIN_VALUE;
                    int maxHeightIndex = 0;
                    for (int k = 0; k < MapGrid.numLayers; k++) {
                        if (heights[k][i][j] > maxHeight && tiles[k][i][j] > -1) {
                            maxHeight = heights[k][i][j];
                            maxHeightIndex = k;
                        }
                    }

                    int tileIndex = tiles[maxHeightIndex][i][j];
                    if (tileIndex != -1) {
                        BufferedImage tileThumbnail = handler.getTileset().get(tileIndex).getSmallThumbnail();

                        g.drawImage(tileThumbnail,
                                i * smallTileSize,
                                (MapGrid.cols - j - 1) * smallTileSize - (tileThumbnail.getHeight() - smallTileSize), //+ tileThumbnail.getHeight(),
                                null);
                    }
                } catch (Exception ex) {
                    log.warn(ex);
                }
            }
        }
    }

    public boolean isUnused() {
        return grid.isEmpty();
    }
}
