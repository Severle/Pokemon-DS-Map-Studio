package formats.bdhcam;

import formats.bdhcam.camplate.*;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@SuppressWarnings("SpellCheckingInspection")
public class Bdhcam {

    public static final String              fileExtension = "bdhcam";
    private final       ArrayList<CamPlate> plates;

    public Bdhcam(){
        this(0);
    }

    public Bdhcam(int numPlates) {
        plates = new ArrayList<>(numPlates);
    }

    public CamPlate getPlate(int index) {
        return plates.get(index);
    }

    public void changePlateType(int index, int type) {
        if (!plates.isEmpty()) {
            if (index >= 0 && index < plates.size()) {
                CamPlate p = plates.get(index);
                if (p.type.ID != type) {
                    if (p.type.ID == CamPlate.Type.POS_INDEPENDENT.ID) {
                        plates.set(index, new CamPlatePosDep(p, type, 0));
                        for(CamParameter param : p.parameters){
                            plates.get(index).parameters.add(new CamParameterPosDep(param.type, 0, 0));
                        }
                    } else {
                        if (type == CamPlate.Type.POS_INDEPENDENT.ID) {
                            plates.set(index, new CamPlatePosIndep(p, type, 0));
                            for(CamParameter param : p.parameters){
                                plates.get(index).parameters.add(new CamParameterPosIndep(param.type, 1, 0));
                            }
                        } else {
                            CamPlatePosDep newPlate = new CamPlatePosDep(p, type, 0);
                            newPlate.parameters = p.parameters;
                            plates.set(index, newPlate);
                        }
                    }
                }
            }
        }
    }

    public int getNumValidPlates(){
        int count = 0;
        for(CamPlate plate : plates){
            if(!plate.parameters.isEmpty()){
                count ++;
            }
        }
        return count;
    }


}
