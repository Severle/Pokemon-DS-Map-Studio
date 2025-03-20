
package tileset;

import java.util.ArrayList;

/**
 * @author Trifindo
 */
public class TileGeometryDecompressor {

    @SuppressWarnings("SpellCheckingInspection")
    public static float[] decompressObjData(ArrayList<Face> fInds,
                                            ArrayList<Float> coords, int coordsPerVertex, int vertexPerFace) {
        float[] decompressedData = new float[fInds.size() * vertexPerFace * coordsPerVertex];
        for (int i = 0, c = 0; i < fInds.size(); i++) {//face
            Face f = fInds.get(i);
            for (int j = 0; j < vertexPerFace; j++) {//vertex
                for (int k = 0; k < coordsPerVertex; k++, c++) {
                    decompressedData[c] = coords.get((f.tInd[j] - 1) * coordsPerVertex + k);
                }
            }
        }
        return decompressedData;
    }
}
