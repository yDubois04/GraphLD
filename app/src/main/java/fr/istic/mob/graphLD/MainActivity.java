package fr.istic.mob.graphLD;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {

    private DrawableGraph drawableGraph;
    private Graph graph;
    ImageView imageView;
    int imageViewHeight, imageViewWidth;
    Modes mode;
    private int initialNodeSize;

    Node initialNode = null;
    Node nodeTMP = null;
    Arc arc = null;

    Node currentNode = null;
    Arc currentArc = null;
    boolean hasTouchMoved = false;

    final float SENSITIVE_MOVE = 2f;
    private float currentTouchX = 0;
    private float currentTouchY = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            graph = (Graph) savedInstanceState.getSerializable("Graph");
        }

        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        registerForContextMenu(imageView);
        getSupportActionBar().setTitle(R.string.title_appli);

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
                currentArc = findTouchArc(x, y);

                if ((mode == Modes.NodeMode || mode == Modes.EditMode) && touchNode != null) {
                    if (action == MotionEvent.ACTION_DOWN ) {
                        hasTouchMoved = false;
                        currentNode = touchNode;
                    }
                    else if(action == MotionEvent.ACTION_MOVE && (xTouchVariation > SENSITIVE_MOVE || yTouchVariation > SENSITIVE_MOVE) && currentNode != null){
                        if ((x< imageViewWidth-currentNode.getNodeSize()/2 && y <imageViewHeight-currentNode.getNodeSize()/2) && (x> currentNode.getNodeSize()/2 && y > currentNode.getNodeSize()/2)) {
                            hasTouchMoved = true;

                            currentNode.setCoordX(x);
                            currentNode.setCoordY(y);
                            for (Arc arc : graph.getArcs()) {
                                if (arc.getNode1() == currentNode || arc.getNode2() == currentNode) {
                                    arc.reset();
                                    Node node1 = arc.getNode1();
                                    Node node2 = arc.getNode2();
                                    arc.moveTo(node1.getCoordX(), node1.getCoordY());
                                    //arc.quadTo(node2.getCoordX(), node2.getCoordX()/2, node2.getCoordX(), node2.getCoordY());
                                  //  arc.quadTo(Math.abs(node1.getCoordX() - node2.getCoordX()), Math.abs(node1.getCoordY() - node2.getCoordY()), node2.getCoordX(), node2.getCoordY());
                                    arc.lineTo(node2.getCoordX(), node2.getCoordY());
                                }
                            }
                            update();
                        }
                    }
                }
                else if(touchNode == null){
                    currentNode = null;
                    hasTouchMoved = false;
                }

                if (mode == Modes.ArcMode) {
                    if (action == MotionEvent.ACTION_DOWN && touchNode != null) {
                        initialNode = touchNode;
                        nodeTMP = new Node(x, y, initialNodeSize);
                        arc = new Arc(initialNode, nodeTMP);
                        graph.addArc(arc);

                        arc.reset();
                        arc.moveTo(initialNode.getCoordX(), initialNode.getCoordY());
                        arc.lineTo(nodeTMP.getCoordX(), nodeTMP.getCoordY());

                        update();
                    }
                    if (action == MotionEvent.ACTION_MOVE && nodeTMP != null && arc != null) {
                        nodeTMP.setCoordX(x);
                        nodeTMP.setCoordY(y);

                        arc.reset();
                        arc.moveTo(initialNode.getCoordX(), initialNode.getCoordY());
                        arc.lineTo(nodeTMP.getCoordX(), nodeTMP.getCoordY());

                        update();
                    }
                    if (action == MotionEvent.ACTION_UP && arc != null) {
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
                            arc = null;
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

                initialNodeSize = imageViewWidth/12;

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
                n = node;
            }
        }
        return n;
    }

    public Arc findTouchArc(float x, float y){
        Arc a = null;

        for (Arc arc : graph.getArcs()) {
            RectF bounds = new RectF();
            arc.computeBounds(bounds,true);
            System.out.println("bottom "+bounds.bottom+" top "+bounds.top+" left "+bounds.left+ " right "+bounds.right);
            imageView.invalidate();
            if (bounds.contains(x,y)) {
                a = arc;
            }
        }
        return a;
    }

    private void initialiserGraph () {
        mode = Modes.EditMode;
        graph = new Graph();
        for (int i = 0; i < 9; i++) {
            graph.getNodes().add(new Node(imageViewWidth/9f * i + initialNodeSize/2f, initialNodeSize/2f, initialNodeSize));
        }
    }

    @Override
    public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu (menu,v,menuInfo);
        if(currentNode != null && !hasTouchMoved && mode == Modes.EditMode) {
            getMenuInflater().inflate(R.menu.context_menu_modify_node, menu);
        }

        else if(currentArc != null && mode == Modes.EditMode) {
            getMenuInflater().inflate(R.menu.context_menu_modify_arc_menu, menu);
        }

        else if (mode == Modes.NodeMode && !hasTouchMoved) {
            getMenuInflater().inflate(R.menu.add_node_menu, menu);
        }

    }

    @Override
    public boolean onContextItemSelected (MenuItem item) {

        if (item.getItemId() == R.id.itemDelete) {
            this.graph.removeNodes(currentNode);
        }
        if (item.getItemId() == R.id.itemModifiyColor) {
            showModifyColorPopup(MainActivity.this, "Node");
        }

        if (item.getItemId() == R.id.itemModifiyLabel) {
            showModifyNodeLabelPopup(MainActivity.this);
        }

        if (item.getItemId() == R.id.itemModifiySize) {
            showModifyNodeSizePopup (MainActivity.this);
        }

        if (item.getItemId() == R.id.itemModifyNodeLabelColor) {
            showModifyColorPopup(MainActivity.this, "NodeLabel");
        }

        if (item.getItemId() == R.id.itemDeleteArc) {
            this.graph.removeArc(currentArc);
        }

        if (item.getItemId() == R.id.itemModifiyArcColor) {
            showModifyColorPopup(MainActivity.this, "Arc");
        }

        if (item.getItemId() == R.id.itemModifiyArcLabel) {
            showModifyArcLabelPopup(MainActivity.this);
        }

        if (item.getItemId() == R.id.itemModifiyArcLabelSize) {
            showModifyArcLabelSizePopup(MainActivity.this);
        }

        if (item.getItemId() == R.id.itemModifiyArcThickness) {
            showModifyArcThicknessPopup(MainActivity.this);
        }
        if (item.getItemId() == R.id.addNewNode) {
            showAddNodeDialog(MainActivity.this);
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
            mode = Modes.NodeMode;
        }
        else if(itemID == R.id.modeArcButton){
            mode = Modes.ArcMode;
        }
        else if(itemID == R.id.modeEditButton){
            mode = Modes.EditMode;
        }
        else if (itemID == R.id.sendGraphByEmailButton) {
          saveGraphInBitmap();
        }
        else if (itemID == R.id.saveGraphButton) {
            saveGraphInMemory();
        }
        else if (itemID == R.id.displayGraphButton) {
           displayGraph();
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveGraphInMemory () {
        FileOutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        String nameFile = "graph.txt";
        try {
            outputStream = openFileOutput(nameFile, MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(graph);
        } catch (IOException e) {
            System.out.println("Erreur ! : " + e);
        }
    }

    private void displayGraph () {
        try {
            FileInputStream input = openFileInput("graph.txt");
            ObjectInputStream inputStream = new ObjectInputStream(input);
            graph = (Graph) inputStream.readObject();
            for (Arc arc : graph.getArcs()) {
                arc.moveTo(arc.getNode1().getCoordX(), arc.getNode1().getCoordY());
                arc.lineTo(arc.getNode2().getCoordX(), arc.getNode2().getCoordY());
            }
            update();
        }
        catch (Exception e) {
            System.out.println("Erreur ! : " +e);
        }
    }

    private void saveGraphInBitmap () {
        Bitmap bitmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        imageView.setBackgroundColor(Color.WHITE);
        imageView.draw(c);

        String bitmapUrl = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                getString(R.string.title_graph),
                getString(R.string.graph_description)
        );
        Uri uri = Uri.parse(bitmapUrl);
        imageView.setImageURI(uri);
        sendGraphByMail(uri);
    }

    private void sendGraphByMail (Uri path) {
        Intent emailInt = new Intent(Intent.ACTION_SEND);
        emailInt.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.title_graph));
        emailInt.setType("image/*");
        emailInt.putExtra(Intent.EXTRA_STREAM, path);
        startActivity(Intent.createChooser(emailInt,"Send graph"));
    }

    private void update(){
        drawableGraph = new DrawableGraph(graph, getApplicationContext());
        imageView.setImageDrawable(drawableGraph);
    }


    /***
     *
     *
     * Define application popup
     *
     *
     */

    private void showAddNodeDialog(final Context c) {
        final EditText taskEditText = new EditText(c);
        final AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(R.string.add_node_menu)
                .setMessage(R.string.popup_modify_label_node_message)
                .setView(taskEditText)
                .setPositiveButton(R.string.popup_validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = taskEditText.getText().toString();
                        Node node = new Node (currentTouchX, currentTouchY, initialNodeSize);
                        node.setLabel(value);
                        graph.addNode(node);
                        update();
                    }
                })
                .create();
        dialog.show();
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
                        arc = null;
                        nodeTMP = null;
                    }
                })
                .create();
        dialog.show();
    }

    private void showModifyNodeLabelPopup(final Context c) {
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

    private void showModifyArcLabelPopup(final Context c) {
        final EditText taskEditText = new EditText(c);
        final AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(R.string.popup_modify_arc_label_title)
                .setMessage(R.string.popup_modify_arc_label_message)
                .setView(taskEditText)
                .setPositiveButton(R.string.popup_validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = taskEditText.getText().toString();
                        currentArc.setLabel(value);
                        update();
                    }
                })
                .create();
        dialog.show();
    }

    private void showModifyColorPopup(final Context c, final String type) {

        final String [] colors = {getString(R.string.color_blue), getString(R.string.color_cyan), getString(R.string.color_green),
                getString(R.string.color_noir), getString(R.string.color_magenta), getString(R.string.color_orange), getString(R.string.color_red)};

        AlertDialog.Builder popup = new AlertDialog.Builder(c);
        popup.setTitle(R.string.popup_modify_color_node_message);
        int  itemChecked = -1;
        popup.setSingleChoiceItems(colors, itemChecked, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                if (type == "Node") {
                    currentNode.setColor(colors [index]);
                }
                else if (type == "NodeLabel") {
                    currentNode.setLabelColor(colors[index]);
                }
                else if (type == "Arc") {
                    currentArc.setColor(colors [index]);
                }
                update();
            }
        });
        popup.setPositiveButton(R.string.popup_validate, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = popup.create();
        dialog.show();
    }

    private void showModifyNodeSizePopup (Context c) {
        final EditText modifySizeText = new EditText(c);
        modifySizeText.setInputType(InputType.TYPE_CLASS_NUMBER);
        modifySizeText.setRawInputType(Configuration.KEYBOARD_12KEY);

        final AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(R.string.popup_modify_node_size)
                .setMessage(R.string.popup_modify_node_size_message)
                .setView(modifySizeText)
                .setPositiveButton(R.string.popup_validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int value = Integer.parseInt(modifySizeText.getText().toString());
                            if (value >= 50 && value <= 250) {
                                currentNode.setNodeSize(value);
                            }
                            update();
                        }
                        catch(Exception e){}
                    }
                })
                .create();
        dialog.show();
    }

    private void showModifyArcLabelSizePopup (Context c) {
        final EditText modifySizeText = new EditText(c);
        modifySizeText.setInputType(InputType.TYPE_CLASS_NUMBER);
        modifySizeText.setRawInputType(Configuration.KEYBOARD_12KEY);

        final AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(R.string.popup_modify_arc_label_size)
                .setMessage(R.string.popup_modify_arc_size_label_message)
                .setView(modifySizeText)
                .setPositiveButton(R.string.popup_validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int value = Integer.parseInt(modifySizeText.getText().toString());
                            if (value >= 20 && value <= 100) {
                                currentArc.setLabelSize(value);
                            }
                            update();
                        }
                        catch(Exception e){}
                    }
                })
                .create();
        dialog.show();
    }

    private void showModifyArcThicknessPopup (Context c) {
        final EditText modifySizeText = new EditText(c);
        modifySizeText.setInputType(InputType.TYPE_CLASS_NUMBER);
        modifySizeText.setRawInputType(Configuration.KEYBOARD_12KEY);

        final AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(R.string.popup_modify_arc_thickness)
                .setMessage(R.string.popup_modify_arc_thickness_message)
                .setView(modifySizeText)
                .setPositiveButton(R.string.popup_validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int value = Integer.parseInt(modifySizeText.getText().toString());
                            if (value >= 1 && value <= 25) {
                                currentArc.setThickness(value);
                            }
                            update();
                        }
                        catch(Exception e){}
                    }
                })
                .create();
        dialog.show();
    }
}
