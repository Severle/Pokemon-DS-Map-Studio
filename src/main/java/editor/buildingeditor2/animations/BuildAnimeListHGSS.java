
package editor.buildingeditor2.animations;

import formats.narc2.Narc;
import formats.narc2.NarcFile;
import formats.narc2.NarcFolder;
import lombok.Getter;

import java.util.ArrayList;

/**
 * @author Trifindo
 */
@Getter
public class BuildAnimeListHGSS {
    private final ArrayList<BuildAnimInfoHGSS> animations;

    public BuildAnimeListHGSS(Narc narc) {
        NarcFolder root = narc.root();
        animations = new ArrayList<>(root.getFiles().size());
        for (NarcFile file : root.getFiles()) {
            try {
                animations.add(new BuildAnimInfoHGSS(file.getData()));
            } catch (Exception ex) {
                animations.add(new BuildAnimInfoHGSS());
            }
        }
    }

    public Narc toNarc() throws Exception {
        NarcFolder root = new NarcFolder();

        ArrayList<NarcFile> files = new ArrayList<>(animations.size());
        for (BuildAnimInfoHGSS anim : animations) {
            files.add(new NarcFile("", root, anim.toByteArray()));
        }
        root.setFiles(files);
        return new Narc(root);
    }

    @SuppressWarnings("unused")
    public void addBuildingAnimation(ArrayList<Integer> newAnimations, byte secondByte) {
        BuildAnimInfoHGSS anim = new BuildAnimInfoHGSS();
        anim.setAnimIDs(newAnimations);
        animations.add(anim);
    }

    public void addBuildingAnimation(int buildIndex, int animationIndex) {
        if (buildIndex >= 0 && buildIndex < animations.size()) {
            ArrayList<Integer> buildAnimations = animations.get(buildIndex).getAnimIDs();
            if (buildAnimations.size() < BuildAnimInfoHGSS.MAX_ANIMS_PER_BUILDING) {
                buildAnimations.add(animationIndex);
            }
        }
    }

    public void removeBuildingAnimation(int buildIndex, int animationIndex) {
        if (buildIndex >= 0 && buildIndex < animations.size()) {
            ArrayList<Integer> buildAnimations = animations.get(buildIndex).getAnimIDs();
            if (!buildAnimations.isEmpty()) {
                buildAnimations.remove(animationIndex);
                if (buildAnimations.isEmpty()) {
                    animations.get(buildIndex).setAnimIDs(new ArrayList<>());
                }
            }
        }
    }

    public void replaceBuildingAnimation(int buildIndex, int animationIndex, int oldAnimationIndex) {
        if (buildIndex >= 0 && buildIndex < animations.size()) {
            ArrayList<Integer> buildAnimations = animations.get(buildIndex).getAnimIDs();
            if (oldAnimationIndex >= 0 && oldAnimationIndex < buildAnimations.size()) {
                buildAnimations.set(oldAnimationIndex, animationIndex);
            }
        }
    }

    public void replaceBuildingAnimationIDs(int index, ArrayList<Integer> newAnimations) {
        if (index >= 0 && index < animations.size()) {
            animations.get(index).setAnimIDs(newAnimations);
        }
    }

    public void removeBuildingAnimation(int index) {
        if (index >= 0 && index < animations.size()) {
            animations.remove(index);
        }
    }

}
