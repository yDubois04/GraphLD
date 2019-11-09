package fr.istic.mob.graphLD;

import android.graphics.Path;

import java.io.Serializable;

public class Arc extends Path  implements Serializable {

    private Node node1;
    private Node node2;
    private String label;
    private String color;
    private float thickness;
    private float labelSize;

    public Arc (Node node1, Node node2) {
        super ();
        this.node1 = node1;
        this.node2 = node2;
        this.moveTo(node1.getCoordX(),node1.getCoordY());
        this.lineTo(node2.getCoordX(),node2.getCoordY());
        this.color = "Bleu";
        this.labelSize = 30;
        this.thickness = 8;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public float getThickness() {
        return thickness;
    }

    public void setThickness(float labelThickness) {
        this.thickness = labelThickness;
    }

    public float getLabelSize() {
        return labelSize;
    }

    public void setLabelSize(float labelSize) {
        this.labelSize = labelSize;
    }
}
