package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PullFilter;
import at.fhv.sysarch.lab3.pipeline.PushFilter;
import at.fhv.sysarch.lab3.pipeline.data.Pipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FilterDepthSorting implements PushFilter<Face, Face>, PullFilter<Face, Face> {
    private Pipe<Face> predecessor;
    private Pipe<Face> successor;
    private final List<Face> depthSortedFaces = new ArrayList<>();

    @Override
    public Face read() {
        return predecessor.read();
    }

    @Override
    public void setPipePredecessor(Pipe<Face> predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public void write(Face face) {
        successor.write(face);
    }

    @Override
    public void setPipeSuccessor(Pipe<Face> successor) {
        this.successor = successor;
    }

    @Override
    public Face transform(Face face) {
        // No transformation needed for depth sorting
        return face;
    }

    private void sortFaces() {
        depthSortedFaces.sort(Comparator.comparing(f -> f.getV1().getZ() + f.getV2().getZ() + f.getV3().getZ()));
    }
}
