package fr.istic.mob.graphLD;

import fr.istic.mob.graphLD.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PathMeasure;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DrawableGraph extends Drawable {

    private Graph graph;
    private Paint paintNode;
    private Paint paintArc;
    private Paint paintText;
    private Context context;


    public DrawableGraph(Graph graph, Context context) {
        this.graph = graph;
        this.paintNode = new Paint ();
        this.paintArc = new Paint ();
        this.paintText = new Paint ();
        this.context = context;

        this.initPaint();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void draw(@NonNull Canvas canvas) {
        float [] middle;

        for (Node node : graph.getNodes()) {
            if (node.getLabel() != null) {
                float textSize = paintText.measureText(node.getLabel());
                if (textSize > node.getNodeSize()) {
                    node.setNodeLength((int) textSize + node.getNodeSize());
                }
            }
            this.chooseNodeColor(node.getColor());
            canvas.drawRoundRect(node.left, node.top, node.right, node.bottom,50,50,paintNode);

            if (node.getLabel() != null) {
                canvas.drawText(node.getLabel(), node.getCoordX() - node.getNodeLength()/2 + node.getNodeSize()/2, node.getCoordY(), paintText);
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

    private void chooseNodeColor (String color) {

        if (color.equals(context.getString(R.string.color_red))) {
            paintNode.setColor(Color.RED);
        }
        else if (color.equals(context.getString(R.string.color_green))) {
            paintNode.setColor(Color.GREEN);
        }
        else if (color.equals(context.getString(R.string.color_blue))) {
            paintNode.setColor(Color.BLUE);
        }
        else if (color.equals(context.getString(R.string.color_orange))) {
           paintNode.setColor(Color.rgb(255,102,0));
        }
        else if (color.equals(context.getString(R.string.color_cyan))) {
            paintNode.setColor(Color.CYAN);
        }
        else if (color.equals(context.getString(R.string.color_magenta))) {
            paintNode.setColor(Color.MAGENTA);
        }
        else if (color.equals(context.getString(R.string.color_noir))) {
            paintNode.setColor(Color.BLACK);
        }
        else {
            paintNode.setColor(Color.BLACK);
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
