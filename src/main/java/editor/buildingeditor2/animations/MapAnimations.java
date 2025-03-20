
package editor.buildingeditor2.animations;

import formats.narc2.Narc;
import formats.narc2.NarcFile;
import formats.narc2.NarcFolder;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Trifindo
 */
public class MapAnimations {

    private ArrayList<ModelAnimation> animations;

    public MapAnimations(Narc narc) {
        final int numAnimations = narc.root().getFiles().size();
        animations = new ArrayList<>(numAnimations);
        for (int i = 0; i < narc.root().getFiles().size(); i++) {
            animations.add(new ModelAnimation(narc.root().getFiles().get(i).getData(), numAnimations));
        }
    }

    public Narc toNarc() {
        NarcFolder root = new NarcFolder();
        ArrayList<NarcFile> files = new ArrayList<>(animations.size());
        for (ModelAnimation animation : animations) {
            files.add(new NarcFile("", root, animation.getData()));
        }
        root.setFiles(files);
        return new Narc(root);
    }

    public void addAnimation(String path) throws IOException {
        try {
            animations.add(new ModelAnimation(path, animations.size()));
        } catch (Exception ex) {
            throw new IOException();
        }
    }

    public void replaceAnimation(int index, String path) throws IOException {
        if (index >= 0 && index < animations.size()) {
            try {
                animations.set(index, new ModelAnimation(path, animations.size()));
            } catch (Exception ex) {
                throw new IOException();
            }
        }
    }

    public void saveAnimation(int index, String path) throws IOException {
        if (index >= 0 && index < animations.size()) {
            animations.get(index).saveToFile(path);
        }
    }

    public ArrayList<ModelAnimation> getAnimations() {
        return animations;
    }

    public String getAnimationTypeName(int index) {
        return animations.get(index).getAnimationTypeName();
    }

    public int getAnimationType(int index) {
        return animations.get(index).getAnimationType();
    }

}
