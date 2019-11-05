package fr.istic.mob.graphLD;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PathMeasure;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DrawableGraph extends Drawable {

    private Graph graph;
    private int nodeSize;
    private Paint paintNode;
    private Paint paintArc;
    private Paint paintText;


    public DrawableGraph(Graph graph, int newNodeSize) {
        this.graph = graph;
        this.nodeSize = newNodeSize;
        this.paintNode = new Paint ();
        this.paintArc = new Paint ();
        this.paintText = new Paint ();

        this.initPaint();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void draw(@NonNull Canvas canvas) {
        float [] middle;

        for (Node node : graph.getNodes()) {
            canvas.drawRoundRect(node.getCoordX()-50, node.getCoordY()-50, node.getCoordX()+nodeSize/2, node.getCoordY()+nodeSize/2,50,50,paintNode);
            if (node.getLabel() != null) {
                canvas.drawText(node.getLabel(), node.getCoordX(),node.getCoordY(),paintText);
            }
        }

        for (Arc arc : graph.getArcs()) {
            canvas.drawPath(arc,paintArc);
            if (arc.getLabel() != null) {
                middle = getArcMiddle(arc);
                canvas.drawText(arc.getLabel(),middle[0]+10,middle[1],paintText);
            }
        }
    }

    private void initPaint () {
        paintNode.setColor(Color.BLUE);

        paintArc.setColor(Color.BLUE);
        paintArc.setStrokeWidth(8);
        paintArc.setStyle(Paint.Style.STROKE);

        paintText.setStyle(Paint.Style.FILL);
        paintText.setColor(Color.BLACK);
        paintText.setTextSize(30);

    }

    private float[] getArcMiddle (Arc arc) {
        float [] middle = {0f, 0f};
        float [] tangent = {0f, 0f};
        PathMeasure measure = new PathMeasure (arc, false);
        measure.getPosTan(measure.getLength() * 0.5f, middle,tangent);

        return middle;
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
