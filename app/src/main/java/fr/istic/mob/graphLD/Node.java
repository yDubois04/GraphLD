package fr.istic.mob.graphLD;

import android.graphics.RectF;

public class Node extends RectF {

    private float coordX;
    private float coordY;
    private String couleur;
    private String label;
    private int nodeSize;

    public Node(float coordX, float coordY, String couleur, String label, int newNodeSize) {
        super(coordX-newNodeSize/2,coordY-newNodeSize/2,coordX+newNodeSize/2,coordY+newNodeSize/2);
        this.coordX = coordX;
        this.coordY = coordY;
        this.couleur = couleur;
        this.label = label;

        nodeSize = newNodeSize;
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

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
