
package formats.imd.nodes;

import formats.imd.ImdAttribute;
import formats.imd.ImdNode;
import formats.imd.ImdTextureIndexed;
import formats.nsbtx2.NsbtxTexture;

import java.util.ArrayList;

/**
 * @author Trifindo
 */
@SuppressWarnings("unused")
public class ImdBitmap extends ImdNode {

    public ImdBitmap(ImdTextureIndexed imdTexture) {
        super("bitmap");

        attributes = new ArrayList<>() {
            {
                add(new ImdAttribute("size", imdTexture.getTextureDataSize()));
            }
        };

        content = imdTexture.getTexDataAsHexString();
    }

    public ImdBitmap(NsbtxTexture texture) {
        super("bitmap");

        attributes = new ArrayList<>() {
            {
                add(new ImdAttribute("size", texture.getDataSizeImd()));
            }
        };

        content = texture.getDataAsHexStringImd();
    }

    public ImdBitmap(int size) {
        super("bitmap");

        attributes = new ArrayList<>() {
            {
                add(new ImdAttribute("size", size));
            }
        };

        StringBuilder pixels = new StringBuilder();
        for (int i = 0; i < size / 8; i++) {
            pixels.append("0000 ".repeat(7));
            pixels.append("0000 ");
        }
        content = pixels.toString();
    }


}
