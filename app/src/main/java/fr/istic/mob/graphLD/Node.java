package fr.istic.mob.graphLD;

import android.graphics.RectF;

public class Node extends RectF {

    private float coordX;
    private float coordY;
    private String couleur;
    private String label;

    public Node(float coordX, float coordY, String couleur, String label) {
        super(coordX,coordY,coordX+100,coordY+100);
        this.coordX = coordX;
        this.coordY = coordY;
        this.couleur = couleur;
        this.label = label;
    }

    public float getCoordX() {
        return coordX;
    }

    public void setCoordX(float coordX) {
        super.set(coordX-50,this.coordY-50, coordX+50, this.coordY+50);
        this.coordX = coordX;
    }

    public float getCoordY() {
        return coordY;
    }

    public void setCoordY(float coordY) {
        super.set(this.coordX-50,coordY-50, this.coordX+50, coordY+50);
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
