package fr.istic.mob.graphLD;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DrawableGraph drawableGraph;
    private Graph graph;
    ImageView imageView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initialiserGraph();
        drawableGraph = new DrawableGraph(graph);
        imageView = findViewById(R.id.imageView);
        imageView.setImageDrawable(drawableGraph);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int action = motionEvent.getAction();
                float x = motionEvent.getX();
                float y = motionEvent.getY();

                Node touchNode = findTouchNode (x,y);

                if (action == MotionEvent.ACTION_DOWN && touchNode != null) {
                    touchNode.setCoordX((int)x);
                    touchNode.setCoordY((int)y);
                    drawableGraph = new DrawableGraph(graph);
                    imageView.setImageDrawable(drawableGraph);
                }

                if ( action == MotionEvent.ACTION_MOVE && touchNode != null) {
                    touchNode.setCoordX((int)x);
                    touchNode.setCoordY((int)y);
                    drawableGraph = new DrawableGraph(graph);
                    imageView.setImageDrawable(drawableGraph);
                }

                if ( action == MotionEvent.ACTION_UP && touchNode != null) {
                    touchNode.setCoordX((int)x);
                    touchNode.setCoordY((int)y);
                    drawableGraph = new DrawableGraph(graph);
                    imageView.setImageDrawable(drawableGraph);
                }

                return true;
            }
        });
    }

    public Node findTouchNode (float coordX, float coordY) {
        Node n = null;

        for (Node node : graph.getNodes()) {
            if (node.contains(coordX,coordY)) {
                return node;
            }
        }
        return n;
    }

    private void initialiserGraph () {
        graph = new Graph();
        for (int i = 0; i < 9; i++) {
            graph.getNodes().add(new Node(120f*i, 0, "noir","noeud"+i));
        }
    }
}
