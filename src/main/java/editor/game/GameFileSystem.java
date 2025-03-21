
package editor.game;

import java.io.File;

/**
 * @author Trifindo
 */
public abstract class GameFileSystem {

    protected static String getPath(String[] splitPath) {
        StringBuilder path = new StringBuilder();
        for (int i = 0; i < splitPath.length - 1; i++) {
            path.append(splitPath[i]).append(File.separator);
        }
        path.append(splitPath[splitPath.length - 1]);
        return path.toString();
    }

}
