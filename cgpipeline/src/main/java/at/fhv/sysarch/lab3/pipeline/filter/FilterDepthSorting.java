package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PullFilter;
import at.fhv.sysarch.lab3.pipeline.PushFilter;
import at.fhv.sysarch.lab3.pipeline.data.Pipe;
import at.fhv.sysarch.lab3.utils.PipelineHelperUtil;

import java.util.Comparator;
import java.util.LinkedList;

public class FilterDepthSorting implements PushFilter<Face, Face>, PullFilter<Face, Face> {
    private Pipe<Face> predecessor;
    private Pipe<Face> successor;
    private LinkedList<Face> faces = new LinkedList<>();
    private LinkedList<Face> depthSortedFaces = new LinkedList<>();
    private boolean sortingCompleted = false;
    private int currentIndex = 0;

    @Override
    public Face read() {
        Face face = predecessor.read();
        if (!PipelineHelperUtil.isPipelineDone(face)) {
            faces.add(predecessor.read());
        }
        else {
            faces.add(face);
            pullSortFaces();
        }
        if (!depthSortedFaces.isEmpty()) {
            return depthSortedFaces.removeFirst();
        }
        return null;
    }

    @Override
    public void setPipePredecessor(Pipe<Face> predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public void write(Face face) {
        if (!PipelineHelperUtil.isPipelineDone(face)) {
            depthSortedFaces.add(face);
        } else {
            sortFaces();
            pushSortedFacesToSuccessor();
        }
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

    private void pullSortFaces() {
        if (!faces.isEmpty()) {
            faces.sort(Comparator.comparing(f ->
                    f.getV1().getZ() +
                    f.getV2().getZ() +
                    f.getV3().getZ()));
            depthSortedFaces = faces;
            faces.clear();
        }
    }

    private void pushSortedFacesToSuccessor() {
        while (!depthSortedFaces.isEmpty()) {
            successor.write(depthSortedFaces.removeFirst());
        }
    }

    private void sortFaces() {
        depthSortedFaces.sort(Comparator.comparing(f -> f.getV1().getZ() + f.getV2().getZ() + f.getV3().getZ()));
    }
}
