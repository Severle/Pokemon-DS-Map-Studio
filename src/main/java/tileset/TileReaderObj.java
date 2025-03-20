
package tileset;

import utils.Utils;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Trifindo
 */
@SuppressWarnings({"SpellCheckingInspection", "DuplicatedCode", "unused"})
public class TileReaderObj {

    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "UnusedAssignment", "CommentedOutCode"})
    public static Tile2 loadTileObj(String folderPath, String objName, Tileset tileset)
            throws IOException, IndexOutOfBoundsException, TextureNotFoundException {

        //Load OBJ file data
        InputStream inputObj = new FileInputStream(folderPath + "/" + objName);
        BufferedReader brObj = new BufferedReader(new InputStreamReader(inputObj));

        ArrayList<ArrayList<Float>> vCoordsObj = new ArrayList<>();
        ArrayList<ArrayList<Float>> tCoordsObj = new ArrayList<>();
        ArrayList<ArrayList<Float>> nCoordsObj = new ArrayList<>();

        ArrayList<String> materialNames = new ArrayList<>();

        ArrayList<ArrayList<Face>> fIndsQuadArray = new ArrayList<>();
        ArrayList<ArrayList<Face>> fIndsTriArray = new ArrayList<>();

        String mtlName = "";
        int materialIndex = 0;
        String lineObj;
        while ((lineObj = brObj.readLine()) != null) {
            if (lineObj.startsWith("mtllib")) {
                mtlName = lineObj.substring(lineObj.indexOf(" ") + 1);
            } else //noinspection StatementWithEmptyBody
                if (lineObj.startsWith("o")) {

            } else if (lineObj.startsWith("v ")) {
                vCoordsObj.add(loadFloatLineObj(lineObj, 3, 3));
            } else if (lineObj.startsWith("vt")) {
                tCoordsObj.add(loadFloatLineObj(lineObj, 2, 2));
            } else if (lineObj.startsWith("vn")) {
                nCoordsObj.add(loadFloatLineObj(lineObj, 3, 3));
            } else if (lineObj.startsWith("usemtl")) {
                String name = lineObj.split(" ")[1];
                materialIndex = materialNames.indexOf(name);
                if (materialIndex == -1) {
                    fIndsQuadArray.add(new ArrayList<>());
                    fIndsTriArray.add(new ArrayList<>());
                    materialIndex = materialNames.size();
                    materialNames.add(name);
                }
            } else if (lineObj.startsWith("f")) {
                String[] splittedLine = (lineObj.substring(2)).split(" ");
                int numVertex = 0;
                    for (String s : splittedLine) {
                        if (s.contains("/")) {
                            numVertex++;
                        }
                    }
                if (numVertex > 3) {
                    fIndsQuadArray.get(materialIndex).add(loadFaceIndicesObj(splittedLine, 4));
                } else {
                    fIndsTriArray.get(materialIndex).add(loadFaceIndicesObj(splittedLine, 3));
                }
            }
        }
        inputObj.close();

        //Load Mtl file
        ArrayList<Integer> textureIDs = new ArrayList<>();
        int numMaterials = countNumberOfStarts(new File(folderPath + "/" + mtlName));
        for (int i = 0; i < numMaterials; i++) {
            textureIDs.add(0);
        }
        InputStream inputMtl = new FileInputStream(folderPath + "/" + mtlName);
        BufferedReader brMtl = new BufferedReader(new InputStreamReader(inputMtl));
        int matIndex = 0;
        String lineMtl;
        while ((lineMtl = brMtl.readLine()) != null) {
            if (lineMtl.startsWith("newmtl")) {
                String matName = lineMtl.split(" ")[1];
                matIndex = materialNames.indexOf(matName);
            } else if (lineMtl.startsWith("map_Kd")) {
                String textName = lineMtl.split(" ")[1];
                int textIndex = tileset.getIndexOfMaterialByImgName(textName);
                if (textIndex == -1) { //Not found
                    if (new File(folderPath + "/" + textName).exists()) {
                        textureIDs.set(matIndex, tileset.getMaterials().size());
                        try {
                            TilesetMaterial material = getTilesetMaterial(folderPath, textName);
                            tileset.getMaterials().add(material);
                        } catch (IOException ex) {
                            throw new TextureNotFoundException(
                                    "Can't open texture named: \"" + textName + "\"" + "\n"
                                            + "Make sure that the texture is in the same folder as the OBJ file");
                        }
                    } else {
                        throw new TextureNotFoundException(
                                "Texture named: \"" + textName
                                        + "\" does not exist in the folder of the OBJ file");
                    }
                } else {
                    textureIDs.set(matIndex, textIndex);
                }
            }
        }
        inputMtl.close();


        //Fix material names for avoiding sub folder issues
        for (int i = 0; i < textureIDs.size(); i++) {
            TilesetMaterial material = tileset.getMaterial(textureIDs.get(i));
            String[] splittedName = material.getImageName().split("/");
            if (splittedName.length > 1) {
                String newName = splittedName[splittedName.length - 1];
                int index = tileset.getIndexOfMaterialByImgName(newName);
                if (index == -1) {
                    material.setImageName(newName);
                    newName = Utils.removeExtensionFromPath(newName);
                    material.setMaterialName(newName);
                    material.setTextureNameImd(newName);
                    material.setPaletteNameImd(newName + "_pl");
                } else {
                    textureIDs.set(i, index);
                }
            }
        }

        //Fix duplicated Texture IDs
        ArrayList<ArrayList<Face>> fIndsQuadArrayFixed = new ArrayList<>();
        ArrayList<ArrayList<Face>> fIndsTriArrayFixed = new ArrayList<>();
        ArrayList<Integer> textureIDsFixed = new ArrayList<>();
        for (int i = 0; i < textureIDs.size(); i++) {
            int id = textureIDs.get(i);
            int index = textureIDsFixed.indexOf(id);
            if (index == -1) {
                ArrayList<Face> fIndsQuad = new ArrayList<>(fIndsQuadArray.get(i));
                ArrayList<Face> fIndsTri  = new ArrayList<>(fIndsTriArray.get(i));
                fIndsQuadArrayFixed.add(fIndsQuad);
                fIndsTriArrayFixed.add(fIndsTri);
                textureIDsFixed.add(id);
            } else {
                ArrayList<Face> fIndsQuad = fIndsQuadArrayFixed.get(index);
                ArrayList<Face> fIndsTri = fIndsTriArrayFixed.get(index);
                fIndsQuad.addAll(fIndsQuadArray.get(i));
                fIndsTri.addAll(fIndsTriArray.get(i));
            }
        }
        textureIDs = textureIDsFixed;
        fIndsQuadArray = fIndsQuadArrayFixed;
        fIndsTriArray = fIndsTriArrayFixed;
        
        
        /*
        //Group all indices by material into a single array
        ArrayList<Face> fIndsQuad = new ArrayList<>();
        ArrayList<Face> fIndsTri = new ArrayList<>();
        for (int i = 0, c = 0; i < fIndsQuadArray.size(); i++) {
            texOffsetsQuad.add(c);
            for (int j = 0; j < fIndsQuadArray.get(i).size(); j++, c++) {
                fIndsQuad.add(fIndsQuadArray.get(i).get(j));
            }
        }
        for (int i = 0, c = 0; i < fIndsTriArray.size(); i++) {
            texOffsetsTri.add(c);
            for (int j = 0; j < fIndsTriArray.get(i).size(); j++, c++) {
                fIndsTri.add(fIndsTriArray.get(i).get(j));
            }
        }
        */
        return null;//Change this
    }

    private static TilesetMaterial getTilesetMaterial(String folderPath, String textName) throws IOException {
        TilesetMaterial material = new TilesetMaterial();
        material.loadTextureImgFromPath(folderPath + "/" + textName);
        material.setImageName(textName);
        String textNameImd = Utils.removeExtensionFromPath(textName);
        material.setMaterialName(textNameImd);
        material.setTextureNameImd(textNameImd);
        material.setPaletteNameImd(textNameImd + "_pl");
        return material;
    }

    private static ArrayList<Float> loadFloatLineObj(String line, int minNumElemn,
                                                     int maxNumElem) {
        String[] splittedLine = line.split(" ");
        int numElements = Math.max(Math.min(maxNumElem, splittedLine.length - 1), minNumElemn);
        ArrayList<Float> floats = new ArrayList<>(numElements);
        for (int i = 0; i < numElements; i++) {
            float value;
            try {
                value = Float.parseFloat(splittedLine[i + 1]);
            } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                value = (float) 0.0;
            }
            floats.add(value);
        }
        return floats;
    }

    private static Face loadFaceIndicesObj(String[] splittedLine, int numVertex) {
        Face f = new Face(numVertex);
        for (int i = 0; i < numVertex; i++) {
            String[] sArray = splittedLine[i].split("/");
            f.vInd[i] = Integer.parseInt(sArray[0]);
            f.tInd[i] = Integer.parseInt(sArray[1]);
            if (sArray.length > 2) {
                f.nInd[i] = Integer.parseInt(sArray[2]);
            } else {
                f.nInd[i] = -1;
            }
        }
        return f;
    }

    private static int countNumberOfStarts(File file) throws IOException {
        InputStream input = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        int count = 0;
        String line;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("newmtl")) {
                count++;
            }
        }
        return count;
    }

    private static class Face {

        public int[] vInd;
        public int[] tInd;
        public int[] nInd;

        public Face(int nVertex) {
            vInd = new int[nVertex];
            tInd = new int[nVertex];
            nInd = new int[nVertex];
        }
    }

}
