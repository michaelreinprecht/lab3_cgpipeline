package at.fhv.sysarch.lab3.utils;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Vec4;

public class PipelineHelperUtil {

    public static boolean isPipelineDone(Face face) {
        if (face == null) {
            return false;
        }
        return face.getV1().equals(Vec4.VEC4_ZERO) && face.getV2().equals(Vec4.VEC4_ZERO) && face.getV3().equals(Vec4.VEC4_ZERO) && face.getN1().equals(Vec4.VEC4_ZERO) && face.getN2().equals(Vec4.VEC4_ZERO) && face.getN3().equals(Vec4.VEC4_ZERO);
    }

    public static Face endOfPipelineSignal() {
        return new Face(Vec4.VEC4_ZERO, Vec4.VEC4_ZERO, Vec4.VEC4_ZERO, Vec4.VEC4_ZERO, Vec4.VEC4_ZERO, Vec4.VEC4_ZERO);
    }
}