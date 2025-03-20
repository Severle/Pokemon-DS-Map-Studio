
package formats.narc2;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
public class NarcFolder {

    @Setter
    private       String                name = "";
    @Setter
    private       NarcFolder            parent;
    private final ArrayList<NarcFolder> subfolders = new ArrayList<>();
    @Setter
    private       ArrayList<NarcFile>   files      = new ArrayList<>();
    @Setter
    private       int                   ID;

    public NarcFolder() {
        name = "";
    }

    public NarcFolder(NarcFolder parent) {
        this.parent = parent;
    }

    public NarcFolder(String name, NarcFolder parent) {
        this.name = name;
        this.parent = parent;
    }

    public NarcFolder getFolderByName(String folderName){
        for(NarcFolder folder : subfolders){
            if(folder.getName().equals(folderName)){
                return folder;
            }
        }
        return null;
    }

    public NarcFile getFileByName(String fileName){
        for(NarcFile file : files){
            if(file.getName().equals(fileName)){
                return file;
            }
        }
        return null;
    }

}
