
package formats.nsbtx2;

import formats.nsbtx2.exceptions.NsbtxTextureFormatException;
import lombok.Getter;
import lombok.Setter;
import utils.Utils;

/**
 * @author Trifindo
 */
@SuppressWarnings({"SpellCheckingInspection", "DuplicatedCode", "unused"})
public class NsbtxTexture {

    @Setter
    @Getter
    private       String  name;
    @Getter
    private final int     colorFormat;
    private boolean isTransparent;
    @Setter
    @Getter
    private int    width;
    @Setter
    @Getter
    private int    height;
    @Setter
    private byte[] data;

    public NsbtxTexture(byte[] nsbtxData, int offsetTextureInfo,
                        int textureDataOffset, int offsetTextureName) throws
            NsbtxTextureFormatException, IndexOutOfBoundsException {
        byte b1 = nsbtxData[offsetTextureInfo + 0x03];
        byte b2 = nsbtxData[offsetTextureInfo + 0x02];

        int offsetTextureData = ((nsbtxData[offsetTextureInfo + 0x01] & 0xFF) << 8 | (nsbtxData[offsetTextureInfo] & 0xFF)) << 3;

        //Read color format
        colorFormat = (b1 & 0x1C) >> 2;
        if (colorFormat == Nsbtx2.FORMAT_NO_TEXTURE
                || colorFormat == Nsbtx2.FORMAT_4X4_TEXEL
                || colorFormat == Nsbtx2.FORMAT_DIRECT_TEXTURE) {
            throw new NsbtxTextureFormatException(colorFormat);
        }

        //Read image properites
        isTransparent = ((b1 & 0x20) >> 5) == 1;
        height = 8 << (((b1 & 0x03) << 1) | ((b2 & 0x80) >> 7));
        width = 8 << ((b2 & 0x70) >> 4);
        int dataSize = (width * height * Nsbtx2.bitDepth[colorFormat]) / 8;

        //Read data
        data = new byte[dataSize];
        System.arraycopy(nsbtxData, textureDataOffset + offsetTextureData, data, 0, dataSize);

        //Read texture name
        name = Utils.removeLastOccurrences(new String(nsbtxData, offsetTextureName, 16), '\u0000');
    }

    public NsbtxTexture(String name, int colorFormat, boolean isTransparent,
                        int width, int height) {
        this.name = name;
        this.colorFormat = colorFormat;
        this.isTransparent = isTransparent;
        this.width = width;
        this.height = height;
        this.data = new byte[(width * height * Nsbtx2.bitDepth[colorFormat]) / 8];
    }

    public byte[] getColorIndices() {
        int bitDepth = getBitDepth();
        int pixelsPerByte = 8 / bitDepth;
        int mask = 0xFF >> (8 - bitDepth);
        int pixelIndex = 0;
        byte[] colorIndices = new byte[width * height];
        for (byte dataByte : data) {
            for (int j = 0; j < pixelsPerByte; j++) {
                byte colorIndex = (byte) ((dataByte >> bitDepth * j) & mask);
                colorIndices[pixelIndex] = colorIndex;
                pixelIndex++;
            }
        }
        return colorIndices;
    }


    public String getDataAsHexStringImd() {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < data.length; i += 2) {
            hexString.append(" ");
            hexString.append(String.format("%02x", data[i + 1]));
            hexString.append(String.format("%02x", data[i]));
        }
        return hexString.toString();
    }

    public int getBitDepth() {
        return Nsbtx2.bitDepth[colorFormat];
    }

    public int getNumColors() {
        return Nsbtx2.numColors[colorFormat];
    }

    public boolean isTransparent() {
        return isTransparent;
    }

    public void setTransparent(boolean isTransparent) {
        this.isTransparent = isTransparent;
    }

    public int getDataSizeImd() {
        return data.length / 2;
    }

}
