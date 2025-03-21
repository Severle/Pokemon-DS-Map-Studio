
package formats.backsound;

import lombok.Getter;
import utils.io.BinaryReader;
import utils.io.BinaryWriter;
import utils.exceptions.WrongFormatException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Trifindo
 */
@Getter
@SuppressWarnings("SpellCheckingInspection")
public class BackSound {

    public static final String fileExtension = "bgs";
    private static final int SIGNATURE = 4660;
    private static final int BYTES_PER_SOUNDPLATE = 8;

    private final ArrayList<SoundPlate> soundPlates;

    public BackSound() {
        soundPlates = new ArrayList<>();
        //soundplates.add(new Soundplate());
    }

    public BackSound(String path) throws IOException, WrongFormatException {
        BinaryReader reader = new BinaryReader(path);

        if (reader.readUInt16() != SIGNATURE) {
            throw new WrongFormatException("The signature of the file doesn't correspond to a background sound file");
        }

        final int sectionSize = reader.readUInt16();
        final int numPlates = sectionSize / BYTES_PER_SOUNDPLATE;

        soundPlates = new ArrayList<>(numPlates);
        for (int i = 0; i < numPlates; i++) {
            int soundCode = reader.readUInt8();
            int volume = reader.readUInt8();
            int byte3 = reader.readUInt8(); //Unknown
            int byte4 = reader.readUInt8(); //Unknown
            int x = reader.readUInt8();
            int y = reader.readUInt8();
            int width = reader.readUInt8() - x + 1;
            int height = reader.readUInt8() - y + 1;

            soundPlates.add(new SoundPlate(soundCode, volume, byte3, byte4, x, y, width, height));
        }

        reader.close();
    }

    public void writeToFile(String path) throws IOException {
        BinaryWriter writer = new BinaryWriter(path);

        writer.writeUInt16(SIGNATURE);
        writer.writeUInt16(soundPlates.size() * BYTES_PER_SOUNDPLATE);

        for (SoundPlate soundplate : soundPlates) {
            writer.writeUInt8(soundplate.getSoundCode());
            writer.writeUInt8(soundplate.getVolume());
            writer.writeUInt8(soundplate.byte3);//Unknown
            writer.writeUInt8(soundplate.byte4);//Unknown
            writer.writeUInt8(soundplate.getX());
            writer.writeUInt8(soundplate.getY());
            writer.writeUInt8(soundplate.getWidth() + soundplate.getX() - 1);
            writer.writeUInt8(soundplate.getHeight() + soundplate.getY() - 1);
        }

        writer.close();
    }

    public SoundPlate getSoundplate(int index) {
        return soundPlates.get(index);
    }

}
