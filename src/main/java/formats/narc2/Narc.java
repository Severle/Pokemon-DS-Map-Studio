
package formats.narc2;

import utils.Utils.MutableInt;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("unused")
public record Narc(NarcFolder root) {

    public boolean hasNamedFiles() {
        return hasNamedFiles(root);
    }

    private boolean hasNamedFiles(NarcFolder folder) {
        for (NarcFile file : folder.getFiles()) {
            if (file.getName() != null) {
                if (!file.getName().isEmpty()) {
                    return true;
                }
            }
        }
        for (NarcFolder subfolder : folder.getSubfolders()) {
            if (hasNamedFiles(subfolder)) {
                return true;
            }
        }
        return false;
    }

    public void calculateIndices() {
        root.setID(0);
        calculateIndices(root, new MutableInt(0));
    }

    private static void calculateIndices(NarcFolder folder, MutableInt lastID) {
        for (NarcFolder subfolder : folder.getSubfolders()) {
            subfolder.setID(++lastID.value);
            calculateIndices(subfolder, lastID);
        }
    }

    public int getNumDirectories() {
        MutableInt sum = new MutableInt(1);
        getNumDirectories(root.getSubfolders(), sum);
        return sum.value;
    }

    private static void getNumDirectories(ArrayList<NarcFolder> subfolders, MutableInt sum) {
        sum.value += subfolders.size();
        for (NarcFolder subfolder : subfolders) {
            getNumDirectories(subfolder.getSubfolders(), sum);
        }
    }

    public ArrayList<NarcFile> getAllFiles() {
        ArrayList<NarcFile> files = new ArrayList<>();
        addFolderFiles(files, root);
        return files;
    }

    private static void addFolderFiles(ArrayList<NarcFile> files, NarcFolder folder) {
        files.addAll(folder.getFiles());
        for (NarcFolder subfolder : folder.getSubfolders()) {
            addFolderFiles(files, subfolder);
        }
    }

    public ArrayList<NarcFolder> getAllFolders() {
        ArrayList<NarcFolder> folders = new ArrayList<>();
        folders.add(root);
        addFolderSubfolders(folders, root);
        return folders;
    }

    private static void addFolderSubfolders(ArrayList<NarcFolder> folders, NarcFolder folder) {
        for (NarcFolder subfolder : folder.getSubfolders()) {
            folders.add(subfolder);
            addFolderSubfolders(folders, subfolder);
        }
    }

    public NarcFile getFileByPath(String path) {
        return getFileByPath(path.split(File.pathSeparator));
    }

    public NarcFile getFileByPath(String[] splitPath) {
        NarcFolder nextFolder = root;
        for (int i = 0; i < splitPath.length - 1; i++) {
            nextFolder = nextFolder.getFolderByName(splitPath[i]);
            if (nextFolder == null) {
                return null;
            }
        }
        return nextFolder.getFileByName(splitPath[splitPath.length - 1]);
    }

}
