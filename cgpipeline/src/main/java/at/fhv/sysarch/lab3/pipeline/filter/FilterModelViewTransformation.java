package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.PullFilter;
import at.fhv.sysarch.lab3.pipeline.PushFilter;
import at.fhv.sysarch.lab3.pipeline.data.Pipe;
import com.hackoeur.jglm.Mat4;

public class FilterModelViewTransformation implements PullFilter<Face, Face>, PushFilter<Face, Face> {
    private Pipe<Face> predecessor;
    private Pipe<Face> successor;
    private final PipelineData pipelineData;
    private Mat4 rotationMatrix;

    public FilterModelViewTransformation(PipelineData pipelineData) {
        this.pipelineData = pipelineData;
    }

    @Override
    public Face transform(Face input) {

        Mat4 modelTranslation = pipelineData.getModelTranslation();
        Mat4 viewTransform = pipelineData.getViewTransform();
        Mat4 newTransformation = viewTransform.multiply(modelTranslation).multiply(rotationMatrix);

        return new Face(newTransformation.multiply(input.getV1()), newTransformation.multiply(input.getV2()), newTransformation.multiply(input.getV3()), newTransformation.multiply(input.getN1()), newTransformation.multiply(input.getN2()), newTransformation.multiply(input.getN3()));
    }

    @Override
    public Face read() {
        Face face =  predecessor.read();
        return transform(face);
    }

    @Override
    public void setPipePredecessor(Pipe<Face> predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public void write(Face input) {
        Face result = transform(input);

        if (result != null && this.successor != null) {
            this.successor.write(result);
        }
    }

    @Override
    public void setPipeSuccessor(Pipe<Face> successor) {
        this.successor = successor;
    }

    public void setRotationMatrix(Mat4 rotationMatrix) {
        this.rotationMatrix = rotationMatrix;
    }
}
