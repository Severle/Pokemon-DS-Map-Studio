
package formats.imd.nodes;

import formats.imd.ImdAttribute;
import formats.imd.ImdNode;

import java.util.ArrayList;

/**
 * @author Trifindo
 */
public class Head extends ImdNode {

    public Head() {
        super("head");

        ImdNode snCreate = new ImdNode("create");
        snCreate.attributes = new ArrayList<>() {
            {
                add(new ImdAttribute("user", "unknown"));
                add(new ImdAttribute("host", "unknown"));
                add(new ImdAttribute("date", "2019-01-11T13:52:52"));
                add(new ImdAttribute("source", "untitled"));
            }
        };
        subnodes.add(snCreate);

        ImdNode snTitle = new ImdNode("title");
        snTitle.content = "Model Data for NINTENDO NITRO-System";
        subnodes.add(snTitle);

        ImdNode snGenerator = new ImdNode("generator");
        snGenerator.attributes = new ArrayList<>() {
            {
                add(new ImdAttribute("name", "Pokemon DS Map Studio"));
                add(new ImdAttribute("version", "1.0"));
            }
        };
        subnodes.add(snGenerator);

    }

}
