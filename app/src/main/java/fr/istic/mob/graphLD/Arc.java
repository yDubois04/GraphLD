package fr.istic.mob.graphLD;

import android.graphics.Path;
import android.widget.TextView;

public class Arc extends Path {

    private Node node1;
    private Node node2;
    private TextView label;

    public Arc (Node node1, Node node2) {
        super ();
        this.node1 = node1;
        this.node2 = node2;
        this.moveTo(node1.getCoordX(),node1.getCoordY());
        this.lineTo(node2.getCoordX(),node2.getCoordY());
    }

    public Node getNode1() {
        return node1;
    }

    public void setNode1(Node node1) {
        this.node1 = node1;
    }

    public Node getNode2() {
        return node2;
    }

    public void setNode2(Node node2) {
        this.node2 = node2;
    }

    public TextView getLabel() {
        return label;
    }

    public void setLabel(TextView label) {
        this.label = label;
    }
}
