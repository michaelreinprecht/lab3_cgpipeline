package at.fhv.sysarch.lab3.pipeline.data;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.PullFilter;
import at.fhv.sysarch.lab3.pipeline.PushFilter;
import at.fhv.sysarch.lab3.rendering.RenderingMode;
import at.fhv.sysarch.lab3.utils.PipelineHelperUtil;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer implements PullFilter<Pair<Face, Color>, Pair<Face, Color>>, PushFilter<Pair<Face, Color>, Pair<Face, Color>> {

    private Pipe<Pair<Face, Color>> predecessor;
    private final PipelineData pipelineData;

    public Renderer(PipelineData pipelineData) {
        this.pipelineData = pipelineData;
    }

    @Override
    public Pair<Face, Color> read() {
        Pair<Face, Color> input;
        while (true) {
            input = predecessor.read();
            if (input == null) {
                continue;
            }
            if (PipelineHelperUtil.isPipelineDone(input.first())) {
                break;
            }
            transform(input);
        }
        return null;
    }

    @Override
    public void write(Pair<Face, Color> pair) {
        if (pipelineData.getRenderingMode().equals(RenderingMode.WIREFRAME))
            renderWireframe(pipelineData.getGraphicsContext(), pair);
        else if (pipelineData.getRenderingMode().equals(RenderingMode.FILLED))
            renderFilled(pipelineData.getGraphicsContext(), pair);
        else
            renderPoint(pipelineData.getGraphicsContext(), pair);
    }

    @Override
    public void setPipePredecessor(Pipe<Pair<Face, Color>> predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public void setPipeSuccessor(Pipe<Pair<Face, Color>> successor) {
    }

    @Override
    public Pair<Face, Color> transform(Pair<Face, Color> input) {
        Face face = input.first();
        Color color = input.second();

        double[] x = new double[]{face.getV1().getX(), face.getV2().getX(), face.getV3().getX()};
        double[] y = new double[]{face.getV1().getY(), face.getV2().getY(), face.getV3().getY()};

        pipelineData.getGraphicsContext().setStroke(color);
        pipelineData.getGraphicsContext().setFill(color);

        switch (pipelineData.getRenderingMode()) {
            case POINT:
                pipelineData.getGraphicsContext().fillOval(x[0], y[0], 3, 3);
                break;
            case WIREFRAME:
                pipelineData.getGraphicsContext().strokePolygon(x, y, x.length);
                break;
            case FILLED:
                pipelineData.getGraphicsContext().fillPolygon(x, y, x.length);
                pipelineData.getGraphicsContext().strokePolygon(x, y, x.length);
                break;
        }

        return null;
    }

    private void renderWireframe(GraphicsContext gc, Pair<Face, Color> pair) {
        Face face = pair.first();
        Color color = pair.second();

        gc.setStroke(color);

        double dx1 = face.getV1().getX();
        double dx2 = face.getV2().getX();
        double dx3 = face.getV3().getX();
        double[] x = {dx1, dx2, dx3};

        double dy1 = face.getV1().getY();
        double dy2 = face.getV2().getY();
        double dy3 = face.getV3().getY();
        double[] y = {dy1, dy2, dy3};

        gc.strokePolygon(x, y, 3);
    }

    private void renderFilled(GraphicsContext gc, Pair<Face, Color> pair) {
        Face face = pair.first();
        Color color = pair.second();

        gc.setFill(color);

        double dx1 = face.getV1().getX();
        double dx2 = face.getV2().getX();
        double dx3 = face.getV3().getX();
        double[] x = {dx1, dx2, dx3};

        double dy1 = face.getV1().getY();
        double dy2 = face.getV2().getY();
        double dy3 = face.getV3().getY();
        double[] y = {dy1, dy2, dy3};

        gc.fillPolygon(x, y, 3);
    }

    private void renderPoint(GraphicsContext gc, Pair<Face, Color> pair) {
        Face face = pair.first();
        Color color = pair.second();

        gc.setFill(color);

        gc.fillOval(face.getV1().getX(), face.getV1().getY(), 3, 3);
    }
}
