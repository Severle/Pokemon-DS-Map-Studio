
package formats.narc2;


import lombok.Getter;

@SuppressWarnings("unused")
public class NarcFile {

    @Getter
    private final String     name;
    private final NarcFolder parent;
    @Getter
    private       byte[]     data;

    public NarcFile(String name, NarcFolder parent) {
        this.name = name;
        this.parent = parent;
    }

    public NarcFile(String name, NarcFolder parent, byte[] data) {
        this.name = name;
        this.parent = parent;
        this.data = data;
    }
}
