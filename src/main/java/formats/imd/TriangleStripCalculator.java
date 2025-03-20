
package formats.imd;

import java.util.ArrayList;

/**
 * @author Trifindo
 */
@SuppressWarnings({"unused", "DuplicatedCode", "SpellCheckingInspection"})
public class TriangleStripCalculator {

    private final float[] vCoords;
    private final float[] tCoords;
    private final        float[] nCoords;
    private final        float[] colors;
    private static final int     vPerVertex = 3;
    private static final int tPerVertex = 2;
    private static final int nPerVertex = 3;
    private static final int cPerVertex = 3;
    private static final int vertexPerFace = 3;
    private static final int edgesPerFace = 3;
    private final int numFaces;
    private final int numEdges;

    private ArrayList<Edge> edges;
    private ArrayList<ArrayList<Integer>> edgesConnected;
    private boolean[] usedFaces;

    private final boolean useUniformNormalOrientation;
    private final boolean useVertexColors;

    public TriangleStripCalculator(PolygonData pData, boolean useUniformNormalOrientation, boolean useVertexColors) {
        this.vCoords = pData.vCoordsTri;
        this.tCoords = pData.tCoordsTri;
        this.nCoords = pData.nCoordsTri;
        this.colors = pData.colorsTri;

        this.useVertexColors = useVertexColors;
        this.useUniformNormalOrientation = useUniformNormalOrientation;

        this.numFaces = vCoords.length / (vPerVertex * vertexPerFace);
        this.numEdges = numFaces * edgesPerFace;
    }

    public ArrayList<PolygonData> calculateTriStrip() {
        edges = calculateEdges();
        edgesConnected = generateConnectedEdges(edges);
        usedFaces = new boolean[numFaces];
        ArrayList<ArrayList<Integer>> strips = new ArrayList<>();
        ArrayList<Integer> looseTris = new ArrayList<>();
        for (int i = 0; i < numFaces; i++) {
            if (!usedFaces[i]) {
                ArrayList<ArrayList<Integer>> stripCandidates = new ArrayList<>();
                for (int j = 0; j < edgesPerFace; j++) {
                    ArrayList<Integer> stripForward = getStripVertexIndices(i, j, true);
                    if (stripContainsFace(stripForward, i)) {
                        stripCandidates.add(stripForward);
                    }
                    ArrayList<Integer> stripBackward = getStripVertexIndices(i, j, false);
                    if (stripContainsFace(stripBackward, i)) {
                        stripCandidates.add(stripBackward);
                    }
                }

                ArrayList<Integer> longestStrip;
                if (!stripCandidates.isEmpty()) {
                    longestStrip = getLongestStrip(stripCandidates);
                } else {
                    longestStrip = new ArrayList<>();
                    for (int j = 0; j < edgesPerFace; j++) {
                        longestStrip.add(i + j);
                    }
                }
                disableFacesUsedByStrip(longestStrip);

                if (longestStrip.size() > vertexPerFace) {
                    strips.add(longestStrip);
                } else {
                    looseTris.addAll(longestStrip);
                }
            }
        }

        ArrayList<PolygonData> pDataStrips = new ArrayList<>();
        if (!looseTris.isEmpty()) {
            PolygonData pDataTriangles = new PolygonData();
            pDataTriangles.initTrisVertices(looseTris.size());
            for (int i = 0; i < looseTris.size(); i++) {
                copyVertexData(vCoords, looseTris.get(i) * vPerVertex, pDataTriangles.vCoordsTri, i * vPerVertex, vPerVertex);
                copyVertexData(tCoords, looseTris.get(i) * tPerVertex, pDataTriangles.tCoordsTri, i * tPerVertex, tPerVertex);
                copyVertexData(nCoords, looseTris.get(i) * nPerVertex, pDataTriangles.nCoordsTri, i * nPerVertex, nPerVertex);
                if (useVertexColors) {
                    copyVertexData(colors, looseTris.get(i) * cPerVertex, pDataTriangles.colorsTri, i * cPerVertex, cPerVertex);
                }
            }
            pDataStrips.add(pDataTriangles);
        } else {
            pDataStrips.add(null);
        }
        for (ArrayList<Integer> strip : strips) {
            PolygonData pDataStrip = new PolygonData();
            pDataStrip.initTrisVertices(strip.size());
            for (int j = 0; j < strip.size(); j++) {
                copyVertexData(vCoords, strip.get(j) * vPerVertex, pDataStrip.vCoordsTri, j * vPerVertex, vPerVertex);
                copyVertexData(tCoords, strip.get(j) * tPerVertex, pDataStrip.tCoordsTri, j * tPerVertex, tPerVertex);
                copyVertexData(nCoords, strip.get(j) * nPerVertex, pDataStrip.nCoordsTri, j * nPerVertex, nPerVertex);
                if (useVertexColors) {
                    copyVertexData(colors, strip.get(j) * cPerVertex, pDataStrip.colorsTri, j * cPerVertex, cPerVertex);
                }
            }
            pDataStrips.add(pDataStrip);
        }

        return pDataStrips;
    }

    private boolean stripContainsFace(ArrayList<Integer> strip, int faceIndex) {
        for (Integer integer : strip) {
            if (integer / edgesPerFace == faceIndex) {
                return true;
            }
        }
        return false;
    }

    private void copyVertexData(float[] src, int srcOffset, float[] dst, int dstOffset, int elePerVertex) {
        if (elePerVertex >= 0) System.arraycopy(src, srcOffset, dst, dstOffset, elePerVertex);
    }

    private ArrayList<Integer> getStripVertexIndices(int faceIndex, int edgeIndexInFace, boolean forward) {
        boolean[] usedFacesInStrip = new boolean[numFaces];

        //Edges backward
        int edgeIndex = getEdgeIndex(faceIndex, edgeIndexInFace);
        int previousEdgeIndex = edgeIndex;
        usedFacesInStrip[edgeIndex / edgesPerFace] = true;

        boolean firstEdge = forward;
        while (isEdgeConnectedToNewFace(edgeIndex, usedFacesInStrip)) {
            int connectedEdgeIndex = edgesConnected.get(edgeIndex).getFirst();
            previousEdgeIndex = connectedEdgeIndex;
            if (firstEdge) {
                edgeIndex = getNextEdgeIndex(connectedEdgeIndex);
            } else {
                edgeIndex = getPrevEdgeIndex(connectedEdgeIndex);
            }
            usedFacesInStrip[edgeIndex / edgesPerFace] = true;

            firstEdge = !firstEdge;
        }
        int firstVertexIndex = getOppositeVertexIndex(previousEdgeIndex);

        //Get full strip starting from first vertex
        ArrayList<Integer> vertexIndices = new ArrayList<>();
        vertexIndices.add(firstVertexIndex);
        vertexIndices.add(getNextEdgeIndex(vertexIndices.get(0)));
        vertexIndices.add(getNextEdgeIndex(vertexIndices.get(1)));

        usedFacesInStrip = new boolean[numFaces];
        edgeIndex = getOppositeEdgeIndex(firstVertexIndex);
        firstEdge = false;
        while (isEdgeConnectedToNewFace(edgeIndex, usedFacesInStrip)) {
            int connectedEdgeIndex = edgesConnected.get(edgeIndex).getFirst();
            if (firstEdge) {
                edgeIndex = getNextEdgeIndex(connectedEdgeIndex);
            } else {
                edgeIndex = getPrevEdgeIndex(connectedEdgeIndex);
            }
            usedFacesInStrip[edgeIndex / edgesPerFace] = true;

            vertexIndices.add(getOppositeVertexIndex(connectedEdgeIndex));

            firstEdge = !firstEdge;
        }

        return vertexIndices;
    }

    private ArrayList<Integer> getLongestStrip(ArrayList<ArrayList<Integer>> strips) {
        int maxSize = -1;
        int maxIndex = 0;
        for (int i = 0; i < strips.size(); i++) {
            if (strips.get(i).size() > maxSize) {
                maxSize = strips.get(i).size();
                maxIndex = i;
            }
        }
        return strips.get(maxIndex);
    }

    private Edge getConnectedEdge(int edgeIndex) {
        return edges.get(edgesConnected.get(edgeIndex).getFirst());
    }

    private boolean isEdgeConnected(int edgeIndex) {
        return !edgesConnected.get(edgeIndex).isEmpty();
    }

    private boolean isEdgeConnectedToNewFace(int edgeIndex, boolean[] localUsedFaces) {
        if (!edgesConnected.get(edgeIndex).isEmpty()) {
            int faceIndex = edgesConnected.get(edgeIndex).getFirst() / edgesPerFace;
            return !usedFaces[faceIndex] && !localUsedFaces[faceIndex];
        }
        return false;
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

    private void disableFacesUsedByStrip(ArrayList<Integer> strip) {
        for (Integer integer : strip) {
            usedFaces[integer / edgesPerFace] = true;
        }
    }

    private int getEdgeIndex(int faceIndex, int edgeIndexInFace) {
        return faceIndex * edgesPerFace + edgeIndexInFace;
    }

    private Edge getEdge(int faceIndex, int edgeIndexInFace) {
        return edges.get(getEdgeIndex(faceIndex, edgeIndexInFace));
    }

    private int getNextEdgeIndex(int edgeIndex) {
        int newIndex = edgeIndex + 1;
        return newIndex - (newIndex % edgesPerFace == 0 ? edgesPerFace : 0);
    }

    private int getPrevEdgeIndex(int edgeIndex) {
        int newIndex = edgeIndex - 1;
        return newIndex + (edgeIndex % edgesPerFace == 0 ? edgesPerFace : 0);
    }

    private int getOppositeVertexIndex(int edgeIndex) {
        int sum = edges.get(edgeIndex).vertexIndex1 + edges.get(edgeIndex).vertexIndex2;
        int mod = edges.get(edgeIndex).vertexIndex1 / edgesPerFace;
        return (mod * 2 + mod + 1) * edgesPerFace - sum;
    }

    private int getOppositeEdgeIndex(int vertexIndex) {
        int newIndex = vertexIndex + 1;
        return newIndex - (newIndex % edgesPerFace == 0 ? edgesPerFace : 0);
    }

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
            if (sameVertexCoords(coords, coordsPerVertex, this.vertexIndex1, other.vertexIndex2)) {
                return sameVertexCoords(coords, coordsPerVertex, this.vertexIndex2, other.vertexIndex1);
            }
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Edge other)) return false;
            if (useUniformNormalOrientation) {
                if (useVertexColors) {
                    return sameEdgeCoords(vCoords, vPerVertex, other)
                            && sameEdgeCoords(tCoords, tPerVertex, other)
                            && sameEdgeCoords(colors, cPerVertex, other);
                } else {
                    return sameEdgeCoords(vCoords, vPerVertex, other)
                            && sameEdgeCoords(tCoords, tPerVertex, other);
                }
            } else {
                if (useVertexColors) {
                    return sameEdgeCoords(vCoords, vPerVertex, other)
                            && sameEdgeCoords(tCoords, tPerVertex, other)
                            && sameEdgeCoords(nCoords, nPerVertex, other)
                            && sameEdgeCoords(colors, cPerVertex, other);
                } else {
                    return sameEdgeCoords(vCoords, vPerVertex, other)
                            && sameEdgeCoords(tCoords, tPerVertex, other)
                            && sameEdgeCoords(nCoords, nPerVertex, other);
                }
            }
        }

    }

}
