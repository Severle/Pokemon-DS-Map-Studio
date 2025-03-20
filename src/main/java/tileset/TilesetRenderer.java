
package tileset;

import com.jogamp.opengl.*;
import com.jogamp.opengl.util.awt.AWTGLReadBufferUtil;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import utils.Utils;

import java.awt.image.BufferedImage;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.GL_ALPHA_TEST;
import static com.jogamp.opengl.GL2ES3.GL_QUADS;

/**
 * @author Trifindo
 */
@Log4j2
@SuppressWarnings({"SpellCheckingInspection", "unused", "DuplicatedCode"})
public class TilesetRenderer {

    private final int       tileSize      = 16; //in pixels
    private final boolean[] renderVboTile = new boolean[4];
    @Setter
    private       Tileset   tileset;
    private final float     cameraX = 0.0f;
    private final float cameraY = 0.0f;
    @SuppressWarnings("FieldCanBeLocal")
    private final float cameraZ = 32.0f;

    @SuppressWarnings("FieldCanBeLocal")
    private final int smallTileSize = 2;

    private GLAutoDrawable drawable;
    private GL2 gl;

    public TilesetRenderer(Tileset tileset) {
        this.tileset = tileset;

        init();
    }

    public void init() {
        GLProfile glp = GLProfile.getGL2ES1();//.getDefault();
        try {
            GLCapabilities caps = new GLCapabilities(glp);
            caps.setHardwareAccelerated(true);
            caps.setDoubleBuffered(true);
            caps.setAlphaBits(8);
            caps.setRedBits(8);
            caps.setBlueBits(8);
            caps.setGreenBits(8);
            caps.setOnscreen(false);
            caps.setPBuffer(false);
            //caps.setTransparentAlphaValue(0);//NEW CODE

            GLDrawableFactory factory = GLDrawableFactory.getFactory(glp);

            drawable = factory.createOffscreenAutoDrawable(factory.getDefaultDevice(), caps,
                    new DefaultGLCapabilitiesChooser(), Tile.maxTileSize * tileSize, Tile.maxTileSize * tileSize);

            drawable.display();
            drawable.getContext().makeCurrent();

            gl = drawable.getGL().getGL2();

            //gl.glClearColor(0.0f, 0.5f, 0.5f, 0.0f); //Use this for transparent background
            gl.glClearColor(0.0f, 0.5f, 0.5f, 1.0f);

            tileset.loadTexturesGL();
        } catch (GLException ex) {
            log.error(ex);
        }
    }

    public void renderTiles() {
        //GL4 gl = (GL4) GLContext.getCurrentGL();
        for (int i = 0; i < tileset.size(); i++) {
            renderTileThumbnail(i);
        }
        gl.getContext().release();
    }

    public void renderTileThumbnail(int tileIndex) {
        Tile tile = tileset.get(tileIndex);
        try {

            gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            setupVAOsVBOs(tile);

            //noinspection GrazieInspection
            drawOpaqueTile(tile); //NEW CODE REMOVED Remove this for transparent background
            drawTransparentTile(tile);

            BufferedImage img = new AWTGLReadBufferUtil(drawable.getGLProfile(), false).readPixelsToBufferedImage(drawable.getGL(), true /* awtOrientation */);
            img = img.getSubimage(0, 0, tile.getWidth() * tileSize, tile.getHeight() * tileSize);

            //tile.setThumbnail(Utils.addBackgroundColor(new Color(0.0f, 0.5f, 0.5f, 1.0f), img));//Use this for transparent background
            tile.setThumbnail(img);
            tile.setSmallThumbnail(Utils.resize(
                    img, smallTileSize * tile.getWidth(), smallTileSize * tile.getHeight()));
        } catch (GLException ex) {
            log.error(ex);
        }
    }

    private void setupVAOsVBOs(Tile tile) {
        renderVboTile[0] = tile.getVCoordsQuad().length > 0;
        renderVboTile[1] = tile.getTCoordsQuad().length > 0;
        renderVboTile[2] = tile.getVCoordsTri().length > 0;
        renderVboTile[3] = tile.getTCoordsTri().length > 0;
    }

    private void drawTile(Tile tile) {
        gl.glLoadIdentity();

        float v = 0.5f * Tile.maxTileSize;
        gl.glOrtho(-v, v, -v, v, -100.0f, 100.0f);

        //gl.glTranslatef(-cameraX, -cameraY, -cameraZ);
        gl.glTranslatef(
                ((float) (-Tile.maxTileSize)) / 2,
                ((float) (Tile.maxTileSize)) / 2 - tile.getHeight(),
                -cameraZ);

        gl.glColor3f(1.0f, 1.0f, 1.0f);

        drawQuads(gl, tile, 0);
        drawTris(gl, tile, 2);
    }

    @SuppressWarnings("SameParameterValue")
    private void drawQuads(GL2 gl, Tile tile, int vboID) {
        if (!(renderVboTile[vboID] && renderVboTile[vboID + 1])) {
            return;
        }

        for (int k = 0; k < tile.getTextureIDs().size(); k++) {
            // activate texture unit #0 and bind it to the brick texture object
            //gl.glActiveTexture(GL_TEXTURE0);
            gl.glBindTexture(GL_TEXTURE_2D, tile.getTexture(k).getTextureObject());

            gl.glEnable(GL_TEXTURE_2D);

            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

            //Draw polygons
            int start, end;
            final int vPerPolygon = 4;
            start = tile.getTexOffsetsQuad().get(k);
            if (k + 1 < tile.getTextureIDs().size()) {
                end = (tile.getTexOffsetsQuad().get(k + 1));
            } else {
                end = tile.getVCoordsQuad().length / (3 * vPerPolygon);
            }

            gl.glBegin(GL_QUADS);
            for (int i = start; i < end; i++) {
                for (int j = 0; j < vPerPolygon; j++) {
                    gl.glTexCoord2fv(tile.getTCoordsQuad(), (i * vPerPolygon + j) * 2);
                    gl.glColor3fv(tile.getColorsQuad(), (i * vPerPolygon + j) * 3);
                    gl.glVertex3fv(tile.getVCoordsQuad(), (i * vPerPolygon + j) * 3);
                }
            }
            gl.glEnd();
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void drawTris(GL2 gl, Tile tile, int vboID) {
        if (!(renderVboTile[vboID] && renderVboTile[vboID + 1])) {
            return;
        }

        for (int k = 0; k < tile.getTextureIDs().size(); k++) {
            // activate texture unit #0 and bind it to the brick texture object
            //gl.glActiveTexture(GL_TEXTURE0);
            gl.glBindTexture(GL_TEXTURE_2D, tile.getTexture(k).getTextureObject());

            gl.glEnable(GL_TEXTURE_2D);

            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

            //Draw polygons
            int start, end;
            final int vPerPolygon = 3;
            start = tile.getTexOffsetsTri().get(k);
            if (k + 1 < tile.getTextureIDs().size()) {
                end = (tile.getTexOffsetsTri().get(k + 1));
            } else {
                end = tile.getVCoordsTri().length / (3 * vPerPolygon);
            }

            gl.glBegin(GL_TRIANGLES);
            for (int i = start; i < end; i++) {
                for (int j = 0; j < vPerPolygon; j++) {
                    gl.glTexCoord2fv(tile.getTCoordsTri(), (i * vPerPolygon + j) * 2);
                    gl.glColor3fv(tile.getColorsTri(), (i * vPerPolygon + j) * 3);
                    gl.glVertex3fv(tile.getVCoordsTri(), (i * vPerPolygon + j) * 3);
                }
            }
            gl.glEnd();
        }
    }

    private void drawOpaqueTile(Tile tile) {
        GL2 gl = (GL2) GLContext.getCurrentGL();

        //Use program
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_DST_ALPHA);

        // adjust OpenGL settings and draw model
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LESS); //Less instead of equal for drawing the grid

        gl.glEnable(GL_ALPHA_TEST);
        gl.glAlphaFunc(GL_GREATER, 0.9f);

        drawTile(tile);
    }

    private void drawTransparentTile(Tile tile) {
        GL2 gl = (GL2) GLContext.getCurrentGL();

        //Use program
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // adjust OpenGL settings and draw model
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LESS); //Less instead of equal for drawing the grid

        gl.glEnable(GL_ALPHA_TEST);
        gl.glAlphaFunc(GL_NOTEQUAL, 0.0f);

        drawTile(tile);
    }

    public void destroy() {
        drawable.getContext().destroy();
    }
}
