package formats.bdhcam.camplate;

@SuppressWarnings("SpellCheckingInspection")
public class CamPlatePosIndep extends CamPlate {

    public CamPlatePosIndep(int x, int y, int z, int width, int height, int type, int numParams, boolean useZ) {
        super(x, y, z, width, height, type, numParams, useZ);
    }

    public CamPlatePosIndep(){
        super(16, 16, 0, 2, 2, Type.POS_INDEPENDENT.ID, 0, false);
    }

    public CamPlatePosIndep(CamPlate other, int type, int numPlates){
        super(other, type, numPlates);
    }

    @Override
    public void addParameter() {
        parameters.add(new CamParameterPosIndep(CamParameter.Type.CAMERA_X,30,0));//TODO: Improve this
    }

}
