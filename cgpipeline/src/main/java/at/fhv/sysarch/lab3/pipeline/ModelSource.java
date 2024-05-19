package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.data.Pipe;
import com.hackoeur.jglm.Vec4;

public class ModelSource implements PullFilter<Face, Face> {
    private Pipe<Face> successor;
    private Model model;
    private int index;

    public ModelSource() {
    }

    public ModelSource(Model model) {
        this.model = model;
    }

    @Override
    public Face read() {
        if (index >= model.getFaces().size()) {
            return new Face(Vec4.VEC4_ZERO, Vec4.VEC4_ZERO, Vec4.VEC4_ZERO, Vec4.VEC4_ZERO, Vec4.VEC4_ZERO, Vec4.VEC4_ZERO);
        }
        return model.getFaces().get(index++);
    }

    public void write() {
        for (Face face : model.getFaces()) {
            successor.write(face);
        }
    }

    @Override
    public void setPipePredecessor(Pipe<Face> predecessor) {
    }

    public void setPipeSuccessor(Pipe<Face> successor) {
        this.successor = successor;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public Face transform(Face input) {
        return input;
    }
}
