package fr.istic.mob.graphLD;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DrawableGraph extends Drawable {

    private Graph graph;
    private int nodeSize;

    public DrawableGraph(Graph graph, int newNodeSize) {
        this.graph = graph;
        nodeSize = newNodeSize;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void draw(@NonNull Canvas canvas) {
        Paint p = new Paint();
        for (Node node : graph.getNodes()) {
            canvas.drawRoundRect(node.getCoordX()-50, node.getCoordY()-50, node.getCoordX()+nodeSize/2, node.getCoordY()+nodeSize/2,50,50,p);
        }

        for (Arc arc : graph.getArcs()) {
            p.setStyle (Paint.Style.STROKE);
            canvas.drawPath(arc,p);
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
