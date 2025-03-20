
package formats.imd;

import java.util.ArrayList;

/**
 * @author Trifindo
 */
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public class ImdNode {
    public String nodeName;
    public ArrayList<ImdAttribute> attributes;
    public String content;
    public ArrayList<ImdNode> subnodes;

    public ImdNode(String nodeName) {
        this.nodeName = nodeName;
        subnodes = new ArrayList<>();
        attributes = new ArrayList<>();
        content = "";
    }

    public ImdAttribute getAttribute(String tag) {
        for (ImdAttribute p : attributes) {
            if (p.tag.equals(tag)) {
                return p;
            }
        }
        return null;
    }
}
