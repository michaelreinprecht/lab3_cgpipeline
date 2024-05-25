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
    private final PipelineData pipelineData;

    public FilterColoring(PipelineData pipelineData) {
        this.pipelineData = pipelineData;
    }

    @Override
    public Pair<Face, Color> read() {
        Face face = predecessor.read();
        if (face == null) {
            return null;
        }

        return transform(face);
    }

    @Override
    public void write(Face input) {
        Pair<Face, Color> result = transform(input);

        if (result != null && this.successor != null) {
            this.successor.write(result);
        }
    }

    @Override
    public Pair<Face, Color> transform(Face input) {
        Color color = pipelineData.getModelColor();
        return new Pair<>(input, color);
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
