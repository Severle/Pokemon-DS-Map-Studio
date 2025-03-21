
package editor.buildingeditor2.areadata;

import lombok.Getter;
import lombok.Setter;
import utils.io.BinaryReader;
import utils.io.BinaryWriter;

/**
 * @author Trifindo
 */
@SuppressWarnings("SpellCheckingInspection")
@Setter
public class AreaDataDPPt {

    @Getter
    private int buildingTilesetID;
    @Getter
    private int mapTilesetID;
    @Getter
    private int unknown1;
    private int areaType;

    public AreaDataDPPt(byte[] data) throws Exception {
        buildingTilesetID = (int) BinaryReader.readUInt16(data, 0);
        mapTilesetID = (int) BinaryReader.readUInt16(data, 2);
        unknown1 = (int) BinaryReader.readUInt16(data, 4);
        areaType = (int) BinaryReader.readUInt16(data, 6);
    }

    public AreaDataDPPt() {
        buildingTilesetID = 0;
        mapTilesetID = 0;
        unknown1 = 0;
        areaType = 0;
    }

    public byte[] toByteArray() throws Exception {
        byte[] data = new byte[8];
        BinaryWriter.writeUInt16(data, 0, buildingTilesetID);
        BinaryWriter.writeUInt16(data, 2, mapTilesetID);
        BinaryWriter.writeUInt16(data, 4, unknown1);
        BinaryWriter.writeUInt16(data, 6, areaType);
        return data;
    }

    public int getUnknown2() {
        return areaType;
    }


}
