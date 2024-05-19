package at.fhv.sysarch.lab3.filters.pushPipeline;

import at.fhv.sysarch.lab3.filters.Filter;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.PipelineData;

import java.util.ArrayList;
import java.util.List;

public class ModelViewTransformation implements Filter<Model, Model> {
    private final PipelineData pd;

    public ModelViewTransformation(PipelineData pd) {
        this.pd = pd;
    }

    @Override
    public Model process(Model model) {
        // TODO: Implement model-view transformation using pd.getViewMatrix()
        // For now, let's assume a simple transformation (you'll need to replace this with actual transformation logic)
        List<Face> transformedFaces = new ArrayList<>();
        model.getFaces().forEach(face -> modelViewTransformation(face));
        model.getFaces().forEach(face -> {
            // Transform each vertex of the face from model to view space
            // and add it to viewSpaceModel
        });
        return model;
    }

    private Face modelViewTransformation(Face face) {
        return face;
    }
}
