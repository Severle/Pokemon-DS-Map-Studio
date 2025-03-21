
package editor.buildingeditor2.animations;

import lombok.Getter;
import lombok.Setter;
import utils.io.BinaryReader;
import utils.io.BinaryWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Trifindo
 */
@Setter
@Getter
@SuppressWarnings("SpellCheckingInspection")
public class BuildAnimInfoHGSS {

    public static final int MAX_ANIMS_PER_BUILDING = 4;

    public static final Map<Integer, String> namesAnimType1 = new HashMap<>() {
        {
            put(255, "No Animation");
            put(0, "Loop");
            put(2, "Trigger (?)");
            put(3, "Trigger");
            put(8, "Day/Night Cycle");
        }
    };

    public static final Map<Integer, String> namesLoopType = new HashMap<>() {
        {
            put(0, "Loop");
            put(1, "Trigger");
        }
    };

    public static final Map<Integer, String> namesDoorSound = new HashMap<>() {
        {
            put(0, "No sound");
            put(1, "Wooden Door");
            put(2, "Automatic Door");
            put(3, "Old door (?)");
            put(4, "Sliding Door");
        }
    };

    public static final Map<Integer, String> namesNumAnims = new HashMap<>() {
        {
            put(0, "0");
            put(1, "1");
            put(2, "2");
            put(3, "3");
            put(4, "4");
        }
    };

    public static final Map<Integer, String> namesAnimType2 = new HashMap<>() {
        {
            put(0, "No Animation");
            put(1, "Loop");
            put(2, "Trigger");
        }
    };

    public static final Map<String, Integer> namesAnimType1Swap = namesAnimType1.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    public static final Map<String, Integer> namesLoopTypeSwap = namesLoopType.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    public static final Map<String, Integer> namesDoorSoundSwap = namesDoorSound.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    public static final Map<String, Integer> namesNumAnimsSwap = namesNumAnims.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    public static final Map<String, Integer> namesAnimType2Swap = namesAnimType2.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));


    private ArrayList<Integer> animIDs = new ArrayList<>();
    private int animType1;//second byte: FF=no animation, 00=loop, 02=trigger(?), 03=trigger, 08=day/night cycle
    private int loopType; //fourth byte: 00=Loop, 01=Trigger

    private int doorSound; //first byte; 00, 01, 02, 03
    private int numAnims; //third byte; Num animations (?)
    private int animType2; //fourth byte: 00=no animation, 01=loop animation, 02=trigger animation

    public BuildAnimInfoHGSS() {
        animType1 = 0xFF;
        loopType = 0;

        doorSound = 0;
        numAnims = 0;
        animType2 = 0;
    }

    public BuildAnimInfoHGSS(byte[] data) throws Exception {
        animType1 = BinaryReader.readUInt8(data, 1);
        loopType = BinaryReader.readUInt8(data, 3);

        doorSound = BinaryReader.readUInt8(data, 4);
        numAnims = BinaryReader.readUInt8(data, 6);
        animType2 = BinaryReader.readUInt8(data, 7);

        for (int i = 0; i < MAX_ANIMS_PER_BUILDING; i++) {
            int anim = (int) BinaryReader.readUInt32(data, i * MAX_ANIMS_PER_BUILDING + 8);
            if (anim != 0xFFFFFFFF) {
                animIDs.add(anim);
            }
        }
    }

    public byte[] toByteArray() throws Exception {
        byte[] data = new byte[24];

        if (!animIDs.isEmpty()) {//Has animation
            BinaryWriter.writeUInt8(data, 0, 0x01);
        } else {
            BinaryWriter.writeUInt8(data, 0, 0xFF);
        }
        BinaryWriter.writeUInt8(data, 1, animType1);
        BinaryWriter.writeUInt8(data, 3, loopType);
        BinaryWriter.writeUInt8(data, 4, doorSound);
        BinaryWriter.writeUInt8(data, 6, numAnims);
        BinaryWriter.writeUInt8(data, 7, animType2);

        int i;
        for (i = 0; i < animIDs.size(); i++) {
            BinaryWriter.writeUInt32(data, 8 + i * MAX_ANIMS_PER_BUILDING, animIDs.get(i));
        }
        for (; i < MAX_ANIMS_PER_BUILDING; i++) {
            BinaryWriter.writeUInt32(data, 8 + i * MAX_ANIMS_PER_BUILDING, 0xFFFFFFFFL);
        }
        return data;
    }
}
