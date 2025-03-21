
package editor.buildingeditor2.areadata;

import lombok.Getter;
import lombok.Setter;
import utils.io.BinaryReader;
import utils.io.BinaryWriter;

/**
 * @author Trifindo
 */
@SuppressWarnings({"SpellCheckingInspection", "unused"})
@Setter
@Getter
public class AreaDataHGSS {

    private int buildingTilesetID;
    private int mapTilesetID;
    private int dynamicTexType;
    private int areaType;
    private int lightType;

    public AreaDataHGSS(byte[] data) throws Exception {
        buildingTilesetID = (int) BinaryReader.readUInt16(data, 0);
        mapTilesetID = (int) BinaryReader.readUInt16(data, 2);
        dynamicTexType = (int) BinaryReader.readUInt16(data, 4);
        areaType = BinaryReader.readUInt8(data, 6);
        lightType = BinaryReader.readUInt8(data, 7);

    }

    public AreaDataHGSS() {
        buildingTilesetID = 0;
        mapTilesetID = 0;
        dynamicTexType = 65535;
        areaType = 1;
        lightType = 1;
    }

    public byte[] toByteArray() throws Exception {
        byte[] data = new byte[8];
        BinaryWriter.writeUInt16(data, 0, buildingTilesetID);
        BinaryWriter.writeUInt16(data, 2, mapTilesetID);
        BinaryWriter.writeUInt16(data, 4, dynamicTexType);
        BinaryWriter.writeUInt8(data, 6, areaType);
        BinaryWriter.writeUInt8(data, 7, lightType);
        return data;
    }

    public int getUnknown1() {
        return dynamicTexType;
    }

    public void setUnknown1(int unknown1) {
        this.dynamicTexType = unknown1;
    }

}
