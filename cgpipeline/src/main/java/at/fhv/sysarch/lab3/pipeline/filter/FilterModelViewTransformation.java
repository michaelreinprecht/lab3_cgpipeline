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
    public Face transform(Face input) {

        Mat4 modelTranslation = pd.getModelTranslation();
        Mat4 viewTransform = pd.getViewTransform();
        Mat4 newTransformation = viewTransform.multiply(modelTranslation).multiply(rotationMatrix);

        return new Face(newTransformation.multiply(input.getV1()), newTransformation.multiply(input.getV2()), newTransformation.multiply(input.getV3()), newTransformation.multiply(input.getN1()), newTransformation.multiply(input.getN2()), newTransformation.multiply(input.getN3()));
    }

    @Override
    public Face read() {
        return predecessor.read();
    }

    @Override
    public void setPipePredecessor(Pipe<Face> predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public void write(Face input) {
        Face result = transform(input);

        if (null == result)
            return;

        if (this.successor != null)
            this.successor.write(result);
    }

    @Override
    public void setPipeSuccessor(Pipe<Face> successor) {
        this.successor = successor;
    }

    public void setRotationMatrix(Mat4 rotationMatrix) {
        this.rotationMatrix = rotationMatrix;
    }
}
