
package formats.backsound;

import editor.handler.MapEditorHandler;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * @author Trifindo
 */
@SuppressWarnings("SpellCheckingInspection")
public class BacksoundHandler {

    private final MapEditorHandler handler;
    //private Backsound backsound;

    @Getter
    private final BacksoundEditorDialog dialog;

    @Setter
    @Getter
    private int indexSelected;

    public BacksoundHandler(MapEditorHandler handler, BacksoundEditorDialog dialog) {
        this.handler = handler;
        this.dialog = dialog;

        indexSelected = 0;
    }

    public BackSound getBacksound() {
        return handler.getBacksound();
    }

    public SoundPlate getSelectedSoundplate() {
        return handler.getBacksound().getSoundplate(indexSelected);
    }

    public ArrayList<SoundPlate> getSoundplates() {
        return handler.getBacksound().getSoundPlates();
    }

    public void addSoundplate() {
        handler.getBacksound().getSoundPlates().add(new SoundPlate());
    }

    public void removeSelectedSoundplate() {
        handler.getBacksound().getSoundPlates().remove(indexSelected);
        if (indexSelected > 0) {
            indexSelected--;
        } else {
            indexSelected = 0;
        }
    }
}
