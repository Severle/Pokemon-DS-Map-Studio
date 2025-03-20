
package formats.imd;

import formats.imd.nodes.Body;
import formats.imd.nodes.Head;

/**
 * @author Trifindo
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class ImdTileset extends ImdNode {

    public ImdTileset() {
        super("imd");

        subnodes.add(new Head());

        Body body = new Body();


    }


}
