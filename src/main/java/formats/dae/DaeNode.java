
package formats.dae;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Trifindo
 */
@Setter
@Getter
public class DaeNode {

    protected String id;
    protected String name;

    public DaeNode(String id, String name) {
        this.id = id;
        this.name = name;
    }


}
