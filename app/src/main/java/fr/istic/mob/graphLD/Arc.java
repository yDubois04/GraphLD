package fr.istic.mob.graphLD;

import android.graphics.Path;

public class Arc extends Path {

    private Node node1;
    private Node node2;
    private String label;

    public Arc (Node node1, Node node2, String label) {
        super ();
        this.node1 = node1;
        this.node2 = node2;
        this.label = label;
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
}
