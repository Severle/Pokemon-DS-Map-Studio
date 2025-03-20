
package geometry;

import java.util.ArrayList;

/**
 * @author Trifindo
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public class TrisToQuads {

    private final float[] vCoordsTri;
    private final float[] nCoordsTri;
    private final float[] tCoordsTri;

    @SuppressWarnings("FieldCanBeLocal")
    private final float[] vCoordsQuad;
    @SuppressWarnings("FieldCanBeLocal")
    private final float[] nCoordsQuad;
    @SuppressWarnings("FieldCanBeLocal")
    private final float[] tCoordsQuad;

    private static final int vPerVertex = 3;
    private static final int tPerVertex = 2;
    private static final int nPerVertex = 3;
    private static final int vertexPerFace = 3;
    private static final int edgesPerFace = 3;
    private final int numFaces;
    private final int numEdges;

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private ArrayList<Edge>               edges;
    @SuppressWarnings("FieldCanBeLocal")
    private ArrayList<ArrayList<Integer>> edgesConnected;
    @SuppressWarnings({"FieldCanBeLocal"})
    private boolean[]                     usedFaces;

    public TrisToQuads(PolygonData pData) {

        this.vCoordsTri = pData.vCoordsTri;
        this.tCoordsTri = pData.tCoordsTri;
        this.nCoordsTri = pData.nCoordsTri;

        this.vCoordsQuad = pData.vCoordsQuad;
        this.tCoordsQuad = pData.tCoordsQuad;
        this.nCoordsQuad = pData.nCoordsQuad;

        this.numFaces = vCoordsTri.length / (vPerVertex * vertexPerFace);
        this.numEdges = numFaces * edgesPerFace;
    }

    private ArrayList<Edge> calculateEdges() {
        ArrayList<Edge> edges = new ArrayList<>(numEdges);
        for (int i = 0; i < numFaces; i++) {
            int offset = edgesPerFace * i;
            for (int j = 0; j < edgesPerFace; j++) {
                edges.add(new Edge(offset + j, offset + (j + 1) % edgesPerFace));
            }
        }
        return edges;
    }

    @SuppressWarnings("SpellCheckingInspection")
    private ArrayList<ArrayList<Integer>> generateConnectedEdges(ArrayList<Edge> edges) {
        ArrayList<ArrayList<Integer>> conectedEdges = new ArrayList<>(numEdges);
        for (int i = 0; i < numEdges; i++) {
            conectedEdges.add(new ArrayList<>());
        }
        for (int i = 0; i < numEdges; i++) {
            for (int j = i + 1; j < numEdges; j++) {
                if (edges.get(i).equals(edges.get(j))) {
                    conectedEdges.get(i).add(j);
                    conectedEdges.get(j).add(i);
                }
            }
        }
        return conectedEdges;
    }

    public static class PolygonData {

        public float[] vCoordsTri;
        public float[] nCoordsTri;
        public float[] tCoordsTri;

        public float[] vCoordsQuad;
        public float[] nCoordsQuad;
        public float[] tCoordsQuad;
    }

    @SuppressWarnings("DuplicatedCode")
    private class Edge {

        public int vertexIndex1;
        public int vertexIndex2;

        public Edge(int vertexIndex1, int vertexIndex2) {
            this.vertexIndex1 = vertexIndex1;
            this.vertexIndex2 = vertexIndex2;
        }

        public Edge(int edgeIndex) {
            vertexIndex1 = edges.get(edgeIndex).vertexIndex1;
            vertexIndex2 = edges.get(edgeIndex).vertexIndex2;
        }

        public void flip() {
            int temp = vertexIndex1;
            vertexIndex1 = vertexIndex2;
            vertexIndex2 = temp;
        }

        @SuppressWarnings("SpellCheckingInspection")
        private boolean sameVertexCoords(float[] coordData, int coordsPerVertex,
                                         int vertexIndex1, int vertexIndex2) {
            int offset1 = vertexIndex1 * coordsPerVertex;
            int offset2 = vertexIndex2 * coordsPerVertex;
            for (int i = 0; i < coordsPerVertex; i++) {
                if (Math.abs(coordData[offset1 + i] - coordData[offset2 + i]) > 0.0001f) {
                    return false;
                }
            }
            return true;
        }

        private boolean sameEdgeCoords(float[] coords, int coordsPerVertex,
                                       Edge other) {
            /*if (sameVertexCoords(coords, coordsPerVertex, this.vertexIndex1, other.vertexIndex1)) {
                if (sameVertexCoords(coords, coordsPerVertex, this.vertexIndex2, other.vertexIndex2)) {
                    return true;
                }
            } else */
            if (sameVertexCoords(coords, coordsPerVertex, this.vertexIndex1, other.vertexIndex2)) {
                return sameVertexCoords(coords, coordsPerVertex, this.vertexIndex2, other.vertexIndex1);
            }
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Edge other)) return false;
            return sameEdgeCoords(vCoordsTri, vPerVertex, other)
                    && sameEdgeCoords(tCoordsTri, tPerVertex, other)
                    && sameEdgeCoords(nCoordsTri, nPerVertex, other);
        }

    }

}
