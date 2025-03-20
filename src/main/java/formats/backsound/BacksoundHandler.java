
package formats.backsound;

import editor.handler.MapEditorHandler;

import java.util.ArrayList;

/**
 * @author Trifindo
 */
public class BacksoundHandler {

    private MapEditorHandler handler;
    //private Backsound backsound;

    private BacksoundEditorDialog dialog;

    private int indexSelected = 0;

    public BacksoundHandler(MapEditorHandler handler, BacksoundEditorDialog dialog) {
        this.handler = handler;
        this.dialog = dialog;

        indexSelected = 0;
    }

    public int getIndexSelected() {
        return indexSelected;
    }

    public void setIndexSelected(int index) {
        this.indexSelected = index;
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

    public BacksoundEditorDialog getDialog() {
        return dialog;
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
