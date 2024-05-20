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

public class PullPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {
        // TODO: pull from the source (model)
        Source source = new Source(pd.getModel());

        // TODO 1. perform model-view transformation from model to VIEW SPACE coordinates
        FilterModelViewTransformation pullModelViewTransformationFilter = new FilterModelViewTransformation(pd);
        Pipe<Face> toModelViewTransformationPipe = new Pipe<>();
        pullModelViewTransformationFilter.setPipePredecessor(toModelViewTransformationPipe);
        toModelViewTransformationPipe.setPredecessor(source);

        // TODO 2. perform backface culling in VIEW SPACE

        // TODO 3. perform depth sorting in VIEW SPACE

        // TODO 4. add coloring (space unimportant)
        FilterColoring filterColoring = new FilterColoring(pd);
        Pipe<Face> toColoringPipe = new Pipe<>();
        filterColoring.setPipePredecessor(toColoringPipe);
        toColoringPipe.setPredecessor(pullModelViewTransformationFilter);

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
        filterPerspectiveDivision.setPipePredecessor(toPerspectiveDivisionPipe);
        toPerspectiveDivisionPipe.setPredecessor(filterPerspectiveDivision);

        // TODO 7. feed into the sink (renderer)
        Sink sink = new Sink(pd);
        Pipe<Pair<Face, Color>> toSinkPipe = new Pipe<>();
        sink.setPipePredecessor(toSinkPipe);
        toSinkPipe.setPredecessor(filterPerspectiveDivision);

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
                // TODO compute rotation in radians
                rotation = rotation + fraction;
                double rotationRad = Math.toRadians(rotation);

                // TODO create new model rotation matrix using pd.getModelRotAxis and Matrices.rotate
                Mat4 rotationMatrix = Matrices.rotate((float) rotationRad, pd.getModelRotAxis());
/*
                // TODO compute updated model-view tranformation
                pullModelViewTransformationFilter.setRotationMatrix(rotationMatrix);
*/
                // TODO update model-view filter
                pullModelViewTransformationFilter.setRotationMatrix(rotationMatrix);

                // TODO trigger rendering of the pipeline
                source.setIndex(0);
                sink.read();
            }
        };
    }
}