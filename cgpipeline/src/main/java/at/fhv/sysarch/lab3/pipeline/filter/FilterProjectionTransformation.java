package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.PullFilter;
import at.fhv.sysarch.lab3.pipeline.PushFilter;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.data.Pipe;
import com.hackoeur.jglm.Mat4;
import javafx.scene.paint.Color;

public class FilterProjectionTransformation implements PullFilter<Pair<Face, Color>, Pair<Face, Color>>, PushFilter<Pair<Face, Color>, Pair<Face, Color>> {

    private Pipe<Pair<Face, Color>> predecessor;
    private Pipe<Pair<Face, Color>> successor;
    private final PipelineData pipelineData;

    public FilterProjectionTransformation(PipelineData pipelineData) {
        this.pipelineData = pipelineData;
    }

    @Override
    public Pair<Face, Color> read() {
        Pair<Face, Color> input = predecessor.read();
        if (input == null) {
            return null;
        }

        return transform(input);
    }

    @Override
    public void write(Pair<Face, Color> input) {
        Pair<Face, Color> result = transform(input);

        if (result != null && this.successor != null) {
            this.successor.write(result);
        }
    }

    @Override
    public void setPipeSuccessor(Pipe<Pair<Face, Color>> successor) {
        this.successor = successor;
    }

    @Override
    public void setPipePredecessor(Pipe<Pair<Face, Color>> predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public Pair<Face, Color> transform(Pair<Face, Color> input) {
        Face face = input.first();
        Color color = input.second();

        Mat4 projTransform = pipelineData.getProjTransform();

        Face result = new Face(
                projTransform.multiply(face.getV1()),
                projTransform.multiply(face.getV2()),
                projTransform.multiply(face.getV3()),
                face);

        return new Pair<>(result, color);
    }
}
