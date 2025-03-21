
package formats.nsbtx;

import editor.handler.MapEditorHandler;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Trifindo
 */
@Getter
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public class NsbtxHandler {

    private final MapEditorHandler  handler;
    private final NsbtxEditorDialog dialog;
    private Nsbtx  nsbtx     = null;
    @Setter
    private String nsbtxPath = null;

    @Setter
    private int textureIndexSelected = 0;
    @Setter
    private int paletteIndexSelected = 0;
    @Setter
    private int colorIndexSelected   = 0;

    public NsbtxHandler(MapEditorHandler handler, NsbtxEditorDialog dialog) {
        this.handler = handler;
        this.dialog = dialog;
    }

    public void loadNsbtx(String path) throws IOException {
        this.nsbtx = NsbtxLoader.loadNsbtx(path);
        this.nsbtxPath = path;
    }

    public BufferedImage getSelectedImage() {
        if (nsbtx != null) {
            return nsbtx.getImage(textureIndexSelected, paletteIndexSelected);
        }
        return null;
    }

    public int getSelectedTextureWidth() {
        return nsbtx.textureInfos.get(textureIndexSelected).width;
    }

    public int getSelectedTextureHeight() {
        return nsbtx.textureInfos.get(textureIndexSelected).height;
    }

    public int getNumColorsInSelectedPalette() {
        return nsbtx.paletteInfos.get(paletteIndexSelected).getNumColors();
    }

    public void incrementColorIndexSelected(int increment) {
        this.colorIndexSelected += increment;
    }

    public void decrementColorIndexSelected() {
        this.colorIndexSelected--;
    }

}
