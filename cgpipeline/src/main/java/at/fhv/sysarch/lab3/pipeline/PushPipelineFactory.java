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

public class PushPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {
        // TODO 7. feed into the sink (renderer)
        Renderer renderer = new Renderer(pd);
        Pipe<Pair<Face, Color>> renderPipe = new Pipe<>();
        renderPipe.setPushSuccessor(renderer);

        // TODO 6. perform perspective division to screen coordinates
        FilterPerspectiveDivision filterPerspectiveDivision = new FilterPerspectiveDivision(pd);
        filterPerspectiveDivision.setPipeSuccessor(renderPipe);
        Pipe<Pair<Face, Color>> perspectiveDivisionPipe = new Pipe<>();
        perspectiveDivisionPipe.setPushSuccessor(filterPerspectiveDivision);

        FilterProjectionTransformation filterProjectionTransformation = new FilterProjectionTransformation(pd);
        filterProjectionTransformation.setPipeSuccessor(perspectiveDivisionPipe);
        Pipe<Pair<Face, Color>> filterProjectionPipe = new Pipe<>();
        filterProjectionPipe.setPushSuccessor(filterProjectionTransformation);

        FilterColoring filterColoring = new FilterColoring(pd);

        // lighting can be switched on/off
        if (pd.isPerformLighting()) {
            // 5. TODO perform projection transformation on VIEW SPACE coordinates

            // 4a. TODO perform lighting in VIEW SPACE
            FilterLighting filterLighting = new FilterLighting(pd);
            filterLighting.setPipeSuccessor(filterProjectionPipe);
            Pipe<Pair<Face, Color>> filterLightingPipe = new Pipe<>();
            filterLightingPipe.setPushSuccessor(filterLighting);

            filterColoring.setPipeSuccessor(filterLightingPipe);
        } else {
            // 5. TODO perform projection transformation

            // TODO 4. add coloring (space unimportant)
            filterColoring.setPipeSuccessor(filterProjectionPipe);
        }

        // TODO 4. add coloring (space unimportant)
        Pipe<Face> coloringPipe = new Pipe<>();
        coloringPipe.setPushSuccessor(filterColoring);

        // TODO 3. perform depth sorting in VIEW SPACE
        FilterDepthSorting filterDepthSorting = new FilterDepthSorting();
        filterDepthSorting.setPipeSuccessor(coloringPipe);
        Pipe<Face> depthSortingPipe = new Pipe<>();
        depthSortingPipe.setPushSuccessor(filterDepthSorting);

        // TODO 2. perform backface culling in VIEW SPACE
        FilterBackfaceCulling filterBackfaceCulling = new FilterBackfaceCulling();
        filterBackfaceCulling.setPipeSuccessor(depthSortingPipe);
        Pipe<Face> backfaceCullingPipe = new Pipe<>();
        backfaceCullingPipe.setPushSuccessor(filterBackfaceCulling);

        // TODO 1. perform model-view transformation from model to VIEW SPACE coordinates
        FilterModelViewTransformation filterModelViewTransformation = new FilterModelViewTransformation(pd);
        filterModelViewTransformation.setPipeSuccessor(backfaceCullingPipe);
        Pipe<Face> modelViewTransformationPipe = new Pipe<>();
        modelViewTransformationPipe.setPushSuccessor(filterModelViewTransformation);

        // TODO: push from the source (model)
        Source source = new Source();
        source.setPipeSuccessor(modelViewTransformationPipe);

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
                source.setModel(model);
                source.write();
            }
        };
    }
}