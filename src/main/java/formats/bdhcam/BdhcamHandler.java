package formats.bdhcam;

import formats.bdhcam.animation.CamAnimPosDep;
import formats.bdhcam.animation.CamAnimPosIndep;
import formats.bdhcam.animation.CamAnimator;
import formats.bdhcam.camplate.CamParameter;
import formats.bdhcam.camplate.CamPlate;
import formats.bdhcam.camplate.CamPlatePosDep;
import formats.bdhcam.camplate.CamPlatePosIndep;
import editor.handler.MapEditorHandler;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("SpellCheckingInspection")
public class BdhcamHandler {

    private final MapEditorHandler   handler;
    //private Bdhcam bdhcam;//Move this to Map Editor Handler
    @Getter
    private final BdhcamEditorDialog dialog;
    @Getter
    private       int                indexSelected      = 0;
    @Getter
    @Setter
    private       int                indexParamSelected = 0;

    @Getter
    private CamAnimator animator;

    @Setter
    @Getter
    private float playerX = 16.0f, playerY = 16.0f;

    public BdhcamHandler(MapEditorHandler handler, BdhcamEditorDialog dialog){
        this.handler = handler;
        this.dialog = dialog;

    }

    public Bdhcam getBdhcam() {
        return handler.getBdhcam();
    }

    public CamPlate getSelectedPlate(){
        Bdhcam bdhcam = handler.getBdhcam();
        if(indexSelected < bdhcam.getPlates().size()){
            return bdhcam.getPlates().get(indexSelected);
        }
        return null;
    }

    public CamParameter getSelectedParameter(){
        CamPlate plate = getSelectedPlate();
        if(plate != null){
            if(indexParamSelected < plate.parameters.size()){
                return plate.parameters.get(indexParamSelected);
            }
        }
        return null;
    }

    public void setSelectedPlate(int index){
        this.indexSelected = index;
        if(getSelectedPlate() != null){
            setPlayerInPlate(getSelectedPlate());
        }
    }

    public float getSelectedPlateZ(){
        CamPlate plate = getSelectedPlate();
        if(plate != null){
            if(plate.useZ){
                return plate.z;
            }
        }
        return 0.0f;
    }

    public void setPlayerInPlate(CamPlate plate){
        float[] center = plate.getCenter();
        playerX = (int)center[0];
        playerY = (int)center[1];
    }

    public void startAnimation(){
        Bdhcam bdhcam = handler.getBdhcam();
        if(!bdhcam.getPlates().isEmpty()) {
            if (animator != null) {
                animator.finish();
            }

            if (getSelectedPlate().type.ID == CamPlate.Type.POS_INDEPENDENT.ID) {
                animator = new CamAnimPosIndep(handler, this, (CamPlatePosIndep) getSelectedPlate());
                animator.start();
            }else{
                animator = new CamAnimPosDep(handler, this, (CamPlatePosDep) getSelectedPlate());
                animator.start();
            }
        }
    }

    public void stopAnimation(){
        if (animator != null) {
            animator.finish();
        }
    }

    public void setBdhcam(Bdhcam bdhcam) {
        handler.setBdhcam(bdhcam);
    }
}
