package formats.bdhcam.animation;

import formats.bdhcam.BdhcamHandler;
import formats.bdhcam.camplate.*;
import editor.handler.MapEditorHandler;

@SuppressWarnings("SpellCheckingInspection")
public class CamAnimPosIndep extends CamAnimator{

    private final CamPlatePosIndep plate;
    private       int              frame = 0;
    private int finalFrame = 0;

    public CamAnimPosIndep(MapEditorHandler handler, BdhcamHandler bdhcamHandler, CamPlatePosIndep plate) {
        super(handler, bdhcamHandler);
        this.plate = plate;
        for(CamParameter param : plate.parameters){
            CamParameterPosIndep paramPosIndep = (CamParameterPosIndep) param;
            if(paramPosIndep.duration > finalFrame){
                finalFrame = paramPosIndep.duration;
            }
        }
    }

    @Override
    protected void updateLogic() {
        bdhcamHandler.getDialog().getBdhcamDisplay().setCamera(new CameraSettings(plate, frame));
        frame++;
        if(frame >= finalFrame){
            running = false;
        }
    }

    @Override
    protected void repaint() {
        bdhcamHandler.getDialog().getBdhcamDisplay().repaint();
    }
}
