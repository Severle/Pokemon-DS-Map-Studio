
package formats.backsound;

import editor.grid.MapGrid;
import lombok.Getter;
import lombok.Setter;

import java.awt.Rectangle;

/**
 * @author Trifindo
 */
public class SoundPlate {

    @Setter
    @Getter
    public int soundCode;
    @Setter
    @Getter
    public int volume;
    public int byte3;
    public int byte4;
    @Setter
    @Getter
    public int x, y;
    @Setter
    @Getter
    public int width, height;

    public SoundPlate(int soundCode, int volume, int byte3, int byte4, int x, int y, int width, int height) {
        this.soundCode = fitInBounds(soundCode, 0, 15);
        this.volume = fitInBounds(volume, 0, 2);
        this.byte3 = byte3;
        this.byte4 = byte4;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public SoundPlate() {
        this(0, 0, 0, 0, MapGrid.cols / 2, MapGrid.rows / 2, 2, 2);
    }

    @SuppressWarnings("SameParameterValue")
    private static int fitInBounds(int value, int min, int max) {
        return Math.max(Math.min(value, max), min);
    }

    @SuppressWarnings("unused")
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
