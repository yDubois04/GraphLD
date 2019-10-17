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
    final int nodeSize = 100;

    public DrawableGraph(Graph graph) {
        this.graph = graph;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void draw(@NonNull Canvas canvas) {
        Paint p = new Paint();
        for (Node noeud : graph.getNodes()) {
            canvas.drawRoundRect(noeud.getCoordX(), noeud.getCoordY(), noeud.getCoordX()+nodeSize, noeud.getCoordY()+nodeSize,50,50,p);
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
