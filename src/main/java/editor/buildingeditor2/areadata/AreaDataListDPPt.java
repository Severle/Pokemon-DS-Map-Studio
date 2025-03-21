
package editor.buildingeditor2.areadata;

import formats.narc2.Narc;
import formats.narc2.NarcFile;
import formats.narc2.NarcFolder;
import lombok.Getter;

import java.util.ArrayList;

/**
 * @author Trifindo
 */
@SuppressWarnings("SpellCheckingInspection")
@Getter
public class AreaDataListDPPt {

    private final ArrayList<AreaDataDPPt> areaDatas;

    public AreaDataListDPPt(Narc narc) throws Exception {
        final int numFiles = narc.root().getFiles().size();
        areaDatas = new ArrayList<>(numFiles);
        for (int i = 0; i < numFiles; i++) {
            areaDatas.add(new AreaDataDPPt(narc.root().getFiles().get(i).getData()));
        }
    }

    public Narc toNarc() throws Exception {
        NarcFolder root = new NarcFolder();
        ArrayList<NarcFile> files = new ArrayList<>(areaDatas.size());
        for (AreaDataDPPt areaData : areaDatas) {
            files.add(new NarcFile("", root, areaData.toByteArray()));
        }
        root.setFiles(files);
        return new Narc(root);
    }

}
