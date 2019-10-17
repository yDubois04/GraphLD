package fr.istic.mob.graphLD;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DrawableGraph graphView;
    private Graph graphe;
    ImageView imageGraph;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initialiserGraph();
        graphView = new DrawableGraph(graphe);
        imageGraph = findViewById(R.id.imageView);
        imageGraph.setImageDrawable(graphView);

        imageGraph.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int action = motionEvent.getAction();
                float x = motionEvent.getX();
                float y = motionEvent.getY();

                Node touchNode = findTouchNode (x,y);

                if (action == MotionEvent.ACTION_DOWN && touchNode != null) {
                    touchNode.setCoordX((int)x);
                    touchNode.setCoordY((int)y);
                    graphView = new DrawableGraph(graphe);
                    imageGraph.setImageDrawable(graphView);
                }

                if ( action == MotionEvent.ACTION_MOVE && touchNode != null) {
                    touchNode.setCoordX((int)x);
                    touchNode.setCoordY((int)y);
                    graphView = new DrawableGraph(graphe);
                    imageGraph.setImageDrawable(graphView);
                }

                if ( action == MotionEvent.ACTION_UP && touchNode != null) {
                    touchNode.setCoordX((int)x);
                    touchNode.setCoordY((int)y);
                    graphView = new DrawableGraph(graphe);
                    imageGraph.setImageDrawable(graphView);
                }

                return true;
            }
        });
    }

    private void initialGraph(){
        graphe = new Graph();
        for(int i = 0; i < 9; i++){
            graphe.addNode(new Node(i * 120, i * 120, "blue", ""));
        }
    }

    public Node findTouchNode (float coordX, float coordY) {
        Node n = null;

        for (Node node : graphe.getNodes()) {
            if (node.contains(coordX,coordY)) {
                return node;
            }
        }
        return n;
    }

    private void initialiserGraph () {
        graphe = new Graph();
        for (int i = 0; i < 9; i++) {
            graphe.getNodes().add(new Node(120f*i, 0, "noir","noeud"+i));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        if(itemID == R.id.resetButton){
            Toast.makeText(getApplicationContext(),"RESET", Toast.LENGTH_SHORT).show();
            initialGraph();
            update();
        }
        if(itemID == R.id.delButton){
            Toast.makeText(getApplicationContext(),"DELETE", Toast.LENGTH_SHORT).show();
            graphe = new Graph();
            update();
        }
        else if(itemID == R.id.addNodeButton){
            Toast.makeText(getApplicationContext(),"NOEUD", Toast.LENGTH_SHORT).show();
            graphe.addNode(new Node(300, 300, "blue", ""));
            update();
        }
        else if(itemID == R.id.addArcButton){
            Toast.makeText(getApplicationContext(),"ARC", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void update(){
        graphView = new DrawableGraph(graphe);
        imageGraph.setImageDrawable(graphView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}
