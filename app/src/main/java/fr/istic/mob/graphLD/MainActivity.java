package fr.istic.mob.graphLD;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private DrawableGraph drawableGraph;
    private Graph graph;
    ImageView imageView;
    int imageViewHeight, imageViewWidth;
    Modes mode;
    private int nodeSize = 0;

    Node initialNode = null;
    Node nodeTMP = null;
    Arc arc = null;

    Node currentNode = null;
    boolean hasTouchMoved = false;

    final float SENSITIVE_MOVE = 2f;
    private float currentTouchX = 0;
    private float currentTouchY = 0;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            Graph retrievedGraph = (Graph) savedInstanceState.getSerializable("Graph");
            graph = retrievedGraph;
        }

        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        registerForContextMenu(imageView);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int action = motionEvent.getAction();
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                float xTouchVariation = Math.abs(currentTouchX-x);
                float yTouchVariation = Math.abs(currentTouchY-y);
                currentTouchX = motionEvent.getX();
                currentTouchY = motionEvent.getY();

                Node touchNode = findTouchNode(x,y);

                if ((mode == Modes.NodeMode || mode == Modes.EditMode) && touchNode != null) {
                    if (action == MotionEvent.ACTION_DOWN ) {
                        hasTouchMoved = false;
                        currentNode = touchNode;
                    }
                    else if(action == MotionEvent.ACTION_MOVE && (xTouchVariation > SENSITIVE_MOVE || yTouchVariation > SENSITIVE_MOVE) && currentNode != null){
                        if ((currentTouchX < imageViewWidth-nodeSize/2 && currentTouchY <imageViewHeight-nodeSize/2) && (currentTouchX> nodeSize/2 && y >nodeSize/2)) {
                            hasTouchMoved = true;

                            currentNode.setCoordX(currentTouchX);
                            currentNode.setCoordY(currentTouchY);
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

                            showAddArcItemDialog(MainActivity.this);
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

                if (graph == null){
                    initialiserGraph();
                }
                update();
            }
        });
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Graph", graph);
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
        if(currentNode != null && !hasTouchMoved && mode == Modes.EditMode) {
            getMenuInflater().inflate(R.menu.context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected (MenuItem item) {

        if (item.getItemId() == R.id.itemDelete) {
            this.graph.removeNodes(currentNode);
        }
        if (item.getItemId() == R.id.itemModifiyColor) {
            Toast.makeText(getApplicationContext(), "Modifier couleur", Toast.LENGTH_SHORT);
        }

        if (item.getItemId() == R.id.itemModifiyLabel) {
            showModifyNodeLabelItemDialog(MainActivity.this);
        }

        if (item.getItemId() == R.id.itemModifiySize) {
            Toast.makeText(getApplicationContext(), "Modifier la taille", Toast.LENGTH_SHORT);
        }
        this.update();
        return super.onContextItemSelected(item);
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
        drawableGraph = new DrawableGraph(graph, nodeSize, getApplicationContext());
        imageView.setImageDrawable(drawableGraph);
    }

    private void showAddArcItemDialog(final Context c) {
        final EditText taskEditText = new EditText(c);
        final AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(R.string.popup_add_arc_title)
                .setMessage(R.string.popup_add_arc_message)
                .setView(taskEditText)
                .setPositiveButton(R.string.popup_validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = taskEditText.getText().toString();
                        arc.setLabel(value);
                        update();
                    }
                })
                .create();
        dialog.show();
    }

    private void showModifyNodeLabelItemDialog(final Context c) {
        final EditText taskEditText = new EditText(c);
        final AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(R.string.popup_modify_label_node)
                .setMessage(R.string.popup_modify_label_node_message)
                .setView(taskEditText)
                .setPositiveButton(R.string.popup_validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = taskEditText.getText().toString();
                        currentNode.setLabel(value);
                        update();
                    }
                })
                .create();
        dialog.show();
    }
}
