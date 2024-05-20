package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.PullFilter;
import at.fhv.sysarch.lab3.pipeline.PushFilter;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.data.Pipe;
import javafx.scene.paint.Color;

public class FilterColoring implements PullFilter<Pair<Face, Color>, Face>, PushFilter<Face, Pair<Face, Color>> {

    private Pipe<Face> predecessor;
    private Pipe<Pair<Face, Color>> successor;
    private final PipelineData pd;

    public FilterColoring(PipelineData pd) {
        this.pd = pd;
    }

    @Override
    public Pair<Face, Color> read() {
        Face input = predecessor.read();

        if (null == input)
            return null;

        return transform(input);
    }

    @Override
    public void write(Face input) {
        Pair<Face, Color> result = transform(input);

        if (null == result)
            return;

        if (null != this.successor)
            this.successor.write(result);
    }

    @Override
    public Pair<Face, Color> transform(Face input) {
        return new Pair<>(input, pd.getModelColor());
    }

    @Override
    public void setPipePredecessor(Pipe<Face> predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public void setPipeSuccessor(Pipe<Pair<Face, Color>> successor) {
        this.successor = successor;
    }
}
