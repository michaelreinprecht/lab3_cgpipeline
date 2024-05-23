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

public class FilterPerspectiveDivision implements PullFilter<Pair<Face, Color>, Pair<Face, Color>>, PushFilter<Pair<Face, Color>, Pair<Face, Color>> {

    private Pipe<Pair<Face, Color>> predecessor;
    private Pipe<Pair<Face, Color>> successor;
    private final PipelineData pipelineData;

    public FilterPerspectiveDivision(PipelineData pipelineData) {
        this.pipelineData = pipelineData;
    }

    @Override
    public Pair<Face, Color> read() {
        Pair<Face, Color> input = predecessor.read();

        if (input == null || PipelineHelperUtil.isFaceDone(input.fst())) {
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
        Face face = input.fst();

        Face result = new Face(face.getV1().multiply(0.5f / face.getV1().getW()), face.getV2().multiply(0.5f / face.getV2().getW()), face.getV3().multiply(0.5f / face.getV3().getW()), face);

        Mat4 viewportTransform = pipelineData.getViewportTransform();

        return new Pair<>(new Face(viewportTransform.multiply(result.getV1()), viewportTransform.multiply(result.getV2()), viewportTransform.multiply(result.getV3()), result), input.snd());
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
