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
        for (Node noeud : graph.getNodes()) {
            canvas.drawRoundRect(noeud.getCoordX()-50, noeud.getCoordY()-50, noeud.getCoordX()+nodeSize/2, noeud.getCoordY()+nodeSize/2,50,50,p);
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
