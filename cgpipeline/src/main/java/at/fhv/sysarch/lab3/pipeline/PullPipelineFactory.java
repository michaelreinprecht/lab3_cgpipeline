package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.data.Pipe;
import at.fhv.sysarch.lab3.pipeline.data.Renderer;
import at.fhv.sysarch.lab3.pipeline.data.Source;
import at.fhv.sysarch.lab3.pipeline.filter.*;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import com.hackoeur.jglm.Vec3;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;

public class PullPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {
        // TODO: pull from the source (model)
        Source source = new Source(pd.getModel());

        // TODO 1. perform model-view transformation from model to VIEW SPACE coordinates
        FilterModelViewTransformation filterModelViewTransformation = new FilterModelViewTransformation(pd);
        Pipe<Face> modelViewTransformationPipe = new Pipe<>();
        filterModelViewTransformation.setPipePredecessor(modelViewTransformationPipe);
        modelViewTransformationPipe.setPullPredecessor(source);

        // TODO 2. perform backface culling in VIEW SPACE
        FilterBackfaceCulling filterBackfaceCulling = new FilterBackfaceCulling();
        Pipe<Face> backfaceCullingPipe = new Pipe<>();
        filterBackfaceCulling.setPipePredecessor(backfaceCullingPipe);
        backfaceCullingPipe.setPullPredecessor(filterModelViewTransformation);

        // TODO 3. perform depth sorting in VIEW SPACE

        // TODO 4. add coloring (space unimportant)
        FilterColoring filterColoring = new FilterColoring(pd);
        Pipe<Face> coloringPipe = new Pipe<>();
        filterColoring.setPipePredecessor(coloringPipe);
        coloringPipe.setPullPredecessor(filterModelViewTransformation);

        // lighting can be switched on/off
        FilterProjectionTransformation filterProjectionTransformation = new FilterProjectionTransformation(pd);

        if (pd.isPerformLighting()) {
            // 4a. TODO perform lighting in VIEW SPACE
            FilterLighting filterLighting = new FilterLighting(pd);
            Pipe<Pair<Face, Color>> lightingPipe = new Pipe<>();
            filterLighting.setPipePredecessor(lightingPipe);
            lightingPipe.setPullPredecessor(filterColoring);
            
            // 5. TODO perform projection transformation on VIEW SPACE coordinates
            Pipe<Pair<Face, Color>> projectionTransformationPipe = new Pipe<>();
            filterProjectionTransformation.setPipePredecessor(projectionTransformationPipe);
            projectionTransformationPipe.setPullPredecessor(filterLighting);
        } else {
            // 5. TODO perform projection transformation
            Pipe<Pair<Face, Color>> projectionTransformationPipe = new Pipe<>();
            filterProjectionTransformation.setPipePredecessor(projectionTransformationPipe);
            projectionTransformationPipe.setPullPredecessor(filterColoring);
        }

        // TODO 6. perform perspective division to screen coordinates
        FilterPerspectiveDivision filterPerspectiveDivision = new FilterPerspectiveDivision(pd);
        Pipe<Pair<Face, Color>> perspectiveDivisionPipe = new Pipe<>();
        filterPerspectiveDivision.setPipePredecessor(perspectiveDivisionPipe);
        perspectiveDivisionPipe.setPullPredecessor(filterProjectionTransformation);

        // TODO 7. feed into the sink (renderer)
        Renderer sink = new Renderer(pd);
        Pipe<Pair<Face, Color>> sinkPipe = new Pipe<>();
        sink.setPipePredecessor(sinkPipe);
        sinkPipe.setPullPredecessor(filterPerspectiveDivision);

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
                rotation = rotation + fraction*4;
                double rotationRad = Math.toRadians(rotation);

                // TODO create new model rotation matrix using pd.modelRotAxis
                Vec3 rotationAxis = pd.getModelRotAxis();

                // TODO compute updated model-view tranformation
                Mat4 rotationMatrix = Matrices.rotate((float) rotationRad, rotationAxis);

                // TODO update model-view filter
                filterModelViewTransformation.setRotationMatrix(rotationMatrix);

                // TODO trigger rendering of the pipeline
                source.setIndex(0);
                sink.read();
            }
        };
    }
}