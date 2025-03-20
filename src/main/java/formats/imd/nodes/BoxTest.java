
package formats.imd.nodes;

import formats.imd.ImdAttribute;
import formats.imd.ImdNode;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;

/**
 * @author Trifindo
 */
@Log4j2
public class BoxTest extends ImdNode {

    public BoxTest(int posScale, float[] xyz, float[] whd) {
        super("box_test");
        log.debug("Box test xyz: {} {} {}", xyz[0], xyz[1], xyz[2]);
        log.debug("Box test whd: {} {} {}", whd[0], whd[1], whd[2]);
        attributes = new ArrayList<>() {
            {
                add(new ImdAttribute("pos_scale", posScale));
                add(new ImdAttribute("xyz", xyz));
                add(new ImdAttribute("whd", whd));
            }
        };
    }

}
