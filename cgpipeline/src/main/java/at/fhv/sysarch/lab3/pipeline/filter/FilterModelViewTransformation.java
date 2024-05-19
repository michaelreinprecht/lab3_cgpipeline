package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.PullFilter;
import at.fhv.sysarch.lab3.pipeline.PushFilter;
import at.fhv.sysarch.lab3.pipeline.data.Pipe;
import at.fhv.sysarch.lab3.utils.PipelineHelperUtils;
import com.hackoeur.jglm.Mat4;

public class FilterModelViewTransformation implements PullFilter<Face, Face>, PushFilter<Face, Face> {
    private Pipe<Face> predecessor;
    private Pipe<Face> successor;
    private final PipelineData pd;
    private Mat4 rotationMatrix;

    public FilterModelViewTransformation(PipelineData pd) {
        this.pd = pd;
    }

    @Override
    public Face process(Face input) { //TODO

        Mat4 modelTranslation = pd.getModelTranslation();
        Mat4 viewTransform = pd.getViewTransform();
        Mat4 newTransformation = viewTransform.multiply(modelTranslation).multiply(rotationMatrix);

        return new Face(
                newTransformation.multiply(input.getV1()),
                newTransformation.multiply(input.getV2()),
                newTransformation.multiply(input.getV3()),
                newTransformation.multiply(input.getN1()),
                newTransformation.multiply(input.getN2()),
                newTransformation.multiply(input.getN3())
        );
    }

    @Override
    public Face read() {
        return predecessor.read();
    }

    @Override
    public void setPipePredecessor(Pipe<Face> predecessor) {

    }

    @Override
    public Face transform(Face input) {
        return null;
    }

    @Override
    public void write(Face input) {

    }

    @Override
    public void setPipeSuccessor(Pipe<Face> successor) {

    }

    public void setRotationMatrix(Mat4 rotationMatrix) {
        this.rotationMatrix = rotationMatrix;
    }
}
