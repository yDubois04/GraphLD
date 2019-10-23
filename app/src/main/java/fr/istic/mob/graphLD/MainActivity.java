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
    Modes mode;
    private int nodeSize = 80;

    @SuppressLint("ClickableViewAccessibility")
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
                Node currentNode = new Node (x,y, "noir",nodeSize);

                if ((mode == Modes.NodeMode || mode == Modes.EditMode) && touchNode != null ) {
                    if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_UP) {
                        touchNode.setCoordX(x);
                        touchNode.setCoordY(y);
                        for (Arc arc : graph.getArcs()) {
                            if(arc.getNode1() == touchNode || arc.getNode2() == touchNode){
                                arc.reset();
                                Node node1 = arc.getNode1();
                                Node node2 = arc.getNode2();
                                arc.moveTo(node1.getCoordX(), node1.getCoordY());
                                arc.lineTo(node2.getCoordX(), node2.getCoordY());
                            }
                            update();
                        }
                    }
                }

                if (mode == Modes.ArcMode && touchNode != null) {
                    Arc arc = new Arc(touchNode,currentNode);

                    if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_UP) {
                        arc.moveTo(touchNode.getCoordX(), touchNode.getCoordY());
                        arc.lineTo(currentNode.getCoordX(),currentNode.getCoordY());
                        graph.addArc(arc);
                    }
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
        mode = Modes.NodeMode;
        graph = new Graph();
        for (int i = 0; i < 9; i++) {
            graph.getNodes().add(new Node(nodeSize/2,(90f + nodeSize/2)*i, "noir", nodeSize));
        }
        Arc arc = new Arc(graph.getNodes().get(1),graph.getNodes().get(2));
        graph.addArc(arc);

        drawableGraph = new DrawableGraph(graph, nodeSize);
        imageView = findViewById(R.id.imageView);
        imageView.setImageDrawable(drawableGraph);
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
            initialiserGraph();
            update();
        }
        if(itemID == R.id.delButton) {
            graph = new Graph();
            update();
        }
        else if(itemID == R.id.addNodeModeButton){
            graph.addNode(new Node(300, 300, "blue", nodeSize));
            update();
            mode = Modes.NodeMode;
        }
        else if(itemID == R.id.modeArcButton){
            mode = Modes.ArcMode;
        }
        else if(itemID == R.id.modeEditButton){
            mode = Modes.NodeMode;
        }

        return super.onOptionsItemSelected(item);
    }

    private void update(){
        drawableGraph = new DrawableGraph(graph, nodeSize);
        imageView.setImageDrawable(drawableGraph);
    }
}
