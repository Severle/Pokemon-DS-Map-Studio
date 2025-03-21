
package editor.mapdisplay;

import com.jogamp.opengl.GL2;
import editor.state.MapLayerState;
import math.vec.Vec3f;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.Objects;

/**
 * @author Trifindo
 */
@SuppressWarnings({"SpellCheckingInspection", "DuplicatedCode"})
public class ViewOrthoMode extends ViewMode {

    @Override
    public void mousePressed(MapDisplay d, MouseEvent e) {
        if (d.SHIFT_PRESSED) {
            if (SwingUtilities.isLeftMouseButton(e) || SwingUtilities.isMiddleMouseButton(e)) {
                d.lastMouseX = e.getX();
                d.lastMouseY = e.getY();
            }
        } else {
            switch (d.editMode) {
                case MODE_EDIT:
                    if (d.handler.getTileset().size() > 0) {
                        d.setMapSelected(e);
                        d.handler.setLayerChanged(false);
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            d.dragStart = d.getCoordsInSelectedMap(e);
                            d.handler.addMapState(new MapLayerState("Draw Tile", d.handler));
                            d.setTileInGrid(e);
                            d.updateActiveMapLayerGL();
                            d.repaint();
                        } else if (SwingUtilities.isMiddleMouseButton(e)) {
                            d.handler.addMapState(new MapLayerState("Flood Fill Tile", d.handler));
                            d.floodFillTileInGrid(e);
                            d.updateActiveMapLayerGL();
                            d.repaint();
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            d.setTileIndexFromGrid(e);
                            d.repaint();
                            d.handler.getMainFrame().repaintTileSelector();
                            d.handler.getMainFrame().updateTileSelectorScrollBar();
                            d.handler.getMainFrame().repaintTileDisplay();
                        }
                    }
                    break;

                case MODE_CLEAR:
                    if (d.handler.getTileset().size() > 0) {
                        d.setMapSelected(e);
                        d.handler.setLayerChanged(false);
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            d.handler.addMapState(new MapLayerState("Clear Tile", d.handler));
                            d.clearTileInGrid(e);
                            d.updateActiveMapLayerGL();
                            d.repaint();
                        } else if (SwingUtilities.isMiddleMouseButton(e)) {
                            d.handler.addMapState(new MapLayerState("Flood Fill Clear Tile", d.handler));
                            d.floodFillClearTileInGrid(e);
                            d.updateActiveMapLayerGL();
                            d.repaint();
                        }
                    }
                    break;

                case MODE_SMART_PAINT:
                    if (d.handler.getTileset().size() > 0) {
                        if (SwingUtilities.isLeftMouseButton(e) || SwingUtilities.isMiddleMouseButton(e)) {
                            d.setMapSelected(e);
                            d.handler.setLayerChanged(false);
                            d.handler.addMapState(new MapLayerState("Smart Drawing Tile", d.handler));
                            d.smartFillTileInGrid(e, false);
                            //d.disableSmartGrid();
                            d.updateActiveMapLayerGL();
                            d.repaint();

                        } else //noinspection StatementWithEmptyBody
                            if (SwingUtilities.isRightMouseButton(e)) {

                        }
                    }
                    break;

                case MODE_INV_SMART_PAINT:
                    if (d.handler.getTileset().size() > 0) {
                        if (SwingUtilities.isLeftMouseButton(e) || SwingUtilities.isMiddleMouseButton(e)) {
                            d.setMapSelected(e);
                            d.handler.setLayerChanged(false);
                            d.handler.addMapState(new MapLayerState("Smart Drawing Tile", d.handler));
                            d.smartFillTileInGrid(e, true);
                            //d.disableSmartGrid();
                            d.updateActiveMapLayerGL();
                            d.repaint();
                        } else //noinspection StatementWithEmptyBody
                            if (SwingUtilities.isRightMouseButton(e)) {

                        }
                    }
                    break;

                case MODE_MOVE:
                    if (SwingUtilities.isLeftMouseButton(e) || SwingUtilities.isMiddleMouseButton(e)) {
                        d.lastMouseX = e.getX();
                        d.lastMouseY = e.getY();
                    }
                    break;

                case MODE_ZOOM:
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        d.orthoScale *= 1.5F;
                        d.repaint();
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        d.orthoScale /= 1.5F;
                        d.repaint();
                    }
                    break;
            }
        }
    }

    @Override
    public void mouseReleased(MapDisplay d, MouseEvent e) {
        if (Objects.requireNonNull(d.editMode) == MapDisplay.EditMode.MODE_CLEAR) {
            d.handler.getMapMatrix().removeUnusedMaps();
            if (d.handler.mapSelectedNotExists()) {
                d.handler.setDefaultMapSelected();

                d.handler.getMainFrame().getThumbnailLayerSelector().drawAllLayerThumbnails();
                d.handler.getMainFrame().getThumbnailLayerSelector().repaint();
            }
        }
        d.handler.updateLayerThumbnail(d.handler.getActiveLayerIndex());
        d.handler.repaintThumbnailLayerSelector();

        d.editedMapCoords.add(d.handler.getMapSelected());
        d.handler.updateMapThumbnails(d.editedMapCoords);
        d.editedMapCoords = new HashSet<>();

        d.handler.getMainFrame().updateMapMatrixDisplay();

        d.handler.getMainFrame().updateViewGeometryCount();
    }

    @Override
    public void mouseDragged(MapDisplay d, MouseEvent e) {
        d.updateMousePostion(e);
        if (d.SHIFT_PRESSED) {
            if (SwingUtilities.isLeftMouseButton(e) || SwingUtilities.isMiddleMouseButton(e)) {
                d.moveCamera(e);
                d.repaint();
            }
        } else {
            switch (d.editMode) {
                case MODE_EDIT:
                    if (d.handler.getTileset().size() > 0) {
                        d.setMapSelected(e);
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            //d.updateLastMapState();
                            d.editedMapCoords.add(d.getMapCoords(e));
                            d.dragTileInGrid(e);
                            d.updateActiveMapLayerGL();
                            d.repaint();
                        }
                    }
                    break;

                case MODE_CLEAR:
                    if (d.handler.getTileset().size() > 0) {
                        d.setMapSelected(e);
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            //d.updateLastMapState();
                            d.editedMapCoords.add(d.getMapCoords(e));
                            d.clearTileInGrid(e);
                            d.updateActiveMapLayerGL();
                            d.repaint();
                        }
                    }
                    break;

                case MODE_MOVE:
                    if (SwingUtilities.isLeftMouseButton(e) || SwingUtilities.isMiddleMouseButton(e)) {
                        d.moveCamera(e);
                        d.repaint();
                    }
                    break;
            }
        }
    }

    @Override
    public void mouseMoved(MapDisplay d, MouseEvent e) {
        d.updateMousePostion(e);
        d.repaint();
    }

    @Override
    public void keyPressed(MapDisplay d, KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                d.set3DView();
                d.repaint();
                break;
            case KeyEvent.VK_H:
                d.setHeightView();
                d.repaint();
                break;
            case KeyEvent.VK_C:
                d.toggleClearTile();
                d.repaint();
                break;
            case KeyEvent.VK_S:
                d.toggleSmartGrid();
                d.repaint();
                break;
            case KeyEvent.VK_RIGHT:
                d.setCameraAtNextMapAndSelect(new Point(1, 0));
                d.repaint();
                break;
            case KeyEvent.VK_LEFT:
                d.setCameraAtNextMapAndSelect(new Point(-1, 0));
                d.repaint();
                break;
            case KeyEvent.VK_UP:
                d.setCameraAtNextMapAndSelect(new Point(0, -1));
                d.repaint();
                break;
            case KeyEvent.VK_DOWN:
                d.setCameraAtNextMapAndSelect(new Point(0, 1));
                d.repaint();
                break;
        }
    }

    @Override
    public void keyReleased(MapDisplay d, KeyEvent e) {

    }

    @Override
    public void mouseWheelMoved(MapDisplay d, MouseWheelEvent e) {
        if (d.SHIFT_PRESSED) {
            d.zoomCameraOrtho(e);
            d.repaint();
        } else {
            switch (d.editMode) {
                case MODE_EDIT:
                    int delta = e.getWheelRotation() > 0 ? 1 : -1;
                    d.handler.incrementTileSelected(delta);
                    d.handler.getMainFrame().repaintTileSelector();
                    d.handler.getMainFrame().repaintTileDisplay();
                    d.repaint();
                    break;
                case MODE_MOVE, MODE_ZOOM:
                    d.zoomCameraOrtho(e);
                    d.repaint();
                    break;
            }
        }
    }

    @Override
    public void paintComponent(MapDisplay d, Graphics g) {
        if (d.handler != null) {
            Graphics2D g2d = (Graphics2D) g;

            AffineTransform transform = g2d.getTransform();
            d.applyGraphicsTransform(g2d);

            if (d.backImageEnabled && d.backImage != null) {
                d.drawBackImage(g);
            }

            switch (d.editMode) {
                case MODE_EDIT:
                    d.drawTileThumbnail(g);
                    break;
                case MODE_CLEAR, MODE_SMART_PAINT, MODE_INV_SMART_PAINT:
                    d.drawUnitTileBounds(g);
                    break;
            }

            g.setColor(Color.white);
            d.drawAllMapBounds(g);

            if (d.drawAreasEnabled) {
                d.drawAllMapContours(g);
            }

            g.setColor(Color.white);
            d.drawBorderBounds(g,
                    d.handler.getMapSelected().x * d.cols * d.tileSize,
                    d.handler.getMapSelected().y * d.rows * d.tileSize, 1);

            g.setColor(Color.red);
            d.drawBorderBounds(g,
                    d.handler.getMapSelected().x * d.cols * d.tileSize,
                    d.handler.getMapSelected().y * d.rows * d.tileSize, 4);

            g2d.setTransform(transform);
        }
    }

    @Override
    public void applyCameraTransform(MapDisplay d, GL2 gl) {
        float v = (16.0f + d.borderSize) / d.orthoScale;
        gl.glOrtho(-v, v, -v, v, -100.0f, 100.0f);
        //TODO: Use this code for keeping the aspect ratio
    }

    @Override
    public void setCameraAtMap(MapDisplay d) {
        d.orthoScale = 1.0f;
    }

    @Override
    public ViewID getViewID() {
        return ViewID.VIEW_ORTHO;
    }

    @Override
    public float getZNear(MapDisplay d) {
        return -100.0f;
    }

    @Override
    public float getZFar(MapDisplay d) {
        return 100.0f;
    }

    public Vec3f[][] getFrustumPlanes(MapDisplay d) {
        Vec3f camAngles = new Vec3f(d.cameraRotX, d.cameraRotY, d.cameraRotZ);
        Vec3f tarPos = new Vec3f(d.cameraX, d.cameraY, 0.0f);
        Vec3f camDir = MapDisplay.rotToDir_(camAngles);
        Vec3f camUp = MapDisplay.rotToUp_(camAngles);
        Vec3f camRight = camDir.cross_(camUp);
        //Vec3f camPos = tarPos.add_(camDir.negate_().scale_(d.cameraZ));
        Vec3f camPos = tarPos.add_(camDir.negate_().scale_(40.0f));

        float zNear = getZNear(d);
        float zFar = getZFar(d);

        float v = (16.0f + d.borderSize) / d.orthoScale;
        float hNear = 2 * v;
        float wNear = hNear * d.getAspectRatio();

        float hFar = 2 * v;
        float wFar = hFar * d.getAspectRatio();

        //Far plane points
        Vec3f fc = camDir.scale_(zFar).add(camPos);
        Vec3f ftl = fc.add_(camUp.scale_(hFar / 2.0f)).sub(camRight.scale_(wFar / 2.0f));
        Vec3f ftr = fc.add_(camUp.scale_(hFar / 2.0f)).add(camRight.scale_(wFar / 2.0f));
        Vec3f fbl = fc.sub_(camUp.scale_(hFar / 2.0f)).sub(camRight.scale_(wFar / 2.0f));
        Vec3f fbr = fc.sub_(camUp.scale_(hFar / 2.0f)).add(camRight.scale_(wFar / 2.0f));

        //Near plane points
        Vec3f nc = camDir.scale_(zNear).add(camPos);
        Vec3f ntl = nc.add_(camUp.scale_(hNear / 2.0f)).sub(camRight.scale_(wNear / 2.0f));
        Vec3f ntr = nc.add_(camUp.scale_(hNear / 2.0f)).add(camRight.scale_(wNear / 2.0f));
        Vec3f nbl = nc.sub_(camUp.scale_(hNear / 2.0f)).sub(camRight.scale_(wNear / 2.0f));
        Vec3f nbr = nc.sub_(camUp.scale_(hNear / 2.0f)).add(camRight.scale_(wNear / 2.0f));

        //Return frustum planes defined by 3 points
        return new Vec3f[][]{
                {ntr, ntl, ftl},
                {nbl, nbr, fbr},
                {ntl, nbl, fbl},
                {nbr, ntr, fbr},
                {ntl, ntr, nbr},
                {ftr, ftl, fbl}
        };
    }
}
