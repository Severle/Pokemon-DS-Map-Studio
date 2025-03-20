
package formats.bdhc;

import lombok.Getter;

import java.util.ArrayList;

/**
 * @author Trifindo
 */
@SuppressWarnings("SpellCheckingInspection")
@Getter
public class Bdhc {

    public static final String fileExtension = "bdhc";

    public static final int sizePointDP = 12;
    public static final int sizeSlopeDP = 12;
    public static final int sizePlateDP = 12;
    public static final int sizeStripeDP = 10;

    private ArrayList<Plate> plates = new ArrayList<>();

    public Bdhc(ArrayList<Plate> plates) {
        this.plates = plates;
    }

    public Bdhc() {
        plates.add(new Plate());
        plates.add(new Plate());

    }

    public Plate getPlate(int index) {
        return plates.get(index);
    }

    public void addPlate() {
        plates.add(new Plate());
    }


}
