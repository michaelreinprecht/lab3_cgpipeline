package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.data.Pipe;
import at.fhv.sysarch.lab3.pipeline.data.Sink;
import at.fhv.sysarch.lab3.pipeline.data.Source;
import at.fhv.sysarch.lab3.pipeline.filter.FilterColoring;
import at.fhv.sysarch.lab3.pipeline.filter.FilterModelViewTransformation;
import at.fhv.sysarch.lab3.pipeline.filter.FilterPerspectiveDivision;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;

public class PushPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {
        // TODO: push from the source (model)
        Source source = new Source(pd.getModel());

        // TODO 1. perform model-view transformation from model to VIEW SPACE coordinates
        FilterModelViewTransformation pullModelViewTransformationFilter = new FilterModelViewTransformation(pd);
        Pipe<Face> toModelViewTransformationPipe = new Pipe<>();
        source.setPipeSuccessor(toModelViewTransformationPipe);
        toModelViewTransformationPipe.setSuccessor(pullModelViewTransformationFilter);

        // TODO 2. perform backface culling in VIEW SPACE

        // TODO 3. perform depth sorting in VIEW SPACE

        // TODO 4. add coloring (space unimportant)
        FilterColoring filterColoring = new FilterColoring(pd);
        Pipe<Face> toColoringPipe = new Pipe<>();
        pullModelViewTransformationFilter.setPipeSuccessor(toColoringPipe);
        toColoringPipe.setSuccessor(filterColoring);

        // lighting can be switched on/off
        if (pd.isPerformLighting()) {
            // 4a. TODO perform lighting in VIEW SPACE
            
            // 5. TODO perform projection transformation on VIEW SPACE coordinates
        } else {
            // 5. TODO perform projection transformation
        }

        // TODO 6. perform perspective division to screen coordinates
        FilterPerspectiveDivision filterPerspectiveDivision = new FilterPerspectiveDivision(pd);
        Pipe<Pair<Face, Color>> toPerspectiveDivisionPipe = new Pipe<>();
        filterPerspectiveDivision.setPipeSuccessor(toPerspectiveDivisionPipe);
        toPerspectiveDivisionPipe.setSuccessor(filterPerspectiveDivision);

        // TODO 7. feed into the sink (renderer)
        Sink sink = new Sink(pd);
        Pipe<Pair<Face, Color>> toSinkPipe = new Pipe<>();
        sink.setPipeSuccessor(toSinkPipe);
        toSinkPipe.setSuccessor(sink);

        // returning an animation renderer which handles clearing of the
        // viewport and computation of the praction
        return new AnimationRenderer(pd) {
            // TODO rotation variable goes in here
            float rotation = 0f;

            /** This method is called for every frame from the JavaFX Animation
             * system (using an AnimationTimer, see AnimationRenderer). 
             * @param fraction the time which has passed since the last render call in a fraction of a second
             * @param model    the model to render 
             */
            @Override
            protected void render(float fraction, Model model) {
                pd.getGraphicsContext().setStroke(Color.RED);
                /*
                model.getFaces().forEach(face -> {
                   pd.getGraphicsContext().strokeLine(face.getV1().getX()*100, face.getV1().getY()*100, face.getV2().getX()*100, face.getV2().getY()*100);
                   pd.getGraphicsContext().strokeLine(face.getV2().getX()*100, face.getV2().getY()*100, face.getV3().getX()*100, face.getV3().getY()*100);
                   pd.getGraphicsContext().strokeLine(face.getV1().getX()*100, face.getV1().getY()*100, face.getV3().getX()*100, face.getV3().getY()*100);
                });*/
                // TODO compute rotation in radians
                rotation = rotation + fraction;
                double rotationRad = Math.toRadians(rotation);

                // TODO create new model rotation matrix using pd.modelRotAxis

                // TODO compute updated model-view tranformation
                Mat4 rotationMatrix = Matrices.rotate((float) rotationRad, pd.getModelRotAxis());

                // TODO update model-view filter
                pullModelViewTransformationFilter.setRotationMatrix(rotationMatrix);

                // TODO trigger rendering of the pipeline
                source.setModel(model);
                source.write();
            }
        };
    }
}