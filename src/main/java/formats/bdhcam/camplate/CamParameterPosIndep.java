package formats.bdhcam.camplate;

@SuppressWarnings("SpellCheckingInspection")
public class CamParameterPosIndep extends CamParameter{

    public int duration;
    public float finalValue;


    public CamParameterPosIndep(CamParameter.Type type, int duration, float finalValue){
        super(type);
        this.duration = duration;
        this.finalValue = finalValue;
    }

    @Override
    public float getWeightedValue(float weight) {
        return finalValue * weight;
    }
}
