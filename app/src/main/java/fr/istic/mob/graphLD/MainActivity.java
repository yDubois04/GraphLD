package fr.istic.mob.graphLD;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.GestureDetector;
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
    int imageViewHeight, imageViewWidth;
    Modes mode;
    private int nodeSize = 80;

    Node initialNode = null;
    Node nodeTMP = null;
    Arc arc = null;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int action = motionEvent.getAction();
                float x = motionEvent.getX();
                float y = motionEvent.getY();

                Node touchNode = findTouchNode (x,y);

                if (mode == Modes.NodeMode && touchNode != null) {
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
                        }
                        update();
                    }
                }

                if (mode == Modes.ArcMode){
                    if(action == MotionEvent.ACTION_DOWN && touchNode != null){
                        initialNode = touchNode;
                        nodeTMP = new Node(x, y, "noir", nodeSize);
                        arc = new Arc(initialNode, nodeTMP);
                        graph.addArc(arc);

                        arc.reset();
                        arc.moveTo(initialNode.getCoordX(), initialNode.getCoordY());
                        arc.lineTo(nodeTMP.getCoordX(), nodeTMP.getCoordY());

                        update();
                    }
                    if(action == MotionEvent.ACTION_MOVE){
                        if(nodeTMP != null) {
                            nodeTMP.setCoordX(x);
                            nodeTMP.setCoordY(y);

                            arc.reset();
                            arc.moveTo(initialNode.getCoordX(), initialNode.getCoordY());
                            arc.lineTo(nodeTMP.getCoordX(), nodeTMP.getCoordY());

                            update();
                        }
                    }
                    if(action == MotionEvent.ACTION_UP){
                        if(touchNode != null && nodeTMP != touchNode){
                            graph.removeArc(arc);
                            arc = new Arc(initialNode, touchNode);
                            graph.addArc(arc);

                            arc.reset();
                            arc.moveTo(initialNode.getCoordX(), initialNode.getCoordY());
                            arc.lineTo(touchNode.getCoordX(), touchNode.getCoordY());

                            update();
                        }
                        else{
                            graph.removeArc(arc);
                            update();
                        }
                    }
                }

                return true;
            }
        });

        imageView.post(new Runnable() {
            @Override
            public void run() {
                imageViewWidth = imageView.getWidth();
                imageViewHeight = imageView.getHeight();

                initialiserGraph();
                update();
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
        mode = Modes.NodeMode;
        graph = new Graph();
        for (int i = 0; i < 9; i++) {
            graph.getNodes().add(new Node(imageViewWidth/10f * i + nodeSize/2f + 10, nodeSize/2f + 10, "noir", nodeSize));
        }
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
            //Toast.makeText(getApplicationContext(),"RESET", Toast.LENGTH_SHORT).show();
            initialiserGraph();
            update();
        }
        if(itemID == R.id.delButton){
            //Toast.makeText(getApplicationContext(),"DELETE", Toast.LENGTH_SHORT).show();
            graph = new Graph();
            update();
        }
        else if(itemID == R.id.addNodeModeButton){
            //Toast.makeText(getApplicationContext(),"NOEUD", Toast.LENGTH_SHORT).show();
            graph.addNode(new Node(300, 300, "blue", nodeSize));
            update();
            mode = Modes.NodeMode;
        }
        else if(itemID == R.id.modeArcButton){
            //Toast.makeText(getApplicationContext(),"ARC", Toast.LENGTH_SHORT).show();
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
