package formats.bdhcam.camplate;

@SuppressWarnings("unused")
public class CamPlatePosDep extends CamPlate {

    public CamPlatePosDep(int x, int y, int z, int width, int height, int type, int numParams, boolean useZ) {
        super(x, y, z, width, height, type, numParams, useZ);
    }

    public CamPlatePosDep(){
        super(16, 16, 0, 2, 2, Type.POS_DEPENDENT_X.ID, 0, false);
    }

    public CamPlatePosDep(CamPlate other, int type, int numPlates){
        super(other, type, numPlates);
    }

    @Override
    public void addParameter() {
        parameters.add(new CamParameterPosDep(CamParameter.Type.CAMERA_X,0,0));//TODO: Improve this

    }
}
