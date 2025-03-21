
package editor.buildingeditor2.tileset;

import lombok.Getter;
import lombok.Setter;
import utils.Utils;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Trifindo
 */
@SuppressWarnings("SpellCheckingInspection")
@Setter
@Getter
public class BuildTileset {

    private byte[] data;

    public BuildTileset(byte[] data) {
        this.data = data;
    }

    public void save(String path) throws IOException {
        path = Utils.addExtensionToPath(path, "nsbtx");
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(data);
        fos.close();
    }


}
