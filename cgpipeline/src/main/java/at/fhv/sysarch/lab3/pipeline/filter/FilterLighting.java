package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.PullFilter;
import at.fhv.sysarch.lab3.pipeline.PushFilter;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.data.Pipe;
import at.fhv.sysarch.lab3.utils.PipelineHelperUtil;
import javafx.scene.paint.Color;
import com.hackoeur.jglm.Mat4;

public class FilterLighting implements PullFilter<Pair<Face, Color>, Pair<Face, Color>>, PushFilter<Pair<Face, Color>, Pair<Face, Color>> {

    private Pipe<Pair<Face, Color>> predecessor;
    private Pipe<Pair<Face, Color>> successor;
    private final PipelineData pipelineData;

    public FilterLighting(PipelineData pipelineData) {
        this.pipelineData = pipelineData;
    }

    @Override
    public Pair<Face, Color> read() {
        Pair<Face, Color> input = predecessor.read();

        if (input == null || PipelineHelperUtil.isPipelineDone(input.first())) {
            return input;
        }

        return transform(input);
    }

    @Override
    public void write(Pair<Face, Color> input) {
        Pair<Face, Color> result = transform(input);

        if (this.successor != null) {
            this.successor.write(result);
        }
    }

    @Override
    public Pair<Face, Color> transform(Pair<Face, Color> input) {
        Face face = input.first();

        float shading = face.getN1().toVec3().dot(pipelineData.getLightPos().getUnitVector());

        if (shading <= 0) {
            return new Pair<>(face, input.second().deriveColor(0, 1, 0, 1));
        }

        return new Pair<>(face, input.second().deriveColor(0, 1, shading, 1));
    }

    @Override
    public void setPipePredecessor(Pipe<Pair<Face, Color>> predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public void setPipeSuccessor(Pipe<Pair<Face, Color>> successor) {
        this.successor = successor;
    }
}
