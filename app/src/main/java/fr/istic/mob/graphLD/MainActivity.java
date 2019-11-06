package fr.istic.mob.graphLD;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private DrawableGraph drawableGraph;
    private Graph graph;
    ImageView imageView;
    int imageViewHeight, imageViewWidth;
    Modes mode;
    private int nodeSize;

    Node initialNode = null;
    Node nodeTMP = null;
    Arc arc = null;

    Node currentNode = null;
    boolean hasTouchMoved = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        registerForContextMenu(imageView);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int action = motionEvent.getAction();
                float x = motionEvent.getX();
                float y = motionEvent.getY();

                Node touchNode = findTouchNode(x, y);

                if ((mode == Modes.NodeMode || mode == Modes.EditMode) && touchNode != null) {
                    if (action == MotionEvent.ACTION_DOWN ) {
                        hasTouchMoved = false;
                        currentNode = touchNode;
                    }
                    else if(action == MotionEvent.ACTION_MOVE && currentNode != null){
                        if ((x < imageViewWidth-nodeSize/2 && y <imageViewHeight-nodeSize/2) && (x > nodeSize/2 && y >nodeSize/2)) {
                            hasTouchMoved = true;

                            currentNode.setCoordX(x);
                            currentNode.setCoordY(y);
                            for (Arc arc : graph.getArcs()) {
                                if (arc.getNode1() == currentNode || arc.getNode2() == currentNode) {
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
                }
                else if(touchNode == null){
                    currentNode = null;
                }

                if (mode == Modes.ArcMode) {
                    if (action == MotionEvent.ACTION_DOWN && touchNode != null) {
                        initialNode = touchNode;
                        nodeTMP = new Node(x, y, "noir", nodeSize);
                        arc = new Arc(initialNode, nodeTMP);
                        graph.addArc(arc);

                        arc.reset();
                        arc.moveTo(initialNode.getCoordX(), initialNode.getCoordY());
                        arc.lineTo(nodeTMP.getCoordX(), nodeTMP.getCoordY());

                        update();
                    }
                    if (action == MotionEvent.ACTION_MOVE) {
                        if (nodeTMP != null) {
                            nodeTMP.setCoordX(x);
                            nodeTMP.setCoordY(y);

                            arc.reset();
                            arc.moveTo(initialNode.getCoordX(), initialNode.getCoordY());
                            arc.lineTo(nodeTMP.getCoordX(), nodeTMP.getCoordY());

                            update();
                        }
                    }
                    if (action == MotionEvent.ACTION_UP) {
                        if (touchNode != null && nodeTMP != touchNode) {
                            graph.removeArc(arc);
                            arc = new Arc(initialNode, touchNode);
                            graph.addArc(arc);

                            arc.reset();
                            arc.moveTo(initialNode.getCoordX(), initialNode.getCoordY());
                            arc.lineTo(touchNode.getCoordX(), touchNode.getCoordY());

                            showAddItemDialog(MainActivity.this);
                            update();
                        } else {
                            graph.removeArc(arc);
                            update();
                        }
                    }
                }

                return false;
            }
        });

        imageView.post(new Runnable() {
            @Override
            public void run() {
                imageViewWidth = imageView.getWidth();
                imageViewHeight = imageView.getHeight();

                nodeSize = imageViewWidth/12;

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
            graph.getNodes().add(new Node(imageViewWidth/9f * i + nodeSize/2f + 10, nodeSize/2f + 10, "noir", nodeSize));
        }
    }

    @Override
    public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu (menu,v,menuInfo);
        if(currentNode != null && !hasTouchMoved) {
            getMenuInflater().inflate(R.menu.context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected (MenuItem item) {

        if (item.getItemId() == R.id.itemDelete) {
            Toast.makeText(getApplicationContext(), "Noeud supprimé", Toast.LENGTH_SHORT);
        }
        if (item.getItemId() == R.id.itemModifiyColor) {
            Toast.makeText(getApplicationContext(), "Modifier couleur", Toast.LENGTH_SHORT);
        }

        if (item.getItemId() == R.id.itemModifiyLabel) {
            Toast.makeText(getApplicationContext(), "Modifier etiquette", Toast.LENGTH_SHORT);
        }

        if (item.getItemId() == R.id.itemModifiySize) {
            Toast.makeText(getApplicationContext(), "Modifier la taille", Toast.LENGTH_SHORT);
        }
        return true;
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
        if(itemID == R.id.delButton){
            graph = new Graph();
            update();
        }
        else if(itemID == R.id.addNodeModeButton){
            graph.addNode(new Node(300, 300, "noir", nodeSize));
            update();
            mode = Modes.NodeMode;
        }
        else if(itemID == R.id.modeArcButton){
            mode = Modes.ArcMode;
        }
        else if(itemID == R.id.modeEditButton){
            mode = Modes.EditMode;
        }

        return super.onOptionsItemSelected(item);
    }

    private void update(){
        drawableGraph = new DrawableGraph(graph, nodeSize);
        imageView.setImageDrawable(drawableGraph);
    }


    private void showAddItemDialog(final Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Add a new arc")
                .setMessage("What is the name of the arc?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView tV = new TextView(c);
                        tV.setX(arc.getNode1().getCoordX());
                        tV.setY(arc.getNode2().getCoordY());
                        tV.setText(taskEditText.getText());
                        tV.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                        arc.setLabel(tV);
                        arc.getLabel().setVisibility(imageView.VISIBLE);
                        update();
                    }
                })
                .create();
        dialog.show();
    }
}
