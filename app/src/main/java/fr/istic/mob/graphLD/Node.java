package fr.istic.mob.graphLD;

import android.graphics.RectF;

import java.io.Serializable;

public class Node extends RectF  implements Serializable {

    private float coordX;
    private float coordY;
    private String color;
    private int nodeSize;
    private int nodeLength;
    private String label;
    private String labelColor;

    public Node(float coordX, float coordY, int newNodeSize) {
        super(coordX-newNodeSize/2,coordY-newNodeSize/2,coordX+newNodeSize/2,coordY+newNodeSize/2);
        this.coordX = coordX;
        this.coordY = coordY;
        this.color = "Noir";
        this.nodeSize = newNodeSize;
        nodeLength = nodeSize;
        this.labelColor = "Bleu";
    }

    public float getCoordX() {
        return coordX;
    }

    public void setCoordX(float coordX) {
        super.set(coordX-nodeSize/2,this.coordY-nodeSize/2, coordX+nodeSize/2, this.coordY+nodeSize/2);
        this.coordX = coordX;
    }

    public float getCoordY() {
        return coordY;
    }

    public void setCoordY(float coordY) {
        super.set(this.coordX-nodeSize/2,coordY-nodeSize/2, this.coordX+nodeSize/2, coordY+nodeSize/2);
        this.coordY = coordY;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String couleur) {
        this.color = couleur;
    }

    public int getNodeSize() {
        return nodeSize;
    }

    public void setNodeSize(int nodeSize) {
        this.nodeSize = nodeSize;
        this.setNodeLength(nodeSize);
    }

    public int getNodeLength() {
        return nodeLength;
    }

    public void setNodeLength(int nodeLength) {
        this.nodeLength = nodeLength;
        super.set (coordX-nodeLength/2,this.coordY-nodeSize/2, coordX+nodeLength/2, this.coordY+nodeSize/2);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public String getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

}
