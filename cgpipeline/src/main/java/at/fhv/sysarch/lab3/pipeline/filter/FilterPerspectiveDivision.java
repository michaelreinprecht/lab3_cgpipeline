package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.PullFilter;
import at.fhv.sysarch.lab3.pipeline.PushFilter;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.data.Pipe;
import at.fhv.sysarch.lab3.utils.PipelineHelperUtils;
import javafx.scene.paint.Color;

public class FilterPerspectiveDivision implements PullFilter<Pair<Face, Color>, Pair<Face, Color>>, PushFilter<Pair<Face, Color>, Pair<Face, Color>> {

    private Pipe<Pair<Face, Color>> predecessor;
    private Pipe<Pair<Face, Color>> successor;
    private final PipelineData pd;

    public FilterPerspectiveDivision(PipelineData pd) {
        this.pd = pd;
    }

    @Override
    public Pair<Face, Color> read() {
        Pair<Face, Color> input = predecessor.read();

        if (null == input)
            return null;
        else if (PipelineHelperUtils.isFaceEnd(input.fst()))
            return input;

        return transform(input);
    }

    @Override
    public void write(Pair<Face, Color> input) {
        Pair<Face, Color> result = transform(input);

        if (null != this.successor)
            this.successor.write(result);
    }

    @Override
    public Pair<Face, Color> transform(Pair<Face, Color> input) {
        Face face = input.fst();

        Face dividedFace = new Face(
                face.getV1().multiply((1.0f / face.getV1().getW())),
                face.getV2().multiply((1.0f / face.getV2().getW())),
                face.getV3().multiply((1.0f / face.getV3().getW())),
                face
        );

        Face transformedFace = new Face(
                pd.getViewportTransform().multiply(dividedFace.getV1()),
                pd.getViewportTransform().multiply(dividedFace.getV2()),
                pd.getViewportTransform().multiply(dividedFace.getV3()),
                dividedFace
        );

        return new Pair<>(transformedFace, input.snd());
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
