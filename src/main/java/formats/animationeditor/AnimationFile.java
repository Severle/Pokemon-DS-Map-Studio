
package formats.animationeditor;

import utils.io.BinaryReader;
import utils.io.BinaryWriter;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Trifindo
 */
public class AnimationFile {

    private final ArrayList<Animation> animations;

    public AnimationFile(String path) throws IOException,
            NullPointerException {
        BinaryReader br = new BinaryReader(path);

        int numAnimations = (int) br.readUInt32();
        animations = new ArrayList<>(numAnimations);
        for (int i = 0; i < numAnimations; i++) {
            String name = Utils.removeLastOccurrences(br.readString(16), '\u0000');
            int[] frames = new int[Animation.maxNumFrames];
            int[] delays = new int[Animation.maxNumFrames];
            for (int j = 0; j < Animation.maxNumFrames; j++) {
                frames[j] = br.readUInt8();
                delays[j] = br.readUInt8();
            }
            animations.add(new Animation(name, frames, delays));
        }

        br.close();
    }

    public void saveAnimationFile(String path) throws IOException {
        BinaryWriter bw = new BinaryWriter(path);

        int numAnimations = animations.size();
        bw.writeUInt32(numAnimations);

        for (Animation animation : animations) {
            bw.writeString(animation.getName(), Animation.maxNameSize, (byte) 0);
            for (int j = 0; j < Animation.maxNumFrames; j++) {
                bw.writeUInt8(animation.getFrame(j));
                bw.writeUInt8(animation.getDelay(j));
            }
        }
        bw.close();
    }

    public Animation getAnimation(int index) {
        if (animations != null) {
            if (index >= 0 && index < animations.size()) {
                return animations.get(index);
            }
        }
        return null;

    }

    public void addAnimation(String name) {
        animations.add(new Animation(name));
    }

    public void removeAnimation(int index) {
        animations.remove(index);
    }

    public int size() {
        if (animations != null) {
            return animations.size();
        } else {
            return 0;
        }
    }

}
