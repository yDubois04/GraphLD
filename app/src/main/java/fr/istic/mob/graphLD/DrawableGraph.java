package fr.istic.mob.graphLD;

import android.content.Context;
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
    private Paint paintNode;
    private Paint paintArc;
    private Paint paintTextNode;
    private Context context;
    private Paint paintTextArc;


    public DrawableGraph(Graph graph, Context context) {
        this.graph = graph;
        this.paintNode = new Paint ();
        this.paintArc = new Paint ();
        this.paintTextNode = new Paint ();
        this.context = context;
        this.paintTextArc = new Paint();

        this.initPaint();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void draw(@NonNull Canvas canvas) {
        float [] middle;

        //drawing nodes
        for (Node node : graph.getNodes()) {
            if (node.getLabel() != null) {
                float textSize = paintTextNode.measureText(node.getLabel());
                if (textSize * 1.2 > node.getNodeSize()) {
                    node.setNodeLength((int) (textSize * 1.2));
                }else{
                    node.setNodeSize((int) (node.getNodeSize()));
                }
            }
            this.chooseColor(paintNode, node.getColor());
            canvas.drawRoundRect(node.left, node.top, node.right, node.bottom,50,50,paintNode);

            if (node.getLabel() != null) {
                chooseColor(paintTextNode,node.getLabelColor());
                canvas.drawText(node.getLabel(), node.getCoordX() - node.getNodeLength()/2 + node.getNodeLength() * 0.1f, node.getCoordY(), paintTextNode);
            }
        }

        //drawing arcs
        for (Arc arc : graph.getArcs()) {
            this.chooseColor(paintArc, arc.getColor());
            paintArc.setStrokeWidth(arc.getThickness());

            Node node1 = arc.getNode1();
            Node node2 = arc.getNode2();
            arc.moveTo(node1.getCoordX(), node1.getCoordY());
            arc.lineTo(node2.getCoordX(), node2.getCoordY());

            canvas.drawPath(arc,paintArc);

            if (arc.getLabel() != null) {
                middle = getArcMiddle(arc);
                paintTextArc.setTextSize(arc.getLabelSize());
                canvas.drawText(arc.getLabel(),middle[0]+10,middle[1],paintTextArc);
            }
        }
    }

    private void initPaint () {
        paintArc.setStyle(Paint.Style.STROKE);

        paintTextArc.setStyle(Paint.Style.FILL);

        paintTextNode.setStyle(Paint.Style.FILL);
        paintTextNode.setTextSize(30);

    }

    private float[] getArcMiddle (Arc arc) {
        float [] middle = {0f, 0f};
        float [] tangent = {0f, 0f};
        PathMeasure measure = new PathMeasure (arc, false);
        measure.getPosTan(measure.getLength() * 0.5f, middle,tangent);

        return middle;
    }

    private void chooseColor (Paint paint, String color) {

        if (color.equals(context.getString(R.string.color_red))) {
            paint.setColor(Color.RED);
        }
        else if (color.equals(context.getString(R.string.color_green))) {
            paint.setColor(Color.GREEN);
        }
        else if (color.equals(context.getString(R.string.color_blue))) {
            paint.setColor(Color.BLUE);
        }
        else if (color.equals(context.getString(R.string.color_orange))) {
           paint.setColor(Color.rgb(255,102,0));
        }
        else if (color.equals(context.getString(R.string.color_cyan))) {
            paint.setColor(Color.CYAN);
        }
        else if (color.equals(context.getString(R.string.color_magenta))) {
            paint.setColor(Color.MAGENTA);
        }
        else if (color.equals(context.getString(R.string.color_noir))) {
            paint.setColor(Color.BLACK);
        }
        else {
            paint.setColor(Color.BLACK);
        }
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
