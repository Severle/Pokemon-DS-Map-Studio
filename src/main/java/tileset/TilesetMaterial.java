
package tileset;

import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Trifindo
 */
@SuppressWarnings("SpellCheckingInspection")
public class TilesetMaterial {

    //Textures
    @Setter
    @Getter
    private BufferedImage textureImg;
    @Setter
    @Getter
    private String        imageName;
    @Setter
    @Getter
    private String materialName;
    @Setter
    @Getter
    private String  paletteNameImd;
    @Setter
    @Getter
    private String  textureNameImd;
    private boolean enableFog;
    @Setter
    private boolean renderBothFaces;
    @Setter
    private boolean uniformNormalOrientation;
    @Setter
    private boolean alwaysIncludeInImd;
    @Setter
    private boolean light0;
    @Setter
    private boolean light1;
    @Setter
    private boolean light2;
    @Setter
    private boolean light3;
    @Setter
    @Getter
    private int     alpha;
    @Setter
    @Getter
    private int     texGenMode;
    @Setter
    @Getter
    private int texTilingU;
    @Setter
    @Getter
    private int texTilingV;
    @Setter
    @Getter
    private int colorFormat;
    @Setter
    private boolean renderBorder;
    @Setter
    private boolean vertexColorsEnabled;

    public TilesetMaterial() {
        enableFog = true;
        alpha = 31;
        texGenMode = 0;
        texTilingU = 0;
        texTilingV = 0;
        colorFormat = 1;
        light0 = true;
        light1 = false;
        light2 = false;
        light3 = false;
        renderBorder = false;
        vertexColorsEnabled = false;
        uniformNormalOrientation = true;
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public TilesetMaterial clone() {
        TilesetMaterial material = new TilesetMaterial();
        material.imageName = imageName;
        material.materialName = materialName;
        material.paletteNameImd = paletteNameImd;
        material.textureNameImd = textureNameImd;
        material.textureImg = textureImg;
        material.enableFog = enableFog;
        material.renderBothFaces = renderBothFaces;
        material.uniformNormalOrientation = uniformNormalOrientation;
        material.alwaysIncludeInImd = alwaysIncludeInImd;
        material.light0 = light0;
        material.light1 = light1;
        material.light2 = light2;
        material.light3 = light3;
        material.alpha = alpha;
        material.texGenMode = texGenMode;
        material.texTilingU = texTilingU;
        material.texTilingV = texTilingV;
        material.colorFormat = colorFormat;
        material.renderBorder = renderBorder;
        material.vertexColorsEnabled = vertexColorsEnabled;

        return material;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.materialName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TilesetMaterial other = (TilesetMaterial) obj;

        return Objects.equals(this.imageName, other.imageName);
    }


    public boolean renderBorder() {
        return renderBorder;
    }

    public boolean light0() {
        return light0;
    }

    public boolean light1() {
        return light1;
    }

    public boolean light2() {
        return light2;
    }

    public boolean light3() {
        return light3;
    }

    public void loadTextureImgFromPath(String path) throws IOException {
        this.textureImg = ImageIO.read(new File(path));
    }

    public boolean isFogEnabled() {
        return enableFog;
    }

    public void setFogEnabled(boolean enableFog) {
        this.enableFog = enableFog;
    }

    public boolean renderBothFaces() {
        return renderBothFaces;
    }

    public boolean uniformNormalOrientation() {
        return uniformNormalOrientation;
    }

    public boolean alwaysIncludeInImd() {
        return alwaysIncludeInImd;
    }

    public boolean vertexColorsEnabled() {
        return vertexColorsEnabled;
    }
}
