package formats.collisions.bw;

@SuppressWarnings("unused")
public record CParam2D(int ID, Float slopeX, Float slopeY) implements Comparable<CParam2D> {

    @Override
    public int compareTo(CParam2D o) {
        return slopeX.compareTo(o.slopeX);
    }


}
