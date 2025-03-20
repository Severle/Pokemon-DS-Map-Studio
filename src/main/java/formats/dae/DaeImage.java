
package formats.dae;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Trifindo
 */
@Setter
@Getter
public class DaeImage extends DaeNode {

    private String fileName;

    public DaeImage(String id, String name) {
        super(id, name);
    }


}
