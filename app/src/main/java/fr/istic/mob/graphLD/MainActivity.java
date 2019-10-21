package fr.istic.mob.graphLD;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DrawableGraph drawableGraph;
    private Graph graph;
    ImageView imageView;
    private boolean nodeMode;
    private boolean arcMode;

    private int nodeSize = 100;


    @SuppressLint("ClickableViewAccessibility") //???
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initialiserGraph();

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
                    drawableGraph = new DrawableGraph(graph, nodeSize);
                    imageView.setImageDrawable(drawableGraph);
                }

                if ( action == MotionEvent.ACTION_MOVE && touchNode != null) {
                    touchNode.setCoordX(x);
                    touchNode.setCoordY(y);
                    drawableGraph = new DrawableGraph(graph, nodeSize);
                    imageView.setImageDrawable(drawableGraph);
                }

                if ( action == MotionEvent.ACTION_UP && touchNode != null) {
                    touchNode.setCoordX(x);
                    touchNode.setCoordY(y);
                    drawableGraph = new DrawableGraph(graph, nodeSize);
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

    private void initialiserGraph () { //Tout les noeuds n'apparaissent pas, a regler
        graph = new Graph();
        for (int i = 0; i < 9; i++) {
            graph.getNodes().add(new Node((120f + nodeSize/2)*i, nodeSize/2, "noir","noeud"+i, nodeSize));
        }

        drawableGraph = new DrawableGraph(graph, nodeSize);
        imageView = findViewById(R.id.imageView);
        imageView.setImageDrawable(drawableGraph);
        nodeMode = true;
        arcMode = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        if(itemID == R.id.resetButton){
            Toast.makeText(getApplicationContext(),"RESET", Toast.LENGTH_SHORT).show();
            initialiserGraph();
            update();
        }
        if(itemID == R.id.delButton){
            Toast.makeText(getApplicationContext(),"DELETE", Toast.LENGTH_SHORT).show();
            graph = new Graph();
            update();
        }
        else if(itemID == R.id.addNodeButton){
            Toast.makeText(getApplicationContext(),"NOEUD", Toast.LENGTH_SHORT).show();
            graph.addNode(new Node(300, 300, "blue", "", nodeSize));
            update();
        }
        else if(itemID == R.id.addArcButton){
            Toast.makeText(getApplicationContext(),"ARC", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void update(){
        drawableGraph = new DrawableGraph(graph, nodeSize);
        imageView.setImageDrawable(drawableGraph);
    }
}
