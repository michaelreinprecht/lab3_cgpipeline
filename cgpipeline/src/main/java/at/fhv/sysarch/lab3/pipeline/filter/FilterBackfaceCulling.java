package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PullFilter;
import at.fhv.sysarch.lab3.pipeline.PushFilter;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.data.Pipe;
import at.fhv.sysarch.lab3.utils.PipelineHelperUtil;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class FilterBackfaceCulling implements PullFilter<Face, Face>, PushFilter<Face, Face> {

    private Pipe<Face> predecessor;
    private Pipe<Face> successor;

    @Override
    public Face read() {
        Face face = predecessor.read();

        if (face == null || PipelineHelperUtil.isPipelineDone(face)) {
            return face;
        }

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

    @Override
    public Face transform(Face input) {
        if (input.getV1().dot(input.getN1()) > 0) {
            return null;
        }

        return input;
    }
}
