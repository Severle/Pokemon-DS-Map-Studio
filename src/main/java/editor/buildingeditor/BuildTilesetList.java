
package editor.buildingeditor;

import lombok.Getter;
import utils.io.BinaryReader;
import utils.io.BinaryWriter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Trifindo
 */
@SuppressWarnings("SpellCheckingInspection")
@Getter
public class BuildTilesetList {

    private final ArrayList<Integer> buildingIDs;

    public BuildTilesetList(String path) throws
            IOException {
        BinaryReader br = new BinaryReader(path);
        int numberOfBuildings = br.readUInt16();
        buildingIDs = new ArrayList<>(numberOfBuildings);
        for (int i = 0; i < numberOfBuildings; i++) {
            buildingIDs.add(br.readUInt16());
        }
        br.close();
    }

    public void saveToFile(String path) throws IOException {
        BinaryWriter bw = new BinaryWriter(path);

        bw.writeUInt16(buildingIDs.size());
        for (Integer buildingID : buildingIDs) {
            bw.writeUInt16(buildingID);
        }
        bw.close();

    }


}
