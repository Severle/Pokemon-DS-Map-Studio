
package formats.imd.nodes;

import formats.imd.ImdAttribute;
import formats.imd.ImdNode;
import formats.imd.ImdTextureIndexed;
import formats.nsbtx2.Nsbtx2;
import formats.nsbtx2.NsbtxTexture;

import java.util.ArrayList;

/**
 * @author Trifindo
 */
public class TexImage extends ImdNode {

    private static final String[] colorFormatTable = new String[]{
            "palette4",
            "palette16",
            "palette256",
            "a3i5",
            "a5i3"
    };

    public TexImage(int index, String name, String paletteName,
                    ImdTextureIndexed imdTexture, int colorFormat, String path) {
        super("tex_image");

        String color0mode;
        if (imdTexture.isTransparent()) {
            color0mode = "transparency";
        } else {
            color0mode = "color";
        }

        attributes = new ArrayList<>() {
            {
                add(new ImdAttribute("index", index));
                add(new ImdAttribute("name", name));
                add(new ImdAttribute("width", imdTexture.width));
                add(new ImdAttribute("height", imdTexture.height));
                add(new ImdAttribute("original_width", imdTexture.width));
                add(new ImdAttribute("original_height", imdTexture.height));
                add(new ImdAttribute("format", colorFormatTable[colorFormat]));
                add(new ImdAttribute("color0_mode", color0mode));
                add(new ImdAttribute("palette_name", paletteName));
                add(new ImdAttribute("path", path));
            }
        };

        subnodes.add(new ImdBitmap(imdTexture));
    }

    public TexImage(int index, NsbtxTexture texture, String path) {
        super("tex_image");

        String color0mode;
        if (texture.isTransparent()) {
            color0mode = "transparency";
        } else {
            color0mode = "color";
        }

        attributes = new ArrayList<>() {
            {
                add(new ImdAttribute("index", index));
                add(new ImdAttribute("name", texture.getName()));
                add(new ImdAttribute("width", texture.getWidth()));
                add(new ImdAttribute("height", texture.getHeight()));
                add(new ImdAttribute("original_width", texture.getWidth()));
                add(new ImdAttribute("original_height", texture.getHeight()));
                add(new ImdAttribute("format", Nsbtx2.formatNames[texture.getColorFormat()]));
                add(new ImdAttribute("color0_mode", color0mode));
                add(new ImdAttribute("palette_name", ""));
                add(new ImdAttribute("path", path));
            }
        };

        subnodes.add(new ImdBitmap(texture));
    }
}
